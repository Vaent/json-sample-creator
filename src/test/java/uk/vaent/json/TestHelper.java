package uk.vaent.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestHelper {
    public static ObjectNode schemaFor(String typeName) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        return schema.put("type", typeName);
    }

    public static ObjectNode schemaFor(String[] typeNames) {
        ArrayNode typeOptions = JsonNodeFactory.instance.arrayNode().addAll(
            Arrays.stream(typeNames)
                .map(TextNode::new)
                .collect(Collectors.toSet()));
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        return schema.set("type", typeOptions);
    }
}
