package uk.vaent.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.vaent.json.config.JsonSampleCreatorConfig;

@SpringBootApplication
public class JsonSampleCreator implements CommandLineRunner {
    @Autowired private JsonSampleCreatorConfig config;
    @Autowired private ObjectMapper objectMapper;

    public static void main(String... args) {
        SpringApplication.run(JsonSampleCreator.class, args);
    }

    public void run(String... args) {
        if (args.length < 1) return;
        try {
            JsonNode tree = objectMapper.readTree(args[0]);
            System.out.println(config.getJsonSampleFactory(tree).getSampleFor(tree));
        } catch (JsonProcessingException ex) {
            System.out.println("Unable to parse argument as JSON");
        }
    }
}
