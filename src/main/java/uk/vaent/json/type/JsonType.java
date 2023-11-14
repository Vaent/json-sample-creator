package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.function.Function;
import org.springframework.lang.NonNull;

public enum JsonType {
    ARRAY,
    BOOLEAN,
    INTEGER,
    NULL,
    NUMBER,
    OBJECT,
    STRING;

    /**
     * A matcher function which compares this type with another type.
     * The matcher returns true if this type is equal to the other type,
     * OR if this type is a more specific subtype of the other
     * (i.e. an "integer" is a "number" with additional constraints, so INTEGER.matcher.apply(NUMBER) returns true).
     * In all other cases the matcher returns false
     * (including for NUMBER.matcher.apply(INTEGER), since a "number" is not guaranteed to be an "integer").
     */
    public final Function<JsonType, Boolean> matcher = other -> this.equals(other)
            || ("INTEGER".equals(this.name()) && "NUMBER".equals(other.name()));

    /**
     * Alternative matcher which takes a string representing another type, and converts it to a JsonType value
     * before comparing the types.
     */
    public final Function<String, Boolean> stringMatcher = other -> matcher.apply(valueOf(other.toUpperCase()));

    public static JsonType of(@NonNull JsonNode value) {
        return switch (value.getNodeType()) {
            case ARRAY -> ARRAY;
            case BOOLEAN -> BOOLEAN;
            case NULL -> NULL;
            case OBJECT -> OBJECT;
            case NUMBER -> value.isIntegralNumber() ? INTEGER : NUMBER;
            case STRING -> STRING;
            default -> throw new IllegalArgumentException("JsonType of JSON value/container could not be determined: " + value);
        };
    }
}
