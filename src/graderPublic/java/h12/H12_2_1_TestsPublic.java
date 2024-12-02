package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.compression.rle.BitRunningLengthCompressor;
import h12.rubric.H12_Tests;
import h12.util.MockBitInputStream;
import h12.util.MockBitOutputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.io.IOException;
import java.util.Arrays;
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
        "bits", JsonConverters::toBitInputStream,
        "count", JsonNode::asInt,
        "startBits", JsonConverters::toIntArray,
        "counts", JsonConverters::toIntArray,
        "expected", node -> JsonConverters.toList(node, JsonNode::asInt)
    );

    /**
     * The compressor to test.
     */
    private BitRunningLengthCompressor compressor;

    @AfterEach
    void tearDown() throws Exception {
        compressor.close();
    }

    @Override
    public Class<?> getClassType() {
        return BitRunningLengthCompressor.class;
    }

    @DisplayName("Die Methode getBitCount(int bit) gibt die korrekte Anzahl an aufeinanderfolgenden wiederholenden Bits zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_Tests_testGetBitCount.json", customConverters = CUSTOM_CONVERTERS)
    void testGetBitCount(JsonParameterSet parameters) throws Throwable {
        // Access the method and fields for testing purposes or validation
        MethodLink getBitCount = getMethod("getBitCount", int.class);
        FieldLink lastRead = Links.getField(getType(), "lastRead");

        // Mock underlying streams
        MockBitInputStream in = parameters.get("bits");
        MockBitOutputStream out = new MockBitOutputStream();
        compressor = new BitRunningLengthCompressor(in, out);

        // Context information
        int startBit = in.readBit();
        List<Integer> otherBits = in.getBits().subList(1, in.getBits().size());
        int count = parameters.get("count");
        Context context = contextBuilder(getBitCount)
            .add("Method call", "getBitCount(%d)".formatted(startBit))
            .add("Stream", otherBits)
            .add("Expected count", count)
            .build();


        // Start the test
        int actual = getBitCount.invoke(compressor, in.getBits().getFirst());

        // Validate the output
        int expectedLastRead = startBit == 1 ? 0 : 1;
        int lastReadValue = lastRead.get(compressor);
        Assertions2.assertEquals(count, actual, context,
            comment -> "Expected the bit '%d' %dx, but got %d.".formatted(startBit, count, actual));
        Assertions2.assertEquals(expectedLastRead, lastReadValue, context,
            comment -> "The last read bit should be %d, but was %d.".formatted(expectedLastRead, lastReadValue));
    }

    /**
     * Asserts the compress method of the compressor.
     *
     * @param parameters the parameters for the test
     *
     * @throws IOException if an I/O error occurso
     */
    private void assertCompress(JsonParameterSet parameters) throws IOException {
        // Access the method and fields for testing purposes or validation
        MethodLink compress = getMethod("getBitCount", int.class);
        FieldLink inLink = Links.getField(getType(), "in");
        FieldLink outLink = Links.getField(getType(), "out");
        FieldLink lastRead = Links.getField(getType(), "lastRead");

        // Mock underlying streams
        MockBitInputStream in = parameters.get("bits");
        MockBitOutputStream out = new MockBitOutputStream();
        compressor = Mockito.mock(BitRunningLengthCompressor.class, Mockito.CALLS_REAL_METHODS);

        // Context information
        int[] startBits = parameters.get("startBits");
        int[] counts = parameters.get("counts");
        inLink.set(compressor, in);
        outLink.set(compressor, out);
        Context context = contextBuilder(compress)
            .add("Method call", "compress()")
            .add("Stream", in.getBits())
            .add("Expected counts", Arrays.toString(counts))
            .add("Expected start bits", Arrays.toString(startBits))
            .build();

        // Mocked getBitCount method to make the implementation testing of compress independent
        Answer<Integer> getBitCountAnswer = new Answer<>() {
            int index = 0;

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                int last = -1;
                int count = counts[index++];
                for (int i = 0; i < count; i++) {
                    last = in.readBit();
                }
                lastRead.set(compressor, last);
                return count;
            }
        };

        for (int i = 0; i <= 1; i++) {
            Mockito.doAnswer(getBitCountAnswer).when(compressor).getBitCount(i);
        }

        // Start the test
        compressor.compress();

        // Validate the output
        List<Integer> expected = parameters.get("expected");
        List<Integer> actual = out.getBits();
        Assertions2.assertEquals(expected, actual, context,
            comment -> "Expected the compressed bits %s, but got %s.".formatted(expected, actual));
    }

    @DisplayName("Die Methode compress() schreibt die Anzahl an aufeinanderfolgenden wiederholenden Bits korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_Tests_testCompressBitCount.json", customConverters = CUSTOM_CONVERTERS)
    void testCompressBitCount(JsonParameterSet parameters) throws IOException {
        assertCompress(parameters);
    }

    @DisplayName("Die Methode compress() komprimiert korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_Tests_testCompress.json", customConverters = CUSTOM_CONVERTERS)
    void testCompress(JsonParameterSet parameters) throws IOException {
        assertCompress(parameters);
    }
}
