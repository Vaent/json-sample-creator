package uk.vaent.json.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.type.*;

@Configuration
@PropertySource("classpath:config/application.properties")
public class JsonSampleCreatorConfig {
    @Value("${jsonSchemaDialect}")
    private JsonSchemaDialect jsonSchemaDialect;

    public JsonSchemaDialect getJsonSchemaDialect() {
        return jsonSchemaDialect;
    }

    public void setJsonSchemaDialect(JsonSchemaDialect newDialect) {
        jsonSchemaDialect = newDialect;
    }

    /**
     * Updates the JSON schema dialect (version) referenced by the application, if the supplied schema document contains
     * a valid <code>$schema</code> declaration.
     *
     * @param schema the schema document, unmarshalled to a JsonNode.
     * @return the new configured dialect (which may be unchanged from the previous value).
     */
    public JsonSchemaDialect setJsonSchemaDialectFrom(JsonNode schema) {
        JsonSchemaDialect dialectFromSchema = JsonSchemaDialect.of(schema);
        if (dialectFromSchema != null) jsonSchemaDialect = dialectFromSchema;
        return jsonSchemaDialect;
    }

    @Bean(autowireCandidate = false)
    @Scope("prototype")
    public JsonTypeFactory getJsonSampleFactory(JsonNode schema) {
        return switch (JsonSchemaParser.getType(schema)) {
            case ARRAY -> new JsonArrayFactory();
            case BOOLEAN -> new JsonBooleanFactory();
            case INTEGER -> new JsonIntegerFactory();
            case NULL -> new JsonNullFactory();
            case NUMBER -> new JsonNumberFactory();
            case OBJECT -> new JsonObjectFactory();
            case STRING -> new JsonStringFactory();
        };
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
