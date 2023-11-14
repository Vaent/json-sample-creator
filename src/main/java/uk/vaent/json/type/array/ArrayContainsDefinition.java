package uk.vaent.json.type.array;

import static uk.vaent.json.JsonSchemaKeyword.CONTAINS;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import uk.vaent.json.JsonSchemaParser;

public class ArrayContainsDefinition {
    private final JsonNode containsSchema;

    public final boolean matchesGeneralItems;
    /**
     * Unmodifiable list of every position in the generated array capable of satisfying the "contains" schema.
     * If there is a general item definition, its index is equal to the length of the tuple (which may be zero).
     */
    public final List<Integer> matchingIndices;

    public ArrayContainsDefinition(JsonNode parentSchema, ArrayItemDefinitions itemDefinitions) {
        containsSchema = parentSchema.get(CONTAINS);
        matchingIndices = Collections.unmodifiableList(determineMatches(itemDefinitions));
        matchesGeneralItems = (matchingIndices.contains(itemDefinitions.tupleLength()));
    }

    private List<Integer> determineMatches(ArrayItemDefinitions itemDefinitions) {
        List<Integer> matches = new ArrayList<>();
        int testIndex = 0;
        for (JsonNode definition : itemDefinitions) {
            if (containsSchemaAgreesWith(definition)) matches.add(testIndex);
            if (testIndex == itemDefinitions.tupleLength()) break;
            testIndex++;
        }
        if (matches.isEmpty()) throw new NoSuchElementException("No item definition was found matching \"contains\": " + containsSchema);
        return matches;
    }

    private boolean containsSchemaAgreesWith(JsonNode definition) {
        return (containsSchema == null)
            || JsonSchemaParser.schemaSatisfiesOtherSchema(definition, containsSchema)
            || JsonSchemaParser.schemaSatisfiesOtherSchema(containsSchema, definition);
    }

    public List<Integer> matchingIndices(int arraySize) {
        List<Integer> copyList = new ArrayList<>(matchingIndices.stream().filter(i -> i < arraySize).toList());
        if (copyList.isEmpty()) throw new IndexOutOfBoundsException("No matches identified for array of size " + arraySize);
        if (matchesGeneralItems && (arraySize > Collections.max(matchingIndices))) {
            for (int i = Collections.max(matchingIndices) + 1; i < arraySize; i++) {
                copyList.add(i);
            }
        }
        return copyList;
    }
}
