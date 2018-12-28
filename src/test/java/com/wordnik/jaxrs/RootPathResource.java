package com.wordnik.jaxrs;

import io.swagger.v3.core.annotations.Api;
import io.swagger.v3.core.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author andrewb
 */
@Path("/")
@Api(value = "/")
public class RootPathResource {
    @GET
    @ApiOperation(value = "testingRootPathResource")
    public String testingRootPathResource() {
        return "testingRootPathResource";
    }
}
