package uk.vaent.json.type.array;

import com.fasterxml.jackson.databind.JsonNode;

public interface ArrayItemDefinitions extends Iterable<JsonNode> {
    boolean isClosedTuple();
    int tupleLength();
}
