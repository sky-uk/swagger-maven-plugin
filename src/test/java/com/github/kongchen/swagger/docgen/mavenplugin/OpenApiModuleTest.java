package com.github.kongchen.swagger.docgen.mavenplugin;

import com.github.kongchen.swagger.docgen.mavenplugin.resources.DummyJaxRsResource;
import com.github.kongchen.swagger.docgen.mavenplugin.resources.DummyOpenAPIResource;
import com.github.kongchen.swagger.docgen.mavenplugin.resources.DummySpringWebResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;

public class OpenApiModuleTest {

    private OpenApiModule module;

    @BeforeMethod
    public void setUp() {
        module = new OpenApiModule("com.github.kongchen");
    }

    @Test
    public void scansAllClassesInClassPath() {
        assertThat(module.getScanner().classes(), containsInAnyOrder(
                DummyOpenAPIResource.class,
                DummyJaxRsResource.class,
                DummySpringWebResource.class
        ));
    }

    @Test
    public void rendersOpenApiToString() {
        String openApi = module.getOpenApiYamlAsString();

        // not created by the reader implemented from scratch
//        assertThat(openApi, containsString("/jax-rs-resource"));

        assertThat(openApi, containsString("/salutations/hello"));
        assertThat(openApi, containsString("A possibly exclaimed salutation"));
    }

}
