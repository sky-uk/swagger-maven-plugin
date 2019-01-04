package com.github.kongchen.swagger.docgen.mavenplugin;

import com.github.kongchen.swagger.docgen.mavenplugin.spring.SpringReaderFromScratch;
import com.github.kongchen.swagger.docgen.mavenplugin.spring.SpringScanner;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.integration.api.OpenApiScanner;
import lombok.Getter;

import java.util.HashSet;

import static java.util.Collections.singletonList;

@Getter
public class OpenApiModule {
    private final GenericOpenApiContext context = new GenericOpenApiContext();
    private final SwaggerConfiguration configuration = new SwaggerConfiguration();
    private final OpenApiReader reader = new SpringReaderFromScratch(); //new SpringReaderBasedOnSwagger();
    private final OpenApiScanner scanner = new SpringScanner();

    public OpenApiModule(String scanPackage) {
        configuration.setResourcePackages(new HashSet<>(singletonList(scanPackage)));
        reader.setConfiguration(configuration);
        scanner.setConfiguration(configuration);

        context.setOpenApiConfiguration(configuration);
        context.setOpenApiReader(reader);
        context.setOpenApiScanner(scanner);

    }

    public String getOpenApiYamlAsString() {
        return Yaml.pretty(getContext().read());
    }
}
