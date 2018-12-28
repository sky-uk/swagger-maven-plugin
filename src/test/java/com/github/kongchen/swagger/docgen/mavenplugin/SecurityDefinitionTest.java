package com.github.kongchen.swagger.docgen.mavenplugin;

import com.github.kongchen.swagger.docgen.GenerateException;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class SecurityDefinitionTest {
    @Test
    public void testSecurityDefinitionRetainsWantedName() throws GenerateException {
        SecurityDefinition definition = new SecurityDefinition();
        definition.setJson("securityDefinition.json");

        /*Map<String, SecuritySchemeDefinition> definitions =*/
        Map<String, SecurityScheme> definitions = definition.generateSecuritySchemeDefinitions();

        SecurityScheme api_key = definitions.get("api_key");
        Assert.assertNotNull(api_key);
        Assert.assertEquals(api_key.getName(), "api_key_name");


        // No name is set for this auth
        // The name should be set to the name of the definition
        // So that the name is never actually empty
        SecurityScheme api_key_empty_name = definitions.get("api_key_empty_name");
        Assert.assertNotNull(api_key_empty_name);
        Assert.assertEquals(api_key_empty_name.getName(), "api_key_empty_name");


        SecurityScheme petstore_auth = definitions.get("petstore_auth");
        Assert.assertNotNull(petstore_auth);
        Assert.assertEquals(petstore_auth.getType(), SecurityScheme.Type.OAUTH2);
    }
}
