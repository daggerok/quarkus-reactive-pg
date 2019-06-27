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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
  public CompletionStage<Map<String, List<Employee>>> getClient() {
    log.info("compute async");
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloRestClient.getHello());
    CompletableFuture<List<Employee>> list = CompletableFuture.supplyAsync(() -> employeeRestClient.getEmployees());
    log.info("compose results");
    return hello.thenCombine(list, Collections::singletonMap)
                .whenCompleteAsync((map, err) -> log.info("{} / {}", map, err))
                .exceptionally(throwable -> singletonMap("error", new ArrayList<>()));
  }

  public CompletionStage<Map<String, List<Employee>>> getHelloFallback() {
    return CompletableFuture.supplyAsync(
        () -> singletonMap("error", new ArrayList<>())
    );
  }
}
