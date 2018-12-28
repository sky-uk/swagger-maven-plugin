package com.github.kongchen.swagger.docgen;

import com.github.kongchen.swagger.docgen.mavenplugin.ApiSource;
import com.github.kongchen.swagger.docgen.reader.ClassSwaggerReader;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AbstractDocumentSourceTest {
    @Mock
    private Log log;
    @Mock
    private ApiSource apiSource;

    private AbstractDocumentSource source;

    @BeforeMethod
    public void setUp() throws MojoFailureException {
        MockitoAnnotations.initMocks(this);
        source = new AbstractDocumentSource(log, apiSource) {
            @Override
            protected ClassSwaggerReader resolveApiReader() throws GenerateException {
                return null;
            }
        };
    }

    @Test
    public void testExternalDocsGetAdded() throws MojoFailureException {
        // arrange
        Mockito.when(apiSource.getExternalDocs()).thenReturn(new ExternalDocumentation()
                .description("Example external docs")
                .url("https://example.com/docs")
        );

        // act
        AbstractDocumentSource externalDocsSource = new AbstractDocumentSource(log, apiSource) {
            @Override
            protected ClassSwaggerReader resolveApiReader() throws GenerateException {
                return null;
            }
        };

        // assert
        assertThat(externalDocsSource.swagger.getExternalDocs(), notNullValue());
        assertThat(externalDocsSource.swagger.getExternalDocs().getDescription(), equalTo("Example external docs"));
        assertThat(externalDocsSource.swagger.getExternalDocs().getUrl(), equalTo("https://example.com/docs"));
    }
}
