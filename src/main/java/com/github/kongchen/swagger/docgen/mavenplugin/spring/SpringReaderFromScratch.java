package com.github.kongchen.swagger.docgen.mavenplugin.spring;

import com.github.kongchen.swagger.docgen.mavenplugin.utils.OpenAPIFactory;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * Implementation from scratch of an OpenApiReader that knows about Spring annotations.
 *
 * There is a lot of work to do in order to support all of Swagger's annotations, as well as Spring's.
 *
 * This seems a more promising route than extending Swagger's reader, but it won't detect JaxRs annotations (unless we implement that part too).
 */
public class SpringReaderFromScratch implements OpenApiReader {

    @Setter
    protected OpenAPIConfiguration configuration;

    @Override
    public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources) {
        return classes.stream().map(this::readClass).reduce(OpenAPIFactory.create(), OpenAPIFactory::merge);
    }

    protected OpenAPI readClass(Class<?> cls) {
        OpenAPI output = OpenAPIFactory.create();
        if (cls.isAnnotationPresent(Hidden.class) || !cls.isAnnotationPresent(RestController.class)) {
            return output;
        }

        String rootPath = ReflectionUtils.getAnnotation(cls, RestController.class).value();
        output.addTagsItem(new Tag().name(rootPath));
        output.setPaths(new Paths());

        stream(cls.getMethods())
                .flatMap(m -> readMethod(m, cls, rootPath))
                .forEach(namedPathItem -> output.getPaths().addPathItem(namedPathItem.name, namedPathItem.pathItem));

        return output;
    }

    protected Stream<NamedPathItem> readMethod(Method method, Class<?> cls, String rootPath) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (requestMapping == null || ReflectionUtils.isOverriddenMethod(method, cls) || method.isAnnotationPresent(Hidden.class)) {
            return Stream.empty();
        }

        String[] methodProduces = requestMapping.produces();
        String[] methodConsumes = requestMapping.consumes();
        String[] methodPaths = Stream.of(requestMapping.value(), requestMapping.path())
                .flatMap(Stream::of)
                .toArray(String[]::new);
        RequestMethod httpMethod = requestMapping.method()[0];

        String operationPath = rootPath + "/" + methodPaths[0];
        operationPath = PathUtils.parsePath(operationPath, new LinkedHashMap<>());
        if (operationPath == null) {
            return Stream.empty();
        }

        Operation operation = new Operation()
                .parameters(
                        stream(method.getParameters())
                                .flatMap(this::readParameter)
                                .collect(Collectors.toList())
                );
        // TODO set operation fields

        PathItem pathItem = OpenAPIFactory.createPathItem(httpMethod, operation);

        return Stream.of(new NamedPathItem(operationPath, pathItem));
    }

    protected Stream<Parameter> readParameter(java.lang.reflect.Parameter param) {
        if (param.isAnnotationPresent(Hidden.class)) {
            return Stream.empty();
        }

        RequestParam requestParam = AnnotatedElementUtils.findMergedAnnotation(param, RequestParam.class);
        PathVariable pathVariable = AnnotatedElementUtils.findMergedAnnotation(param, PathVariable.class);
        org.springframework.web.bind.annotation.RequestBody requestBody = AnnotatedElementUtils.findMergedAnnotation(param, org.springframework.web.bind.annotation.RequestBody.class);
        if (pathVariable == null && requestParam == null && requestBody == null) {
            return Stream.empty();
        }
        if (pathVariable != null && requestParam != null) { // TODO request body
            // TODO log that something is wrong
            return Stream.empty();
        }
        String name = requestParam != null ? requestParam.name() : pathVariable != null ? pathVariable.name() : "";
        return Stream.of(new Parameter()
                .name(name)
                .required(requestParam != null ? requestParam.required() : pathVariable != null ? pathVariable.required() : requestBody.required())
                .in(pathVariable != null ? "path" : requestParam != null ? "query" : "body")
        );
    }

    @RequiredArgsConstructor
    private static class NamedPathItem {
        final String name;
        final PathItem pathItem;
    }
}
