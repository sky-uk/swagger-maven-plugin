package com.wordnik.jaxrs;

import com.wordnik.sample.model.ListItem;
import com.wordnik.sample.model.Pet;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@OpenAPIDefinition(info = @Info(description = "Operations about pets"))
@Produces({"application/json", "application/xml"})
public interface MyResource<T> {

    //contrived example test case for swagger-maven-plugin issue #358
    @GET
    @Operation(summary = "Find pet(s) by ID",
            description = "This is a contrived example",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Pet.class)))}
    )
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Pet not found")})
    Response getPetsById(
            @Parameter(in = PATH, description = "start ID of pets that need to be fetched", required = true)
            @QueryParam("startId") Long startId,
            @Parameter(in = QUERY, description = "end ID of pets that need to be fetched", required = true, schema = @Schema(allowableValues = "range[1,99]"))
            @QueryParam("endId") Long endId)
            throws com.wordnik.sample.exception.NotFoundException;

    //contrived example test case for swagger-maven-plugin issue #505
    @GET
    @Operation(summary = "Get a list of items",
            description = "This is a contrived example"
    )
    List<ListItem> getListOfItems();

    //contrived example test case for swagger-maven-plugin issue #504
    @GET
    @Operation(summary = "Get a response", description = "This is a contrived example")
    Response testParamInheritance(
            @PathParam("firstParamInterface") String firstParam,
            @PathParam("secondParamInterface") String secondParam,
            @QueryParam("thirdParamInterface") String thirdParam);

    Response insertResource(T resource);
}
