package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonTypeFactory {
    public JsonNode getSampleFor(JsonNode schema);
}
