package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
public class JsonBooleanFactory implements JsonTypeFactory {
    @Autowired
    private Random random;

    public BooleanNode getRandom() {
        return BooleanNode.valueOf(random.nextBoolean());
    }

    @Override
    public JsonNode getSampleFor(JsonNode schema) {
        if (!JsonType.BOOLEAN.equals(JsonSchemaParser.getType(schema))) return null;
        return getRandom();
    }
}
