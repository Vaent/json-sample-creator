package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
public class JsonNullFactory implements JsonTypeFactory {
    @Override
    public JsonNode getSampleFor(JsonNode schema) {
        if (!JsonType.NULL.equals(JsonSchemaParser.getType(schema))) return null;
        return NullNode.getInstance();
    }
}
