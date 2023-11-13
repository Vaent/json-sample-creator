package uk.vaent.json.type.array;

import static uk.vaent.json.JsonSchemaParser.INVALID_TYPE_FORMAT_MESSAGE;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;
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

    private boolean validateType(JsonNode typeTextNode) {
        return typeTextNode.isTextual()
            && JsonSchemaParser.validate(JsonType.valueOf(typeTextNode.textValue().toUpperCase()), containsSchema);
    }
}
