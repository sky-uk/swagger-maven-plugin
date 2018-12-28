package com.github.kongchen.swagger.docgen.jaxrs;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import io.swagger.v3.jaxrs2.ResolvedParameter;
import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.*;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author chekong on 15/5/12.
 */
public class JaxrsParameterExtension extends AbstractOpenAPIExtension {

    @Override
    public ResolvedParameter extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip,
                                               Components components, javax.ws.rs.Consumes classConsumes,
                                               javax.ws.rs.Consumes methodConsumes, boolean includeRequestBody, JsonView jsonViewAnnotation, Iterator<OpenAPIExtension> chain) {
        ResolvedParameter resolvedParameter = new ResolvedParameter();
        if (this.shouldIgnoreType(type, typesToSkip)) {
            resolvedParameter.parameters = new ArrayList<>();
            return resolvedParameter;
        }

        ClassToInstanceMap<Annotation> annotationMap = toMap(annotations);

        List<Parameter> parameters = new ArrayList<>(extractParametersFromAnnotation(type, annotationMap));

        if (!parameters.isEmpty()) {
            resolvedParameter.parameters = parameters;
            return resolvedParameter;
        }
        return super.extractParameters(annotations, type, typesToSkip, components, classConsumes, methodConsumes, includeRequestBody, jsonViewAnnotation, chain);
    }

    private ClassToInstanceMap<Annotation> toMap(Collection<? extends Annotation> annotations) {
        ClassToInstanceMap<Annotation> annotationMap = MutableClassToInstanceMap.create();
        for (Annotation annotation : annotations) {
            if (annotation == null) {
                continue;
            }
            annotationMap.put(annotation.annotationType(), annotation);
        }

        return annotationMap;
    }

    private List<Parameter> extractParametersFromAnnotation(Type type, ClassToInstanceMap<Annotation> annotations) {
        String defaultValue = "";
        if (annotations.containsKey(DefaultValue.class)) {
            DefaultValue defaultValueAnnotation = annotations.getInstance(DefaultValue.class);
            defaultValue = defaultValueAnnotation.value();
        }

        List<Parameter> parameters = new ArrayList<>();
        if (annotations.containsKey(QueryParam.class)) {
            QueryParam param = annotations.getInstance(QueryParam.class);
            Parameter queryParameter = extractQueryParam(type, defaultValue, param);
            parameters.add(queryParameter);
        } else if (annotations.containsKey(PathParam.class)) {
            PathParam param = annotations.getInstance(PathParam.class);
            Parameter pathParameter = extractPathParam(type, defaultValue, param);
            parameters.add(pathParameter);
        } else if (annotations.containsKey(HeaderParam.class)) {
            HeaderParam param = annotations.getInstance(HeaderParam.class);
            Parameter headerParameter = extractHeaderParam(type, defaultValue, param);
            parameters.add(headerParameter);
        } else if (annotations.containsKey(CookieParam.class)) {
            CookieParam param = annotations.getInstance(CookieParam.class);
            Parameter cookieParameter = extractCookieParameter(type, defaultValue, param);
            parameters.add(cookieParameter);
        } else if (annotations.containsKey(FormParam.class)) {
            FormParam param = annotations.getInstance(FormParam.class);
            Parameter formParameter = extractFromParam(type, defaultValue, param);
            parameters.add(formParameter);
        }

        return parameters;
    }


    private Parameter extractFromParam(Type type, String defaultValue, FormParam param) {
        Schema schema = new Schema().type(type.getTypeName());
        schema.setDefault(defaultValue);
        return new QueryParameter()
                .name(param.value())
                .schema(schema);
    }

    private Parameter extractQueryParam(Type type, String defaultValue, QueryParam param) {
        Schema schema = new Schema().type(type.getTypeName());
        schema.setDefault(defaultValue);
        return new QueryParameter()
                .name(param.value())
                .schema(schema);
    }

    private Parameter extractPathParam(Type type, String defaultValue, PathParam param) {
        Schema schema = new Schema().type(type.getTypeName());
        schema.setDefault(defaultValue);
        return new PathParameter()
                .name(param.value());
    }

    private Parameter extractHeaderParam(Type type, String defaultValue, HeaderParam param) {
        Schema schema = new Schema().type(type.getTypeName());
        schema.setDefault(defaultValue);
        return new HeaderParameter()
                .name(param.value());
    }

    private Parameter extractCookieParameter(Type type, String defaultValue, CookieParam param) {
        Schema schema = new Schema().type(type.getTypeName());
        schema.setDefault(defaultValue);
        return new CookieParameter()
                .name(param.value());
    }

}
