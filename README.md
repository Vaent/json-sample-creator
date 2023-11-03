# json-sample-creator

A tool for generating sample JSON data based on user-supplied schemas and parameters.

Example usage (JAR):

```
java -jar -DschemaSourceDirectory="file:///C:/example/json-schemas" json-sample-creator-0.2.0.jar boolean.schema.json string.schema.json
```

Example usage (Maven):
```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DschemaSourceDirectory=file:///C:/example/json-schemas" -Dspring-boot.run.arguments="boolean.schema.json string.schema.json"
```

Example output:
```
Output for << file:///C:/example/json-schemas/boolean.schema.json >> follows...
false

Output for << file:///C:/example/json-schemas/string.schema.json >> follows...
"rnd42"
```

With version 0.2.0 the app takes schema file names as its arguments, instead of requiring the schema contents to be passed in directly. This update also processes each argument in turn rather than only considering the first argument.

Each schema file should be located in the directory specified by the `-DschemaSourceDirectory` option. System file paths must be prefixed with `file:///` or they will be treated as classpath resources. If no directory is specified via the command line, the property will be picked up from `application.properties` through Spring configuration (by default the directory path is an empty string, referencing the classpath).

## Current status

Spring Boot functionality has been added throughout the application code.

The various JSON type factories implement a common interface with a single method which accepts a JSON schema (unmarshalled into a JsonNode) and returns a sample of the appropriate type, or constant value if such a constraint exists in the schema.

JsonSampleCreator immediately defers to the factories to handle sample production when the app is run with at least one argument identifying a JSON schema file.

### Constant values

These are naively inserted into samples instead of generating a node based on a declared type, if the schema contains the `const` keyword.

All implementations of `typeFactory` currently prioritise constant values regardless of their type, with no checking of the value against other keywords in the schema. This will likely be tightened up in due course.

### Type validation

All factories validate the `type` keyword of the schema passed in (unless inserting a constant value as described above).

If a type array like `"type":["integer","boolean"]` is supplied in the schema, provided the array contains only valid JSON types, a type will be selected from the array elements before fetching the relevant factory.

If the schema contains no type definition, or the type keyword is an empty array, a type is selected at random before fetching its factory (all types are valid in the absence of a constraint).

### Features implemented for each JSON type

- *JsonArrayFactory*
  - ignores details in schema, always returns an empty array
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
