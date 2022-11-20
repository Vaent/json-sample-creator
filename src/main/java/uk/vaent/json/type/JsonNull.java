package uk.vaent.json.type;

import com.fasterxml.jackson.databind.node.NullNode;

public class JsonNull {
    public static NullNode get() {
        return NullNode.getInstance();
    }
}
