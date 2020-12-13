package com.github.iwangjie.discovery.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service-discovery")
public class ServiceDiscoveryProperties {

    private String host = "localhost";
    private int port = 2181;
    private String discoveryPath = "/discovery";

    public String getDiscoveryPath() {
        return discoveryPath;
    }

    public void setDiscoveryPath(String discoveryPath) {
        this.discoveryPath = discoveryPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
