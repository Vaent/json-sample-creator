package uk.vaent.json.type;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import java.util.Random;

public class JsonNumber {
    private static final Random random = new Random();

    public static NumericNode getRandom() {
        return DoubleNode.valueOf(random.nextDouble());
    }
}
