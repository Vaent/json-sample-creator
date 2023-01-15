package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
public class JsonArrayFactory implements JsonTypeFactory {
    public JsonNode getSampleFor(JsonNode schema) {
        if (!JsonSchemaParser.validate(JsonType.ARRAY, schema)) return null;
        return JsonNodeFactory.instance.arrayNode();
    }
}
