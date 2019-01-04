package com.wordnik.jaxrs;

import io.swagger.v3.oas.annotations.Parameter;

import javax.ws.rs.HeaderParam;


/**
 * Represents a {@code @BeanParam} target that is nested within another bean.
 */
public class MyNestedBean {
    
    @Parameter(description = "Header from nested bean")
    @HeaderParam("myNestedBeanHeader")
    private String myNestedBeanHeader;

    public String getMyNestedBeanHeader() {
        return myNestedBeanHeader;
    }
    
    public void setMyNestedBeanHeader(String myNestedBeanHeader) {
        this.myNestedBeanHeader = myNestedBeanHeader;
    }
}
