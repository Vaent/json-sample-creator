package uk.vaent.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Random;
import uk.vaent.json.type.JsonType;

public class JsonSchemaParser {
    public static final String INVALID_TYPE_FORMAT_MESSAGE = "Format of type definition in schema is invalid";
    public static final String INVALID_TYPE_NAME_MESSAGE = "Type declared in schema was not recognised";

    private static final Random random = new Random();

    public static JsonType getType(JsonNode schema) {
        JsonNode type = schema.get("type");
        if (type == null) return null;
        if (type.isTextual()) {
            try {
                return JsonType.valueOf(type.asText().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(INVALID_TYPE_NAME_MESSAGE, ex);
            }
        } else if (type.isArray()) {
            type.forEach(t -> {
                if (!t.isTextual()) throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
                try {
                    JsonType.valueOf(t.asText().toUpperCase());
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException(INVALID_TYPE_NAME_MESSAGE, ex);
                }
            });
            JsonNode randomType = type.get(random.nextInt(type.size()));
            return JsonType.valueOf(randomType.asText().toUpperCase());
        } else {
            throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
        }
    }

    public static boolean validate(JsonType type, JsonNode schema) {
        JsonNode schemaType = schema.get("type");
        if (schemaType.isTextual()) {
            return type.equals(JsonType.valueOf(schemaType.asText().toUpperCase()));
        } else if (schemaType.isArray()) {
            for (JsonNode t : schemaType) {
                if (type.equals(JsonType.valueOf(t.asText().toUpperCase()))) return true;
            }
            return false;
        } else {
            throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
        }
    }
}
