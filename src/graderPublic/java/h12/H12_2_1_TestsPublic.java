package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.compression.Compressor;
import h12.io.compression.rle.BitRunningLengthCompressor;
import h12.rubric.H12_Tests;
import h12.util.MockBitInputStream;
import h12.util.MockBitOutputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the public tests for H12.1.1.
 *
 * @author Per Göttlicher, Nhan Huynh
 */

@TestForSubmission
@DisplayName("H12.2.1 | BitRunningLengthCompressor")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_2_1_TestsPublic extends H12_Tests {

    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "bits", JsonConverters::toBitInputStream,
        "count", JsonNode::asInt
    );

    private Compressor compressor;

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
        MethodLink getBitCount = getMethod("getBitCount", int.class);
        MockBitInputStream in = parameters.get("bits");
        int startBit = in.readBit();
        List<Integer> otherBits = in.getBits().subList(1, in.getBits().size());
        int count = parameters.get("count");

        Context context = contextBuilder(getBitCount)
            .add("Method call", "getBitCount(%d)".formatted(startBit))
            .add("Stream", otherBits)
            .add("Expected count", count)
            .build();

        MockBitOutputStream out = new MockBitOutputStream();
        compressor = new BitRunningLengthCompressor(in, out);
        int actual = getBitCount.invoke(compressor, in.getBits().getFirst());

        FieldLink lastRead = Links.getField(getType(), "lastRead");
        int expectedLastRead = startBit == 1 ? 0 : 1;
        int lastReadValue = lastRead.get(compressor);
        Assertions2.assertEquals(count, actual, context,
            comment -> "Expected the bit '%d' %dx, but got %d.".formatted(startBit, count, actual));
        Assertions2.assertEquals(expectedLastRead, lastReadValue, context,
            comment -> "The last read bit should be %d, but was %d.".formatted(expectedLastRead, lastReadValue));
    }

    @DisplayName("Die Methode compress() schreibt die Anzahl an aufeinanderfolgenden wiederholenden Bits korrekt.")
    @Test
    void testCompressBitCount() {

    }

    @DisplayName("Die Methode compress() komprimiert korrekt.")
    @Test
    void testCompress() {

    }
}
