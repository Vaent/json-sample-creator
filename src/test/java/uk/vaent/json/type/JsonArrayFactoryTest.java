package uk.vaent.json.type;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import uk.vaent.json.config.JsonSampleCreatorConfig;
import uk.vaent.json.config.JsonSchemaDialect;

@SpringBootTest
public class JsonArrayFactoryTest {
    @Autowired
    private JsonSampleCreatorConfig config;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ResourceLoader resourceLoader;

    @Test
    public void strictSinglet2020_12() throws IOException {
        config.setJsonSchemaDialect(JsonSchemaDialect._2020_12);
        JsonNode sample = config.getJsonSampleFactory(schemaFromFile("schemas/array/strict-singlet-2020-12.schema.json"))
            .getSample();
        assertTrue(sample.isArray());
        assertTrue(sample.get(0).isTextual());
        assertEquals("Unique array entry", sample.get(0).textValue());
    }

    @Test
    public void strictSingletPre2020() throws IOException {
        config.setJsonSchemaDialect(JsonSchemaDialect.DRAFT_04);
        JsonNode sample = config.getJsonSampleFactory(schemaFromFile("schemas/array/strict-singlet-pre-2020.schema.json"))
            .getSample();
        assertTrue(sample.isArray());
        assertTrue(sample.get(0).isTextual());
        assertEquals("Unique array entry", sample.get(0).textValue());
    }

    @Test
    public void strictTriplet2020_12() throws IOException {
        config.setJsonSchemaDialect(JsonSchemaDialect._2020_12);
        JsonNode sample = config.getJsonSampleFactory(schemaFromFile("schemas/array/strict-triplet-2020-12.schema.json"))
            .getSample();
        assertTrue(sample.isArray());
        assertEquals("First element", sample.get(0).textValue());
        assertEquals("Second element", sample.get(1).textValue());
        assertEquals("Third element", sample.get(2).textValue());
    }

    @Test
    public void strictTripletPre2020() throws IOException {
        config.setJsonSchemaDialect(JsonSchemaDialect.DRAFT_04);
        JsonNode sample = config.getJsonSampleFactory(schemaFromFile("schemas/array/strict-triplet-pre-2020.schema.json"))
            .getSample();
        assertTrue(sample.isArray());
        assertEquals("First element", sample.get(0).textValue());
        assertEquals("Second element", sample.get(1).textValue());
        assertEquals("Third element", sample.get(2).textValue());
    }

    private JsonNode schemaFromFile(String schemaFilePath) throws IOException {
        Resource schema = resourceLoader.getResource(schemaFilePath);
        return objectMapper.readTree(schema.getFile());
    }
}
