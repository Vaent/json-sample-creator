package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.config.JsonSampleCreatorConfig;
import uk.vaent.json.type.array.ArrayItemDefinitions;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonArrayFactory extends JsonTypeFactory {
    @Autowired private JsonSampleCreatorConfig config;
    @Autowired private Random random;

    private final ArrayItemDefinitions arrayItems;
    private int maxItems;
    private int minItems;

    public JsonArrayFactory(JsonNode schema, ArrayItemDefinitions itemDefinitionsSupplier) {
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
        Iterator<JsonNode> itemSchemas = arrayItems.iterator();
        int itemCount = 0;
        int targetItemCount = random.nextInt(minItems, maxItems + 1);
        while (itemCount++ < targetItemCount) {
            if (!itemSchemas.hasNext()) {
                if (sample.size() < minItems) System.out.println("Generated array size (" + sample.size()
                    + ") less than minItems (" + minItems + ") for schema " + schema);
                break;
            }
            sample.add(config.getJsonSampleFactory(itemSchemas.next()).getSample());
        }
        return sample;
    }
}
