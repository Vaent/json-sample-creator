package uk.vaent.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.vaent.json.type.*;

public class JsonSampleCreator {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String... args) {
        try {
            JsonNode tree = objectMapper.readTree(args[0]);
            System.out.println(getSampleFor(tree));
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Unable to parse argument as JSON");
        }
    }

    public static JsonNode getSampleFor(JsonNode schema) {
        return switch (JsonSchemaParser.getType(schema)) {
            case ARRAY -> JsonArray.getEmpty();
            case BOOLEAN -> JsonBoolean.getRandom();
            case INTEGER -> JsonInteger.getRandom();
            case NULL -> JsonNull.get();
            case NUMBER -> JsonNumber.getRandom();
            case OBJECT -> JsonObject.getSampleFor(schema);
            case STRING -> JsonString.getRandom();
        };
    }
}
