package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
public class JsonNumberFactory implements JsonTypeFactory {
    @Autowired
    private Random random;

    @Override
    public JsonNode getSampleFor(JsonNode schema) {
        if (!JsonType.NUMBER.equals(JsonSchemaParser.getType(schema))) return null;
        return DoubleNode.valueOf(random.nextDouble());
    }
}
