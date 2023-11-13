package uk.vaent.json.type.array;

import static uk.vaent.json.JsonSchemaParser.INVALID_TYPE_FORMAT_MESSAGE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.type.JsonType;

public class ArrayContainsDefinition {
    private final JsonNode containsSchema;

    public final boolean matchesGeneralItems;
    /**
     * Unmodifiable list of every position in the generated array capable of satisfying the "contains" schema.
     * If there is a general item definition, its index is equal to the length of the tuple (which may be zero).
     */
    public final List<Integer> matchingIndices;

    public ArrayContainsDefinition(JsonNode parentSchema, ArrayItemDefinitions itemDefinitions) {
        containsSchema = parentSchema.get("contains");
        matchingIndices = Collections.unmodifiableList(determineMatches(itemDefinitions));
        matchesGeneralItems = (matchingIndices.contains(itemDefinitions.tupleLength()));
    }

    private List<Integer> determineMatches(ArrayItemDefinitions itemDefinitions) {
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
        if (containsSchema == null || containsSchema.equals(definition) || BooleanNode.TRUE.equals(definition)) return true;
        if (containsSchema.has("const")) {
            JsonNode constValue = containsSchema.get("const");
            if (definition.has("const")) return constValue.equals(definition.get("const"));
            return JsonSchemaParser.validate(JsonType.of(constValue), definition);
        }
        if (containsSchema.has("type")) {
            if (definition.has("const")) {
                return JsonSchemaParser.validate(JsonType.of(definition.get("const")), containsSchema);
            } else if (definition.has("type")) {
                JsonNode type = definition.get("type");
                if (type.isTextual()) return validateType(type);
                if (type.isArray()) {
                    return StreamSupport.stream(type.spliterator(), true)
                        .anyMatch(this::validateType);
                }
                throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
            }
        }
        return false;
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

    private boolean validateType(JsonNode typeTextNode) {
        return typeTextNode.isTextual()
            && JsonSchemaParser.validate(JsonType.valueOf(typeTextNode.textValue().toUpperCase()), containsSchema);
    }
}
