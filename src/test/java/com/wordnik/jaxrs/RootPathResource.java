package com.wordnik.jaxrs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author andrewb
 */
@Path("/")
@OpenAPIDefinition
public class RootPathResource {
    @GET
    @Operation(summary = "testingRootPathResource")
    public String testingRootPathResource() {
        return "testingRootPathResource";
    }
}
