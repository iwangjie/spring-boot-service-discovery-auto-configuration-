package com.github.iwangjie.discovery.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring")
public class SpringProperties {

    private Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}

class Application {
    private String name;
    private String description;

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name == null ? "Unknown service":name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
