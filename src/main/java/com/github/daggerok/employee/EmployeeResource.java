package com.github.daggerok.employee;

import io.reactiverse.axle.pgclient.PgPool;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/employees")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class EmployeeResource {

  @Inject
  PgPool client;

  @Inject
  @ConfigProperty(name = "schema.create", defaultValue = "true")
  boolean schemaCreate;

  @PostConstruct
  public void init() {
    if (schemaCreate) {
      initdb();
    }
  }

  private void initdb() {
    client.query("DROP TABLE IF EXISTS employees")
          .thenCompose(r -> client.query(
              "CREATE TABLE employees (name character varying NOT NULL, salary integer NOT NULL, id SERIAL PRIMARY KEY, organization_id bigint)"))
          .thenCompose(r -> client.query("INSERT INTO employees (name, salary) VALUES ('Ololo', 1)"))
          .thenCompose(r -> client.query("INSERT INTO employees (name, salary) VALUES ('Trololo', 22)"))
          .thenCompose(r -> client.query("INSERT INTO employees (name, salary) VALUES ('Ho', 333)"))
          .toCompletableFuture()
          .join();
  }

  @GET
  public CompletionStage<List<Employee>> getEmployees() {
    return Employee.findAll(client)
                   .exceptionally(throwable -> new ArrayList<>());
  }
}
