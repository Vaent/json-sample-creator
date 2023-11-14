package uk.vaent.json;

import static uk.vaent.json.JsonSchemaKeyword.CONST;
import static uk.vaent.json.JsonSchemaKeyword.TYPE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.lang.NonNull;
import uk.vaent.json.type.JsonType;

public class JsonSchemaParser {
    public static final String INVALID_TYPE_FORMAT_MESSAGE = "Format of type definition in schema is invalid";
    private static final JsonType[] jsonTypesCached = JsonType.values();
    private static final List<JsonType> jsonValueTypes = Stream.of(JsonType.values())
        .filter(type -> (type != JsonType.ARRAY) && (type != JsonType.OBJECT))
        .toList();
    private static final Random random = new Random();

    /**
     * Compares two schemas and returns a new schema which satisfies both of the inputs.
     */
    public static JsonNode getIntersection(@NonNull JsonNode schemaA, @NonNull JsonNode schemaB) {
        if (schemaA.equals(schemaB)) return schemaA.deepCopy();
        if (schemaSatisfiesOtherSchema(schemaA, schemaB)) return schemaA.deepCopy();
        if (schemaSatisfiesOtherSchema(schemaB, schemaA)) return schemaB.deepCopy();
        return null;
    }

    public static JsonType getType(JsonNode schema) {
        if (BooleanNode.TRUE.equals(schema)) return selectFromValueTypes(); // all values are valid for the `true` schema
        if (!schema.isObject()) return null;
        return processTypeKeyword(schema,
            schemaType -> JsonType.valueOf(schemaType.toUpperCase()),
            schemaType -> StreamSupport.stream(schemaType.spliterator(), true)
                .map(JsonNode::textValue)
                .map(String::toUpperCase)
                .map(JsonType::valueOf)
                .findAny().orElseGet(JsonSchemaParser::selectFromValueTypes),
            JsonSchemaParser::selectFromValueTypes);
    }

    /**
     * Returns true if all values which satisfy <code>schema</code> will also satisfy <code>otherSchema</code>,
     * based on a comparison of the constraints in both schemas.
     */
    public static boolean schemaSatisfiesOtherSchema(@NonNull JsonNode schema, @NonNull JsonNode otherSchema) {
        if (schema.equals(otherSchema)) return true;
        if (otherSchema.equals(BooleanNode.TRUE)) return true;
        if (otherSchema.has(CONST)) return otherSchema.get(CONST).equals(schema.get(CONST));
        if (schema.has(CONST) && validate(JsonType.of(schema.get(CONST)), otherSchema)) return true;
        if (schema.has(TYPE)) {
            JsonNode schemaType = schema.get(TYPE);
            if (schemaType.isTextual()) return validateType(schemaType, otherSchema);
            if (schemaType.isArray()) return StreamSupport.stream(schemaType.spliterator(), true)
                .anyMatch(t -> validateType(t, otherSchema));
        }
        return false;
    }

    public static boolean validate(@NonNull JsonType type, JsonNode schema) {
        if (BooleanNode.TRUE.equals(schema)) return true; // all values are valid for the `true` schema
        if (!schema.isObject()) return false;
        if (schema.has(CONST)) return JsonType.of(schema.get(CONST)).matcher.apply(type);
        return processTypeKeyword(schema,
            type.stringMatcher,
            schemaType -> schemaType.isEmpty() // all values are valid if no type is specified
                || StreamSupport.stream(schemaType.spliterator(), true)
                    .map(JsonNode::textValue)
                    .anyMatch(type.stringMatcher::apply),
            () -> true);
    }

// Private methods

    private static <R> R processTypeKeyword(JsonNode parentSchema,
                                            Function<String, R> actionIfString,
                                            Function<ArrayNode, R> actionIfArray,
                                            Supplier<R> actionIfNull) {
        JsonNode type = parentSchema.get(TYPE);
        if (type == null) return actionIfNull.get();
        if (type.isTextual()) return actionIfString.apply(type.textValue());
        if (type.isArray()) return actionIfArray.apply((ArrayNode)type);
        throw new IllegalArgumentException(INVALID_TYPE_FORMAT_MESSAGE);
    }

    /* This legacy method is preserved, but should be avoided in favour of selectFromValueTypes()
     unless you are somehow enforcing a hard limit on generation of nested containers. */
    private static JsonType selectFromAllTypes() {
        return jsonTypesCached[random.nextInt(jsonTypesCached.length)];
    }

    private static JsonType selectFromValueTypes() {
        return jsonValueTypes.get(random.nextInt(jsonValueTypes.size()));
    }

    private static boolean validateType(JsonNode typeTextNode, JsonNode schema) {
        return typeTextNode.isTextual()
            && validate(JsonType.valueOf(typeTextNode.textValue().toUpperCase()), schema);
    }
}
