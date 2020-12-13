package com.github.iwangjie.discovery.autoconfigure;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.env.MockEnvironment;


public class ServiceDiscoveryAutomaticConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }


    @Test
    public void testInjectedSuccessfully() {
        load(EmptyConfiguration.class, "service-discovery.host=127.0.0.1");
        ServiceDiscovery discovery = context.getBean(ServiceDiscovery.class);
        Assert.assertNotNull(discovery);
    }


    @Test
    public void testCustomHostOk() {
        load(EmptyConfiguration.class, "service-discovery.host=127.0.0.1", "service-discovery.port=2181");
        CuratorFramework curatorFramework = context.getBean(CuratorFramework.class);
        Assert.assertEquals("127.0.0.1:2181", curatorFramework.getZookeeperClient().getCurrentConnectionString());
    }


    @Configuration
    static class EmptyConfiguration {
    }


    private void load(Class<?> config, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        MockEnvironment mockEnvironment = new MockEnvironment();
        for (int i = 0; i < environment.length; i++) {
            String[] kv = environment[i].split("=");
            mockEnvironment.setProperty(kv[0], kv[1]);
        }
        applicationContext.setEnvironment(mockEnvironment);

        applicationContext.register(config);
        applicationContext.register(ServiceDiscoveryAutomaticConfiguration.class);
        applicationContext.refresh();
        this.context = applicationContext;
    }


}