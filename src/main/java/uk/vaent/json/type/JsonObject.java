package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.vaent.json.JsonSampleCreator;
import uk.vaent.json.JsonSchemaParser;

public class JsonObject {
    public static ObjectNode getEmpty() {
        return JsonNodeFactory.instance.objectNode();
    }

    public static ObjectNode getSampleFor(JsonNode schema) {
        if (!JsonType.OBJECT.equals(JsonSchemaParser.getType(schema))) return null;
        ObjectNode sample = JsonNodeFactory.instance.objectNode();
        JsonNode propertiesSchema = schema.get("properties");
        if (propertiesSchema != null) {
            propertiesSchema.fields().forEachRemaining(entry -> {
                sample.set(entry.getKey(), JsonSampleCreator.getSampleFor(entry.getValue()));
            });
        }
        return sample;
    }
}
