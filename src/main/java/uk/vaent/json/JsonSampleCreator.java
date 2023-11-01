package uk.vaent.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import uk.vaent.json.config.JsonSampleCreatorConfig;
import uk.vaent.json.config.JsonSchemaDialect;

@SpringBootApplication
public class JsonSampleCreator implements CommandLineRunner {
    @Autowired private JsonSampleCreatorConfig config;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ResourceLoader resourceLoader;

    @Value("${schemaSourceDirectory}")
    private String schemaSourceDirectory;
    private final Stack<JsonSchemaDialect> dialectStack = new Stack<>();

    public static void main(String... args) {
        SpringApplication.run(JsonSampleCreator.class, args);
    }

    public void run(String... schemaFileNames) {
        dialectStack.push(config.getJsonSchemaDialect());
        for (String schemaFileName : schemaFileNames) {
            String schemaFilePath = schemaSourceDirectory + File.separator + schemaFileName;
            System.out.println("\nOutput for << " + schemaFilePath + " >> follows...");
            JsonNode schemaTree = getTree(schemaFilePath);
            if (schemaTree != null) {
                dialectStack.push(config.setJsonSchemaDialectFrom(schemaTree));
                System.out.println("Schema dialect: " + config.getJsonSchemaDialect());
                System.out.println(config.getJsonSampleFactory(schemaTree).getSample());
                dialectStack.pop();
                config.setJsonSchemaDialect(dialectStack.peek());
            }
        }
    }

    private JsonNode getTree(String filePath) {
        try {
            Resource resource = resourceLoader.getResource(filePath);
            InputStream file = resource.getInputStream();
            String fileContents = new String(file.readAllBytes());
            return objectMapper.readTree(fileContents);
        } catch (JsonProcessingException ex) {
            System.out.println("Unable to parse file contents as JSON");
        } catch (IOException ex) {
            System.out.println("Unable to access file");
        }
        return null;
    }
}
