package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.BufferedBitOutputStream;
import h12.lang.MyBit;
import h12.lang.MyByte;
import h12.rubric.H12_Tests;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the private tests for H12.1.2.
 *
 * @author Nhan Huynh
 */

@TestForSubmission
@DisplayName("H12.1.2 | Bits schreiben")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_1_2_TestsPrivate extends H12_Tests {

    /**
     * The custom converters for the JSON parameter set test annotation.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "initialPosition", JsonNode::asInt,
        "initialBuffer", JsonNode::asInt,
        "bit", JsonNode::asInt,
        "byte", JsonNode::asInt,
        "expectedPosition", JsonNode::asInt,
        "expectedBuffer", JsonNode::asInt,
        "expectedOutput", JsonConverters::toByteArray
    );

    /**
     * The output stream for testing.
     */
    private BufferedBitOutputStream stream;

    @AfterEach
    void tearDown() throws IOException {
        stream.close();
    }

    @Override
    public Class<?> getClassType() {
        return BufferedBitOutputStream.class;
    }

    @Test
    @DisplayName("Die Methode flushBuffer() aktualisiert den Puffer und Position korrekt.")
    void testFlushBufferUpdateYes() throws Throwable {
        // Access test method and fields
        MethodLink flushBuffer = getMethod("flushBuffer");
        FieldLink position = Links.getField(getType(), "position");
        FieldLink buffer = Links.getField(getType(), "buffer");

        // Test data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        stream = new BufferedBitOutputStream(out);
        int initialPosition = 2;
        position.set(stream, initialPosition);
        MyByte initialBuffer = new MyByte(69);
        buffer.set(stream, initialBuffer);

        int expectedPos = MyByte.NUMBER_OF_BITS - 1;
        MyByte expectedBuffer = new MyByte();

        // Context information
        Context context = contextBuilder(flushBuffer)
            .add("Initial position", initialPosition)
            .add("Initial buffer", initialBuffer)
            .add("Expected position after flush", expectedPos)
            .add("Expected buffer after flush", expectedBuffer)
            .build();

        // Test method
        flushBuffer.invoke(stream);

        // Validate output
        Assertions2.assertEquals(expectedPos, position.get(stream), context,
            comment -> "Position was not updated correctly.");
        Assertions2.assertEquals(expectedBuffer, buffer.get(stream), context,
            comment -> "Buffer was not updated correctly.");
    }

    @Test
    @DisplayName("Die Methode flushBuffer() aktualisiert nicht den Puffer und Position, wenn nötig.")
    void testFlushBufferUpdateNo() throws Throwable {
        // Access test method and fields
        MethodLink flushBuffer = getMethod("flushBuffer");
        FieldLink position = Links.getField(getType(), "position");
        FieldLink buffer = Links.getField(getType(), "buffer");

        // Test data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        stream = new BufferedBitOutputStream(out);
        int initialPosition = MyByte.NUMBER_OF_BITS - 1;
        position.set(stream, initialPosition);
        MyByte initialBuffer = new MyByte(69);
        buffer.set(stream, initialBuffer);
        // Context information
        Context context = contextBuilder(flushBuffer)
            .add("Initial position", initialPosition)
            .add("Initial buffer", initialBuffer)
            .add("Expected position after flush", initialPosition)
            .add("Expected buffer after flush", initialPosition)
            .build();

        // Test method
        flushBuffer.invoke(stream);

        // Validate output
        Assertions2.assertEquals(initialPosition, position.get(stream), context,
            comment -> "Position was not updated correctly.");
        Assertions2.assertEquals(initialBuffer, buffer.get(stream), context,
            comment -> "Buffer was not updated correctly.");
    }

    @DisplayName("Die Methode flushBuffer() schreibt das Zeichen in den internen OutputStream korrekt.")
    @Test
    void testFlushBufferWrite() throws Throwable {
        // Access test method and fields
        MethodLink flushBuffer = getMethod("flushBuffer");
        FieldLink position = Links.getField(getType(), "position");
        FieldLink buffer = Links.getField(getType(), "buffer");

        // Test data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        stream = new BufferedBitOutputStream(out);
        int initialPosition = 2;
        position.set(stream, initialPosition);
        MyByte initialBuffer = new MyByte(69);
        buffer.set(stream, initialBuffer);

        int expectedPos = MyByte.NUMBER_OF_BITS - 1;
        MyByte expectedBuffer = new MyByte();

        // Context information
        Context context = contextBuilder(flushBuffer)
            .add("Initial position", initialPosition)
            .add("Initial buffer", initialBuffer)
            .add("Expected position after flush", expectedPos)
            .add("Expected buffer after flush", expectedBuffer)
            .build();

        // Test method
        flushBuffer.invoke(stream);

        // Validate output
        Assertions2.assertEquals(initialBuffer, new MyByte(out.toByteArray()[0]),
            context, comment -> "Buffer was written correctly");
    }

    @DisplayName("Die Methode writeBit(Bit bit) schreibt das Zeichen in den internen OutputStream, falls der Puffer voll ist.")
    @Test
    void testWriteBitFlushYes() throws Throwable {
        // Access test method and fields
        MethodLink writeBit = getMethod("writeBit", MyBit.class);
        FieldLink position = Links.getField(getType(), "position");
        FieldLink buffer = Links.getField(getType(), "buffer");

        // Test data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        stream = new BufferedBitOutputStream(out);
        int initialPosition = -1;
        position.set(stream, initialPosition);
        MyByte initialBuffer = new MyByte(69);
        buffer.set(stream, initialBuffer);
        MyBit toWrite = MyBit.ONE;

        // Context information
        Context context = contextBuilder(writeBit)
            .add("Initial position", initialPosition)
            .add("Initial buffer", initialBuffer)
            .add("Bit to write", toWrite)
            .build();

        // Test method
        writeBit.invoke(stream, toWrite);

        // Validate output
        Assertions2.assertEquals(initialBuffer, new MyByte(out.toByteArray()[0]),
            context, comment -> "Buffer was written correctly");
    }

    @DisplayName("Die Methode writeBit(Bit bit) schreibt das Zeichen in den internen OutputStream, falls der Puffer voll ist.")
    @Test
    void testWriteBitFlushNo() throws Throwable {
        // Access test method and fields
        MethodLink writeBit = getMethod("writeBit", MyBit.class);
        FieldLink position = Links.getField(getType(), "position");
        FieldLink buffer = Links.getField(getType(), "buffer");

        // Test data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        stream = new BufferedBitOutputStream(out);
        int initialPosition = 3;
        position.set(stream, initialPosition);
        MyByte initialBuffer = new MyByte(69);
        buffer.set(stream, initialBuffer);
        MyBit toWrite = MyBit.ONE;

        // Context information
        Context context = contextBuilder(writeBit)
            .add("Initial position", initialPosition)
            .add("Initial buffer", initialBuffer)
            .add("Bit to write", toWrite)
            .build();

        // Test method
        writeBit.invoke(stream, toWrite);

        // Validate output
        Assertions2.assertEquals(0, out.toByteArray().length,
            context, comment -> "Buffer should not have been written.");
    }

    @DisplayName("Die Methode writeBit(Bit bit) schreibt ein Bit korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testWriteBit.json", customConverters = CUSTOM_CONVERTERS)
    void testWriteBit(JsonParameterSet parameters) throws Throwable {
        // Access test method and fields
        MethodLink writeBit = getMethod("writeBit", MyBit.class);
        FieldLink position = Links.getField(getType(), "position");
        FieldLink buffer = Links.getField(getType(), "buffer");

        // Test data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        stream = new BufferedBitOutputStream(out);
        int initialPosition = parameters.getInt("initialPosition");
        position.set(stream, initialPosition);
        MyByte initialBuffer = new MyByte(parameters.getInt("initialBuffer"));
        buffer.set(stream, initialBuffer);
        MyBit bit = MyBit.fromInt(parameters.getInt("bit"));

        // Context information
        Context context = contextBuilder(writeBit)
            .add("Initial position", initialPosition)
            .add("Initial buffer", initialBuffer)
            .add("Bit to write", bit)
            .build();

        // Test method
        writeBit.invoke(stream, bit);

        // Validate output
        int expectedPosition = parameters.getInt("expectedPosition");
        Assertions2.assertEquals(expectedPosition, position.get(stream),
            context, comment -> "Position was not updated correctly.");
        MyByte expectedBuffer = new MyByte(parameters.getInt("expectedBuffer"));
        Assertions2.assertEquals(expectedBuffer, buffer.get(stream),
            context, comment -> "Buffer was not updated correctly.");
    }

    @DisplayName("Die Methode write(int b) schreibt ein Byte korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testWrite.json", customConverters = CUSTOM_CONVERTERS)
    void testWrite(JsonParameterSet parameters) throws Throwable {
        // Access test method and fields
        MethodLink writeBit = getMethod("write", int.class);
        FieldLink position = Links.getField(getType(), "position");
        FieldLink buffer = Links.getField(getType(), "buffer");

        // Test data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        stream = new BufferedBitOutputStream(out);
        int initialPosition = parameters.getInt("initialPosition");
        position.set(stream, initialPosition);
        MyByte initialBuffer = new MyByte(parameters.getInt("initialBuffer"));
        buffer.set(stream, initialBuffer);
        int byteValue = parameters.getInt("byte");

        // Context information
        Context context = contextBuilder(writeBit)
            .add("Initial position", initialPosition)
            .add("Initial buffer", initialBuffer)
            .add("Byte to write", byteValue)
            .build();

        // Test method
        writeBit.invoke(stream, byteValue);

        // Validate output
        int expectedPosition = parameters.getInt("expectedPosition");
        Assertions2.assertEquals(expectedPosition, position.get(stream),
            context, comment -> "Position was not updated correctly.");
        MyByte expectedBuffer = new MyByte(parameters.getInt("expectedBuffer"));
        Assertions2.assertEquals(expectedBuffer, buffer.get(stream),
            context, comment -> "Buffer was not updated correctly.");
        byte[] expectedOutput = parameters.get("expectedOutput");
        TutorAssertions.assertArrayEquals(expectedOutput, out.toByteArray(),
            context, comment -> "Output was not written correctly.");
    }

    @DisplayName("Die Methode write(int b) wirft eine IllegalArgumentException, falls die Eingabe kein Byte ist.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testWriteIllegalArgumentException.json", customConverters = CUSTOM_CONVERTERS)
    void testWriteIllegalArgumentException(JsonParameterSet parameters) throws Throwable {
        // Access test method and fields
        MethodLink writeBit = getMethod("write", int.class);
        FieldLink position = Links.getField(getType(), "position");
        FieldLink buffer = Links.getField(getType(), "buffer");

        // Test data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        stream = new BufferedBitOutputStream(out);
        int initialPosition = parameters.getInt("initialPosition");
        position.set(stream, initialPosition);
        MyByte initialBuffer = new MyByte(parameters.getInt("initialBuffer"));
        buffer.set(stream, initialBuffer);
        int byteValue = parameters.getInt("byte");

        // Context information
        Context context = contextBuilder(writeBit)
            .add("Initial position", initialPosition)
            .add("Initial buffer", initialBuffer)
            .add("Byte to write", byteValue)
            .build();

        // Validate output
        Assertions2.assertThrows(IllegalArgumentException.class, () -> writeBit.invoke(stream, byteValue),
            context, comment -> "Method should throw an IllegalArgumentException for invalid byte value.");
    }
}
