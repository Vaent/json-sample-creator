package uk.vaent.json.config;

import static uk.vaent.json.JsonSchemaKeyword.$SCHEMA;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.NonNull;

public enum JsonSchemaDialect {
    DRAFT_04,
    DRAFT_06,
    DRAFT_07,
    _2019_09,
    _2020_12;

    public static JsonSchemaDialect of(@NonNull JsonNode schema) {
        if (!schema.has($SCHEMA)) return null;
        return switch (schema.get($SCHEMA).textValue()) {
            // draft-05 should never be encountered; it is functionally equivalent to draft-04
            case "http://json-schema.org/draft-04/schema#", "http://json-schema.org/draft-05/schema#" -> DRAFT_04;
            case "http://json-schema.org/draft-06/schema#" -> DRAFT_06;
            case "http://json-schema.org/draft-07/schema#" -> DRAFT_07;
            case "https://json-schema.org/draft/2019-09/schema" -> _2019_09;
            case "https://json-schema.org/draft/2020-12/schema" -> _2020_12;
            default -> null;
        };
    }
}
