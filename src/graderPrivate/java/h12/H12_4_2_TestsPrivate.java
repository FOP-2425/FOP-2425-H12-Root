package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compression.EncodingTable;
import h12.io.compression.huffman.HuffmanCodingDecompressor;
import h12.lang.MyByte;
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

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for H12.4.2.
 *
 * @author Nhan Huynh
 */

@TestForSubmission
@DisplayName("H12.4.2 | Huffman-Dekomprimierung")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_4_2_TestsPrivate extends H12_Tests {

    /**
     * The custom converters for the JSON parameter set test annotation.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "inPreState", JsonConverters::toBitInputStream,
        "encodingTable", JsonConverters::toEncodingTable,
        "inPostState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "outPostState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "skipBits", JsonNode::asInt,
        "character", node -> node.asText().charAt(0),
        "text", JsonNode::asText
    );

    /**
     * The input stream used for testing.
     */
    private @Nullable MockBitInputStream in;

    /**
     * The output stream used for testing.
     */
    private @Nullable MockBitOutputStream out;

    /**
     * The decompressor to test.
     */
    private @Nullable HuffmanCodingDecompressor decompressor;

    @AfterEach
    void tearDown() throws Exception {
        if (decompressor != null) {
            decompressor.close();
        }
    }

    @Override
    public Class<?> getTestClass() {
        return HuffmanCodingDecompressor.class;
    }

    @DisplayName("Die Methode skipBits() überspringt die Füllbits korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_2_testSkipBits.json", customConverters = CUSTOM_CONVERTERS)
    void testSkipBits(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink method = getMethod("skipBits");

        // Test setup
        in = parameters.get("inPreState");
        out = new MockBitOutputStream();
        decompressor = new HuffmanCodingDecompressor(in, out);

        assert in != null;
        TestInformation.TestInformationBuilder builder = testInformation(method)
            .preState(
                TestInformation.builder()
                    .add("in", in.getBits().toString())
                    .add("out", out.getBits().toString())
                    .build()
            )
            .postState(
                TestInformation.builder()
                    .add("in", parameters.get("inPostState"))
                    .add("out", parameters.get("outPostState"))
                    .build()
            );

        // Test execution
        method.invoke(decompressor);

        // Test evaluation
        Context context = builder.actualState(
            TestInformation.builder()
                .add("in", in.getBits().toString())
                .add("out", out.getBits().toString())
                .build()
        ).build();
        int skipBits = parameters.get("skipBits");
        int actualSkipBits = in.getBits().size() - in.getRemainingBits().size() - MyByte.NUMBER_OF_BITS;
        Assertions2.assertEquals(skipBits, actualSkipBits, context,
            comment -> "Wrong number of bits skipped."
        );
        Assertions2.assertEquals(0, out.getBits().size(), context,
            comment -> "The output stream should be empty when skipping the bits."
        );
    }


    @DisplayName("Die Methode decodeCharacter(int startBit, EncodingTable encodingTable) dekomprimiert einen Zeichen korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_2_testDecodeCharacter.json", customConverters = CUSTOM_CONVERTERS)
    void testDecodeCharacter(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("decodeCharacter", int.class, EncodingTable.class);

        // Test setup
        in = parameters.get("inPreState");
        out = new MockBitOutputStream();
        decompressor = new HuffmanCodingDecompressor(in, out);
        int startBit = parameters.get("startBit");
        EncodingTable encodingTable = parameters.get("encodingTable");

        assert in != null;
        TestInformation.TestInformationBuilder builder = testInformation(method).preState(
            TestInformation.builder()
                .add("in", in.getBits().toString())
                .add("out", out.getBits().toString())
                .add("startBit", startBit)
                .add("encodingTable", encodingTable)
                .build()
        ).postState(
            TestInformation.builder()
                .add("in", parameters.get("inPostState"))
                .add("out", parameters.get("outPostState"))
                .build()
        );

        // Test execution
        char actual = method.invoke(decompressor, startBit, encodingTable);
        // Close it to flush the output
        decompressor.close();

        // Test evaluation
        Context context = builder.actualState(
            TestInformation.builder()
                .add("in", in.getBits().toString())
                .add("out", out.getBits().toString())
                .build()
        ).build();

        char expected = parameters.get("character");
        Assertions2.assertEquals(expected, actual, context,
            comment -> "The decoded character is incorrect."
        );
    }

    @DisplayName("Die Methode decodeContent(EncodingTable encodingTable) dekomprimiert den Text korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_2_testDecodeContent.json", customConverters = CUSTOM_CONVERTERS)
    void testDecodeContent(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink method = getMethod("decodeContent", EncodingTable.class);

        // Test setup
        in = parameters.get("inPreState");
        out = new MockBitOutputStream();
        decompressor = new HuffmanCodingDecompressor(in, out);
        EncodingTable encodingTable = parameters.get("encodingTable");

        assert in != null;
        List<Integer> outPostState = parameters.get("outPostState");
        TestInformation.TestInformationBuilder builder = testInformation(method)
            .preState(
                TestInformation.builder()
                    .add("in", in.getBits().toString())
                    .add("out", out.getBits().toString())
                    .add("encodingTable", encodingTable)
                    .build()
            ).postState(
                TestInformation.builder()
                    .add("in", parameters.get("inPostState"))
                    .add("out", outPostState)
                    .build()
            );

        // Test execution
        method.invoke(decompressor, encodingTable);

        // Close it to flush the output
        decompressor.close();

        // Test evaluation
        Context context = builder.actualState(
            TestInformation.builder()
                .add("in", in.getBits().toString())
                .add("out", out.getBits().toString())
                .build()
        ).build();

        Assertions2.assertEquals(outPostState, out.getBits(), context,
            comment -> "The decoded text is incorrect correct."
        );
    }

    @DisplayName("Die Methode decompress() ist vollständig und korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_2_testDecompress.json", customConverters = CUSTOM_CONVERTERS)
    void testDecompress(JsonParameterSet parameters) throws Throwable {
        // // Access method to test
        MethodLink method = getMethod("decompress");

        // Test setup
        MockBitInputStream in = parameters.get("inPreState");
        MockBitOutputStream out = new MockBitOutputStream();
        decompressor = new HuffmanCodingDecompressor(in, out);
        List<Integer> outPostState = parameters.get("outPostState");

        TestInformation.TestInformationBuilder builder = testInformation(method).preState(
            TestInformation.builder()
                .add("in", in.getBits().toString())
                .add("out", out.getBits().toString())
                .build()
        ).postState(
            TestInformation.builder()
                .add("in", parameters.get("inPostState"))
                .add("out", outPostState)
                .add("out (String)", parameters.get("text"))
                .build()
        );

        // Test execution
        method.invoke(decompressor);

        // Test evaluation
        Context context = builder.actualState(
            TestInformation.builder()
                .add("in", in.getBits().toString())
                .add("out", out.getBits().toString())
                .build()
        ).build();
        Assertions2.assertEquals(outPostState, out.getBits(), context,
            comment -> "The decoded text is incorrect."
        );
    }
}
