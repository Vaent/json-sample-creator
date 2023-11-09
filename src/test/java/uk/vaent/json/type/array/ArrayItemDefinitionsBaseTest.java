package uk.vaent.json.type.array;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
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

    @Test
    public void correctlyReportsClosedTuple() throws Exception {
        assertTrue(definitions(strictTripletSchema).isClosedTuple());
    }

    @Test
    public void correctlyReportsOpenEndedSchema() throws Exception {
        assertFalse(definitions(openDoubletSchema).isClosedTuple());
        ArrayItemDefinitions trueSchema = classUnderTest.getDeclaredConstructor(JsonNode.class).newInstance(BooleanNode.TRUE);
        assertFalse(trueSchema.isClosedTuple());
    }

    @Test
    public void correctlyReportsTupleLength() throws Exception {
        ArrayItemDefinitions trueSchema = classUnderTest.getDeclaredConstructor(JsonNode.class).newInstance(BooleanNode.TRUE);
        assertEquals(0, trueSchema.tupleLength());
        assertEquals(1, definitions(strictSingletSchema).tupleLength());
        assertEquals(2, definitions(openDoubletSchema).tupleLength());
        assertEquals(3, definitions(strictTripletSchema).tupleLength());
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
