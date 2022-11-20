# json-sample-creator

A tool for generating sample JSON data based on user-supplied schemas and parameters.

## Current status

- Running the JsonSampleCreator application, with an argument which contains a JSON type declaration for "null" or a single-valued type (boolean, number or string) will print a randomised value matching the given type.
  - `JsonSampleCreator {\"type\":\"boolean\"}` -> `true`
  - `JsonSampleCreator "{\"type\" : \"number\"}"` -> `42`
  - `JsonSampleCreator {\"type\":\"string\"}` -> `"Hello JSON"`
  - `JsonSampleCreator {\"type\":\"null\"}` -> `null`
  - The command line argument must be properly escaped, and quoted if it contains whitespace.
  - Only the first argument is considered; any additional arguments are ignored. If the first argument is invalid according to the above rules, no output is printed. If no argument is supplied an exception is thrown.
