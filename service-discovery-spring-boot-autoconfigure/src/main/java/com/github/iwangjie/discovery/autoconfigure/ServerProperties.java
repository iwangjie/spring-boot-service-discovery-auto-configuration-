package com.github.iwangjie.discovery.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server")
public class ServerProperties {
    private Integer port;

    public Integer getPort() {
        return port == null ? 8080 : this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
