package com.github.kongchen.swagger.docgen.mavenplugin.resources;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController("/salutations")
public class DummySpringWebResource {

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A possibly exclaimed salutation"),
            @ApiResponse(responseCode = "500", description = "It crashed")
    })
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name, @RequestParam("exclaim") boolean exclaim) {
        return "Hello, " + name + (exclaim ? "!" : ".");
    }

    @PostMapping("/hello")
    public String createSalutation(@RequestBody String salutation) {
        return salutation;
    }

}
