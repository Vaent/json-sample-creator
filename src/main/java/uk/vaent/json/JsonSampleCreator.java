package uk.vaent.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSampleCreator {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String... args) {
        try {
            JsonNode tree = objectMapper.readTree(args[0]);
            String type = tree.get("type").asText();
            switch (type) {
                case "boolean":
                    System.out.println(true);
                    break;
                case "number":
                    System.out.println(42);
                    break;
                case "string":
                    System.out.println("\"Hello JSON\"");
                    break;
            }
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Unable to parse argument as JSON");
        }
    }
}
