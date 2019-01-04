package com.wordnik.springmvc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author carlosjgp
 */
@RequestMapping
@OpenAPIDefinition
public class EmptyRootPathResource {
    @Operation(summary = "testingEmptyRootPathResource")
    @RequestMapping(value="/testingEmptyRootPathResource",method = RequestMethod.GET)
    public ResponseEntity<String> testingEmptyRootPathResource() {
        return new ResponseEntity<String>("testingEmptyRootPathResource", HttpStatus.OK);
    }
}
