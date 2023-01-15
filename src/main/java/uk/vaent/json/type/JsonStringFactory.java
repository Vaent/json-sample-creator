package uk.vaent.json.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mifmif.common.regex.Generex;
import org.springframework.stereotype.Component;
import uk.vaent.json.JsonSchemaParser;

@Component
public class JsonStringFactory implements JsonTypeFactory {
    private static final Generex generexDefault = new Generex("[0-9A-Za-z]{1,10}");

    @Override
    public JsonNode getSampleFor(JsonNode schema) {
        if (!JsonSchemaParser.validate(JsonType.STRING, schema)) return null;
        return TextNode.valueOf(generexDefault.random());
    }
}
