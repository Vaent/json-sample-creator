package uk.vaent.json;

import static uk.vaent.json.JsonSchemaKeyword.CONST;
import static uk.vaent.json.JsonSchemaKeyword.TYPE;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Arrays;
import java.util.stream.Collectors;
import uk.vaent.json.type.JsonType;

public class TestHelper {
    public static ObjectNode schemaForConstant(Object value) {
        if (value == null) {
            return JsonNodeFactory.instance.objectNode().putNull(CONST);
        } else if (String.class.equals(value.getClass())) {
            return JsonNodeFactory.instance.objectNode().put(CONST, (String) value);
        } else if (Integer.class.equals(value.getClass())) {
            return JsonNodeFactory.instance.objectNode().put(CONST, (int) value);
        } else if (Boolean.class.equals(value.getClass())) {
            return JsonNodeFactory.instance.objectNode().put(CONST, (boolean) value);
        } else if (ObjectNode.class.equals(value.getClass())) {
            return JsonNodeFactory.instance.objectNode().set(CONST, (ObjectNode) value);
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

    public static ObjectNode schemaForType() {
        return JsonNodeFactory.instance.objectNode().set(TYPE, JsonNodeFactory.instance.arrayNode());
    }

    public static ObjectNode schemaForType(String... typeNames) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        if (typeNames.length == 1) {
            return schema.put(TYPE, typeNames[0]);
        } else {
            ArrayNode typeOptions = JsonNodeFactory.instance.arrayNode().addAll(
                Arrays.stream(typeNames)
                    .map(TextNode::new)
                    .collect(Collectors.toSet()));
            return schema.set(TYPE, typeOptions);
        }
    }

    public static ObjectNode schemaForType(JsonType... types) {
        String[] typeNames = Arrays.stream(types).map(t -> t.name().toLowerCase()).toArray(String[]::new);
        return schemaForType(typeNames);
    }
}
