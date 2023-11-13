package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.config.JsonSampleCreatorConfig;
import uk.vaent.json.type.array.ArrayContainsDefinition;
import uk.vaent.json.type.array.ArrayItemDefinitions;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonArrayFactory extends JsonTypeFactory {
    @Autowired private JsonSampleCreatorConfig config;
    @Autowired private Random random;

    private final ArrayContainsDefinition arrayContains;
    private final ArrayItemDefinitions arrayItems;
    private int maxItems;
    private int minItems;

    public JsonArrayFactory(JsonNode schema, ArrayItemDefinitions itemDefinitionsSupplier) {
        super(schema);
        this.arrayItems = itemDefinitionsSupplier;
        arrayContains = new ArrayContainsDefinition(schema, itemDefinitionsSupplier);
    }

    @Autowired
    public void init(@Value("${arrayMaxItems}") int defaultMaxItems, @Value("${arrayMinItems}") int defaultMinItems) {
        Optional<Integer> schemaMaxItems = Optional.ofNullable(schema.get("maxItems")).map(JsonNode::intValue);
        Optional<Integer> schemaMinItems = Optional.ofNullable(schema.get("minItems")).map(JsonNode::intValue);
        if (arrayContains.matchingIndices.isEmpty()) {
            minItems = schemaMinItems.orElse(Math.min(defaultMinItems, schemaMaxItems.orElse(defaultMaxItems)));
        } else {
            int minItemCountToSatisfyContains = arrayContains.matchingIndices.get(0) + 1;
            minItems = schemaMinItems.isEmpty() ? minItemCountToSatisfyContains : Math.max(minItemCountToSatisfyContains, schemaMinItems.get());
        }
        maxItems = schemaMaxItems.orElse(Math.max(defaultMaxItems, minItems));
        if (minItems > maxItems) throw new IllegalArgumentException(
            "array minItems (" + minItems + ") cannot be larger than maxItems (" + maxItems + ")");
    }

    @Override
    protected JsonNode generateSample() {
        if (!JsonSchemaParser.validate(JsonType.ARRAY, schema)) return null;
        ArrayNode sample = JsonNodeFactory.instance.arrayNode();
        Iterator<JsonNode> itemSchemas = arrayItems.iterator();
        int targetItemCount = random.nextInt(minItems, maxItems + 1);
        List<Integer> containsMatchesWithinTargetCount = arrayContains.matchingIndices(targetItemCount);
        int indexSatisfyingContains = containsMatchesWithinTargetCount.get(random.nextInt(containsMatchesWithinTargetCount.size()));
        while (sample.size() < targetItemCount) {
            if (!itemSchemas.hasNext()) {
                if (sample.size() < minItems) System.out.println("Generated array size (" + sample.size()
                    + ") less than minItems (" + minItems + ") for schema " + schema);
                break;
            }
            if ((sample.size() == indexSatisfyingContains) && schema.has("contains")) {
                sample.add(config.getJsonSampleFactory(schema.get("contains")).getSample());
                itemSchemas.next();
            } else {
                sample.add(config.getJsonSampleFactory(itemSchemas.next()).getSample());
            }
        }
        return sample;
    }
}
