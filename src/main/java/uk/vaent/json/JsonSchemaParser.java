package uk.vaent.json;

import com.fasterxml.jackson.databind.JsonNode;
import uk.vaent.json.type.JsonType;

public class JsonSchemaParser {
    public static final String INVALID_TYPE_FORMAT_MESSAGE = "Format of type definition in schema is invalid";
    public static final String INVALID_TYPE_NAME_MESSAGE = "Type declared in schema was not recognised";

    public static JsonType getType(JsonNode schema) {
        JsonNode type = schema.get("type");
        if (type == null) return null;
        if (type.isTextual()) {
            try {
                return JsonType.valueOf(type.asText().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(INVALID_TYPE_NAME_MESSAGE, ex);
            }
        } else {
            throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
        }
    }
}
