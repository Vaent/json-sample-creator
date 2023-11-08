package uk.vaent.json.type.array;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.NoSuchElementException;

public class ArrayContainsDefinition {
    private final JsonNode containsSchema;
    private final ArrayItemDefinitions itemDefinitionsSupplier;

    public ArrayContainsDefinition(JsonNode parentSchema, ArrayItemDefinitions itemDefinitionsSupplier) {
        this.itemDefinitionsSupplier = itemDefinitionsSupplier;
        containsSchema = parentSchema.get("contains");
    }

    public int firstPossibleMatchIndex() {
        if (containsSchema == null) return 0;
        int testIndex = 0;
        for (JsonNode definition : itemDefinitionsSupplier) {
            if (containsSchema.equals(definition)) return testIndex;
            testIndex++;
        }
        throw new NoSuchElementException("No item definition was found matching the \"contains\" schema");
    }

    public int lastPossibleMatchIndex() {
        int testIndex = 0;
        int mostRecentMatchIndex = -1;
        for (JsonNode definition : itemDefinitionsSupplier) {
            if (containsSchema.equals(definition)) mostRecentMatchIndex = testIndex;
            testIndex++;
        }
        if (mostRecentMatchIndex >= 0) return mostRecentMatchIndex;
        throw new NoSuchElementException("No item definition was found matching the \"contains\" schema");
    }
}
