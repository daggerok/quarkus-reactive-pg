package com.github.daggerok.client;

import com.github.daggerok.employee.Employee;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
@RegisterRestClient
public interface EmployeeRestClient {

  @GET
  @Produces(APPLICATION_JSON)
  List<Employee> getEmployees();
}
