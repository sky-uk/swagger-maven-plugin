package com.github.kongchen.smp.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kongchen.swagger.docgen.mavenplugin.ApiDocumentMojo;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import net.javacrumbs.jsonunit.core.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;

/**
 * @author chekong on 8/15/14.
 */
public class StringWrapperModelTest extends AbstractMojoTestCase {
    private File swaggerOutputDir = new File(getBasedir(), "generated/swagger-ui-spring-string-wrapper-model");
    private ApiDocumentMojo mojo;
    private List<OpenAPIExtension> extensions;

    @Override
	@BeforeMethod
    protected void setUp() throws Exception {
    	extensions = new ArrayList<>(OpenAPIExtensions.getExtensions());
    	super.setUp();

        try {
            FileUtils.deleteDirectory(swaggerOutputDir);
        } catch (Exception e) {
            //ignore
        }

        File testPom = new File(getBasedir(), "target/test-classes/plugin-config-springmvc-string-wrapper-model.xml");
        mojo = (ApiDocumentMojo) lookupMojo("generate", testPom);
    }

    @Override
    @AfterMethod
    protected void tearDown() throws Exception {
    	super.tearDown();
        OpenAPIExtensions.setExtensions(extensions);
    }

    @Test
    public void testAssertGeneratedSwaggerSpecJson() throws MojoExecutionException, MojoFailureException, IOException {
        mojo.execute();
        ObjectMapper mapper = Json.mapper();
        JsonNode actualJson = mapper.readTree(new File(swaggerOutputDir, "swagger.json"));
        JsonNode expectJson = mapper.readTree(this.getClass().getResourceAsStream("/expectedOutput/swagger-spring-string-wrapper-model.json"));

        assertJsonEquals(expectJson, actualJson, Configuration.empty().when(IGNORING_ARRAY_ORDER));
    }
}
