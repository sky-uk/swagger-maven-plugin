package com.github.kongchen.swagger.docgen.mavenplugin.spring;

import io.swagger.v3.jaxrs2.integration.JaxrsApplicationAndAnnotationScanner;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class SpringScanner extends JaxrsApplicationAndAnnotationScanner {

    @Override
    public Set<Class<?>> classes() {
        Set<String> pkgs = openApiConfiguration.getResourcePackages();
        Reflections reflections = createReflections(pkgs);

        Set<Class<?>> output = new HashSet<>(super.classes());
        output.addAll(
                reflections.getTypesAnnotatedWith(RestController.class).stream()
                        .filter(cls -> pkgs.stream().anyMatch(pkg -> cls.getPackage().getName().startsWith(pkg)))
                        .collect(toSet())
        );

        return output;
    }

    private Reflections createReflections(Set<String> pkgs) {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        pkgs.forEach(pkg -> config.addUrls(ClasspathHelper.forPackage(pkg)));
        return new Reflections(config);
    }
}
