package com.github.iwangjie.servicediscoverydemo.controller;

import com.github.iwangjie.discovery.addition.ExportService;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @GetMapping
    public String echo() {
        return serviceDiscovery.toString();
    }

    @GetMapping("/port1")
    public String port1() {
        return "port1 调用成功";
    }
}
