package uk.vaent.json.type.array;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@SpringBootTest
abstract class ArrayItemDefinitionsBaseTest {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ResourceLoader resourceLoader;

    protected Class<? extends ArrayItemDefinitions> classUnderTest;
    protected String strictSingletSchema;
    protected String strictTripletSchema;
    protected String openDoubletSchema;

    @Test
    public void strictSinglet() throws Exception {
        test(strictSingletSchema, 1, false);
    }

    @Test
    public void strictTriplet() throws Exception {
        test(strictTripletSchema, 3, false);
    }

    @Test
    public void openDoublet() throws Exception {
        test(openDoubletSchema, 2, true);
    }

    private ArrayItemDefinitions definitions(String schemaFilePath) throws Exception {
        Resource schema = resourceLoader.getResource(schemaFilePath);
        JsonNode schemaTree = objectMapper.readTree(schema.getFile());
        return classUnderTest.getDeclaredConstructor(JsonNode.class).newInstance(schemaTree);
    }

    private void test(String schemaFilePath, int expectedNumberOfElements, boolean additionalElementsPermitted) throws Exception {
        Iterator<JsonNode> iterator = definitions(schemaFilePath).iterator();
        for (int i = 0; i < expectedNumberOfElements; i++) {
            System.out.println(i + "::" + iterator.next());
        }
        assertEquals(additionalElementsPermitted, iterator.hasNext());
    }
}
