package uk.vaent.json.type;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import uk.vaent.json.ObjectSchemaBuilder;

class JsonObjectTest {
    @Test
    public void testGetSampleForSchema() {
        ObjectNode objectSchema = new ObjectSchemaBuilder()
            .setProperty("stringField", JsonType.STRING)
            .setProperty("booleanField", JsonType.BOOLEAN)
            .build();
        ObjectNode sampleObject = JsonObject.getSampleFor(objectSchema);
        assertNotNull(sampleObject);
        assertTrue(sampleObject.path("stringField").isTextual());
        assertTrue(sampleObject.path("booleanField").isBoolean());
    }

    @Test
    public void testNestedObjectSchema() {
        ObjectNode nestedObjectSchema = new ObjectSchemaBuilder()
            .setProperty("stringField", JsonType.STRING)
            .build();
        ObjectNode objectSchema = new ObjectSchemaBuilder()
            .setProperty("nestedObjectField", nestedObjectSchema)
            .build();
        ObjectNode sampleObject = JsonObject.getSampleFor(objectSchema);
        assertNotNull(sampleObject);
        assertTrue(sampleObject.path("nestedObjectField").isObject());
        assertTrue(sampleObject.path("nestedObjectField").path("stringField").isTextual());
    }
}
