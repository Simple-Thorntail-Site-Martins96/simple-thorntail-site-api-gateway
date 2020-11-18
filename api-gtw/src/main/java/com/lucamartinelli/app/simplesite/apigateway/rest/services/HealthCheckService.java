package com.lucamartinelli.app.simplesite.apigateway.rest.services;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

@Liveness
@Readiness
public class HealthCheckService implements HealthCheck {

	@Override
	public HealthCheckResponse call() {
		return HealthCheckResponse.up("api-gateway");
	}
	
}
