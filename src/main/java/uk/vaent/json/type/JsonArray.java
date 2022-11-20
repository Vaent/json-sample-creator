package uk.vaent.json.type;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class JsonArray {
    public static ArrayNode getEmpty() {
        return JsonNodeFactory.instance.arrayNode();
    }
}
