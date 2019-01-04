package com.example;

import it.sky.dp.api.framework.jwt.context.*;
import org.springframework.web.bind.annotation.*;

@RestController("foo")
@io.swagger.v3.oas.annotations.OpenAPIDefinition
public class Service {
    /**
     * This method has no context and should not be picked up by the plugin
     */
    @PostMapping("bar")
    public String doIt(String todo) {
        return todo + " is done";
    }

    /**
     * This method has a context and should be picked up by the plugin
     */
    @GetMapping("baz")
    public String getMyPersonalDetails(RealCustomer customer) {
        return customer.getClaims().getSubject();
    }
}
