package com.maarten551.tictactoe_backend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.maarten551.tictactoe_backend.configuration.properties.WebSocketProperties;

@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	private WebSocketProperties properties;

	@Autowired
	public WebSocketConfiguration(WebSocketProperties properties) {
		this.properties = properties;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(this.properties.getEndpoint()).setAllowedOrigins(this.properties.getAllowedOrigins());
	}
}
