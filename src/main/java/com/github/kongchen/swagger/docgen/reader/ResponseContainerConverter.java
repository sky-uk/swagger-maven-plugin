package com.github.kongchen.swagger.docgen.reader;

import io.swagger.v3.core.models.properties.ArrayProperty;
import io.swagger.v3.core.models.properties.MapProperty;
import io.swagger.v3.core.models.properties.Property;

/**
 * Property wrapper for response container.
 */
class ResponseContainerConverter {
    Property withResponseContainer(String responseContainer, Property property) {
        if ("list".equalsIgnoreCase(responseContainer)) {
            return new ArrayProperty(property);
        }
        if ("set".equalsIgnoreCase(responseContainer)) {
            return new ArrayProperty(property).uniqueItems();
        }
        if ("map".equalsIgnoreCase(responseContainer)) {
            return new MapProperty(property);
        }
        return property;
    }
}
