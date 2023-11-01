package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mifmif.common.regex.Generex;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JsonStringFactory extends JsonTypeFactory {
    private static final Generex generexDefault = new Generex("[0-9A-Za-z]{1,10}");

    public JsonStringFactory(JsonNode schema) {
        super(schema);
    }

    @Override
    public JsonNode getSample() {
        if (!JsonSchemaParser.validate(JsonType.STRING, schema)) return null;
        return TextNode.valueOf(generexDefault.random());
    }
}
