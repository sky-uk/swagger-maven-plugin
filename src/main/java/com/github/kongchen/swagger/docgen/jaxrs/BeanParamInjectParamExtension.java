package com.github.kongchen.swagger.docgen.jaxrs;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.kongchen.swagger.docgen.reader.AbstractReader;
import com.github.kongchen.swagger.docgen.reader.JaxrsReader;
import com.google.common.collect.Lists;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import io.swagger.v3.jaxrs2.ResolvedParameter;
import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.oas.models.Components;
import org.apache.commons.lang3.reflect.TypeUtils;

import javax.ws.rs.BeanParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This extension extracts the parameters inside a {@code @BeanParam} by
 * expanding the target bean type's fields/methods/constructor parameters and
 * recursively feeding them back through the {@link JaxrsReader}.
 *
 * @author chekong on 15/5/9.
 */
public class BeanParamInjectParamExtension extends AbstractOpenAPIExtension {


    private AbstractReader reader;

    public BeanParamInjectParamExtension(AbstractReader reader) {
        this.reader = reader;
    }

    @Override
    public ResolvedParameter extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip,
                                               Components components, javax.ws.rs.Consumes classConsumes,
                                               javax.ws.rs.Consumes methodConsumes, boolean includeRequestBody, JsonView jsonViewAnnotation, Iterator<OpenAPIExtension> chain) {
        Class<?> cls = TypeUtils.getRawType(type, type);
        ResolvedParameter resolvedParameter = new ResolvedParameter();

        if (shouldIgnoreClass(cls) || typesToSkip.contains(type)) {
            // stop the processing chain
            typesToSkip.add(type);
            resolvedParameter.parameters = Lists.newArrayList();
            return resolvedParameter;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof BeanParam || annotation instanceof InjectParam) {
                resolvedParameter.parameters = reader.extractTypes(cls, typesToSkip, Lists.newArrayList());
                return resolvedParameter;
            }
        }
        return super.extractParameters(annotations, type, typesToSkip, components, classConsumes, methodConsumes, includeRequestBody, jsonViewAnnotation, chain);
    }

    @Override
    public boolean shouldIgnoreClass(Class<?> cls) {
        return FormDataContentDisposition.class.equals(cls);
    }
}
