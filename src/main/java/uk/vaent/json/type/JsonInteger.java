package uk.vaent.json.type;

import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import java.util.Random;

public class JsonInteger {
    private static final Random random = new Random();

    public static NumericNode getRandom() {
        return LongNode.valueOf(random.nextLong());
    }
}
