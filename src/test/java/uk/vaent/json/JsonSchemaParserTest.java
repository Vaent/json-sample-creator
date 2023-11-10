package uk.vaent.json;

import static org.junit.jupiter.api.Assertions.*;
import static uk.vaent.json.TestHelper.schemaForType;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import uk.vaent.json.type.JsonType;

class JsonSchemaParserTest {
    @Test
    public void testGetValidTypes() {
        assertEquals(JsonType.ARRAY, JsonSchemaParser.getType(schemaForType("array")));
        assertEquals(JsonType.BOOLEAN, JsonSchemaParser.getType(schemaForType("boolean")));
        assertEquals(JsonType.INTEGER, JsonSchemaParser.getType(schemaForType("integer")));
        assertEquals(JsonType.NULL, JsonSchemaParser.getType(schemaForType("null")));
        assertEquals(JsonType.NUMBER, JsonSchemaParser.getType(schemaForType("number")));
        assertEquals(JsonType.OBJECT, JsonSchemaParser.getType(schemaForType("object")));
        assertEquals(JsonType.STRING, JsonSchemaParser.getType(schemaForType("string")));
    }

    @Test
    public void testGetTypeFromArray() {
        JsonType type = JsonSchemaParser.getType(schemaForType("boolean", "integer"));
        assertTrue(JsonType.BOOLEAN.equals(type) || JsonType.INTEGER.equals(type));
    }

    @Test
    public void testGetTypeWhenNotDeclared() {
        // empty object (type field not present) - all types are valid
        assertNotNull(JsonSchemaParser.getType(JsonNodeFactory.instance.objectNode()), "Absent type should produce a value");
        // empty type array - all types are valid
        assertNotNull(JsonSchemaParser.getType(schemaForType()), "Empty type array should produce a value");
        // JsonNode other than object cannot supply a type property
        assertNull(JsonSchemaParser.getType(JsonNodeFactory.instance.textNode("type:string")));
    }

    @Test
    public void testGetInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> JsonSchemaParser.getType(schemaForType("foo")));
    }

    @Test
    public void testGetImproperlyDeclaredType() {
        ObjectNode schema = JsonNodeFactory.instance.objectNode().put("type", 42);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> JsonSchemaParser.getType(schema));
        assertEquals(JsonSchemaParser.INVALID_TYPE_FORMAT_MESSAGE, ex.getMessage());
    }

    @Test
    public void testValidateArrayOfTypes() {
        assertTrue(JsonSchemaParser.validate(JsonType.BOOLEAN, schemaForType("boolean", "integer")));
        assertTrue(JsonSchemaParser.validate(JsonType.INTEGER, schemaForType("boolean", "integer")));
        assertFalse(JsonSchemaParser.validate(JsonType.ARRAY, schemaForType("boolean", "integer")));
        assertFalse(JsonSchemaParser.validate(JsonType.NUMBER, schemaForType("boolean", "integer")));
    }

    @Test
    public void testValidateSingleType() {
        assertTrue(JsonSchemaParser.validate(JsonType.ARRAY, schemaForType("array")));
        assertTrue(JsonSchemaParser.validate(JsonType.STRING, schemaForType("string")));
        assertFalse(JsonSchemaParser.validate(JsonType.NUMBER, schemaForType("boolean")));
        assertFalse(JsonSchemaParser.validate(JsonType.NULL, schemaForType("object")));
    }

    @Test
    public void testValidateNoTypeConstraint() {
        assertTrue(JsonSchemaParser.validate(JsonType.OBJECT, schemaForType()));
        assertTrue(JsonSchemaParser.validate(JsonType.NUMBER, schemaForType()));
        assertTrue(JsonSchemaParser.validate(JsonType.ARRAY, JsonNodeFactory.instance.objectNode()));
        assertTrue(JsonSchemaParser.validate(JsonType.NULL, JsonNodeFactory.instance.objectNode()));
    }
}