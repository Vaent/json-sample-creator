package uk.vaent.json.type;

import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.Random;

public class JsonBoolean {
    private static final Random random = new Random();

    public static BooleanNode getRandom() {
        return BooleanNode.valueOf(random.nextBoolean());
    }
}
