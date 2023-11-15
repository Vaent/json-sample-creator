package uk.vaent.json.type;

import static uk.vaent.json.JsonSchemaKeyword.CONST;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;

public abstract class JsonTypeFactory {
    protected final JsonNode schema;

    public JsonTypeFactory(JsonNode schema) {
        this.schema = schema;
    }

    protected Optional<JsonNode> constantValue() {
        return Optional.ofNullable(schema.get(CONST));
    }

    public JsonNode getSample() {
        return constantValue().orElse(generateSample());
    }

    protected abstract JsonNode generateSample();
}
