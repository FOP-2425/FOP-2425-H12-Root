package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compress.EncodingTable;
import h12.io.compress.huffman.HuffmanCodingCompressor;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the public tests for H12.4.1.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H12.4.1 | Huffman-Komprimierung")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_4_1_TestsPublic extends H12_Tests {

    /**
     * The custom converters for the JSON parameter set test annotation.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "text", JsonNode::asText,
        "compressed", node -> JsonConverters.toList(node, JsonNode::asInt),
        "encodingTable", JsonConverters::toEncodingTable
    );

    /**
     * The compressor instance used for testing.
     */
    private @Nullable HuffmanCodingCompressor compressor;

    @AfterEach
    void tearDown() throws Exception {
        if (compressor != null) {
            compressor.close();
        }
    }

    @Override
    public Class<?> getTestClass() {
        return HuffmanCodingCompressor.class;
    }

    @DisplayName("Die Methode getText() liest den Text korrekt ein.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_1_testGetText.json", customConverters = CUSTOM_CONVERTERS)
    void testGetText(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("getText");

        // Test setup
        String text = parameters.getString("text");

        TestInformation.TestInformationBuilder builder = testInformation(method)
            .preState(
                TestInformation.builder()
                    .add("in", text)
                    .add("out", List.of())
                    .build()
            )
            .postState(
                TestInformation.builder()
                    .add("out", List.of())
                    .build()
            );

        // Test execution
        ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        MockBitOutputStream out = new MockBitOutputStream();
        compressor = new HuffmanCodingCompressor(in, out);
        String result = method.invoke(compressor);

        // Close it to flush the output
        assert compressor != null;
        compressor.close();

        // Test evaluation
        Context context = builder.actualState(
            TestInformation.builder()
                .add("out", out.getBits())
                .build()
        ).build();

        // Validate the output
        Assertions2.assertEquals(text, result, context, comment -> "The read text is incorrect.");
    }

    @DisplayName("Die Methode computeTextSize(String text, EncodingTable encodingTable) berechnet die Anzahl an Bits, die für die Komprimierung des Textes nötig ist, korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_1_testComputeTextSize.json", customConverters = CUSTOM_CONVERTERS)
    void testComputeTextSize(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("computeTextSize", String.class, EncodingTable.class);

        // Test setup
        String text = parameters.getString("text");
        EncodingTable encodingTable = parameters.get("encodingTable");

        Context context = testInformation(method)
            .preState(
                TestInformation.builder()
                    .add("text", text)
                    .add("encodingTable", encodingTable)
                    .build()
            ).build();

        // Test the method
        compressor = new HuffmanCodingCompressor(new ByteArrayInputStream(new byte[0]), new MockBitOutputStream());
        int result = method.invoke(compressor, text, encodingTable);

        // Close it to flush the output
        assert compressor != null;
        compressor.close();

        // Test evaluation
        int textSize = parameters.getInt("textSize");
        Assertions2.assertEquals(textSize, result, context, comment -> "Computed text size is incorrect.");
    }

    @DisplayName("Die Methode encodeContent(String text, EncodingTable encodingTable) komprimiert den Text korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_1_testEncodeText.json", customConverters = CUSTOM_CONVERTERS)
    void testEncodeText(JsonParameterSet parameters) throws Throwable {
        // Access the method to test
        MethodLink method = getMethod("encodeText", String.class, EncodingTable.class);

        // Test setup
        String text = parameters.getString("text");
        EncodingTable encodingTable = parameters.get("encodingTable");
        List<Integer> compressed = parameters.get("compressed");
        InputStream in = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        MockBitOutputStream out = new MockBitOutputStream();

        TestInformation.TestInformationBuilder builder = testInformation(method).preState(
            TestInformation.builder()
                .add("text", text)
                .add("encodingTable", encodingTable)
                .add("out", out.getBits())
                .build()
        ).postState(
            TestInformation.builder()
                .add("out", compressed)
                .build()
        );

        // Test execution
        compressor = new HuffmanCodingCompressor(in, out);
        method.invoke(compressor, text, encodingTable);

        // Close it to flush the output
        assert compressor != null;
        compressor.close();

        // Test evaluation
        Context context = builder.actualState(
            TestInformation.builder()
                .add("out", out.getBits())
                .build()
        ).build();

        Assertions2.assertEquals(compressed, out.getBits(), context, comment -> "The compressed data is incorrect.");
    }

    @DisplayName("Die Methode compress() ist vollständig und korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_1_testCompress.json", customConverters = CUSTOM_CONVERTERS)
    void testCompress(JsonParameterSet parameters) throws IOException {
        // Access method to test
        MethodLink method = getMethod("compress");

        // Test setup
        String text = parameters.getString("text");
        EncodingTable encodingTable = parameters.get("encodingTable");
        List<Integer> compressed = parameters.get("compressed");
        InputStream in = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        MockBitOutputStream out = new MockBitOutputStream();

        TestInformation.TestInformationBuilder builder = testInformation(method).preState(
            TestInformation.builder()
                .add("text", text)
                .add("encodingTable", encodingTable)
                .add("out", out.getBits())
                .build()
        ).postState(
            TestInformation.builder()
                .add("out", compressed)
                .build()
        );

        // Test execution
        compressor = new HuffmanCodingCompressor(in, out);
        compressor.compress();

        // Test evaluation
        Context context = builder.actualState(
            TestInformation.builder()
                .add("out", out.getBits())
                .build()
        ).build();
        Assertions2.assertTrue(out.isFlushed(), context,
            comment -> "The output stream is not flushed.");
        Assertions2.assertEquals(compressed, out.getBits(), context, comment -> "The compressed data is incorrect.");
    }
}
