package uk.vaent.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestHelper {
    public static ObjectNode schemaFor(String typeName) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        return schema.put("type", typeName);
    }
}
