package uk.vaent.json.type.array;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.type.JsonType;

public class ArrayContainsDefinition {
    private final JsonNode containsSchema;
    private final ArrayItemDefinitions itemDefinitions;

    public final boolean matchesGeneralItems;
    public final List<Integer> matchingIndices;

    public ArrayContainsDefinition(JsonNode parentSchema, ArrayItemDefinitions itemDefinitions) {
        this.itemDefinitions = itemDefinitions;
        containsSchema = parentSchema.get("contains");
        matchingIndices = Collections.unmodifiableList(determineMatches());
        matchesGeneralItems = (matchingIndices.contains(itemDefinitions.tupleLength()));
    }

    private List<Integer> determineMatches() {
        List<Integer> matches = new ArrayList<>();
        int testIndex = 0;
        for (JsonNode definition : itemDefinitions) {
            if (containsIsSatisfiedBy(definition)) matches.add(testIndex);
            if (testIndex == itemDefinitions.tupleLength()) break;
            testIndex++;
        }
        if (matches.isEmpty()) throw new NoSuchElementException("No item definition was found matching \"contains\": " + containsSchema);
        return matches;
    }

    private boolean containsIsSatisfiedBy(JsonNode definition) {
        if (containsSchema == null || containsSchema.equals(definition)) return true;
        if (containsSchema.has("const")) {
            JsonNode constValue = containsSchema.get("const");
            if (definition.has("const")) return constValue.equals(definition.get("const"));
            return JsonSchemaParser.validate(JsonType.of(constValue), definition);
        }
        if (containsSchema.has("type")) {
            if (definition.has("const")) {
                return JsonSchemaParser.validate(JsonType.of(definition.get("const")), containsSchema);
            } else if (definition.has("type")) {
                return JsonSchemaParser.validate(JsonType.fromTextNode((TextNode)definition.get("type")), containsSchema);
            }
        }
        return false;
    }
}
