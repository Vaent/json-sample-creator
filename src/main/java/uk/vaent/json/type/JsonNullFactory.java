package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
public class JsonNullFactory implements JsonTypeFactory {
    @Override
    public JsonNode getSampleFor(JsonNode schema) {
        if (!JsonSchemaParser.validate(JsonType.NULL, schema)) return null;
        return NullNode.getInstance();
    }
}
