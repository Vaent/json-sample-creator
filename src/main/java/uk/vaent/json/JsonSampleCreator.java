package uk.vaent.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.vaent.json.type.JsonBoolean;
import uk.vaent.json.type.JsonNumber;
import uk.vaent.json.type.JsonString;

public class JsonSampleCreator {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String... args) {
        try {
            JsonNode tree = objectMapper.readTree(args[0]);
            String type = tree.get("type").asText();
            switch (type) {
                case "boolean":
                    System.out.println(JsonBoolean.getRandom());
                    break;
                case "number":
                    System.out.println(JsonNumber.getRandom());
                    break;
                case "string":
                    System.out.println(JsonString.getRandom());
                    break;
            }
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Unable to parse argument as JSON");
        }
    }
}
