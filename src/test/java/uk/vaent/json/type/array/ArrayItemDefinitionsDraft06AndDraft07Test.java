package uk.vaent.json.type.array;

class ArrayItemDefinitionsDraft06AndDraft07Test extends ArrayItemDefinitionsBaseTest {
    ArrayItemDefinitionsDraft06AndDraft07Test() {
        classUnderTest = ArrayItemDefinitions2019_09.class;
        strictSingletSchema = "schemas/array/strict-singlet-pre-2020.schema.json";
        strictTripletSchema = "schemas/array/strict-triplet-pre-2020.schema.json";
        openDoubletSchema = "schemas/array/open-doublet-pre-2020.schema.json";
    }
}
