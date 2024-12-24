package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.compress.rle.BitRunningLengthCompressor;
import h12.mock.MockBitInputStream;
import h12.mock.MockBitOutputStream;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the public tests for H12.1.1.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H12.2.1 | BitRunningLengthCompressor")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_2_1_TestsPublic extends H12_Tests {

    /**
     * The custom converters for the JSON parameter set test annotation.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "bit", JsonNode::asInt,
        "count", JsonNode::asInt,
        "bitsPreState", JsonConverters::toBitInputStream,
        "lastReadPreState", JsonNode::asInt,
        "bitsPostState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "lastReadPostState", JsonNode::asInt,
        "startBits", node -> JsonConverters.toList(node, JsonNode::asInt),
        "counts", node -> JsonConverters.toList(node, JsonNode::asInt),
        "compressedBits", node -> JsonConverters.toList(node, JsonNode::asInt)
    );

    /**
     * The compressor to test.
     */
    private @Nullable BitRunningLengthCompressor compressor;

    /**
     * The field link for the lastRead field.
     */
    private FieldLink lastRead;

    @Override
    public Class<?> getTestClass() {
        return BitRunningLengthCompressor.class;
    }

    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        lastRead = Links.getField(getType(), "lastRead");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (compressor != null) {
            compressor.close();
        }
    }

    /**
     * Asserts the getBitCount() method.
     *
     * @param parameters the parameters for the test
     *
     * @throws Throwable if an error occurs
     */
    private void assertGetBitCount(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("getBitCount", int.class);

        // Test setup
        MockBitInputStream in = parameters.get("bitsPreState");
        int lastReadPreState = parameters.getInt("lastReadPreState");
        MockBitOutputStream out = new MockBitOutputStream();
        compressor = new BitRunningLengthCompressor(in, out);
        lastRead.set(compressor, lastReadPreState);
        List<Integer> bitsPostState = parameters.get("bitsPostState");
        int lastReadPostState = parameters.getInt("lastReadPostState");
        int bit = parameters.getInt("bit");

        TestInformation.TestInformationBuilder builder = testInformation(method)
            .preState(
                TestInformation.builder()
                    .add("in", in.getBits())
                    .add("out", List.of())
                    .add("lastRead", lastReadPreState)
                    .add("bit", bit)
                    .build()
            )
            .postState(
                TestInformation.builder()
                    .add("in", bitsPostState)
                    .add("out", List.of())
                    .add("lastRead", lastReadPostState)
                    .build()
            );

        // Test execution
        int actualCount = method.invoke(compressor, bit);

        // Close it to flush the output
        assert compressor != null;
        compressor.close();

        // Test evaluation
        int expectedCount = parameters.getInt("count");
        int lastReadActual = lastRead.get(compressor);

        Context context = builder.actualState(
            TestInformation.builder()
                .add("in", in.getRemainingBits())
                .add("out", List.of())
                .add("lastRead", lastReadActual)
                .build()
        ).build();

        Assertions2.assertEquals(lastReadPostState, lastReadActual, context,
            comment -> "Last read bit is not updated correctly.");
        Assertions2.assertEquals(expectedCount, actualCount, context,
            comment -> "Computation of the count is incorrect.");
    }

    @DisplayName("Die Methode getBitCount(int bit) gibt die korrekte Anzahl an aufeinanderfolgenden wiederholenden Bits zurück, für den Fall, dass die Anzahl der Bits nichtmaximal ist.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_Tests_testGetBitCountNotMax.json", customConverters = CUSTOM_CONVERTERS)
    void testGetBitCountNotMax(JsonParameterSet parameters) throws Throwable {
        assertGetBitCount(parameters);
    }

    @DisplayName("Die Methode getBitCount(int bit) gibt die korrekte Anzahl an aufeinanderfolgenden wiederholenden Bits zurück, für den Fall, dass die Anzahl der Bits maximal ist.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_Tests_testGetBitCountMax.json", customConverters = CUSTOM_CONVERTERS)
    void testGetBitCountMax(JsonParameterSet parameters) throws Throwable {
        assertGetBitCount(parameters);
    }

    @DisplayName("Die Methode compress() komprimiert korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_Tests_testCompress.json", customConverters = CUSTOM_CONVERTERS)
    void testCompress(JsonParameterSet parameters) throws IOException {
        // Access method to test
        MethodLink method = Links.getMethod(getType(), "compress");

        // Test setup
        MockBitInputStream in = parameters.get("bitsPreState");
        int lastReadPreState = parameters.getInt("lastReadPreState");
        List<Integer> bitsPostState = parameters.get("bitsPostState");
        int lastReadPostState = parameters.getInt("lastReadPostState");
        MockBitOutputStream out = new MockBitOutputStream();
        List<Integer> compressedBits = parameters.get("compressedBits");

        TestInformation.TestInformationBuilder builder = testInformation(method)
            .preState(
                TestInformation.builder()
                    .add("in", in.getBits())
                    .add("out", out.getBits())
                    .add("lastRead", lastReadPreState)
                    .build()
            )
            .postState(
                TestInformation.builder()
                    .add("in", bitsPostState)
                    .add("out", compressedBits)
                    .add("lastRead", lastReadPostState)
                    .build()
            );

        // Test execution
        List<Integer> startBits = parameters.get("startBits");
        List<Integer> counts = parameters.get("counts");

        // Custom gitBitCount to make the evaluation of compress() independent of getBitCount()
        compressor = new BitRunningLengthCompressor(in, out) {
            private int i = 0;

            @Override
            protected int getBitCount(int bit) throws IOException {
                int startBit = startBits.get(i);
                int count = counts.get(i);
                Assertions2.assertEquals(startBit, bit, builder.build(),
                    comment -> "Unexpected call of getBitCount(%s)".formatted(bit));
                if (i + 1 < counts.size()) {
                    lastRead.set(this, startBits.get(i + 1));
                } else {
                    lastRead.set(this, -1);
                }
                i++;
                return count;
            }
        };
        compressor.compress();

        // Test evaluation
        int lastReadActual = lastRead.get(compressor);
        Context context = builder.actualState(
            TestInformation.builder()
                .add("in", in.getRemainingBits())
                .add("out", out.getBits())
                .add("lastRead", lastReadActual)
                .build()
        ).build();

        Assertions2.assertTrue(out.isFlushed(), context,
            comment -> "The output stream is not flushed.");
        Assertions2.assertEquals(compressedBits, out.getBits(), context,
            comment -> "The compressed bits are incorrect.");
        Assertions2.assertEquals(lastReadPostState, lastReadActual, context,
            comment -> "Last read bit is not updated correctly.");
    }
}
