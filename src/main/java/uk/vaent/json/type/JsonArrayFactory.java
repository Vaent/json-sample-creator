package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.Iterator;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.config.JsonSampleCreatorConfig;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonArrayFactory extends JsonTypeFactory {
    @Autowired private JsonSampleCreatorConfig config;

    private final Iterable<JsonNode> arrayItems;
    private int maxItems;
    private int minItems;

    public JsonArrayFactory(JsonNode schema, Iterable<JsonNode> itemDefinitionsSupplier) {
        super(schema);
        this.arrayItems = itemDefinitionsSupplier;
    }

    @Autowired
    public void init(@Value("${arrayMaxItems}") int defaultMaxItems, @Value("${arrayMinItems}") int defaultMinItems) {
        Optional<Integer> schemaMaxItems = Optional.ofNullable(schema.get("maxItems")).map(JsonNode::intValue);
        Optional<Integer> schemaMinItems = Optional.ofNullable(schema.get("minItems")).map(JsonNode::intValue);
        maxItems = schemaMaxItems.orElse(Math.max(defaultMaxItems, schemaMinItems.orElse(defaultMinItems)));
        minItems = schemaMinItems.orElse(Math.min(defaultMinItems, maxItems));
        if (minItems > maxItems) throw new IllegalArgumentException(
            "array minItems (" + minItems + ") cannot be larger than maxItems (" + maxItems + ")");
    }

    @Override
    protected JsonNode generateSample() {
        if (!JsonSchemaParser.validate(JsonType.ARRAY, schema)) return null;
        ArrayNode sample = JsonNodeFactory.instance.arrayNode();
        int itemCount = 0;
        Iterator<JsonNode> itemSchemas = arrayItems.iterator();
        while (itemCount++ < maxItems) {
            if (!itemSchemas.hasNext()) break;
            sample.add(config.getJsonSampleFactory(itemSchemas.next()).getSample());
        }
        return sample;
    }
}
