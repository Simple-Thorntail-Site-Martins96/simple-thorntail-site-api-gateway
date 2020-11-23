package com.lucamartinelli.app.simplesite.apigateway.gateway.home.client;

import org.eclipse.microprofile.config.ConfigProvider;

import com.lucamartinelli.app.simplesite.apigateway.rest.APIClientProxy;
import com.lucamartinelli.app.simplesite.commons.home.HomeServiceInterface;


public class HomeClientFactory {
	
	private static APIClientProxy<HomeServiceInterface> client;
	
	private static final String ENDPOINT_PROPERTY = "home.endpoint.url";
	private static final String TIMEOUT_PROPERTY = "home.endpoint.timeout";
	
	private HomeClientFactory() {
	}
	
	public static APIClientProxy<HomeServiceInterface> getClient() {
		if (client == null) {
			String timeout = ConfigProvider.getConfig()
					.getOptionalValue(TIMEOUT_PROPERTY, String.class)
					.orElse("5000");
			String endpoint = ConfigProvider.getConfig()
					.getOptionalValue(ENDPOINT_PROPERTY, String.class)
					.orElse("http://localhost:8080");
			
			client = new APIClientProxy<>(endpoint, timeout, HomeServiceInterface.class);
		}
		
		return client;
	}
	
}
