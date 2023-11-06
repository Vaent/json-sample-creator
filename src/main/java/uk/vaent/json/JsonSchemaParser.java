package uk.vaent.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import uk.vaent.json.type.JsonType;

public class JsonSchemaParser {
    public static final String INVALID_TYPE_FORMAT_MESSAGE = "Format of type definition in schema is invalid";
    public static final String INVALID_TYPE_NAME_MESSAGE = "Type declared in schema was not recognised";
    private static final JsonType[] jsonTypesCached = JsonType.values();
    private static final List<JsonType> jsonValueTypes = Stream.of(JsonType.values())
        .filter(type -> (type != JsonType.ARRAY) && (type != JsonType.OBJECT))
        .toList();

    private static final Random random = new Random();

    public static JsonType getType(JsonNode schema) {
        if (BooleanNode.TRUE.equals(schema)) return selectFromValueTypes(); // all values are valid for the `true` schema
        if (!schema.isObject()) return null;
        JsonNode type = schema.get("type");
        if (type == null) {
            return selectFromValueTypes();
        } else if (type.isTextual()) {
            try {
                return JsonType.valueOf(type.textValue().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(INVALID_TYPE_NAME_MESSAGE, ex);
            }
        } else if (type.isArray()) {
            if (type.isEmpty()) return selectFromValueTypes();
            type.forEach(t -> {
                if (!t.isTextual()) throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
                try {
                    JsonType.valueOf(t.textValue().toUpperCase());
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException(INVALID_TYPE_NAME_MESSAGE, ex);
                }
            });
            JsonNode randomType = type.get(random.nextInt(type.size()));
            return JsonType.valueOf(randomType.textValue().toUpperCase());
        } else {
            throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
        }
    }

    /* This legacy method is preserved, but should be avoided in favour of selectFromValueTypes()
     unless you are somehow enforcing a hard limit on generation of nested containers. */
    private static JsonType selectFromAllTypes() {
        return jsonTypesCached[random.nextInt(jsonTypesCached.length)];
    }

    private static JsonType selectFromValueTypes() {
        return jsonValueTypes.get(random.nextInt(jsonValueTypes.size()));
    }

    public static boolean validate(JsonType type, JsonNode schema) {
        if (BooleanNode.TRUE.equals(schema)) return true; // all values are valid for the `true` schema
        if (!schema.isObject()) return false;
        JsonNode schemaType = schema.get("type");
        if (schemaType == null) {
            return true;
        } else if (schemaType.isTextual()) {
            return type.equals(JsonType.valueOf(schemaType.textValue().toUpperCase()));
        } else if (schemaType.isArray()) {
            if (schemaType.isEmpty()) return true;
            for (JsonNode t : schemaType) {
                if (type.equals(JsonType.valueOf(t.textValue().toUpperCase()))) return true;
            }
            return false;
        } else {
            throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
        }
    }
}
