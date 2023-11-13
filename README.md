# json-sample-creator

A tool for generating sample JSON data based on user-supplied schemas and parameters.

Example usage (JAR):

```
java -jar -DschemaSourceDirectory="file:///C:/example/json-schemas" json-sample-creator-0.2.2.jar boolean.schema.json string.schema.json
```

Example usage (Maven):
```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DschemaSourceDirectory=file:///C:/example/json-schemas" -Dspring-boot.run.arguments="boolean.schema.json string.schema.json"
```

Example output:
```
Output for << file:///C:/example/json-schemas/boolean.schema.json >> follows...
Schema dialect: _2020_12
false

Output for << file:///C:/example/json-schemas/string.schema.json >> follows...
Schema dialect: _2020_12
"rnd42"
```

Since version 0.2.0 the app takes schema file names as its arguments, instead of requiring the schema contents to be passed in directly. This update also processes each argument in turn rather than only considering the first argument.

Each schema file should be located in the directory specified by the `-DschemaSourceDirectory` option. System file paths must be prefixed with `file:///` or they will be treated as classpath resources. If no directory is specified via the command line, the property will be picked up from `application.properties` through Spring configuration (by default the directory path is an empty string, referencing the classpath).

## Current status

Spring Boot functionality has been added throughout the application code.

The various JSON type factories implement a common interface with a single method which accepts a JSON schema (unmarshalled into a JsonNode) and returns a sample of the appropriate type, or constant value if such a constraint exists in the schema.

JsonSampleCreator immediately defers to the factories to handle sample production when the app is run with at least one argument identifying a JSON schema file.

### Schema dialect

This is set from the root document if present, otherwise from application.properties (default is 2020-12). The Spring configuration class makes the dialect available to factories and other classes which may need to handle keywords differently depending on the JSON Schema version in force.

### Boolean schemas

At various points in the application code, the `true` schema is accepted as permitting arbitrary values, without checking whether the declared dialect supports this usage in a specific context. [As stated below](#limitations) there is no guarantee that the application will catch syntax errors in the schemas fed to it.

### Constant values

These are naively inserted into samples instead of generating a node based on a declared type, if the schema contains the `const` keyword.

All implementations of `typeFactory` currently prioritise constant values regardless of their type, with no checking of the value against other keywords in the schema. This will likely be tightened up in due course.

### Type validation

All factories validate the `type` keyword of the schema passed in (unless inserting a constant value as described above).

If a type array like `"type":["integer","boolean"]` is supplied in the schema, provided the array contains only valid JSON types, a type will be selected from the array elements before fetching the relevant factory.

If the schema contains no type definition, or the type keyword is an empty array, a value type is selected at random before fetching its factory (all types are valid in the absence of a constraint). Container types are no longer selected if not specified by the schema, as lack of control over the depth of nesting readily leads to stack overflow.

### Features implemented for each JSON type

- *JsonArrayFactory*
  - returns an array containing samples obtained from additional factories as determined by the schema for each array item
  - definitions for tuples and for general items are used if present in the schema
  - if a schema for general items is not supplied by the relevant keyword, the `true` schema will be used for general items
  - maxItems and minItems are taken from the schema if present, otherwise default values from application.properties are used. An exception is raised if minItems is greater than maxItems when both values are taken from the schema; if only one limit is taken from the schema, it automatically has priority over the other (default) limit if they are in conflict; if default values are used for both limits and there is a conflict, minItems takes priority
  - if the `contains` keyword is present, with a constant value or type which is satisfied by at least one of the array's item definitions (including any inferred `true` schema), then the definition provided by `contains` will be applied in place of the item definition at a suitable index. Further refinement of this behaviour is required to identify the cross-section of the affected definitions rather than prioritising one over the other, which will produce invalid samples in some cases
  - a target array length between minItems and maxItems (inclusive) is determined at random
  - items will be added to the generated array until either:
    - the array length is equal to the target; or
    - the tuple is complete, if the schema explicitly forbids general items and the target length exceeds the tuple
- *JsonBooleanFactory*
  - ignores details in schema, always returns a random true/false value
- *JsonIntegerFactory*
  - ignores details in schema, always returns a random long value
- *JsonNullFactory*
  - always returns null (JSON value)
- *JsonNumberFactory*
  - ignores details in schema, always returns a random double value
- *JsonObjectFactory*
  - returns a JSON object containing all fields defined by the schema `properties` keyword, with values of the declared type for each field obtained from the appropriate factory
  - ignores any other constraints in the schema
- *JsonStringFactory*
  - ignores details in schema, always returns a random string of 1-10 alphanumeric characters

## Limitations

Schemas passed into the app are generally assumed to be valid, and are only minimally tested. Executing the app with schemas containing any invalid syntax (according to the relevant dialect) may result in uncaught exceptions and premature termination with null output. Equally, errors in the schema may be silently ignored in some cases, so this app should not be relied on as a schema validator.

Vocabularies are not inspected.

Dialects earlier than draft 4 are not recognised by the application. Any values of `$schema` which aren't detailed in the [JsonSchemaDialect](src/main/java/uk/vaent/json/config/JsonSchemaDialect.java) class will be ignored - this may include any future releases (the codebase is not guaranteed to keep pace with updates to the specification).
