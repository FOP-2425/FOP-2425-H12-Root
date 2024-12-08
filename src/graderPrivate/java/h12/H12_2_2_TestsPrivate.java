package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compression.rle.BitRunningLengthDecompressor;
import h12.lang.MyBit;
import h12.rubric.H12_Tests;
import h12.util.MockBitInputStream;
import h12.util.MockBitOutputStream;
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
import java.util.ArrayList;
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
        "in", node -> JsonConverters.toList(node, JsonNode::asInt),
        "out", node -> JsonConverters.toList(node, JsonNode::asInt),
        "bit", node -> MyBit.fromInt(node.asInt()),
        "countsCheck", node -> JsonConverters.toList(node, JsonNode::asInt),
        "bitsCheck", node -> JsonConverters.toList(node, element -> MyBit.fromInt(element.asInt()))
    );

    /**
     * The decompressor to test.
     */
    private BitRunningLengthDecompressor decompressor;

    @AfterEach
    void tearDown() throws Exception {
        decompressor.close();
    }

    @Override
    public Class<?> getClassType() {
        return BitRunningLengthDecompressor.class;
    }

    @DisplayName("Die Methode writeBit(int count, Bit bit) schreibt die Anzahl an aufeinanderfolgenden wiederholenden Bits korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_2_2_testWriteBit.json", customConverters = CUSTOM_CONVERTERS)
    void testWriteBit(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink writeBit = getMethod("writeBit", int.class, MyBit.class);

        // Test data
        List<Integer> bits = parameters.get("in");
        MockBitInputStream in = new MockBitInputStream(bits);
        MockBitOutputStream out = new MockBitOutputStream();
        decompressor = new BitRunningLengthDecompressor(in, out);

        int count = parameters.getInt("count");
        MyBit bit = parameters.get("bit", MyBit.class);

        // Context information
        Context context = contextBuilder(writeBit)
            .add("Stream", bits)
            .add("Count", count)
            .add("Bit", bit)
            .build();

        // Test the method
        writeBit.invoke(decompressor, count, bit);

        // Validate the output
        List<Integer> expectedOut = parameters.get("out");
        Assertions2.assertEquals(expectedOut, out.getBits(), context,
            comment -> "Wrong bits written to the output stream.");
    }

    @DisplayName("Die Methode decompress() liest die Anzahl an aufeinanderfolgenden wiederholenden Bits.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_2_2_testDecompressBitCount.json", customConverters = CUSTOM_CONVERTERS)
    void testDecompressBitCount(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink decompress = getMethod("decompress");

        // Test data
        List<Integer> bits = parameters.get("in");
        MockBitInputStream in = new MockBitInputStream(bits);
        MockBitOutputStream out = new MockBitOutputStream();
        List<Integer> countsCheck = new ArrayList<>();
        List<MyBit> bitsCheck = new ArrayList<>();
        decompressor = new BitRunningLengthDecompressor(in, out) {

            @Override
            protected void writeBit(int count, MyBit bit) throws IOException {
                countsCheck.add(count);
                bitsCheck.add(bit);
            }
        };

        // Context information
        Context context = contextBuilder(decompress)
            .add("Stream", bits)
            .build();

        // Test the method
        decompress.invoke(decompressor);

        // Validate the output
        List<Integer> expectedCounts = parameters.get("countsCheck");
        List<MyBit> expectedBits = parameters.get("bitsCheck");

        Assertions2.assertEquals(expectedCounts, countsCheck, context,
            comment -> "Wrong counts written to the output stream.");
        Assertions2.assertEquals(expectedBits, bitsCheck, context,
            comment -> "Wrong bits written to the output stream.");
    }

    @DisplayName("Die Methode decompress() dekomprimiert korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_2_2_testDecompress.json", customConverters = CUSTOM_CONVERTERS)
    void testDecompress(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink decompress = getMethod("decompress");

        // Test data
        List<Integer> bits = parameters.get("in");
        MockBitInputStream in = new MockBitInputStream(bits);
        MockBitOutputStream out = new MockBitOutputStream();
        decompressor = new BitRunningLengthDecompressor(in, out);

        // Context information
        Context context = contextBuilder(decompress)
            .add("Stream", bits)
            .build();

        // Test the method
        decompress.invoke(decompressor);

        // Validate the output
        List<Integer> expectedOut = parameters.get("out");
        Assertions2.assertEquals(expectedOut, out.getBits(), context,
            comment -> "Wrong bits written to the output stream.");
    }
}
