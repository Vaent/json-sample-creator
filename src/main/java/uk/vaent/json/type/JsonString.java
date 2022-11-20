package uk.vaent.json.type;

import com.fasterxml.jackson.databind.node.TextNode;
import com.mifmif.common.regex.Generex;

public class JsonString {
    private static final Generex generexDefault = new Generex("[0-9A-Za-z]{1,10}");

    public static TextNode getRandom() {
        return TextNode.valueOf("\"" + generexDefault.random() + "\"");
    }
}
