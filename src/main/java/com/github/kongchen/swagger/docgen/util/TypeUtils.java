package com.github.kongchen.swagger.docgen.util;

import io.swagger.v3.core.converter.AnnotatedType;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class TypeUtils {

    private static final List<String> PRIMITIVE_PROPERTIES = Arrays.asList("integer", "string", "number", "boolean", "array", "file");

    public static boolean isPrimitive(Type cls) {
        String propertyName = new AnnotatedType().type(cls).getPropertyName();
        return PRIMITIVE_PROPERTIES.contains(propertyName);
    }
}
