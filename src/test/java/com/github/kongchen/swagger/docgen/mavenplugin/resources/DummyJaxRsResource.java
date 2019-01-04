package com.github.kongchen.swagger.docgen.mavenplugin.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/jax-rs-resource")
public class DummyJaxRsResource {
    @GET
    public String hello() {
        return "hello";
    }
}
