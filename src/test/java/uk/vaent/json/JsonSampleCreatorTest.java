package uk.vaent.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonSampleCreatorTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputListener = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputListener));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testCreateBoolean() {
        String schema = "{\"type\":\"boolean\"}";
        JsonSampleCreator.main(schema);
        String output = outputListener.toString().strip();
        assertFalse(output.matches("\".*\""), "Boolean should not be represented as a string");
        assertTrue("true".equalsIgnoreCase(output) || "false".equalsIgnoreCase(output));
    }

    @Test
    public void testCreateNull() {
        String schema = "{\"type\":\"null\"}";
        JsonSampleCreator.main(schema);
        String output = outputListener.toString().strip();
        assertEquals("null", output);
    }

    @Test
    public void testCreateNumber() {
        String schema = "{\"type\":\"number\"}";
        JsonSampleCreator.main(schema);
        String output = outputListener.toString().strip();
        assertFalse(output.matches("\".*\""), "Number should not be represented as a string");
        try {
            Double.parseDouble(output);
        } catch (NumberFormatException ex) {
            fail("Output was non-numeric");
        }
    }

    @Test
    public void testCreateString() {
        String schema = "{\"type\":\"string\"}";
        JsonSampleCreator.main(schema);
        String output = outputListener.toString().strip();
        assertTrue(output.matches("^\".*\"$"));
    }
}