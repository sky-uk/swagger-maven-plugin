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

import com.sun.jersey.api.core.InjectParam;
import com.wordnik.sample.JavaRestResourceUtil;
import com.wordnik.sample.data.PetData;
import com.wordnik.sample.model.Pet;
import com.wordnik.sample.model.PetName;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/pet")
@OpenAPIDefinition(servers = {@Server(
        url = "/pet",
        description = "Operations about pets"

)}, security = {
        @SecurityRequirement(name = "petstore_auth", scopes = {"write:pets", "read:pets"})
})
@Produces({"application/json", "application/xml"})
public class PetResource {
    static PetData petData = new PetData();
    static JavaRestResourceUtil ru = new JavaRestResourceUtil();

    @GET
    @Path("/{petId : [0-9]}")
    @Operation(summary = "Find pet by ID",
            description = "Returns a pet when ID < 10.  ID > 10 or nonintegers will simulate API error conditions",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Pet.class)))},
            security = {
                    @SecurityRequirement(name = "api_key")
            }
    )
    @ApiResponses({@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Pet not found")})
    public Response getPetById(
            @Parameter(description = "ID of pet that needs to be fetched", schema = @Schema(allowableValues = "range[1,5]"), required = true)
            @PathParam("petId") Long petId)
            throws com.wordnik.sample.exception.NotFoundException {
        Pet pet = petData.getPetbyId(petId);
        if (pet != null) {
            return Response.ok().entity(pet).build();
        } else {
            throw new com.wordnik.sample.exception.NotFoundException(404, "Pet not found");
        }
    }
    
    @DELETE
    @Path("/{petId}")
    @Operation(summary = "Deletes a pet", tags = {"removePet"})
    @ApiResponses({@ApiResponse(responseCode = "400", description = "Invalid pet value")})
    public Response deletePet(
            @Parameter() @HeaderParam("api_key") String apiKey,
            @Parameter(description = "Pet id to delete", required = true) @PathParam("petId") Long petId) {
        petData.deletePet(petId);
        return Response.ok().build();
    }

    @POST
    @Consumes({"application/json", "application/xml"})
    @Operation(description = "Add a new pet to the store")
    @ApiResponses({@ApiResponse(responseCode = "405", description = "Invalid input")})
    public Response addPet(
            @Parameter(description = "Pet object that needs to be added to the store", required = true) Pet pet) {
        Pet updatedPet = petData.addPet(pet);
        return Response.ok().entity(updatedPet).build();
    }

    @PUT
    @Consumes({"application/json", "application/xml"})
    @Operation(description = "Update an existing pet")
    @ApiResponses({@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "405", description = "Validation exception")})
    public Response updatePet(
            @Parameter(description = "Pet object that needs to be added to the store", required = true) Pet pet) {
        Pet updatedPet = petData.addPet(pet);
        return Response.ok().entity(updatedPet).build();
    }

    @GET
    @Path("/pets/{petName : [^/]*}")
    @Operation(description = "Finds Pets by name",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Pet.class)))})
    // TODO should be a list of pets
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid status value")})
    public Response findPetByPetName(
            @Parameter(description = "petName", required = true)
            @PathParam("petName") PetName petName) {
        return Response.ok(petData.getPetbyId(1)).build();
    }


    @GET
    @Path("/findByTags")
    @Operation(description = "Finds Pets by tags",
            summary = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Pet.class)))})
    @ApiResponses({@ApiResponse(responseCode = "400", description = "Invalid tag value")})
    @Deprecated
    public Response findPetsByTags(
            // TODO allow multiple
            @Parameter(description = "Tags to filter by", required = true) @QueryParam("tags") String tags) {
        return Response.ok(petData.findPetByTags(tags)).build();
    }

    @GET
    @Path("/findAll")
    @Operation(description = "Finds all Pets", summary = "Returns a paginated list of all the Pets.")
    @ApiResponses({@ApiResponse(responseCode = "400", description = "Invalid page number value")})
    public PagedList<Pet> findAllPaginated(
            @Parameter(description = "pageNumber", required = true) @QueryParam("pageNumber") int pageNumber) {
        List<Pet> allPets = petData.findAllPets();
        int pageSize = 5;
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        return new PagedList<Pet>(pageNumber, allPets.size(), allPets.subList(startIndex, endIndex));
    }

    @POST
    @Path("/{petId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Operation(description = "Updates a pet in the store with form data") // TODO consumes form urlencoded
    @ApiResponses({
            @ApiResponse(responseCode = "405", description = "Invalid input")})
    public Response updatePetWithForm(
            @BeanParam MyBean myBean) {
        System.out.println(myBean.getName());
        System.out.println(myBean.getStatus());
        return Response.ok().entity(new com.wordnik.sample.model.ApiResponse(200, "SUCCESS")).build();
    }

    @POST
    @Path("/{petId}/testInjectParam")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Operation(description = "Updates a pet in the store with form data") // TODO consumes form urlencoded
    @ApiResponses({
            @ApiResponse(responseCode = "405", description = "Invalid input")})
    public Response updatePetWithFormViaInjectParam(
            @InjectParam MyBean myBean) {
        System.out.println(myBean.getName());
        System.out.println(myBean.getStatus());
        return Response.ok().entity(new com.wordnik.sample.model.ApiResponse(200, "SUCCESS")).build();
    }

    @Operation(description = "Returns pet", responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Pet.class)))})
    @GET
    @Produces("application/json")
    public Pet get(@Parameter(hidden = true, name = "hiddenParameter") @QueryParam("hiddenParameter") String hiddenParameter) {
        return new Pet();
    }

    @Operation(description = "Test pet as json string in query", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Pet.class)))
    })
    @GET
    @Path("/test")
    @Produces("application/json")
    public Pet test(
            @Parameter(description = "describe Pet in json here")
            @QueryParam("pet") Pet pet) {
        return new Pet();
    }

    @GET
    @Path("/test/extensions")
    @Produces("text/plain")
    @Operation(description = "testExtensions",
            extensions = {
                    @Extension(name = "firstExtension", properties = {
                            @ExtensionProperty(name = "extensionName1", value = "extensionValue1"),
                            @ExtensionProperty(name = "extensionName2", value = "extensionValue2")}),
                    @Extension(properties = {
                            @ExtensionProperty(name = "extensionName3", value = "extensionValue3")})
            }
    )
    public Pet testingExtensions() {
        return new Pet();
    }

