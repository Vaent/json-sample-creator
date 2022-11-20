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
            String type = tree.get("type").asText();
            switch (type) {
                case "array":
                    System.out.println(JsonArray.getEmpty());
                    break;
                case "boolean":
                    System.out.println(JsonBoolean.getRandom());
                    break;
                case "integer":
                    System.out.println(JsonInteger.getRandom());
                    break;
                case "null":
                    System.out.println(JsonNull.get());
                    break;
                case "number":
                    System.out.println(JsonNumber.getRandom());
                    break;
                case "object":
                    System.out.println(JsonObject.getEmpty());
                    break;
                case "string":
                    System.out.println(JsonString.getRandom());
                    break;
                default:
                    throw new IllegalArgumentException("Type declared in schema was not recognised");
            }
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Unable to parse argument as JSON");
        }
    }
}
