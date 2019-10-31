package com.maarten551.tictactoe_backend.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.websocket")
public class WebSocketProperties {
    private String applicationPrefix = "/app";
    private String endpoint = "/socket";
    private String[] allowedOrigins = new String[0];

    public String getApplicationPrefix() {
        return applicationPrefix;
    }

    public void setApplicationPrefix(String applicationPrefix) {
        this.applicationPrefix = applicationPrefix;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
