package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.BufferedBitInputStream;
import h12.lang.MyByte;
import h12.rubric.H12_Tests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the public tests for H12.1.1.
 *
 * @author Per Göttlicher, Nhan Huynh
 */

@TestForSubmission
@DisplayName("H12.1.1 | Bits lesen")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_1_1_TestsPublic extends H12_Tests {

    /**
     * The custom converters for the JSON parameter set test annotation.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "bytes", node -> node.asText().getBytes(StandardCharsets.UTF_8),
        "bytesToRead", JsonNode::intValue,
        "positionBefore", JsonNode::intValue,
        "expectedResult", JsonNode::intValue
    );

    /**
     * The example bytes to read.
     */
    private static final byte[] BYTES_TO_READ = "Hello, World!".getBytes(StandardCharsets.UTF_8);

    /**
     * The field link for the buffer field.
     */
    private FieldLink buffer;

    /**
     * The field link for the position field.
     */
    private FieldLink position;

    /**
     * The underlying byte array input stream for the input stream to test.
     */
    private ByteArrayInputStream underlying;

    /**
     * The input stream to test.
     */
    private BufferedBitInputStream stream;

    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        buffer = Links.getField(getType(), "buffer");
        position = Links.getField(getType(), "position");
    }

    @AfterEach
    void tearDown() throws IOException {
        underlying.close();
        stream.close();
    }

    @Override
    public Class<?> getClassType() {
        return BufferedBitInputStream.class;
    }

    @DisplayName("Die Methode fetch() aktualisiert im Falle von nicht EOF den Puffer und die Position korrekt.")
    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testFetchNotEOF() throws Throwable {
        underlying = new ByteArrayInputStream(BYTES_TO_READ);
        stream = new BufferedBitInputStream(underlying);
        MethodLink fetch = Links.getMethod(getType(), "fetch");
        buffer.set(stream, new MyByte(BYTES_TO_READ[0]));
        underlying.read();
        MyByte expectedBuffer = new MyByte(BYTES_TO_READ[1]);
        int expectedPosition = 7;
        Context context = contextBuilder(fetch)
            .add("Buffer before", buffer.get(stream))
            .add("Position before", position.get(stream))
            .add("Expected buffer after", expectedBuffer)
            .add("Expected position after", expectedPosition)
            .build();

        fetch.invoke(stream);

        MyByte actualBuffer = buffer.get(stream);
        int actualPosition = position.get(stream);

        Assertions2.assertEquals(expectedBuffer, actualBuffer, context, comment -> "Buffer is not updated correctly.");
        Assertions2.assertEquals(expectedPosition, actualPosition, context,
            comment -> "Position is not updated correctly.");
    }

    @DisplayName("Die Methode fetch() aktualisiert im Falle von EOF den Puffer und die Position korrekt.")
    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testFetchEOF() throws Throwable {
        underlying = new ByteArrayInputStream(BYTES_TO_READ);
        stream = new BufferedBitInputStream(underlying);
        MethodLink fetch = Links.getMethod(getType(), "fetch");
        for (int i = 0; i < BYTES_TO_READ.length; i++) {
            underlying.read();
        }
        buffer.set(stream, new MyByte(BYTES_TO_READ[BYTES_TO_READ.length - 1]));
        position.set(stream, -1);

        int expectedPosition = -1;
        Context context = contextBuilder(fetch)
            .add("Buffer before", buffer.get(stream))
            .add("Position before", position.get(stream))
            .add("Expected buffer after", "null")
            .add("Expected position after", expectedPosition)
            .build();

        fetch.invoke(stream);

        MyByte actualBuffer = buffer.get(stream);
        int actualPosition = position.get(stream);

        Assertions2.assertNull(actualBuffer, context, comment -> "Buffer is not updated correctly.");
        Assertions2.assertEquals(expectedPosition, actualPosition, context,
            comment -> "Position is not updated correctly.");
    }

    @DisplayName("Die Methode readBit() liest das nächste Byte korrekt, falls wir bereits alle Bits des vorherigen Bytes gelesen haben")
    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testReadNextByte() throws Throwable {
        underlying = new ByteArrayInputStream(BYTES_TO_READ);
        stream = new BufferedBitInputStream(underlying);
        MethodLink readBit = Links.getMethod(getType(), "readBit");
        MyByte myByteBefore = new MyByte(BYTES_TO_READ[0]);
        int positionBefore = -1;
        this.buffer.set(stream, myByteBefore);
        this.position.set(stream, positionBefore);

        // Skip the first byte
        underlying.read();

        MyByte myByteAfter = new MyByte(BYTES_TO_READ[1]);
        int positionAfter = 7;
        int expectedBit = myByteAfter.get(positionAfter).getValue();
        Context context = contextBuilder(readBit)
            .add("Buffer before", buffer.get(stream))
            .add("Position before", this.position.get(stream))
            .add("Expected buffer after", myByteAfter)
            .add("Expected position after", positionAfter)
            .add("Expected bit", expectedBit)
            .build();

        int actualBit = readBit.invoke(stream);
        Assertions2.assertEquals(expectedBit, actualBit, context,
            comment -> "The method should return the correct bit.");
    }

    /**
     * Asserts that the method readBit() returns the correct bit when reading the bit at the given position.
     *
     * @param position the position to read the bit from
     *
     * @throws Throwable if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void assertReadBit(int position) throws Throwable {
        underlying = new ByteArrayInputStream(BYTES_TO_READ);
        stream = new BufferedBitInputStream(underlying);
        MethodLink readBit = Links.getMethod(getType(), "readBit");
        MyByte myByte = new MyByte(BYTES_TO_READ[0]);
        this.buffer.set(stream, myByte);
        this.position.set(stream, position);

        // Skip the first byte
        underlying.read();

        int expectedBit = myByte.get(position).getValue();
        Context context = contextBuilder(readBit)
            .add("Buffer before", buffer.get(stream))
            .add("Position before", this.position.get(stream))
            .add("Expected bit", expectedBit)
            .add("Position after", position - 1)
            .build();

        int actualBit = readBit.invoke(stream);
        Assertions2.assertEquals(expectedBit, actualBit, context,
            comment -> "The method should return the correct bit.");
    }

    @DisplayName("Die Methode readBit() gibt im Falle von Lesen des ersten Bit das korrekte Bit zurück.")
    @Test
    void testReadBitByteStart() throws Throwable {
        assertReadBit(7);
    }

    @DisplayName("Die Methode readBit() gibt im Falle von Lesen eines mittleren Bit das korrekte Bit zurück.")
    @Test
    void testReadBitByteMiddle() throws Throwable {
        assertReadBit(3);
    }

    @DisplayName("Die Methode readBit() gibt im Falle von Lesen des letzen Bit eines Bytes das korrekte Bit zurück.")
    @Test
    void testReadBitByteEnd() throws Throwable {
        assertReadBit(0);
    }

    @DisplayName("Die Methode readBit() gibt im Falle von EOF das korrekte Bit zurück.")
    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testReadBitEOF() throws Throwable {
        underlying = new ByteArrayInputStream(BYTES_TO_READ);
        stream = new BufferedBitInputStream(underlying);
        MethodLink readBit = Links.getMethod(getType(), "readBit");
        int position = -1;
        this.buffer.set(stream, null);
        this.position.set(stream, position);
        for (int i = 0; i < BYTES_TO_READ.length; i++) {
            underlying.read();
        }

        Context context = contextBuilder(readBit)
            .add("Buffer before", buffer.get(stream))
            .build();

        int actualBit = readBit.invoke(stream);

        Assertions2.assertEquals(-1, actualBit, context,
            comment -> "The method should return -1 if the buffer is empty.");
    }

    /**
     * Asserts that the method read() returns the correct result.
     *
     * @param parameters         the parameters for the test
     * @param preCommentSupplier the supplier for the pre-comment
     *
     * @throws Throwable if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void assertRead(
        JsonParameterSet parameters,
        PreCommentSupplier<? super ResultOfObject<Integer>> preCommentSupplier
    ) throws Throwable {
        byte[] bytes = parameters.get("bytes");
        underlying = new ByteArrayInputStream(bytes);
        stream = new BufferedBitInputStream(underlying);
        int bytesToRead = parameters.get("bytesToRead");
        MyByte myByte = new MyByte(bytes[bytesToRead]);
        for (int i = 0; i < bytesToRead + 1; i++) {
            stream.read();
        }
        int positionBefore = parameters.get("positionBefore");
        buffer.set(stream, myByte);
        position.set(stream, positionBefore);

        MethodLink read = Links.getMethod(getType(), "read", Matcher.of(method -> method.typeList().isEmpty()));
        int expected = parameters.get("expectedResult");
        Context context = contextBuilder(read)
            .add("Stream", Arrays.toString(bytes))
            .add("Buffer before", buffer.get(stream))
            .add("Position before", position.get(stream))
            .add("Expected result", expected)
            .build();
        int actual = read.invoke(stream);
        Assertions2.assertEquals(expected, actual, context, preCommentSupplier);
    }

    @DisplayName("Die Methode read() gibt das korrekte Ergebnis zurück, falls wir am Ende des Streams sind.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadEnd.json", customConverters = CUSTOM_CONVERTERS)
    void testReadEnd(JsonParameterSet parameters) throws Throwable {
        assertRead(parameters, comment -> "The method should return -1 if we are at the end of the file.");
    }

    @DisplayName("Die Methode read() gibt das korrekte Teilergebnis zurück, falls der Stream keine 8 Bits mehr enthält.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadPartial.json", customConverters = CUSTOM_CONVERTERS)
    void testReadPartial(JsonParameterSet parameters) throws Throwable {
        assertRead(parameters, comment -> "The method should return the correct result if the stream does not contain 8 bits.");
    }

    @DisplayName("Die Methode read() gibt in allen anderen Fallen das korrekte Ergebnis zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testRead.json", customConverters = CUSTOM_CONVERTERS)
    void testRead(JsonParameterSet parameters) throws Throwable {
        assertRead(parameters, comment -> "The method should return the correct result.");
    }
}
