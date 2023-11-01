package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonIntegerFactory extends JsonTypeFactory {
    @Autowired
    private Random random;

    public JsonIntegerFactory(JsonNode schema) {
        super(schema);
    }

    @Override
    public JsonNode getSample() {
        if (!JsonSchemaParser.validate(JsonType.INTEGER, schema)) return null;
        return LongNode.valueOf(random.nextLong());
    }
}
