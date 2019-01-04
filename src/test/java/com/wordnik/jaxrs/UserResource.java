/**
 * Copyright 2014 Reverb Technologies, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wordnik.jaxrs;

import com.wordnik.sample.data.UserData;
import com.wordnik.sample.exception.ApiException;
import com.wordnik.sample.exception.NotFoundException;
import com.wordnik.sample.model.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@OpenAPIDefinition(
        servers = {
                @Server(url = "www.example.com:8080/api/user")
        },
        info = @Info(
                title = "Swagger Maven Plugin Sample",
                version = "v1",
                description = "This is a sample.",
                termsOfService = "http://www.github.com/kongchen/swagger-maven-plugin",
                contact = @Contact(name = "Kong Chen", email = "kongchen@gmail.com", url = "http://kongch.com"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        tags = { @Tag(name = "user", description = "Operations about user"),
                 @Tag(name = "spurioustag", description = "Operations about something spurious")
        }
)
@Path("/user")
@Produces({"application/json", "application/xml"})
public class UserResource {
    static UserData userData = new UserData();

    @POST
    @Operation(description = "Create user",
            summary = "This can only be done by the logged in user.")
    public Response createUser(
            @Parameter(description = "Created user object", required = true) User user) {
        userData.addUser(user);
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/createWithArray")
    @Operation(description = "Creates list of users with given input array")
    public Response createUsersWithArrayInput(@Parameter(description = "List of user object", required = true) User[] users) {
        for (User user : users) {
            userData.addUser(user);
        }
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/createWithList")
    @Operation(description = "Creates list of users with given input array")
    public Response createUsersWithListInput(@Parameter(description = "List of user object", required = true) java.util.List<User> users) {
        for (User user : users) {
            userData.addUser(user);
        }
        return Response.ok().entity("").build();
    }

    @PUT
    @Path("/{username}")
    @Operation(description = "Updated user",
            summary = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid user supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public Response updateUser(
            @Parameter(description = "name that need to be deleted", required = true) @PathParam("username") String username,
            @Parameter(description = "Updated user object", required = true) User user) {
        userData.addUser(user);
        return Response.ok().entity("").build();
    }

    @DELETE
    @Path("/{username}")
    @Operation(description = "Delete user",
            summary = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid username supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public Response deleteUser(
            @Parameter(description = "The name that needs to be deleted", required = true) @PathParam("username") String username) {
        userData.removeUser(username);
        return Response.ok().entity("").build();
    }

    @GET
    @Path("/{username}")
    @Operation(description = "Get user by user name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid username supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public Response getUserByName(
            @Parameter(description = "The name that needs to be fetched. Use user1 for testing. ", required = true) @PathParam("username") String username)
            throws ApiException {
        User user = userData.findUserByName(username);
        if (user != null) {
            return Response.ok().entity(user).build();
        } else {
            throw new NotFoundException(404, "User not found");
        }
    }

    @GET
    @Path("/login")
    @Operation(description = "Logs user into the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Invalid username/password supplied")})
    public Response loginUser(
            @Parameter(description = "The user name for login", required = true) @QueryParam("username") String username,
            @Parameter(description = "The password for login in clear text", required = true) @QueryParam("password") String password) {
        return Response.ok()
                .entity("logged in user session:" + System.currentTimeMillis())
                .build();
    }

    @GET
    @Path("/logout")
    @Operation(description = "Logs out current logged in user session")
    public Response logoutUser() {
        return Response.ok().entity("").build();
    }
}
