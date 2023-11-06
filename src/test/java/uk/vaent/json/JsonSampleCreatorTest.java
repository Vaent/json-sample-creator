package uk.vaent.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JsonSampleCreator.class)
@TestPropertySource("classpath:config/application-test.properties")
class JsonSampleCreatorTest {
    private static final String arrayOutputPattern = "^\\[.*\\]$";
    private static final String stringOutputPattern = "^\".*\"$";

    @Autowired
    private JsonSampleCreator jsonSampleCreator;

    private String output;
    private final ByteArrayOutputStream outputListener = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    public void setUp() {
        output = null;
        System.setOut(new PrintStream(outputListener));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testCreateArray() {
        executeWith("array.schema.json");
        assertTrue(output.matches(arrayOutputPattern));
    }

    @Test
    public void testCreateBoolean() {
        executeWith("boolean.schema.json");
        assertTrue("true".equalsIgnoreCase(output) || "false".equalsIgnoreCase(output));
    }

    @Test
    public void testCreateInteger() {
        executeWith("integer.schema.json");
        assertFalse(output.matches(stringOutputPattern), "Number should not be represented as a string");
        try {
            Long.parseLong(output);
        } catch (NumberFormatException ex) {
            fail("Output was not an integer");
        }
    }

    @Test
    public void testCreateNull() {
        executeWith("null.schema.json");
        assertEquals("null", output);
    }

    @Test
    public void testCreateNumber() {
        executeWith("number.schema.json");
        assertFalse(output.matches(stringOutputPattern), "Number should not be represented as a string");
        try {
            Double.parseDouble(output);
        } catch (NumberFormatException ex) {
            fail("Output was non-numeric");
        }
    }

    @Test
    public void testCreateObject() {
        executeWith("object.schema.json");
        assertEquals("{}", output);
    }

    @Test
    public void testCreateString() {
        executeWith("string.schema.json");
        assertTrue(output.matches(stringOutputPattern));
    }

    // helpers
    private void executeWith(String schemaFileName) {
        jsonSampleCreator.run(schemaFileName);
        output = outputListener.toString().strip();
        standardOut.println(output);
        String[] outputLines = output.split("\n");
        output = outputLines[outputLines.length - 1];
    }
}
