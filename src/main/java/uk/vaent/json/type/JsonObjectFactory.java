package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.config.JsonSampleCreatorConfig;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonObjectFactory extends JsonTypeFactory {
    @Autowired
    private JsonSampleCreatorConfig config;

    public JsonObjectFactory(JsonNode schema) {
        super(schema);
    }

    @Override
    protected ObjectNode generateSample() {
        if (!JsonSchemaParser.validate(JsonType.OBJECT, schema)) return null;
        ObjectNode sample = JsonNodeFactory.instance.objectNode();
        JsonNode propertiesSchema = schema.get("properties");
        if (propertiesSchema != null) {
            propertiesSchema.fields().forEachRemaining(entry ->
                sample.set(entry.getKey(), config.getJsonSampleFactory(entry.getValue()).getSample())
            );
        }
        return sample;
    }
}
