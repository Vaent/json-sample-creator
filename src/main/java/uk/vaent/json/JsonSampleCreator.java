package uk.vaent.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import uk.vaent.json.config.JsonSampleCreatorConfig;

@SpringBootApplication
public class JsonSampleCreator implements CommandLineRunner {
    @Autowired private JsonSampleCreatorConfig config;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ResourceLoader resourceLoader;

    @Value("${schemaSourceDirectory}")
    private String schemaSourceDirectory;

    public static void main(String... args) {
        SpringApplication.run(JsonSampleCreator.class, args);
    }

    public void run(String... schemaFileNames) {
        for (String schemaFileName : schemaFileNames) {
            String schemaPath = schemaSourceDirectory + File.separator + schemaFileName;
            System.out.println("\nOutput for << " + schemaPath + " >> follows...");
            try {
                Resource resource = resourceLoader.getResource(schemaPath);
                InputStream file = resource.getInputStream();
                String fileContents = new String(file.readAllBytes());
                JsonNode tree = objectMapper.readTree(fileContents);
                System.out.println(config.getJsonSampleFactory(tree).getSampleFor(tree));
            } catch (JsonProcessingException ex) {
                System.out.println("Unable to parse file contents as JSON");
            } catch (IOException ex) {
                System.out.println("Unable to access file");
            }
        }
    }
}
