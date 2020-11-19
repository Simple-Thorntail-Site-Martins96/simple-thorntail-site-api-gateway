# Simple Site API Gateway

## Description
This module just exposes the registered API, have a API Gateway allow you to have only one point (and domain on Openshift) for all FE layer calls.
In API Gateway also will be registred Fault Tollerance in order to improve the app's stability and resilience, and with the Microprofile Metrics the module can trace the traffic information as well.
The API Gateway works like a proxy and redirect the requests recived by _Routes_ are redirected to the correct service and externaly inaccessible service


## Technologies
 
### Backend
The backend use Thorntail and Maven as core, REST APIs exposed via JAX-RS (RESTEasy Framework).
The structure is divided in 2 section:
#### - Core Logic
Inside the package _com.lucamartinelli.app.simplesite.apigateway.rest_, here is present the Rest Activator class and the API Client Proxy that will create the connection to real service.
Here the requests and response are dumped and logged to file system.
####  - Service Logic
To register an API there are some classes in a structure under _com.lucamartinelli.app.simplesite.apigateway.gateway_ that define the interface, client factory and the exposing logic.
For example, in Home module, the __HomeApiGateway__ create the logic for interface __HomeServiceInterface__, and this logic is exposed via JAX-RS.
In __HomeApiGateway__ there are annotations for Fault tollerance and Metrics, when the service is triggered the flow create or load a Home Proxy Client and involke the real service, dumping the data.
The __HomeClientFactory__ class create and store the client with home endpoint and interface definition.

The API Client Proxy and Dumper use reflection logics in order to adapt itself to different implementations

## Configuration
The configuration is simple, are required 2 properties per module (eg. for Home Service are required `home.endpoint.url` and `home.endpoint.rimeout`)
 

## Deployment

Use `mvn clean package` for generate the full runnable JAR inside the target folder





