package com.lucamartinelli.app.simplesite.apigateway.gateway.home;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static com.lucamartinelli.app.simplesite.apigateway.gateway.home.client.HomeClientFactory.getClient;

@Path("home-gateway")
@RequestScoped
public class HomeApiGateway implements HomeServiceInterface {
	
	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger(HomeApiGateway.class.getCanonicalName());
	
	@Context
	private HttpHeaders headers;
	
	@Context
	private SecurityContext securityContext;
	

	public Response getRoles() {
		return getClient().invoke("getRoles", headers, null, Response.class, new ArrayList<>());
	}
	
	
	public String getFullName() {
		return getClient().invoke("getFullName", headers, null, String.class, new ArrayList<>());
	}

	
	public Response getServices() {
		return getClient().invoke("getServices", headers, null, Response.class, new ArrayList<>());
	}
	
}
