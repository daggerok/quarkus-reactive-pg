package com.github.daggerok.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
@RegisterRestClient
public interface HelloRestClient {

  @GET
  @Produces(APPLICATION_JSON)
  String getHello();
}
