package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class JsonTypeFactory {
    protected final JsonNode schema;

    public JsonTypeFactory(JsonNode schema) {
        this.schema = schema;
    }

    public abstract JsonNode getSample();
}
