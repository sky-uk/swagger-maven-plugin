package com.github.kongchen.swagger.docgen.mavenplugin.utils;

import io.swagger.v3.oas.models.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class OpenAPIFactory {
    public static OpenAPI create() {
        return new OpenAPI()
                .paths(new Paths())
                .tags(new ArrayList<>())
                .extensions(new HashMap<>())
                .externalDocs(new ExternalDocumentation());
    }

    public static PathItem createPathItem(RequestMethod httpMethod, Operation operation) {
        PathItem pathItem = new PathItem();
        switch (httpMethod) {
            case GET:
                pathItem.setGet(operation);
                break;
            case POST:
                pathItem.setPost(operation);
                break;
            case PATCH:
                pathItem.setPatch(operation);
                break;
            case PUT:
                pathItem.setPut(operation);
                break;
            case DELETE:
                pathItem.setDelete(operation);
                break;
            case HEAD:
                pathItem.setHead(operation);
                break;
            case TRACE:
                pathItem.setTrace(operation);
                break;
            case OPTIONS:
                pathItem.setOptions(operation);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
        return pathItem;
    }

    public static OpenAPI merge(OpenAPI fst, OpenAPI snd) {
        Objects.requireNonNull(fst);
        Objects.requireNonNull(snd);
        OpenAPI output = create();
        output.setTags(ListUtils.concat(fst.getTags(), snd.getTags()));
        output.getPaths().putAll(fst.getPaths());
        output.getPaths().putAll(snd.getPaths());
        return output;
    }
}
