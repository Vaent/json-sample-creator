package uk.vaent.json.type.array;

class ArrayItemDefinitionsDraft04Test extends ArrayItemDefinitionsBaseTest {
    ArrayItemDefinitionsDraft04Test() {
        classUnderTest = ArrayItemDefinitions2019_09.class;
        strictSingletSchema = "schemas/array/strict-singlet-pre-2020.schema.json";
        strictTripletSchema = "schemas/array/strict-triplet-pre-2020.schema.json";
        openDoubletSchema = "schemas/array/open-doublet-pre-2020.schema.json";
    }
}
