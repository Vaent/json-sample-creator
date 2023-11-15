package uk.vaent.json.type.array;

import static uk.vaent.json.JsonSchemaKeyword.ADDITIONAL_ITEMS;
import static uk.vaent.json.JsonSchemaKeyword.ITEMS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import org.springframework.lang.NonNull;

public class ArrayItemDefinitionsDraft04 extends ArrayItemDefinitionsBase {
    public ArrayItemDefinitionsDraft04(JsonNode arraySchema) {
        super(arraySchema);
    }

    @Override
    @NonNull
    protected JsonNode getGeneralItemDefinition(JsonNode arraySchema) {
        JsonNode items = arraySchema.get(ITEMS);
        if (items != null && !items.isArray()) return items;
        JsonNode additionalItems = arraySchema.get(ADDITIONAL_ITEMS);
        return (additionalItems == null) ? BooleanNode.TRUE : additionalItems;
    }

    @Override
    protected String tupleKeyword() {
        return ITEMS;
    }
}
