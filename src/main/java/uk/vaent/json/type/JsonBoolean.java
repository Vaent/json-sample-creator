package uk.vaent.json.type;

import java.util.Random;

public class JsonBoolean {
    private static final Random random = new Random();

    public static boolean getRandom() {
        return random.nextBoolean();
    }
}
