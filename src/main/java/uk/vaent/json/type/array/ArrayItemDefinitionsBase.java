package uk.vaent.json.type.array;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.lang.NonNull;

public abstract class ArrayItemDefinitionsBase implements ArrayItemDefinitions {
    protected final JsonNode generalItems;
    protected final List<JsonNode> tuple;

    public ArrayItemDefinitionsBase(JsonNode arraySchema) {
        tuple = getTupleDefinition(arraySchema);
        generalItems = getGeneralItemDefinition(arraySchema);
    }

    /**
     * Default method defined in abstract class since all dialects up to and including 2020-12 share this logic.
     * Subclasses must supply the appropriate keyword for the relevant dialect, by overriding the abstract
     * <code>tupleKeyword()</code> method. If that keyword is present in the top level of the supplied schema,
     * with an array value, it will be used for the tuple definition; otherwise an empty list is returned.
     *
     * @param arraySchema the schema which defines the array, unmarshalled to a JsonNode.
     * @return a list of JsonNode instances representing the schema for each element in the tuple.
     */
    @NonNull
    protected List<JsonNode> getTupleDefinition(JsonNode arraySchema) {
        JsonNode tupleItems = arraySchema.get(tupleKeyword());
        if (tupleItems == null || !tupleItems.isArray()) return Collections.emptyList();
        return StreamSupport.stream(tupleItems.spliterator(), false)
            .collect(Collectors.toList());
    }

    @NonNull
    protected abstract JsonNode getGeneralItemDefinition(JsonNode arraySchema);

    @Override
    @NonNull
    public Iterator<JsonNode> iterator() {
        return new ArrayItemDefinitionsIterator();
    }

    /**
     * Identifies the dialect-specific keyword from which a tuple definition can be obtained.
     * For draft 4 through 2019-09, this is <code>items</code>; for 2020-12 it is <code>prefixItems</code>.
     *
     * @return the appropriate keyword.
     */
    protected abstract String tupleKeyword();

    private class ArrayItemDefinitionsIterator implements Iterator<JsonNode> {
        private int cursor;

        @Override
        public boolean hasNext() {
            return (cursor < tuple.size()) || !BooleanNode.FALSE.equals(generalItems);
        }

        @Override
        @NonNull
        public JsonNode next() {
            // return the next tuple entry until none remain, then return the general item schema indefinitely
            if (cursor < tuple.size()) return tuple.get(cursor++);
            if (!BooleanNode.FALSE.equals(generalItems)) return generalItems;
            // exception will be thrown if the tuple has been fully consumed && generalItems == false
            throw new NoSuchElementException();
        }
    }
}
