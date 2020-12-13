package com.github.iwangjie.servicediscoverydemo;

import com.github.iwangjie.discovery.addition.ExportService;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ExportService("HelloService")
public class ServiceDiscoveryDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDiscoveryDemoApplication.class, args);
    }

}
