package com.wordnik.jaxrs;

import com.wordnik.sample.model.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 *
 * @author pradeep.chaudhary
 */
@Produces(MediaType.APPLICATION_JSON)
@OpenAPIDefinition(security = {@SecurityRequirement(name="api_key")}, tags = {@Tag(name = "Resource-V1")})
public class SubResource {

    @GET
    @Operation(summary="List of users",description="Get user list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation" /*, response = List.class*/)
    })
    public Response getUsers() {
        User john = new User();
        john.setFirstName("John");
        john.setEmail("john@testdomain.com");
        
        User max = new User();
        max.setFirstName("Max");
        max.setEmail("max@testdomain.com");        
        
        return Response.ok(Arrays.asList(john, max)).build();
    }

    @Path("/{username}")
    @GET
    @Operation(description="Fetch a user by username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"/*, response = User.class*/)
    })
    public Response getUserByName(@Parameter(description = "Username of user that needs to be fetched", required = true)
                                    @PathParam("username") String username) {
        User max = new User();
        max.setFirstName("Max");
        max.setEmail("max@testdomain.com");
        max.setUsername("max");
                
        return Response.ok(max).build();
    }

    @PUT
    @Operation(summary="Update User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    public Response updateUser(@Parameter(description = "User to be updated", required = true) User user) {
        return Response.ok().build();
    }
    
    
}
