# json-sample-creator

A tool for generating sample JSON data based on user-supplied schemas and parameters.

Example usage:

```
java -jar json-sample-creator-0.1.0.jar {\"type\":\"boolean\"}
> false

java -jar json-sample-creator-0.1.0.jar {\"type\":\"string\"}
> "rnd42"
```

## Current status

Spring Boot functionality has been added throughout the application code. Note: executing the application with the `mvn spring-boot:run` command will fail due to stripping of quotes from the JSON argument passed in; the application must be launched through the `main` method of the JsonSampleCreator class, as previously.

The various JSON type factories implement a common interface with a single method which accepts a JSON schema (unmarshalled into a JsonNode) and returns a sample of the appropriate type.

JsonSampleCreator immediately defers to the factories to handle sample production when the app is run with at least one JSON schema argument. Only the first argument is processed.

### Features implemented for each JSON type

Note: all factories validate the `type` keyword of the schema passed in. Type arrays like `"type":["integer","boolean"]` are not supported.

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
