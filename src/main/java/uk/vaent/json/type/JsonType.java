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
     * A matcher function which compares this type with a string representing another type.
     * The matcher returns true if this type is equal to the other type,
     * OR if this type is a more specific subtype of the other
     * (i.e. an "integer" is a "number" with additional constraints, so INTEGER.matcher.apply("number") returns true).
     * In all other cases the matcher returns false
     * (including for NUMBER.matcher.apply("integer"), since a "number" is not guaranteed to be an "integer").
     */
    public final Function<String, Boolean> matcher = other -> {
        JsonType otherType = valueOf(other.toUpperCase());
        return this.equals(otherType)
            || ("INTEGER".equals(this.name()) && "NUMBER".equals(otherType.name()));
    };

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
