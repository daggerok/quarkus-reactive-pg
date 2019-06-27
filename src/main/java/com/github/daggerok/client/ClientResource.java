package com.github.daggerok.client;

import com.github.daggerok.employee.Employee;
import com.github.daggerok.infrastructure.LogMe;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/client/hello")
@Produces(APPLICATION_JSON)
public class ClientResource {

  @LogMe
  @Inject
  Logger log;

  @Inject
  @RestClient
  HelloRestClient helloRestClient;

  @Inject
  @RestClient
  EmployeeRestClient employeeRestClient;

  @GET
  @Path("")
  @Bulkhead
  @Timeout(1500)
  @Fallback(fallbackMethod = "getHelloFallback")
  public Map<String, List<Employee>> getClient() {
    log.info("getClient()");
    String hello = helloRestClient.getHello();
    List<Employee> employees = employeeRestClient.getEmployees();
    return singletonMap(hello, employees);
  }

  public Map<String, List<Employee>> getHelloFallback() {
    return singletonMap("error", new ArrayList<>());
  }
}
