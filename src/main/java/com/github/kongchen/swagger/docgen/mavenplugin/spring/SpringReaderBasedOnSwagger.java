package com.github.kongchen.swagger.docgen.mavenplugin.spring;

import com.github.kongchen.swagger.docgen.mavenplugin.utils.OpenAPIFactory;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * Attempt at adding Spring support to the {@link Reader} provided by Swagger.
 *
 * I don't think this is doable at all. I'm more confident in {@link SpringReaderFromScratch}.
 */
public class SpringReaderBasedOnSwagger extends Reader {

    @Override
    public OpenAPI read(
            Class<?> cls,
            String parentPath,
            String parentMethod,
            boolean isSubresource,
            RequestBody parentRequestBody,
            ApiResponses parentResponses,
            Set<String> parentTags,
            List<Parameter> parentParameters,
            Set<Class<?>> scannedResources
    ) {
        if (cls.getAnnotation(Hidden.class) != null) {
            return getOpenAPI();
        }

        if (cls.isAnnotationPresent(RestController.class)) {

            if (isSubresource) {
                return readSubResource(cls);
            } else {
                if (getOpenAPI().getPaths() == null) {
                    getOpenAPI().setPaths(new Paths());
                }

                return readRootResource(cls);
            }
        }

        return super.read(cls, parentPath, parentMethod, isSubresource, parentRequestBody, parentResponses, parentTags, parentParameters, scannedResources);
    }

    private OpenAPI readRootResource(Class<?> cls) {
        String rootPath = ReflectionUtils.getAnnotation(cls, RestController.class).value();
        stream(cls.getMethods())
                .filter(m -> !isOperationHidden(m))
                .filter(m -> !ReflectionUtils.isOverriddenMethod(m, cls))
                .forEach(method -> {
                    RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                    if (requestMapping == null)
                        return;

                    String[] methodProduces = requestMapping.produces();
                    String[] methodConsumes = requestMapping.consumes();
                    String[] methodPaths = Stream.of(requestMapping.value(), requestMapping.path())
                            .flatMap(Stream::of)
                            .toArray(String[]::new);
                    RequestMethod httpMethod = requestMapping.method()[0];

                    String operationPath = rootPath + "/" + methodPaths[0];
                    operationPath = PathUtils.parsePath(operationPath, new LinkedHashMap<>());
                    if (operationPath == null)
                        return;

                    Operation operation = new Operation()
                            .parameters(
                                    stream(method.getParameters())
                                            .filter(p -> p.getAnnotation(Hidden.class) == null)
                                            .map(param -> {
                                                RequestParam requestParam = AnnotatedElementUtils.findMergedAnnotation(param, RequestParam.class);
                                                PathVariable pathVariable = AnnotatedElementUtils.findMergedAnnotation(param, PathVariable.class);
                                                org.springframework.web.bind.annotation.RequestBody requestBody = AnnotatedElementUtils.findMergedAnnotation(param, org.springframework.web.bind.annotation.RequestBody.class);
                                                if (pathVariable == null && requestParam == null && requestBody == null) {
                                                    return null;
                                                }
                                                if (pathVariable != null && requestParam != null) { // TODO request body
                                                    // TODO log that something is wrong
                                                    return null;
                                                }
                                                String name = requestParam != null ? requestParam.name() : pathVariable != null ? pathVariable.name() : "";
                                                return new Parameter()
                                                        .name(name)
                                                        .required(requestParam != null ? requestParam.required() : pathVariable != null ? pathVariable.required() : requestBody.required())
                                                        .in(pathVariable != null ? "path" : requestParam != null ? "query" : "body");
                                            })
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toList())
                            );
                    // TODO set operation fields


                    PathItem pathItem = OpenAPIFactory.createPathItem(httpMethod, operation);

                    getOpenAPI().getPaths().addPathItem(operationPath, pathItem);
                });

        return super.read(cls, rootPath, null, false, null, null, null, null, null);
    }

    private OpenAPI readSubResource(Class<?> cls) {

        return null;
    }


}
