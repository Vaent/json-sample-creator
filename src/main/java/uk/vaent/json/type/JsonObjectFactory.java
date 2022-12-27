package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.config.JsonSampleCreatorConfig;

@Component
public class JsonObjectFactory implements JsonTypeFactory {
    @Autowired
    private JsonSampleCreatorConfig config;

    @Override
    public ObjectNode getSampleFor(JsonNode schema) {
        if (!JsonType.OBJECT.equals(JsonSchemaParser.getType(schema))) return null;
        ObjectNode sample = JsonNodeFactory.instance.objectNode();
        JsonNode propertiesSchema = schema.get("properties");
        if (propertiesSchema != null) {
            propertiesSchema.fields().forEachRemaining(entry ->
                sample.set(entry.getKey(), config.getJsonSampleFactory(entry.getValue()).getSampleFor(entry.getValue()))
            );
        }
        return sample;
    }
}
