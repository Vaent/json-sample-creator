package uk.vaent.json.type.array;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ArrayItemDefinitions2020_12Test extends ArrayItemDefinitionsBaseTest {
    ArrayItemDefinitions2020_12Test() {
        classUnderTest = ArrayItemDefinitions2020_12.class;
        strictSingletSchema = "schemas/array/strict-singlet-2020-12.schema.json";
        strictTripletSchema = "schemas/array/strict-triplet-2020-12.schema.json";
        openDoubletSchema = "schemas/array/open-doublet-2020-12.schema.json";
    }
}
