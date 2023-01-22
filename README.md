# json-sample-creator

A tool for generating sample JSON data based on user-supplied schemas and parameters.

Example usage:

```
java -jar json-sample-creator-0.1.1.jar {\"type\":\"boolean\"}
> false

java -jar json-sample-creator-0.1.1.jar \{\"type\":[\"string\",\"null\"]\}
> "rnd42"
```

Note the importance of escaping special characters when running the app from the command line. Quotes `""` must always be escaped, and if an array `[]` appears anywhere in the JSON schema argument, then curly braces `{}` may also need to be escaped.

**This behaviour is not consistent across different terminals** - manual testing on a Windows machine revealed that Git Bash requires braces to be escaped when using arrays, and permits escaping them (optionally) when no arrays are present; but Command Line does **not** accept escaped braces in either scenario, though quotes must always be escaped in both apps.

A target for future development is for schemas to be loaded from file rather than passed as a command line argument. When implemented, that feature will negate the need for escaping any characters in the JSON.

## Current status

Spring Boot functionality has been added throughout the application code. Note: executing the application with the `mvn spring-boot:run` command will fail due to stripping of quotes from the JSON argument passed in; the application must be launched through the `main` method of the JsonSampleCreator class, as previously.

The various JSON type factories implement a common interface with a single method which accepts a JSON schema (unmarshalled into a JsonNode) and returns a sample of the appropriate type.

JsonSampleCreator immediately defers to the factories to handle sample production when the app is run with at least one JSON schema argument. Only the first argument is processed.

### Type validation

All factories validate the `type` keyword of the schema passed in.

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
