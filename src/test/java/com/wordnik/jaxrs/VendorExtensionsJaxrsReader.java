package com.wordnik.jaxrs;

import com.github.kongchen.swagger.docgen.reader.JaxrsReader;
import com.wordnik.sample.TestVendorExtension;
import io.swagger.v3.core.jaxrs.ext.SwaggerExtension;
import io.swagger.v3.core.jaxrs.ext.SwaggerExtensions;
import io.swagger.v3.core.models.Swagger;
import org.apache.maven.plugin.logging.Log;

import java.util.LinkedList;
import java.util.List;

public class VendorExtensionsJaxrsReader extends JaxrsReader {

    public VendorExtensionsJaxrsReader(Swagger swagger, Log LOG) {
        super(swagger, LOG);

        List<SwaggerExtension> extensions = new LinkedList<SwaggerExtension>(SwaggerExtensions.getExtensions());
        extensions.add(new TestVendorExtension());
        SwaggerExtensions.setExtensions(extensions);
    }
}
