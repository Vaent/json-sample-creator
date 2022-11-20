package uk.vaent.json.type;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonObject {
    public static ObjectNode getEmpty() {
        return JsonNodeFactory.instance.objectNode();
    }
}
