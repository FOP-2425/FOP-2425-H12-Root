package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compress.rle.BitRunningLengthDecompressor;
import h12.lang.MyBit;
import h12.mock.MockBitInputStream;
import h12.mock.MockBitOutputStream;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for H12.2.2.
 *
 * @author Nhan Huynh
 */

@TestForSubmission
@DisplayName("H12.2.2 | BitRunningLengthDecompressor")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_2_2_TestsPrivate extends H12_Tests {

    /**
     * The custom converters for the JSON parameter set test annotation.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "inPreState", JsonConverters::toBitInputStream,
        "count", JsonNode::asInt,
        "bit", JsonConverters::toBit,
        "inPostState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "outPostState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "counts", node -> JsonConverters.toList(node, JsonNode::asInt),
        "bits", node -> JsonConverters.toList(node, JsonConverters::toBit)
    );

    /**
     * The input stream to decompress from.
     */
    private @Nullable MockBitInputStream in;

    /**
     * The output stream to write to.
     */
    private @Nullable MockBitOutputStream out;

    /**
     * The decompressor to test.
     */
    private @Nullable BitRunningLengthDecompressor decompressor;

    @AfterEach
    void tearDown() throws Exception {
        if (decompressor != null) {
            decompressor.close();
        }
    }

    @Override
    public Class<?> getTestClass() {
        return BitRunningLengthDecompressor.class;
    }

    /**
     * Initializes the test with the context.
     *
     * @param method           the method to test
     * @param preStateBuilder  the pre-state builder for the test information
     * @param postStateBuilder the post-state builder for the test information
     * @param parameters       the parameters for the test
     *
     * @return the test information builder used to specify the test information
     */
    private TestInformation.TestInformationBuilder initTest(
        MethodLink method,
        Function<TestInformation.TestInformationBuilder, TestInformation.TestInformationBuilder> preStateBuilder,
        Function<TestInformation.TestInformationBuilder, TestInformation.TestInformationBuilder> postStateBuilder,
        JsonParameterSet parameters) {
        // Test setup
        in = parameters.get("inPreState");
        out = new MockBitOutputStream();
        assert in != null;
        decompressor = new BitRunningLengthDecompressor(in, out);

        List<Integer> inPostState = parameters.get("inPostState");
        List<Integer> outPostState = parameters.get("outPostState");

        return testInformation(method).preState(
            preStateBuilder.apply(TestInformation.builder()
                .add("in", in.getBits().toString())
                .add("out", out.getBits().toString())
            ).build()
        ).postState(
            postStateBuilder.apply(TestInformation.builder()
                .add("in", inPostState.toString())
                .add("out", outPostState.toString())
            ).build()
        );
    }

    /**
     * Initializes the test with the context.
     *
     * @param method     the method to test
     * @param parameters the parameters for the test
     *
     * @return the test information builder used to specify the test information
     */
    private TestInformation.TestInformationBuilder initTest(MethodLink method, JsonParameterSet parameters) {
        return initTest(method, Function.identity(), Function.identity(), parameters);
    }

    @DisplayName("Die Methode writeBit(int count, Bit bit) schreibt die Anzahl an aufeinanderfolgenden wiederholenden Bits korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_2_2_testWriteBit.json", customConverters = CUSTOM_CONVERTERS)
    void testWriteBit(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("writeBit", int.class, MyBit.class);

        // Test setup
        int count = parameters.getInt("count");
        MyBit bit = parameters.get("bit");
        TestInformation.TestInformationBuilder builder = initTest(
            method,
            preState -> preState.add("count", count).add("bit", bit),
            Function.identity(),
            parameters
        );

        // Test execution
        method.invoke(decompressor, count, bit);

        // Close it to flush the output
        assert decompressor != null;
        decompressor.close();

        // Test evaluation
        assert in != null;
        assert out != null;
        List<Integer> outPostState = parameters.get("outPostState");
        Context context = builder.actualState(
            TestInformation.builder()
                .add("in", in.getBits().toString())
                .add("out", out.getBits().toString())
                .build()
        ).build();

        Assertions2.assertEquals(outPostState, out.getBits(), context,
            comment -> "Wrong bits written to the output stream.");
    }

    @DisplayName("Die Methode decompress() liest die Anzahl an aufeinanderfolgenden wiederholenden Bits.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_2_2_testDecompressBitCount.json", customConverters = CUSTOM_CONVERTERS)
    void testDecompressBitCount(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("decompress");

        // Test setup
        TestInformation.TestInformationBuilder builder = initTest(
            method,
            preState -> preState.add("out", "Not tracked"),
            postState -> postState.add("out", "Not tracked"),
            parameters
        );

        // Test execution and evaluation
        assert in != null;
        assert out != null;
        List<Integer> counts = parameters.get("counts");
        List<MyBit> bits = parameters.get("bits");
        assert decompressor != null;
        decompressor.close();
        decompressor = new BitRunningLengthDecompressor(in, out) {
            int i = 0;

            @Override
            protected void writeBit(int count, MyBit bit) throws IOException {
                Context context = builder.actualState(
                    TestInformation.builder()
                        .add("in", in.getBits())
                        .add("out", "Not tracked")
                        .build()
                ).build();
                int expectedCount = counts.get(i);
                MyBit expectedBit = bits.get(i);
                Assertions2.assertEquals(expectedCount, count, context,
                    comment -> "Wrong count written to the output stream.");
                Assertions2.assertEquals(expectedBit, bit, context,
                    comment -> "Wrong bit written to the output stream.");
                i++;
            }
        };
        method.invoke(decompressor);
    }

    @DisplayName("Die Methode decompress() dekomprimiert korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_2_2_testDecompress.json", customConverters = CUSTOM_CONVERTERS)
    void testDecompress(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("decompress");

        // Test setup
        TestInformation.TestInformationBuilder builder = initTest(method, parameters);

        // Test execution
        method.invoke(decompressor);

        // Test evaluation
        assert in != null;
        assert out != null;
        Context context = builder.actualState(
            TestInformation.builder()
                .add("in", in.getBits())
                .add("out", out.getBits())
                .build()
        ).build();
        List<Integer> outPostState = parameters.get("outPostState");

        Assertions2.assertTrue(out.isFlushed(), context,
            comment -> "The output stream is not flushed.");
        Assertions2.assertEquals(outPostState, out.getBits(), context,
            comment -> "Wrong bits written to the output stream.");
    }
}
