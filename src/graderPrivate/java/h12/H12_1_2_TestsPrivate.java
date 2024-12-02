package h12;

import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.BufferedBitOutputStream;
import h12.lang.MyByte;
import h12.rubric.H12_Tests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        Assertions2.assertEquals(initialBuffer, new MyByte(out.toByteArray()[0]),
            context, comment -> "Buffer was written correctly");
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
}
