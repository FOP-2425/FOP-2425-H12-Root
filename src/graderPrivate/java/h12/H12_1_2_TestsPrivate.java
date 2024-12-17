package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.BufferedBitOutputStream;
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
                        .add("underlying", underlying.getBits())
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
                .add("underlying", underlying.getBits())
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
                .add("underlying", underlying.getBits())
                .add("buffer", bufferActualState)
                .add("position", positionActualState)
                .build()
        ).build();

        List<Integer> bitsPostState = parameters.get("bitsPostState");
        Assertions2.assertEquals(bitsPostState, underlying.getBits(),
            context, comment -> "Buffer was written correctly");
    }
}
