package uk.vaent.json.type.array;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.NoSuchElementException;

public class ArrayContainsDefinition {
    private final JsonNode containsSchema;
    private final ArrayItemDefinitions itemDefinitions;

    public ArrayContainsDefinition(JsonNode parentSchema, ArrayItemDefinitions itemDefinitions) {
        this.itemDefinitions = itemDefinitions;
        containsSchema = parentSchema.get("contains");
    }

    public int firstPossibleMatchIndex() {
        if (containsSchema == null) return 0;
        int testIndex = 0;
        for (JsonNode definition : itemDefinitions) {
            if (containsSchema.equals(definition)) return testIndex;
            if (testIndex == itemDefinitions.tupleLength()) break;
            testIndex++;
        }
        throw matchNotFound();
    }

    public int lastPossibleMatchIndex() {
        if (containsSchema == null) {
            return itemDefinitions.isClosedTuple() ? (itemDefinitions.tupleLength() - 1) : Integer.MAX_VALUE;
        }
        int testIndex = 0;
        int mostRecentMatchIndex = -1;
        for (JsonNode definition : itemDefinitions) {
            if (containsSchema.equals(definition)) mostRecentMatchIndex = testIndex;
            if (testIndex == itemDefinitions.tupleLength()) break;
            testIndex++;
        }
        if (mostRecentMatchIndex >= itemDefinitions.tupleLength()) {
            return Integer.MAX_VALUE;
        } else if (mostRecentMatchIndex >= 0) {
            return mostRecentMatchIndex;
        }
        throw matchNotFound();
    }

    private NoSuchElementException matchNotFound() {
        return new NoSuchElementException("No item definition was found matching \"contains\": " + containsSchema);
    }
}
