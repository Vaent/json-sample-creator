package uk.vaent.json.type.array;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

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
            if (containsSchema == null || containsSchema.equals(definition)) matches.add(testIndex);
            if (testIndex == itemDefinitions.tupleLength()) break;
            testIndex++;
        }
        if (matches.isEmpty()) throw new NoSuchElementException("No item definition was found matching \"contains\": " + containsSchema);
        return matches;
    }
}
