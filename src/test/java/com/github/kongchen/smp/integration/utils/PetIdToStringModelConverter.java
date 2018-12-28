package com.github.kongchen.smp.integration.utils;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.AbstractModelConverter;
import io.swagger.v3.core.models.properties.Property;
import io.swagger.v3.core.util.Json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * A ModelConverter used for testing adding custom model converters.
 */
public class PetIdToStringModelConverter extends AbstractModelConverter {

    public PetIdToStringModelConverter() {
        super(Json.mapper().copy());
    }

    @Override
    public Property resolveProperty(Type type, ModelConverterContext modelConverterContext, Annotation[] annotations, Iterator<ModelConverter> iterator) {
        try {
            Type expectedType = _mapper.constructType(Class.forName("com.wordnik.sample.model.PetId"));
            if (type.equals(expectedType)) {
                return super.resolveProperty(_mapper.constructType(Class.forName("java.lang.String")), modelConverterContext, annotations, iterator);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return super.resolveProperty(type, modelConverterContext, annotations, iterator);
    }
}
