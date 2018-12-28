package com.github.kongchen.swagger.docgen.mavenplugin;

import com.google.common.collect.Sets;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ApiSourceTest {

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetExternalDocsNoneFound() {
        // given
        @OpenAPIDefinition
        class TestClassNoExternalDocs { }

        ApiSource apiSource = spy(ApiSource.class);
        when(apiSource.getValidClasses(OpenAPIDefinition.class)).thenReturn(Sets.newHashSet(TestClassNoExternalDocs.class));

        // when
        ExternalDocumentation externalDocs = apiSource.getExternalDocs();

        // then
        Assert.assertNull(externalDocs);
    }

    @Test
    public void testGetExternalDocsFound() {
        // given
        @OpenAPIDefinition(externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(description = "Example external docs", url = "https://example.com/docs"))
        class TestClassExternalDocs { }

        ApiSource apiSource = spy(ApiSource.class);
        when(apiSource.getValidClasses(OpenAPIDefinition.class)).thenReturn(Sets.newHashSet(TestClassExternalDocs.class));

        // when
        ExternalDocumentation externalDocs = apiSource.getExternalDocs();

        // then
        Assert.assertNotNull(externalDocs);
        Assert.assertEquals(externalDocs.getDescription(), "Example external docs");
        Assert.assertEquals(externalDocs.getUrl(), "https://example.com/docs");
    }

    @Test
    public void testGetInfo0VendorExtensions() {
        Map<String, Object> logo = new HashMap<String, Object>();
        logo.put("logo", "logo url");
        logo.put("description", "This is our logo.");

        Map<String, Object> website = new HashMap<String, Object>();
        website.put("website", "website url");
        website.put("description", "This is our website.");

        Map<String, Object> expectedExtensions = new HashMap<String, Object>();
        expectedExtensions.put("x-logo", logo);
        expectedExtensions.put("x-website", website);


        Set<Class<?>> validClasses = Sets.newHashSet(ApiSourceTestClass.class);
        ApiSource apiSource = spy(ApiSource.class);

        when(apiSource.getValidClasses(OpenAPIDefinition.class)).thenReturn(validClasses);
        Info info = apiSource.getInfo();

        Map<String, Object> vendorExtensions = info.getExtensions();
        Assert.assertEquals(vendorExtensions.size(), 2);
        Assert.assertEquals(vendorExtensions, expectedExtensions);
    }

    @OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(
            title = "ApiSourceTestClass",
            version = "1.0.0",
            extensions = {
                    @Extension(name = "logo", properties = {
                            @ExtensionProperty(name = "logo", value = "logo url"),
                            @ExtensionProperty(name = "description", value = "This is our logo.")
                    }),
                    @Extension(name = "website", properties = {
                            @ExtensionProperty(name = "website", value = "website url"),
                            @ExtensionProperty(name = "description", value = "This is our website.")
                    })
            }
    ))
    private static class ApiSourceTestClass {

    }
}
