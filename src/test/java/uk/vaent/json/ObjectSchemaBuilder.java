package uk.vaent.json;

import static uk.vaent.json.TestHelper.schemaFor;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.vaent.json.type.JsonType;

public class ObjectSchemaBuilder {
    ObjectNode schema = JsonNodeFactory.instance.objectNode().put("type", "object");

    public ObjectNode build() {
        return schema;
    }

    private ObjectNode propertiesObject() {
        if (schema.get("properties") == null) {
            schema.set("properties", JsonNodeFactory.instance.objectNode());
        }
        return (ObjectNode) schema.get("properties");
    }

    public ObjectSchemaBuilder setProperty(String newPropertyName, ObjectNode newPropertySchema) {
        propertiesObject().set(newPropertyName, newPropertySchema);
        return this;
    }

    public ObjectSchemaBuilder setProperty(String newPropertyName, JsonType type) {
        propertiesObject().set(newPropertyName, schemaFor(type.name().toLowerCase()));
        return this;
    }
}