/*
    @Operation(description = "Test apiimplicitparams", responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Pet.class)))})
    @GET
    @Path("/test/apiimplicitparams/{path-test-name}")
    @Produces("application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "header-test-name",
                    description = "header-test-value",
                    required = true,
                    dataType = "string",
                    paramType = "header",
                    defaultValue = "z"),

            @ApiImplicitParam(
                    name = "path-test-name",
                    description = "path-test-value",
                    required = true,
                    dataType = "string",
                    paramType = "path",
                    defaultValue = "path-test-defaultValue"),

            @ApiImplicitParam(
                    name = "body-test-name",
                    description = "body-test-value",
                    required = true,
                    dataType = "com.wordnik.sample.model.Pet",
                    paramType = "body")
    })
    public Pet testapiimplicitparams() {
        return new Pet();
    }

    @Operation(description = "Test testFormApiImplicitParams", responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = Pet.class)))})
    @GET
    @Path("/test/testFormApiImplicitParams")
    @Produces("application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "form-test-name",
                    description = "form-test-value",
                    allowMultiple = true,
                    required = true,
                    dataType = "string",
                    paramType = "form",
                    defaultValue = "form-test-defaultValue")
    })
    public Pet testFormApiImplicitParams() {
        return new Pet();
    }
*/

    @Operation(description = "testingHiddenApiOperation", hidden = true)
    @GET
    @Produces("application/json")
    public String testingHiddenApiOperation() {
        return "testingHiddenApiOperation";
    }

    @Operation(description = "testingBasicAuth", security = {@SecurityRequirement(name = "basicAuth")})
    @GET
    @Path("/test/testingBasicAuth")
    public String testingBasicAuth() {
        return "testingBasicAuth";
    }

    @Operation(description = "testingArrayResponse")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "array",
                    content = @Content(schema = @Schema(implementation = Pet.class)) // TODO return list of pets
            )
    )
    @GET
    @Path("/test/testingArrayResponse")
    public Response testingArrayResponse() {
        return null;
    }

    @Operation(summary = "testingVendorExtensions")
    @GET
    @Path("/test/testingVendorExtensions")
    public Response testingVendorExtensions() {
        return null;
    }
}
