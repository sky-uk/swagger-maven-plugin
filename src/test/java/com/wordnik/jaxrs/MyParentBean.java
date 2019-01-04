package com.wordnik.jaxrs;


import io.swagger.v3.oas.annotations.Parameter;

import javax.ws.rs.HeaderParam;

/**
 * @author Vinayak Hulawale [vinhulawale@gmail.com]
 */
public class MyParentBean {

    @Parameter(description = "Header from parent", required = false)
    @HeaderParam("myParentHeader")
    private String myParentheader;

    public String getMyParentheader() {
        return myParentheader;
    }

    public void setMyParentheader(String myParentheader) {
        this.myParentheader = myParentheader;
    }

}
