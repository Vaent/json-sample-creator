package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonNullFactory extends JsonTypeFactory {
    public JsonNullFactory(JsonNode schema) {
        super(schema);
    }

    @Override
    protected JsonNode generateSample() {
        if (!JsonSchemaParser.validate(JsonType.NULL, schema)) return null;
        return NullNode.getInstance();
    }
}
