package uk.vaent.json.type;

import com.fasterxml.jackson.databind.node.TextNode;
import java.util.function.Function;

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
}
