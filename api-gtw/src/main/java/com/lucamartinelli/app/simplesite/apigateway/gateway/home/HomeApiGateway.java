package com.lucamartinelli.app.simplesite.apigateway.gateway.home;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import com.lucamartinelli.app.simplesite.commons.home.HomeServiceInterface;
import com.lucamartinelli.app.simplesite.commons.home.vo.Roles;
import com.lucamartinelli.app.simplesite.commons.home.vo.ServiceVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static com.lucamartinelli.app.simplesite.apigateway.gateway.home.client.HomeClientFactory.getClient;


@Api(value = "/home", tags = "home")
@Path("home-gateway")
@RequestScoped
public class HomeApiGateway implements HomeServiceInterface {
	
	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger(HomeApiGateway.class.getCanonicalName());
	
	@Context
	private HttpHeaders headers;
	
	@Context
	private SecurityContext securityContext;
	
	@Retry(maxRetries = 3,
			delay = 1, delayUnit = ChronoUnit.SECONDS)
	@Bulkhead()
	@Counted(name = "roles-call-counter",
			description = "Number of invokations for Roles Service",
			displayName = "Roles Calls Number",
			absolute = true
	)
	@Timed(name = "roles-call-times",
			description = "The performance times for Roles Service",
			displayName = "Roles Calls Times",
			absolute = true)
	@ApiOperation(httpMethod = "GET", value = "/roles", 
		notes = "Return the roles list for the logged user",
		response = Roles.class,
		produces = MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML + "," 
				+ MediaType.TEXT_PLAIN
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, response = Roles.class, 
			message = "Execution success, roles are returned"),
		@ApiResponse(code = 401, response = String.class,
			message = "User are not logged in"),
		@ApiResponse(code = 500, response = String.class,
			message = "In case of internal server errors")
	})
	public Roles getRoles() {
		return getClient().invoke("getRoles", headers, null, Roles.class, new ArrayList<>());
	}
	
	@Retry(maxRetries = 3,
			delay = 1, delayUnit = ChronoUnit.SECONDS)
	@Bulkhead()
	@Counted(name = "fullname-call-counter",
			description = "Number of invokations for Fullname Service",
			displayName = "Fullname Calls Number",
			absolute = true
	)
	@Timed(name = "fullname-call-times",
			description = "The performance times for Roles Service",
			displayName = "Fullname Calls Times",
			absolute = true)
	@ApiOperation(httpMethod = "GET", value = "/name", 
		notes = "Return the first and last name for the logged user",
		response = String.class,
		produces = MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML + "," 
				+ MediaType.TEXT_PLAIN
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, response = String.class, 
			message = "Execution success, full name is returned"),
		@ApiResponse(code = 401, response = String.class,
			message = "User are not logged in"),
		@ApiResponse(code = 500, response = String.class,
			message = "In case of internal server errors")
	})
	public String getFullName() {
		return getClient().invoke("getFullName", headers, null, String.class, new ArrayList<>());
	}

	
	@SuppressWarnings("unchecked")
	@Retry(maxRetries = 3,
			delay = 1, delayUnit = ChronoUnit.SECONDS)
	@Bulkhead()
	@Counted(name = "services-call-counter",
			description = "Number of invokations for getServices Service",
			displayName = "getServices Calls Number",
			absolute = true
	)
	@Timed(name = "services-call-times",
			description = "The performance times for getServices Service",
			displayName = "getServices Calls Times",
			absolute = true)
	@ApiOperation(httpMethod = "GET", value = "/services", 
		notes = "Return the available ad accessible service for the logged user",
		response = List.class,
		produces = MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML + "," 
				+ MediaType.TEXT_PLAIN
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, response = List.class, 
			message = "Execution success, roles are returned"),
		@ApiResponse(code = 401, response = String.class,
			message = "User are not logged in"),
		@ApiResponse(code = 500, response = String.class,
			message = "In case of internal server errors")
	})
	public List<ServiceVO> getServices() {
		final List<ServiceVO> clazz = new ArrayList<>(0);
		return getClient().invoke("getServices", headers, null, clazz.getClass(), new ArrayList<>());
	}
	
}
