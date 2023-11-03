package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;

public abstract class JsonTypeFactory {
    protected final JsonNode schema;

    public JsonTypeFactory(JsonNode schema) {
        this.schema = schema;
    }

    protected Optional<JsonNode> constantValue() {
        return Optional.ofNullable(schema.get("const"));
    }

    public JsonNode getSample() {
        return constantValue().orElse(generateSample());
    }

    protected abstract JsonNode generateSample();
}
