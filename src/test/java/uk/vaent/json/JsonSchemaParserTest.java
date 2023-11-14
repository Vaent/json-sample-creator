package uk.vaent.json;

import static org.junit.jupiter.api.Assertions.*;
import static uk.vaent.json.JsonSchemaKeyword.TYPE;
import static uk.vaent.json.TestHelper.schemaForConstant;
import static uk.vaent.json.TestHelper.schemaForType;
import static uk.vaent.json.type.JsonType.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import uk.vaent.json.type.JsonType;

class JsonSchemaParserTest {
    @Test
    public void testGetValidTypes() {
        assertEquals(ARRAY, JsonSchemaParser.getType(schemaForType(ARRAY)));
        assertEquals(BOOLEAN, JsonSchemaParser.getType(schemaForType(BOOLEAN)));
        assertEquals(INTEGER, JsonSchemaParser.getType(schemaForType(INTEGER)));
        assertEquals(NULL, JsonSchemaParser.getType(schemaForType(NULL)));
        assertEquals(NUMBER, JsonSchemaParser.getType(schemaForType(NUMBER)));
        assertEquals(OBJECT, JsonSchemaParser.getType(schemaForType(OBJECT)));
        assertEquals(STRING, JsonSchemaParser.getType(schemaForType(STRING)));
    }

    @Test
    public void testGetTypeFromArray() {
        JsonType type = JsonSchemaParser.getType(schemaForType(BOOLEAN, INTEGER));
        assertTrue(BOOLEAN.equals(type) || INTEGER.equals(type));
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
        ObjectNode schema = JsonNodeFactory.instance.objectNode().put(TYPE, 42);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> JsonSchemaParser.getType(schema));
        assertEquals(JsonSchemaParser.INVALID_TYPE_FORMAT_MESSAGE, ex.getMessage());
    }

    @Test
    public void testValidateArrayOfTypes() {
        assertTrue(JsonSchemaParser.validate(BOOLEAN, schemaForType(BOOLEAN, INTEGER)));
        assertTrue(JsonSchemaParser.validate(INTEGER, schemaForType(BOOLEAN, INTEGER)));
        assertFalse(JsonSchemaParser.validate(ARRAY, schemaForType(BOOLEAN, INTEGER)));
        assertFalse(JsonSchemaParser.validate(NUMBER, schemaForType(BOOLEAN, INTEGER)));
    }

    @Test
    public void testValidateSingleType() {
        assertTrue(JsonSchemaParser.validate(ARRAY, schemaForType(ARRAY)));
        assertTrue(JsonSchemaParser.validate(STRING, schemaForType(STRING)));
        assertFalse(JsonSchemaParser.validate(NUMBER, schemaForType(BOOLEAN)));
        assertFalse(JsonSchemaParser.validate(NULL, schemaForType(OBJECT)));
    }

    @Test
    public void testValidateNoTypeConstraint() {
        assertTrue(JsonSchemaParser.validate(OBJECT, schemaForType()));
        assertTrue(JsonSchemaParser.validate(NUMBER, schemaForType()));
        assertTrue(JsonSchemaParser.validate(ARRAY, JsonNodeFactory.instance.objectNode()));
        assertTrue(JsonSchemaParser.validate(NULL, JsonNodeFactory.instance.objectNode()));
    }

    @Test
    public void testValidateConstantValue() {
        assertTrue(JsonSchemaParser.validate(NUMBER, schemaForConstant(42)));
        assertFalse(JsonSchemaParser.validate(STRING, schemaForConstant(42)));
        assertTrue(JsonSchemaParser.validate(STRING, schemaForConstant("Any string value")));
        assertFalse(JsonSchemaParser.validate(NULL, schemaForConstant("Any string value")));
        assertTrue(JsonSchemaParser.validate(NULL, schemaForConstant(null)));
        assertFalse(JsonSchemaParser.validate(BOOLEAN, schemaForConstant(null)));
        assertTrue(JsonSchemaParser.validate(BOOLEAN, schemaForConstant(false)));
        assertFalse(JsonSchemaParser.validate(OBJECT, schemaForConstant(false)));
        assertTrue(JsonSchemaParser.validate(OBJECT, schemaForConstant(JsonNodeFactory.instance.objectNode())));
        assertFalse(JsonSchemaParser.validate(ARRAY, schemaForConstant(JsonNodeFactory.instance.objectNode())));
    }

    @Test
    public void testIntersectionWithConstantValue() {
        JsonNode constSchema = schemaForConstant(42);
        assertEquals(constSchema, JsonSchemaParser.getIntersection(constSchema, constSchema.deepCopy()));
        assertEquals(constSchema, JsonSchemaParser.getIntersection(constSchema, schemaForType(INTEGER)));
        assertEquals(constSchema, JsonSchemaParser.getIntersection(constSchema, schemaForType(NUMBER)));
        assertEquals(constSchema, JsonSchemaParser.getIntersection(schemaForType(INTEGER), constSchema));
        assertEquals(constSchema, JsonSchemaParser.getIntersection(schemaForType(NUMBER), constSchema));
    }
}