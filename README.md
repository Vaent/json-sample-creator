# json-sample-creator

A tool for generating sample JSON data based on user-supplied schemas and parameters.

## Current status

- Running the JsonSampleCreator application, with an argument which contains a JSON type declaration for a single-valued type (boolean, number or string) will print an appropriate value for the given type.
  - `JsonSampleCreator {\"type\":\"boolean\"}` -> `true`
  - `JsonSampleCreator "{\"type\" : \"number\"}"` -> `42`
  - `JsonSampleCreator {\"type\":\"string\"}` -> `"Hello JSON"`
  - The command line argument must be properly escaped, and quoted if it contains whitespace.
  - Only the first argument is considered; any additional arguments are ignored. If the first argument is invalid according to the above rules, no output is printed. If no argument is supplied an exception is thrown.
  - The test is a simple regex and does not require a valid JSON schema to be passed in (example: the enclosing brackets `{ }` can be omitted).
