package uk.vaent.json.type.array;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ArrayItemDefinitions2019_09Test extends ArrayItemDefinitionsBaseTest {
    ArrayItemDefinitions2019_09Test() {
        classUnderTest = ArrayItemDefinitions2019_09.class;
        strictSingletSchema = "schemas/array/strict-singlet-pre-2020.schema.json";
        strictTripletSchema = "schemas/array/strict-triplet-pre-2020.schema.json";
        openDoubletSchema = "schemas/array/open-doublet-pre-2020.schema.json";
    }
}
