package uk.vaent.json.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import uk.vaent.json.JsonSchemaParser;
import uk.vaent.json.type.*;

@Configuration
public class JsonSampleCreatorConfig {
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
