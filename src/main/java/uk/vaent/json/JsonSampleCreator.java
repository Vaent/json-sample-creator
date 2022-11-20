package uk.vaent.json;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonSampleCreator {
    public static void main(String... args) {
        Pattern typeFinder = Pattern.compile("\"type\"\s*:\s*\"([^\"]*)\"");
        Matcher typeMatch = typeFinder.matcher(args[0]);
        if (typeMatch.find()) {
            String type = typeMatch.group(1).toLowerCase();
            switch (type) {
                case "boolean":
                    System.out.println(true);
                    break;
                case "number":
                    System.out.println(42);
                    break;
                case "string":
                    System.out.println("\"Hello JSON\"");
                    break;
            }
        }
    }
}
