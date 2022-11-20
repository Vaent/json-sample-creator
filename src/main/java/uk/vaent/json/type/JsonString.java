package uk.vaent.json.type;

import com.mifmif.common.regex.Generex;

public class JsonString {
    private static final Generex generexDefault = new Generex("[0-9A-Za-z]{1,10}");

    public static String getRandom() {
        return "\"" + generexDefault.random() + "\"";
    }
}
