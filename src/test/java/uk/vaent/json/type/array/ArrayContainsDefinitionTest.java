package uk.vaent.json.type.array;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

@ExtendWith(MockitoExtension.class)
class ArrayContainsDefinitionTest {
    @Mock private ArrayItemDefinitions mockItemDefinitions;
    @Mock private Iterator<JsonNode> mockItemDefinitionsIterator;
    @Mock private JsonNode mockSchema;

    @Test
    public void identifiesFirstPossibleMatch() {
        mockIteratorReturnsExactly(0, 1, 42, 3, 42, 5, 6);
        when(mockSchema.get("contains")).thenReturn(constant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(2, arrayContainsDefinition.firstPossibleMatchIndex());
    }

    @Test
    public void identifiesLastPossibleMatchForClosedTuple() {
        mockIteratorReturnsExactly(0, 1, 42, 3, 42, 5, 6);
        when(mockSchema.get("contains")).thenReturn(constant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(4, arrayContainsDefinition.lastPossibleMatchIndex());
    }

    @Test
    public void identifiesLastPossibleMatchForOpenEndedTuple() {
        mockIteratorReturnsAtLeast(0, 1, 42, 3, 42, 5, 6);
        when(mockSchema.get("contains")).thenReturn(constant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(4, arrayContainsDefinition.lastPossibleMatchIndex());
    }

    @Test
    public void whenGeneralItemIsMatchThenLastPossibleMatchIsMaximumPossibleIndex() {
        mockIteratorReturnsAtLeast(0, 1, 42, 3, 42, 5, 6, 42);
        when(mockSchema.get("contains")).thenReturn(constant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(Integer.MAX_VALUE, arrayContainsDefinition.lastPossibleMatchIndex());
    }

    @Test
    public void throwsExceptionWhenNoMatchFound() {
        mockIteratorReturnsExactly(0, 1, 2, 3, 4, 5, 6);
        when(mockSchema.get("contains")).thenReturn(constant(42));
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertThrows(NoSuchElementException.class, arrayContainsDefinition::firstPossibleMatchIndex);
        assertThrows(NoSuchElementException.class, arrayContainsDefinition::lastPossibleMatchIndex);
    }

    @Test
    public void firstElementIsMatchWhenSchemaHasNoContainsKeyword() {
        when(mockSchema.get("contains")).thenReturn(null);
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(0, arrayContainsDefinition.firstPossibleMatchIndex());
    }

    @Test
    public void lastElementOfClosedTupleIsMatchWhenSchemaHasNoContainsKeyword() {
        when(mockSchema.get("contains")).thenReturn(null);
        when(mockItemDefinitions.isClosedTuple()).thenReturn(true);
        when(mockItemDefinitions.tupleLength()).thenReturn(7);
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(6, arrayContainsDefinition.lastPossibleMatchIndex());
    }

    @Test
    public void whenOpenEndedSchemaHasNoContainsKeywordThenLastPossibleMatchIsMaximumPossibleIndex() {
        when(mockSchema.get("contains")).thenReturn(null);
        when(mockItemDefinitions.isClosedTuple()).thenReturn(false);
        ArrayContainsDefinition arrayContainsDefinition = new ArrayContainsDefinition(mockSchema, mockItemDefinitions);
        assertEquals(Integer.MAX_VALUE, arrayContainsDefinition.lastPossibleMatchIndex());
    }

    private ObjectNode constant(long value) {
        return JsonNodeFactory.instance.objectNode().put("const", value);
    }

    private ObjectNode[] constants(long... values) {
        ObjectNode[] arr = new ObjectNode[values.length];
        for (int i = 0; i < values.length; i++) {
            arr[i] = constant(values[i]);
        }
        return arr;
    }

    private void mockIteratorReturnsExactly(long firstValue, long... otherValues) {
        mockIteratorReturns(false, firstValue, otherValues);
    }

    private void mockIteratorReturnsAtLeast(long firstValue, long... otherValues) {
        mockIteratorReturns(true, firstValue, otherValues);
    }

    private void mockIteratorReturns(boolean isOpenEnded, long firstValue, long... otherValues) {
        when(mockItemDefinitions.iterator()).thenReturn(mockItemDefinitionsIterator);
        if (isOpenEnded) {
            when(mockItemDefinitions.tupleLength()).thenReturn(otherValues.length);
            when(mockItemDefinitionsIterator.hasNext()).thenReturn(true);
        } else {
            when(mockItemDefinitions.tupleLength()).thenReturn(otherValues.length + 1);
            setIteratorHasNextCount(otherValues.length + 1);
        }
        when(mockItemDefinitionsIterator.next()).thenReturn(constant(firstValue), constants(otherValues));
    }

    private void setIteratorHasNextCount(int iteratorLength) {
        OngoingStubbing<Boolean> stub = when(mockItemDefinitionsIterator.hasNext());
        for (int i = 0; i < iteratorLength; i++) {
            stub = stub.thenReturn(true);
        }
        stub.thenReturn(false);
    }
}