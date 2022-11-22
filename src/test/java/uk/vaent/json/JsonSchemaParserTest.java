package uk.vaent.json;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import uk.vaent.json.type.JsonType;

class JsonSchemaParserTest {
    @Test
    public void testGetValidTypes() {
        assertEquals(JsonType.ARRAY, JsonSchemaParser.getType(schemaFor("array")));
        assertEquals(JsonType.BOOLEAN, JsonSchemaParser.getType(schemaFor("boolean")));
        assertEquals(JsonType.INTEGER, JsonSchemaParser.getType(schemaFor("integer")));
        assertEquals(JsonType.NULL, JsonSchemaParser.getType(schemaFor("null")));
        assertEquals(JsonType.NUMBER, JsonSchemaParser.getType(schemaFor("number")));
        assertEquals(JsonType.OBJECT, JsonSchemaParser.getType(schemaFor("object")));
        assertEquals(JsonType.STRING, JsonSchemaParser.getType(schemaFor("string")));
    }

    @Test
    public void testGetTypeWhenNotDeclared() {
        // empty object - type field not present
        assertNull(JsonSchemaParser.getType(JsonNodeFactory.instance.objectNode()));
        // JsonNode other than object cannot supply a type property
        assertNull(JsonSchemaParser.getType(JsonNodeFactory.instance.textNode("type:string")));
    }

    @Test
    public void testGetInvalidType() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> JsonSchemaParser.getType(schemaFor("foo")));
        assertEquals(JsonSchemaParser.INVALID_TYPE_NAME_MESSAGE, ex.getMessage());
    }

    @Test
    public void testGetImproperlyDeclaredType() {
        ObjectNode schema = JsonNodeFactory.instance.objectNode().put("type", 42);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> JsonSchemaParser.getType(schema));
        assertEquals(JsonSchemaParser.INVALID_TYPE_FORMAT_MESSAGE, ex.getMessage());
    }

    // helpers

    private ObjectNode schemaFor(String typeName) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        return schema.put("type", typeName);
    }
}