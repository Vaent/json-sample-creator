package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
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

    public static JsonType fromTextNode(TextNode node) {
        return JsonType.valueOf(node.textValue().toUpperCase());
    }

    public Function<TextNode, Boolean> matcher() {
        return otherType -> this.equals(fromTextNode(otherType));
    }

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
