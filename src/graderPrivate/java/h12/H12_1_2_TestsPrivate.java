package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.BufferedBitOutputStream;
import h12.lang.MyBit;
import h12.lang.MyByte;
import h12.mock.MockBitOutputStream;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

import java.io.IOException;
import java.util.List;
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
        "bufferPreState", JsonConverters::toMyByte,
        "positionPreState", JsonNode::asInt,
        "bit", JsonConverters::toBit,
        "bitsPostState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "bufferPostState", JsonConverters::toMyByte,
        "positionPostState", JsonNode::asInt
    );

    /**
     * The underlying byte array input stream for the input stream to test.
     */
    private @Nullable MockBitOutputStream underlying;

    /**
     * The output stream for testing.
     */
    private @Nullable BufferedBitOutputStream stream;

    /**
     * The field link for the buffer field.
     */
    private FieldLink buffer;

    /**
     * The field link for the position field.
     */
    private FieldLink position;

    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        buffer = Links.getField(getType(), "buffer");
        position = Links.getField(getType(), "position");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (stream != null) {
            stream.close();
        }
    }

    @Override
    public Class<?> getTestClass() {
        return BufferedBitOutputStream.class;
    }

    /**
     * Initializes the test with the context.
     *
     * @param method           the method to test
     * @param preStateBuilder  the pre-state builder for the test information
     * @param postStateBuilder the post-state builder for the test information
     * @param parameters       the parameters for the test
     *
     * @return the test information builder used to specify the test information
     */
    private TestInformation.TestInformationBuilder initTest(
        MethodLink method,
        Function<TestInformation.TestInformationBuilder, TestInformation.TestInformationBuilder> preStateBuilder,
        Function<TestInformation.TestInformationBuilder, TestInformation.TestInformationBuilder> postStateBuilder,
        JsonParameterSet parameters) {
        // Test setup
        underlying = new MockBitOutputStream();
        stream = new BufferedBitOutputStream(underlying);

        MyByte bufferPreState = parameters.get("bufferPreState");
        buffer.set(stream, bufferPreState);
        int positionPreState = parameters.get("positionPreState");
        position.set(stream, positionPreState);

        List<Integer> bitsPostState = parameters.get("bitsPostState");
        MyByte bufferPostState = parameters.get("bufferPostState");
        int positionPostState = parameters.get("positionPostState");

        return testInformation(method)
            .preState(
                preStateBuilder.apply(
                    TestInformation.builder()
                        .add("underlying", underlying.getBits().toString())
                        .add("buffer", bufferPreState)
                        .add("position", positionPreState)
                ).build()
            )
            .postState(
                postStateBuilder.apply(
                    TestInformation.builder()
                        .add("underlying", bitsPostState)
                        .add("buffer", bufferPostState)
                        .add("position", positionPostState)
                ).build()
            );
    }

    /**
     * Initializes the test with the context.
     *
     * @param method     the method to test
     * @param parameters the parameters for the test
     *
     * @return the test information builder used to specify the test information
     */
    private TestInformation.TestInformationBuilder initTest(MethodLink method, JsonParameterSet parameters) {
        return initTest(method, Function.identity(), Function.identity(), parameters);
    }

    /**
     * Asserts that the buffer and position are updated correctly after calling the flushBuffer method.
     *
     * @param parameters the parameters for the test
     *
     * @throws Throwable if an error occurs
     */
    private void assertFlushBufferUpdate(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("flushBuffer");

        // Test setup
        TestInformation.TestInformationBuilder builder = initTest(method, parameters);

        // Test execution
        method.invoke(stream);

        // Test evaluation
        MyByte bufferPostState = parameters.get("bufferPostState");
        int positionPostState = parameters.get("positionPostState");
        MyByte bufferActualState = buffer.get(stream);
        int positionActualState = position.get(stream);

        assert underlying != null;
        Context context = builder.actualState(
            TestInformation.builder()
                .add("underlying", underlying.getBits().toString())
                .add("buffer", bufferActualState)
                .add("position", positionActualState)
                .build()
        ).build();

        Assertions2.assertEquals(bufferPostState, bufferActualState, context, comment -> "Buffer is not updated correctly.");
        Assertions2.assertEquals(positionPostState, positionActualState, context,
            comment -> "Position is not updated correctly.");
    }

    @DisplayName("Die Methode flushBuffer() aktualisiert den Puffer und Position korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testFlushBufferUpdateYes.json", customConverters = CUSTOM_CONVERTERS)
    void testFlushBufferUpdateYes(JsonParameterSet parameters) throws Throwable {
        assertFlushBufferUpdate(parameters);
    }

    @DisplayName("Die Methode flushBuffer() aktualisiert nicht den Puffer und Position, wenn nötig.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testFlushBufferUpdateNo.json", customConverters = CUSTOM_CONVERTERS)
    void testFlushBufferUpdateNo(JsonParameterSet parameters) throws Throwable {
        assertFlushBufferUpdate(parameters);
    }

    @DisplayName("Die Methode flushBuffer() schreibt das Zeichen in den internen OutputStream korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testFlushBufferWrite.json", customConverters = CUSTOM_CONVERTERS)
    void testFlushBufferWrite(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("flushBuffer");

        // Test setup
        TestInformation.TestInformationBuilder builder = initTest(method, parameters);

        // Test execution
        method.invoke(stream);

        // Test evaluation
        MyByte bufferActualState = buffer.get(stream);
        int positionActualState = position.get(stream);

        assert underlying != null;
        Context context = builder.actualState(
            TestInformation.builder()
                .add("underlying", underlying.getBits().toString())
                .add("buffer", bufferActualState)
                .add("position", positionActualState)
                .build()
        ).build();

        List<Integer> bitsPostState = parameters.get("bitsPostState");
        Assertions2.assertEquals(bitsPostState, underlying.getBits(),
            context, comment -> "Buffer was written correctly");
    }

    /**
     * Initializes the test for the writeBit(MyBit) method.
     *
     * @param parameters the parameters for the test
     */
    private TestInformation.TestInformationBuilder initWriteBitTest(JsonParameterSet parameters) {
        // Access method to test
        return initTest(
            getMethod("writeBit", MyBit.class),
            builder -> builder.add("bit", parameters.get("bit")),
            Function.identity(),
            parameters
        );
    }

    /**
     * Asserts that the buffer is written correctly after calling the writeBit method.
     *
     * @param parameters the parameters for the test
     */
    private void assertWriteBitFlush(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("writeBit", MyBit.class);

        // Test setup
        TestInformation.TestInformationBuilder builder = initWriteBitTest(parameters);
        MyBit bit = parameters.get("bit");

        // Test execution
        method.invoke(stream, bit);

        // Test evaluation
        assert underlying != null;
        Context context = builder.actualState(
            TestInformation.builder()
                .add("underlying", underlying.getBits().toString())
                .add("buffer", buffer.get(stream))
                .add("position", position.get(stream))
                .build()
        ).build();
        List<Integer> bitsPostState = parameters.get("bitsPostState");

        // Validate output
        Assertions2.assertEquals(bitsPostState, underlying.getBits(), context,
            comment -> "Buffer was not written correctly.");
    }

    @DisplayName("Die Methode writeBit(Bit bit) schreibt das Zeichen in den internen OutputStream, falls der Puffer voll ist.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testWriteBitFlushYes.json", customConverters = CUSTOM_CONVERTERS)
    void testWriteBitFlushYes(JsonParameterSet parameters) throws Throwable {
        assertWriteBitFlush(parameters);
    }

    @DisplayName("Die Methode writeBit(Bit bit) schreibt das Zeichen in den internen OutputStream, falls der Puffer voll ist.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testWriteBitFlushNo.json", customConverters = CUSTOM_CONVERTERS)
    void testWriteBitFlushNo(JsonParameterSet parameters) throws Throwable {
        assertWriteBitFlush(parameters);
    }

    @DisplayName("Die Methode writeBit(Bit bit) schreibt ein Bit korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testWriteBit.json", customConverters = CUSTOM_CONVERTERS)
    void testWriteBit(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("writeBit", MyBit.class);

        // Test setup
        TestInformation.TestInformationBuilder builder = initWriteBitTest(parameters);
        MyBit bit = parameters.get("bit");

        // Test execution
        method.invoke(stream, bit);

        // Test evaluation
        MyByte bufferActualState = buffer.get(stream);
        int positionActualState = position.get(stream);
        assert underlying != null;
        Context context = builder.actualState(
            TestInformation.builder()
                .add("underlying", bufferActualState)
                .add("buffer", buffer.get(stream))
                .add("position", positionActualState)
                .build()
        ).build();

        Assertions2.assertEquals(parameters.get("positionPostState"), positionActualState,
            context, comment -> "Position was not updated correctly.");
        Assertions2.assertEquals(parameters.get("bufferPostState"), bufferActualState,
            context, comment -> "Buffer was not updated correctly.");
    }

    /**
     * Initializes the test for the write(int) method.
     *
     * @param parameters the parameters for the test
     */
    private TestInformation.TestInformationBuilder initWriteTest(JsonParameterSet parameters) {
        // Access method to test
        return initTest(
            getMethod("writeBit", MyBit.class),
            builder -> builder.add("byte", parameters.get("byte")),
            Function.identity(),
            parameters
        );
    }

    @DisplayName("Die Methode write(int b) schreibt ein Byte korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testWrite.json", customConverters = CUSTOM_CONVERTERS)
    void testWrite(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("write", int.class);

        // Test setup
        TestInformation.TestInformationBuilder builder = initWriteTest(parameters);
        int byteValue = parameters.getInt("byte");

        // Test execution
        method.invoke(stream, byteValue);

        // Test evaluation
        MyByte bufferActualState = buffer.get(stream);
        int positionActualState = position.get(stream);
        assert underlying != null;
        Context context = builder.actualState(
            TestInformation.builder()
                .add("underlying", bufferActualState)
                .add("buffer", buffer.get(stream))
                .add("position", positionActualState)
                .build()
        ).build();

        int positionPostState = parameters.getInt("positionPostState");
        MyByte bufferPostState = parameters.get("bufferPostState");
        List<Integer> bitsPostState = parameters.get("bitsPostState");

        Assertions2.assertEquals(positionPostState, positionActualState,
            context, comment -> "Position was not updated correctly.");
        Assertions2.assertEquals(bufferPostState, bufferActualState,
            context, comment -> "Buffer was not updated correctly.");
        Assertions2.assertEquals(bitsPostState, underlying.getBits(),
            context, comment -> "Buffer was not written correctly.");
    }

    @DisplayName("Die Methode write(int b) wirft eine IllegalArgumentException, falls die Eingabe kein Byte ist.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_2_testWriteIllegalArgumentException.json", customConverters = CUSTOM_CONVERTERS)
    void testWriteIllegalArgumentException(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("write", int.class);

        // Test setup
        TestInformation.TestInformationBuilder builder = initWriteTest(parameters);
        int byteValue = parameters.getInt("byte");

        // Test execution and evaluation
        MyByte bufferActualState = buffer.get(stream);
        int positionActualState = position.get(stream);
        assert underlying != null;
        Context context = builder.actualState(
            TestInformation.builder()
                .add("underlying", bufferActualState)
                .add("buffer", buffer.get(stream))
                .add("position", positionActualState)
                .build()
        ).build();

        Assertions2.assertThrows(IllegalArgumentException.class, () -> method.invoke(stream, byteValue),
            context, comment -> "IllegalArgumentException was not thrown.");
    }
}
