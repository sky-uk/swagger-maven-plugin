package com.github.kongchen.swagger.docgen.mavenplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * User: kongchen
 * Date: 3/7/13
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.COMPILE,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, threadSafe = true)
public class ApiDocumentMojo extends AbstractMojo {

    @Parameter(name = "package", property = "package", required = true)
    private String scanPackage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        OpenApiModule module = new OpenApiModule(scanPackage);
        System.out.println(module.getOpenApiYamlAsString());
    }
}
