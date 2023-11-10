package uk.vaent.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestHelper {
    public static ObjectNode schemaForConstant(Object value) {
        if (value == null) {
            return JsonNodeFactory.instance.objectNode().putNull("const");
        } else if (String.class.equals(value.getClass())) {
            return JsonNodeFactory.instance.objectNode().put("const", (String) value);
        } else if (Integer.class.equals(value.getClass())) {
            return JsonNodeFactory.instance.objectNode().put("const", (int) value);
        }
        throw new IllegalArgumentException("Requested schema for unknown constant type: " + value);
    }

    public static ObjectNode[] schemasForConstants(Object... values) {
        ObjectNode[] arr = new ObjectNode[values.length];
        for (int i = 0; i < values.length; i++) {
            arr[i] = schemaForConstant(values[i]);
        }
        return arr;
    }

    public static ObjectNode schemaForType(String... typeNames) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        if (typeNames.length == 1) {
            return schema.put("type", typeNames[0]);
        } else {
            ArrayNode typeOptions = JsonNodeFactory.instance.arrayNode().addAll(
                Arrays.stream(typeNames)
                    .map(TextNode::new)
                    .collect(Collectors.toSet()));
            return schema.set("type", typeOptions);
        }
    }
}
