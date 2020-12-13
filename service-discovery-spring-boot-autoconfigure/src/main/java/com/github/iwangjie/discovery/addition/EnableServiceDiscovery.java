package com.github.iwangjie.discovery.addition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;

@Configuration
public class EnableServiceDiscovery {


    @Autowired
    public void enableServiceDiscoveryAirMark(ConfigurableListableBeanFactory beanFactory) throws ClassNotFoundException {
        Class applicationStartClass = getApplicationStartClass();
        Annotation exportService = applicationStartClass.getAnnotation(ExportService.class);
        if (exportService != null) {
            beanFactory.registerSingleton("enableServiceDiscoveryAirMark", new EnableServiceDiscoveryAirMark());
        }
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
}
