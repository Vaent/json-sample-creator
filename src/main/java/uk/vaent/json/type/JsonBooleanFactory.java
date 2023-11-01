package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonBooleanFactory extends JsonTypeFactory {
    @Autowired
    private Random random;

    public JsonBooleanFactory(JsonNode schema) {
        super(schema);
    }

    public BooleanNode getRandom() {
        return BooleanNode.valueOf(random.nextBoolean());
    }

    @Override
    public JsonNode getSample() {
        if (!JsonSchemaParser.validate(JsonType.BOOLEAN, schema)) return null;
        return getRandom();
    }
}
