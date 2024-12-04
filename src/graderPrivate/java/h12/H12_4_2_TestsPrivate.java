package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compression.EncodingTable;
import h12.io.compression.huffman.HuffmanCodingDecompressor;
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

import java.io.ByteArrayOutputStream;
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
        "in", JsonConverters::toBitInputStream,
        "skipBits", JsonNode::asInt,
        "encodingTable", JsonConverters::toEncodingTable,
        "character", node -> node.asText().charAt(0),
        "text", JsonNode::asText
    );

    /**
     * The decompressor to test.
     */
    private HuffmanCodingDecompressor decompressor;

    @AfterEach
    void tearDown() throws Exception {
        decompressor.close();
    }

    @Override
    public Class<?> getClassType() {
        return HuffmanCodingDecompressor.class;
    }


    @DisplayName("Die Methode skipBits() überspringt die Füllbits korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_2_testSkipBits.json", customConverters = CUSTOM_CONVERTERS)
    void testSkipBits(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink method = getMethod("skipBits");

        // Test data
        MockBitInputStream in = parameters.get("in");
        MockBitOutputStream out = new MockBitOutputStream();
        decompressor = new HuffmanCodingDecompressor(in, out);
        int skipBits = parameters.get("skipBits");

        // Context information
        Context context = contextBuilder(method)
            .add("Stream", in.getBits())
            .add("Skip Bits", skipBits)
            .build();

        // Test the method
        method.invoke(decompressor);

        // Validate the output
        Assertions2.assertEquals(0, out.getBits().size(), context,
            comment -> "The output stream should be empty when skipping the bits."
        );
    }

    @DisplayName("Die Methode decodeCharacter(int startBit, EncodingTable encodingTable) dekomprimiert einen Zeichen korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_2_testDecodeCharacter.json", customConverters = CUSTOM_CONVERTERS)
    void testDecodeCharacter(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink method = getMethod("decodeCharacter", int.class, EncodingTable.class);

        // Test data
        MockBitInputStream in = parameters.get("in");
        List<Integer> bits = parameters.get("bits");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decompressor = new HuffmanCodingDecompressor(in, out);
        int startBit = bits.getFirst();
        EncodingTable encodingTable = parameters.get("encodingTable");

        // Context information
        Context context = contextBuilder(method)
            .add("Stream", in.getBits())
            .add("Start bit", startBit)
            .add("Encoding Table", encodingTable)
            .build();

        // Test the method
        char actual = method.invoke(decompressor, startBit, encodingTable);

        // Validate the output
        char expected = parameters.get("character");
        Assertions2.assertEquals(expected, actual, context,
            comment -> "The decoded character should be correct."
        );
    }

    @DisplayName("Die Methode decodeContent(EncodingTable encodingTable) Dekomprimiert den Text korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_2_testDecodeContent.json", customConverters = CUSTOM_CONVERTERS)
    void testDecodeContent(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink method = getMethod("decodeContent", EncodingTable.class);

        // Test data
        MockBitInputStream in = parameters.get("in");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decompressor = new HuffmanCodingDecompressor(in, out);
        EncodingTable encodingTable = parameters.get("encodingTable");

        // Context information
        Context context = contextBuilder(method)
            .add("Stream", in.getBits())
            .add("Encoding Table", encodingTable)
            .build();

        // Test the method
        method.invoke(decompressor, encodingTable);

        // Validate the output
        String actual = out.toString();
        String expected = parameters.get("text");
        Assertions2.assertEquals(expected, actual, context,
            comment -> "The decoded text should be correct."
        );
    }

    @DisplayName("Die Methode decompress() ist vollständig und korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_4_2_testDecompress.json", customConverters = CUSTOM_CONVERTERS)
    void testDecompress(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink method = getMethod("decompress");

        // Test data
        MockBitInputStream in = parameters.get("in");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Context information
        Context context = contextBuilder(method)
            .add("Stream", in.getBits())
            .build();

        // Test the method
        method.invoke(decompressor);

        // Validate the output
        String actual = out.toString();
        String expected = parameters.get("text");
        Assertions2.assertEquals(expected, actual, context,
            comment -> "The decoded text should be correct."
        );
    }
}
