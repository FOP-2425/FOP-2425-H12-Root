package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compression.huffman.EncodingTable;
import h12.io.compression.huffman.HuffmanCodingCompressor;
import h12.rubric.H12_Tests;
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
    private HuffmanCodingCompressor compressor;

    @AfterEach
    void tearDown() throws Exception {
        compressor.close();
    }

    @Override
    public Class<?> getClassType() {
        return HuffmanCodingCompressor.class;
    }

    @DisplayName("Die Methode getText() liest den Text korrekt ein.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_1_testGetText.json", customConverters = CUSTOM_CONVERTERS)
    void testGetText(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink getText = getMethod("getText");

        // Test data
        String text = parameters.getString("text");

        // Context information
        Context context = contextBuilder(getText)
            .add("Text", text)
            .build();

        // Test the method
        InputStream in = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        MockBitOutputStream out = new MockBitOutputStream();
        compressor = new HuffmanCodingCompressor(in, out);
        String result = getText.invoke(compressor);

        // Validate the output
        Assertions2.assertEquals(text, result, context, comment -> "The text is incorrect.");
    }

    @DisplayName("Die Methode computeTextSize(String text, EncodingTable encodingTable) berechnet die Anzahl an Bits, die für die Komprimierung des Textes nötig ist, korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_1_testComputeTextSize.json", customConverters = CUSTOM_CONVERTERS)
    void testComputeTextSize(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink computeTextSize = getMethod("computeTextSize", String.class, EncodingTable.class);

        // Test data
        String text = parameters.getString("text");
        EncodingTable encodingTable = parameters.get("encodingTable");

        // Context information
        Context context = contextBuilder(computeTextSize)
            .add("Text", text)
            .add("Encoding table", encodingTable)
            .build();

        // Test the method
        compressor = new HuffmanCodingCompressor(new ByteArrayInputStream(new byte[0]), new MockBitOutputStream());
        int result = computeTextSize.invoke(compressor, text, encodingTable);

        // Validate the output
        int textSize = parameters.getInt("textSize");
        Assertions2.assertEquals(textSize, result, context, comment -> "The text size is incorrect.");
    }

    @DisplayName("Die Methode encodeContent(String text, EncodingTable encodingTable) komprimiert den Text korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_1_testEncodeContent.json", customConverters = CUSTOM_CONVERTERS)
    void testEncodeContent(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink encodeContent = getMethod("encodeContent", String.class, EncodingTable.class);

        // Test data
        String text = parameters.getString("text");
        EncodingTable encodingTable = parameters.get("encodingTable");

        // Context information
        Context context = contextBuilder(encodeContent)
            .add("Text", text)
            .add("Encoding table", encodingTable)
            .build();

        // Test the method
        InputStream in = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        MockBitOutputStream out = new MockBitOutputStream();
        compressor = new HuffmanCodingCompressor(in, out);
        encodeContent.invoke(compressor, text, encodingTable);

        // Validate the output
        List<Integer> compressed = parameters.get("compressed");
        Assertions2.assertEquals(compressed, out.getBits(), context, comment -> "The compressed data is incorrect.");
    }

    @DisplayName("Die Methode getText() liest den Text korrekt ein.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "Die Methode compress() ist vollständig und korrekt.", customConverters = CUSTOM_CONVERTERS)
    void testCompress(JsonParameterSet parameters) throws IOException {
        // Access the method
        MethodLink compress = getMethod("compress");

        // Test data
        String text = parameters.getString("text");
        List<Integer> compressed = parameters.get("compressed");
        InputStream in = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        MockBitOutputStream out = new MockBitOutputStream();

        // Context information
        Context context = contextBuilder(compress)
            .add("Text", text)
            .add("Expected compressed data", compressed)
            .build();

        // Test the method
        compressor = new HuffmanCodingCompressor(in, out);
        compressor.compress();

        // Validate the output
        Assertions2.assertEquals(compressed, out.getBits(), context, comment -> "The compressed data is incorrect.");
    }
}
