package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonArrayFactory extends JsonTypeFactory {
    public JsonArrayFactory(JsonNode schema) {
        super(schema);
    }

    @Override
    protected JsonNode generateSample() {
        if (!JsonSchemaParser.validate(JsonType.ARRAY, schema)) return null;
        return JsonNodeFactory.instance.arrayNode();
    }
}
