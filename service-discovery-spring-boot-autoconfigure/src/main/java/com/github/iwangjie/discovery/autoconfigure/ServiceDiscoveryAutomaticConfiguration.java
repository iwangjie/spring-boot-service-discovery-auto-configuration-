package com.github.iwangjie.discovery.autoconfigure;

import com.github.iwangjie.discovery.addition.EnableServiceDiscovery;
import com.github.iwangjie.discovery.addition.EnableServiceDiscoveryAirMark;
import com.github.iwangjie.discovery.addition.ExportService;
import com.github.iwangjie.discovery.addition.InstanceDetails;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableConfigurationProperties({ServiceDiscoveryProperties.class, ServerProperties.class, SpringProperties.class})
public class ServiceDiscoveryAutomaticConfiguration {

    @Resource
    private ServiceDiscoveryProperties serviceDiscoveryProperties;

    @Resource
    private ServerProperties serverProperties;

    @Resource
    private SpringProperties springProperties;

    @Bean
    @ConditionalOnMissingBean
    public CuratorFramework curatorFramework() throws ClassNotFoundException {
        Class applicationStartClass = getApplicationStartClass();
        Annotation exportService = applicationStartClass.getAnnotation(ExportService.class);
        if (exportService == null) {
           throw new RuntimeException("启动类没有配置 ExportService 注解.");
        }

        String connectString = serviceDiscoveryProperties.getHost() + ":" + serviceDiscoveryProperties.getPort();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        client.start();
        return client;
    }


    protected Class getApplicationStartClass() throws ClassNotFoundException {
        // 获取顶层堆栈
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            if ("main".equals(element.getMethodName())) {
                return Class.forName(element.getClassName());
            }
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceDiscovery serviceDiscovery(CuratorFramework curatorFramework) throws Exception {
        JsonInstanceSerializer<InstanceDetails> serializer1 = new JsonInstanceSerializer<>(InstanceDetails.class);
        ServiceDiscovery<InstanceDetails> serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(curatorFramework)
                .basePath(serviceDiscoveryProperties.getDiscoveryPath())
                .serializer(serializer1)
                .build();
        serviceDiscovery.start();
        return serviceDiscovery;
    }

    @Bean
    public ServiceInstance serviceInstance(ServiceDiscovery serviceDiscovery) throws Exception {

        JsonInstanceSerializer<InstanceDetails> serializer = new JsonInstanceSerializer<>(InstanceDetails.class);
        UriSpec uriSpec = new UriSpec("http://{host}:{port}");
        ServiceInstance<InstanceDetails> currentService = ServiceInstance.<InstanceDetails>builder()
                .name(springProperties.getApplication().getName())
                .address(getServerHost())
                .payload(new InstanceDetails(springProperties.getApplication().getDescription()))
                .port(serverProperties.getPort()) // in a real application, you'd use a common port
                .uriSpec(uriSpec)
                .build();
        serviceDiscovery.registerService(currentService);
        return currentService;
    }

    private String getServerHost() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address.getHostAddress();
    }
}
