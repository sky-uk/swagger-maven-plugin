package com.wordnik.springmvc;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;

public class UpdatePetRequest {
    private String petId;
    private String name;
    private String status;

    public String getName() {
        return name;
    }

    @Parameter(description = "ID of pet that needs to be updated", required = true)
    public void setPetId(@PathVariable("petId") String petId) {
        this.petId = petId;
    }

    public String getStatus() {
        return status;
    }

    @Parameter(description = "Updated name of the pet", required = false)
    public void setName(String name) {
        this.name = name;
    }

    public String getPetId() {
        return petId;
    }

    @Parameter(description = "Updated status of the pet", required = false)
    public void setStatus(String status) {
        this.status = status;
    }

    public UpdatePetRequest() {
    }


}
