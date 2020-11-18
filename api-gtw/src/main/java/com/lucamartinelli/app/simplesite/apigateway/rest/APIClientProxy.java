package com.lucamartinelli.app.simplesite.apigateway.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.lang3.math.NumberUtils;
import org.jboss.weld.exceptions.IllegalArgumentException;

import com.lucamartinelli.app.simplesite.apigateway.gateway.home.HomeServiceInterface;


public class APIClientProxy<T> {
	
	private final Logger log;
	private final Class<T> restInterface;
	
	private final Client client;
	private final String host;
	private String path;
	private String method;
	private String contentType;
	
    public APIClientProxy(String host, String timeout, Class<T> restInterfaceClass) {
    	log = Logger.getLogger(this.getClass().getCanonicalName());
    	this.host = host;
    	restInterface = restInterfaceClass;
    	this.client = ClientBuilder
    			.newBuilder()
    			.connectTimeout(NumberUtils.toLong(timeout, 5000L), TimeUnit.MILLISECONDS)
    			.readTimeout(NumberUtils.toLong(timeout, 5000L), TimeUnit.MILLISECONDS)
    			.build();
    }

    public <R, InType> R invoke(String methodName, HttpHeaders headers, InType payload, Class<R> resultType) {
    	try {
			loadInvokeInformation(methodName);
		} catch (IllegalArgumentException | NoSuchMethodException | SecurityException e) {
			log.severe(e.getLocalizedMessage());
			return null;
		}
    	if (payload == null)
    		return invokeWithoutBody(headers, resultType, payload);
    	else
    		return invokeWithBody(headers, resultType);
    }
    	
	public <ResType> ResType invokeWithBody(HttpHeaders headers, Class<ResType> resultType) {
    	return client
    			.target(host)
    			.path(path)
    			.request()
    			.headers(new MultivaluedHashMap<String, Object>(headers.getRequestHeaders()))
    			.method(method, resultType);
    }

	public <ResType, InType> ResType invokeWithoutBody(HttpHeaders headers, Class<ResType> resultType, InType payload) {
		loadBodyContentType(headers);
		return client
				.target(host)
				.path(path)
				.request()
				.headers(new MultivaluedHashMap<String, Object>(headers.getRequestHeaders()))
				.method(method, Entity.entity(payload, contentType), resultType);
	}
    
    
    
    //--------------------------------------------------------------------------------------------
    
    private void loadInvokeInformation(String methodName) 
    		throws NoSuchMethodException, SecurityException, IllegalArgumentException {
    	Method getRolesMethod = null;
		try {
			getRolesMethod = restInterface.getMethod(methodName, new Class[0]);
		} catch (NoSuchMethodException | SecurityException e) {
			log.log(Level.SEVERE, "Error in reflection: ", e);
			throw e;
		}
		
		String subPath = Optional.of(getRolesMethod.getAnnotation(Path.class).value())
				.orElse(new String());
		if (!subPath.isEmpty() && !subPath.startsWith("/"))
			subPath = "/" + subPath;
		
		this.path = Optional.of(HomeServiceInterface.class
					.getAnnotation(Path.class).value())
				.orElse(new String())
				.concat(subPath);
		
		
		this.method = null;
		for (Annotation annotation : getRolesMethod.getAnnotations()) {
			HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
			if (httpMethod != null) {
				if (method == null) {
					method = httpMethod.value();
				} else {
					final String errMsg = "IllegalArgument, is annotated as more than one http method.";
					log.log(Level.SEVERE, errMsg);
					throw new IllegalArgumentException(errMsg);
				}
			}
		}
    }
    
    private void loadBodyContentType(HttpHeaders headers) {
    	this.contentType = headers.getHeaderString("content-type");
    	if (this.contentType == null)
    		this.contentType = MediaType.APPLICATION_JSON;
    		
    }
    
    
}
