package uk.vaent.json.type;

import java.util.Random;

public class JsonNumber {
    private static final Random random = new Random();

    public static double getRandom() {
        return random.nextDouble();
    }
}
