package uk.vaent.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonSampleCreatorTest {
    private static final String stringOutputPattern = "\".*\"";

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
        executeWith("{\"type\":\"array\"}");
        assertEquals("[]", output);
    }

    @Test
    public void testCreateBoolean() {
        executeWith("{\"type\":\"boolean\"}");
        assertTrue("true".equalsIgnoreCase(output) || "false".equalsIgnoreCase(output));
    }

    @Test
    public void testCreateInteger() {
        executeWith("{\"type\":\"integer\"}");
        assertFalse(output.matches(stringOutputPattern), "Number should not be represented as a string");
        try {
            Long.parseLong(output);
        } catch (NumberFormatException ex) {
            fail("Output was not an integer");
        }
    }

    @Test
    public void testCreateNull() {
        executeWith("{\"type\":\"null\"}");
        assertEquals("null", output);
    }

    @Test
    public void testCreateNumber() {
        executeWith("{\"type\":\"number\"}");
        assertFalse(output.matches(stringOutputPattern), "Number should not be represented as a string");
        try {
            Double.parseDouble(output);
        } catch (NumberFormatException ex) {
            fail("Output was non-numeric");
        }
    }

    @Test
    public void testCreateObject() {
        executeWith("{\"type\":\"object\"}");
        assertEquals("{}", output);
    }

    @Test
    public void testCreateString() {
        executeWith("{\"type\":\"string\"}");
        assertTrue(output.matches("^\".*\"$"));
    }

    // helpers
    private void executeWith(String schema) {
        JsonSampleCreator.main(schema);
        output = outputListener.toString().strip();
    }
}
