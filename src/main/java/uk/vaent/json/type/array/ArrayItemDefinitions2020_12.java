package uk.vaent.json.type.array;

import static uk.vaent.json.JsonSchemaKeyword.ITEMS;
import static uk.vaent.json.JsonSchemaKeyword.PREFIX_ITEMS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import org.springframework.lang.NonNull;

public class ArrayItemDefinitions2020_12 extends ArrayItemDefinitionsBase {
    public ArrayItemDefinitions2020_12(JsonNode arraySchema) {
        super(arraySchema);
    }

    @Override
    @NonNull
    protected JsonNode getGeneralItemDefinition(JsonNode arraySchema) {
        JsonNode items = arraySchema.get(ITEMS);
        return (items == null) ? BooleanNode.TRUE : items;
    }

    @Override
    protected String tupleKeyword() {
        return PREFIX_ITEMS;
    }
}
