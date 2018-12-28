package com.github.kongchen.swagger.docgen.mavenplugin;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * User: kongchen
 * Date: 3/7/13
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.COMPILE, configurator = "include-project-dependencies",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, threadSafe = true)
public class ApiDocumentMojo extends AbstractMojo {

    private final OpenApiContext context;

    public ApiDocumentMojo() {
        context = new GenericOpenApiContext();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        OpenAPI api = context.read();
        System.out.println(Yaml.pretty(api));
    }
}
