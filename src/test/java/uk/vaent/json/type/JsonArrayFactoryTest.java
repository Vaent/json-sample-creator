package uk.vaent.json.type;

import static org.junit.jupiter.api.Assertions.*;
import static uk.vaent.json.config.JsonSchemaDialect.DRAFT_04;
import static uk.vaent.json.config.JsonSchemaDialect._2020_12;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.io.IOException;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import uk.vaent.json.config.JsonSampleCreatorConfig;
import uk.vaent.json.config.JsonSchemaDialect;

@SpringBootTest
public class JsonArrayFactoryTest {
    @Autowired private JsonSampleCreatorConfig config;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ResourceLoader resourceLoader;

    @Test
    public void strictSinglet2020_12() throws IOException {
        JsonNode sample = initialise(_2020_12, "schemas/array/strict-singlet-2020-12.schema.json");
        assertTrue(sample.get(0).isTextual());
        assertEquals("Unique array entry", sample.get(0).textValue());
    }

    @Test
    public void strictSingletPre2020() throws IOException {
        JsonNode sample = initialise(DRAFT_04, "schemas/array/strict-singlet-pre-2020.schema.json");
        assertTrue(sample.get(0).isTextual());
        assertEquals("Unique array entry", sample.get(0).textValue());
    }

    @Test
    public void strictTriplet2020_12() throws IOException {
        JsonNode sample = initialise(_2020_12, "schemas/array/strict-triplet-2020-12.schema.json");
        assertEquals("First element", sample.get(0).textValue());
        assertEquals("Second element", sample.get(1).textValue());
        assertEquals("Third element", sample.get(2).textValue());
    }

    @Test
    public void strictTripletPre2020() throws IOException {
        JsonNode sample = initialise(DRAFT_04, "schemas/array/strict-triplet-pre-2020.schema.json");
        assertEquals("First element", sample.get(0).textValue());
        assertEquals("Second element", sample.get(1).textValue());
        assertEquals("Third element", sample.get(2).textValue());
    }

    @Test
    public void containsConstant() throws IOException {
        JsonNode sample = initialise(_2020_12, "schemas/array/contains-constant.schema.json");
        assertTrue(StreamSupport.stream(sample.spliterator(), true)
            .anyMatch(node -> node.isTextual()
                && "A string which is unlikely to be generated at random".equals(node.textValue())));
    }

    @Test
    public void containsType() throws IOException {
        JsonNode sample = initialise(_2020_12, "schemas/array/contains-type.schema.json");
        assertEquals(JsonNodeType.BOOLEAN, sample.get(5).getNodeType());
    }

    @Test
    public void containsDoesNotReplaceIndexedItemDefinition() throws IOException {
        JsonNode sample = initialise(_2020_12, "schemas/array/contains-string.schema.json");
        for(JsonNode node : sample) {
            assertTrue(node.isTextual(), node + " should be a JSON string");
            assertEquals("A string which is unlikely to be generated at random", node.textValue());
        }
    }

    private JsonNode initialise(JsonSchemaDialect dialect, String schemaFilePath) throws IOException {
        config.setJsonSchemaDialect(dialect);
        JsonNode schema = schemaFromFile(schemaFilePath);
        JsonNode sample = config.getJsonSampleFactory(schema).getSample();
        System.out.println(sample);
        assertTrue(sample.isArray(), sample + " should be a JSON array");
        return sample;
    }

    private JsonNode schemaFromFile(String schemaFilePath) throws IOException {
        Resource schema = resourceLoader.getResource(schemaFilePath);
        return objectMapper.readTree(schema.getFile());
    }
}
