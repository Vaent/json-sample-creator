package uk.vaent.json.type.array;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import org.springframework.lang.NonNull;

public class ArrayItemDefinitionsDraft06AndDraft07 extends ArrayItemDefinitions {
    public ArrayItemDefinitionsDraft06AndDraft07(JsonNode arraySchema) {
        super(arraySchema);
    }

    @Override
    @NonNull
    protected JsonNode getGeneralItemDefinition(JsonNode arraySchema) {
        JsonNode items = arraySchema.get("items");
        if (items == null) return BooleanNode.TRUE;
        if (!items.isArray()) return items;
        JsonNode additionalItems = arraySchema.get("additionalItems");
        return (additionalItems == null) ? BooleanNode.TRUE : additionalItems;
    }

    @Override
    protected String tupleKeyword() {
        return "items";
    }
}
