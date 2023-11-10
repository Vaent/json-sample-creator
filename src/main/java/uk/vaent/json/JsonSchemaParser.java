package uk.vaent.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import uk.vaent.json.type.JsonType;

public class JsonSchemaParser {
    public static final String INVALID_TYPE_FORMAT_MESSAGE = "Format of type definition in schema is invalid";
    private static final JsonType[] jsonTypesCached = JsonType.values();
    private static final List<JsonType> jsonValueTypes = Stream.of(JsonType.values())
        .filter(type -> (type != JsonType.ARRAY) && (type != JsonType.OBJECT))
        .toList();
    private static final Random random = new Random();

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

    public static boolean validate(JsonType type, JsonNode schema) {
        if (BooleanNode.TRUE.equals(schema)) return true; // all values are valid for the `true` schema
        if (!schema.isObject()) return false;
        return processTypeKeyword(schema,
            type.matcher,
            schemaType -> schemaType.isEmpty() // all values are valid if no type is specified
                || StreamSupport.stream(schemaType.spliterator(), true)
                    .map(JsonNode::textValue)
                    .anyMatch(type.matcher::apply),
            () -> true);
    }

// Private methods

    private static <R> R processTypeKeyword(JsonNode parentSchema,
                                            Function<String, R> actionIfString,
                                            Function<ArrayNode, R> actionIfArray,
                                            Supplier<R> actionIfNull) {
        JsonNode type = parentSchema.get("type");
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
}
