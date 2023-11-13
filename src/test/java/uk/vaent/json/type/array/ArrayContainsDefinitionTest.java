package uk.vaent.json.type.array;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static uk.vaent.json.TestHelper.*;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import uk.vaent.json.TestHelper;

@ExtendWith(MockitoExtension.class)
class ArrayContainsDefinitionTest {
    @Mock private ArrayItemDefinitions mockItemDefinitions;
    @Mock private Iterator<JsonNode> mockItemDefinitionsIterator;
    @Mock private JsonNode mockSchema;

    @Test
    public void identifiesAllPossibleMatchesForClosedTuple() {
        mockIteratorReturnsExactly(0, 1, 42, 3, 42, 42, 6);
        when(mockSchema.get("contains")).thenReturn(schemaForConstant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(2, 4, 5), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesAllPossibleMatchesForOpenEndedTuple() {
        mockIteratorReturnsAtLeast(0, 1, 42, 3, 42, 42, 6);
        when(mockSchema.get("contains")).thenReturn(schemaForConstant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(2, 4, 5), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesWhenGeneralItemIsMatch() {
        mockIteratorReturnsAtLeast(0, 1, 42, 3, 42, 5, 6, 42);
        when(mockSchema.get("contains")).thenReturn(schemaForConstant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertTrue(arrayContainsDefinition.matchesGeneralItems);
    }

    @Test
    public void throwsExceptionWhenNoMatchFound() {
        mockIteratorReturnsExactly(0, 1, 2, 3, 4, 5, 6);
        when(mockSchema.get("contains")).thenReturn(schemaForConstant(42));
        assertThrows(NoSuchElementException.class, () -> new ArrayContainsDefinition(mockSchema, mockItemDefinitions));
    }

    @Test
    public void identifiesAllPossibleMatchesWhenSchemaHasNoContainsKeyword() {
        mockIteratorReturnsExactly(0, 1, 2, 3, 4, 5, 6);
        when(mockSchema.get("contains")).thenReturn(null);
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesGeneralItemAsMatchWhenSchemaHasNoContainsKeyword() {
        mockIteratorReturnsAtLeast(0, 1, 2, 3, 4, 5, 6);
        when(mockSchema.get("contains")).thenReturn(null);
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertTrue(arrayContainsDefinition.matchesGeneralItems);
    }

    @Test
    public void identifiesConstantsMatchingIntegerType() {
        mockIteratorReturnsExactly("A", "B", "C", 3, "E", 5, "G");
        when(mockSchema.get("contains")).thenReturn(schemaForType("integer"));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(3, 5), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesConstantsMatchingStringType() {
        mockIteratorReturnsExactly("A", "B", "C", 3, "E", 5, "G");
        when(mockSchema.get("contains")).thenReturn(schemaForType("string"));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(0, 1, 2, 4, 6), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesConstantsMatchingTypeArray() {
        mockIteratorReturnsExactly("A", "B", null, 3, null, 5, "G");
        when(mockSchema.get("contains")).thenReturn(TestHelper.schemaForType("string", "integer"));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(0, 1, 3, 5, 6), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesTypeDefinitionsMatchingStringConstant() {
        mockIteratorReturnsTypes(false, "string", "integer", "string", "null");
        when(mockSchema.get("contains")).thenReturn(schemaForConstant("foo"));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(0, 2), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesTypeDefinitionsMatchingIntegerConstant() {
        mockIteratorReturnsTypes(false, "string", "integer", "number", "null");
        when(mockSchema.get("contains")).thenReturn(schemaForConstant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(1, 2), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesIdenticalTypeDefinitions() {
        mockIteratorReturnsTypes(false, "string", "integer", "string", "null");
        when(mockSchema.get("contains")).thenReturn(schemaForType("string"));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(0, 2), arrayContainsDefinition.matchingIndices);
    }

    @Test
    public void identifiesDefinitionsWithCompatibleTypeFromTypeArrays() {
        configureMocks(false, 4);
        when(mockItemDefinitionsIterator.next())
            .thenReturn(schemaForType("null", "integer", "boolean"))
            .thenReturn(schemaForType("null", "array"))
            .thenReturn(schemaForType("object", "string"))
            .thenReturn(schemaForType("null", "boolean"));
        when(mockSchema.get("contains")).thenReturn(schemaForType("string", "number"));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(List.of(0, 2), arrayContainsDefinition.matchingIndices);
    }

    private void configureMocks(boolean isOpenEnded, int allDefinitionsCount) {
        when(mockItemDefinitions.iterator()).thenReturn(mockItemDefinitionsIterator);
        if (isOpenEnded) {
            when(mockItemDefinitions.tupleLength()).thenReturn(allDefinitionsCount - 1);
            when(mockItemDefinitionsIterator.hasNext()).thenReturn(true);
        } else {
            when(mockItemDefinitions.tupleLength()).thenReturn(allDefinitionsCount);
            setIteratorHasNextCount(allDefinitionsCount);
        }
    }

    private void mockIteratorReturnsExactly(Object firstValue, Object... otherValues) {
        mockIteratorReturns(false, firstValue, otherValues);
    }

    private void mockIteratorReturnsAtLeast(Object firstValue, Object... otherValues) {
        mockIteratorReturns(true, firstValue, otherValues);
    }

    private void mockIteratorReturns(boolean isOpenEnded, Object firstValue, Object... otherValues) {
        configureMocks(isOpenEnded, otherValues.length + 1);
        when(mockItemDefinitionsIterator.next()).thenReturn(schemaForConstant(firstValue), schemasForConstants(otherValues));
    }

    private void mockIteratorReturnsTypes(boolean isOpenEnded, String firstType, String... otherTypes) {
        configureMocks(isOpenEnded, otherTypes.length + 1);
        OngoingStubbing<JsonNode> stub = when(mockItemDefinitionsIterator.next()).thenReturn(schemaForType(firstType));
        for (String type : otherTypes) stub = stub.thenReturn(schemaForType(type));
    }

    private void setIteratorHasNextCount(int iteratorLength) {
        OngoingStubbing<Boolean> stub = when(mockItemDefinitionsIterator.hasNext());
        for (int i = 0; i < iteratorLength; i++) {
            stub = stub.thenReturn(true);
        }
        stub.thenReturn(false);
    }
}