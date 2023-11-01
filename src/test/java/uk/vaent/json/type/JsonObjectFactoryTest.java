package uk.vaent.json.type;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.vaent.json.ObjectSchemaBuilder;
import uk.vaent.json.config.JsonSampleCreatorConfig;

@SpringBootTest
class JsonObjectFactoryTest {
    @Autowired private JsonSampleCreatorConfig config;

    @Test
    public void testGetSampleForSchema() {
        JsonNode objectSchema = new ObjectSchemaBuilder()
            .setProperty("stringField", JsonType.STRING)
            .setProperty("booleanField", JsonType.BOOLEAN)
            .build();
        JsonNode sampleObject = config.getJsonSampleFactory(objectSchema).getSample();
        assertNotNull(sampleObject);
        assertTrue(sampleObject.path("stringField").isTextual());
        assertTrue(sampleObject.path("booleanField").isBoolean());
    }

    @Test
    public void testNestedObjectSchema() {
        JsonNode objectSchema = new ObjectSchemaBuilder()
            .setProperty("nestedObjectField", new ObjectSchemaBuilder()
                .setProperty("stringField", JsonType.STRING)
                .build())
            .build();
        JsonNode sampleObject = config.getJsonSampleFactory(objectSchema).getSample();
        assertNotNull(sampleObject);
        assertTrue(sampleObject.path("nestedObjectField").isObject());
        assertTrue(sampleObject.path("nestedObjectField").path("stringField").isTextual());
    }
}
