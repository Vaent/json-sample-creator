package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
public class JsonIntegerFactory implements JsonTypeFactory {
    @Autowired
    private Random random;

    @Override
    public JsonNode getSampleFor(JsonNode schema) {
        if (!JsonType.INTEGER.equals(JsonSchemaParser.getType(schema))) return null;
        return LongNode.valueOf(random.nextLong());
    }
}
