# json-sample-creator

A tool for generating sample JSON data based on user-supplied schemas and parameters.

## Current status

- Running the JsonSampleCreator application, with an argument which is a JSON schema for types boolean, integer, null, number or string, will print a randomised value matching the given type. For example:
  - `JsonSampleCreator {\"type\":\"boolean\"}` -> `true`
  - `JsonSampleCreator {\"type\":\"integer\"}` -> `42`
  - `JsonSampleCreator {\"type\":\"number\"}` -> `0.42`
  - `JsonSampleCreator {\"type\":\"string\"}` -> `"Hello JSON"`
  - `JsonSampleCreator {\"type\":\"null\"}` -> `null`
- Running the application with an array type schema will print an empty array.
  - `JsonSampleCreator {\"type\":\"array\"}` -> `[]`
- Running the application with an object type schema will print an object, including any properties which are specified.
  - `JsonSampleCreator {\"type\":\"object\"}` -> `{}`
  - `JsonSampleCreator {\"type\":\"object\",\"properties\":{\"foo\":{\"type\":\"boolean\"}}}` -> `{"foo":true}`
- Object schema properties will be assigned values in line with the descriptions above. Properties with type object can have properties of their own, to produce sample JSON with nested objects.
- The command line argument must be properly escaped, and quoted if it contains whitespace.
- Only the first argument is considered; any additional arguments are ignored. If the first argument is invalid according to the above rules, or if no argument is supplied, an exception is thrown.
