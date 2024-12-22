package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.Links;
import h12.assertions.TestConstants;
import h12.io.BufferedBitInputStream;
import h12.lang.MyByte;
import h12.mock.MockBitInputStream;
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
import java.util.Objects;
import java.util.function.Function;

/**
 * Defines the public tests for H12.1.1.
 *
 * @author Nhan Huynh
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
        "bitsPreState", JsonConverters::toBitInputStream,
        "bufferPreState", JsonConverters::toMyByte,
        "positionPreState", JsonNode::asInt,
        "bitsPostState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "bufferPostState", JsonConverters::toMyByte,
        "positionPostState", JsonNode::asInt,
        "expectedBit", JsonNode::asInt
    );

    @Override
    public Class<?> getTestClass() {
        return BufferedBitInputStream.class;
    }

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
    private @Nullable MockBitInputStream underlying;

    /**
     * The input stream to test.
     */
    private @Nullable BufferedBitInputStream stream;

    @BeforeAll
    protected void globalSetup() {
        super.globalSetup();
        buffer = Links.getField(getType(), "buffer");
        position = Links.getField(getType(), "position");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (underlying != null) {
            underlying.close();
        }
        if (stream != null) {
            stream.close();
        }
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
        // Test setup
        underlying = Objects.requireNonNull(parameters.get("bitsPreState"));
        stream = new BufferedBitInputStream(underlying);

        MyByte bufferPreState = parameters.get("bufferPreState");
        buffer.set(stream, bufferPreState);
        int positionPreState = parameters.get("positionPreState");
        position.set(stream, positionPreState);

        List<Integer> bitsPostState = parameters.get("bitsPostState");
        MyByte bufferPostState = parameters.get("bufferPostState");
        int positionPostState = parameters.get("positionPostState");

        return testInformation(method)
            .preState(
                TestInformation.builder()
                    .add("underlying", underlying.getBits())
                    .add("buffer", bufferPreState)
                    .add("position", positionPreState)
                    .build()
            )
            .postState(
                TestInformation.builder()
                    .add("underlying", bitsPostState)
                    .add("buffer", bufferPostState)
                    .add("position", positionPostState)
                    .build()
            );
    }

    /**
     * Asserts the fetch() method.
     *
     * @param parameters the parameters for the test
     *
     * @throws Throwable if an error occurs
     */
    void assertFetch(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("fetch");

        // Test setup
        TestInformation.TestInformationBuilder builder = initTest(method, parameters);

        // Test execution
        method.invoke(stream);

        // Test evaluation
        MyByte bufferPostState = parameters.get("bufferPostState");
        int positionPostState = parameters.get("positionPostState");
        MyByte bufferActualState = buffer.get(stream);
        int positionActualState = position.get(stream);

        Context context = builder.actualState(
            TestInformation.builder()
                .add("buffer", bufferActualState)
                .add("position", positionActualState)
                .build()
        ).build();

        Assertions2.assertEquals(bufferPostState, bufferActualState, context, comment -> "Buffer is not updated correctly.");
        Assertions2.assertEquals(positionPostState, positionActualState, context,
            comment -> "Position is not updated correctly.");
    }

    @DisplayName("Die Methode fetch() aktualisiert im Falle von nicht EOF den Puffer und die Position korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testFetchNotEOF.json", customConverters = CUSTOM_CONVERTERS)
    void testFetchNotEOF(JsonParameterSet parameters) throws Throwable {
        assertFetch(parameters);
    }

    @DisplayName("Die Methode fetch() aktualisiert im Falle von EOF den Puffer und die Position korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testFetchEOF.json", customConverters = CUSTOM_CONVERTERS)
    void testFetchEOF(JsonParameterSet parameters) throws Throwable {
        assertFetch(parameters);
    }

    /**
     * Asserts the readBit() method.
     *
     * @param parameters the parameters for the test
     *
     * @throws Throwable if an error occurso
     */
    private void assertReadBit(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("readBit");

        // Test setup
        TestInformation.TestInformationBuilder builder = initTest(method, parameters);

        // Test execution
        int actualBit = method.invoke(stream);

        // Test evaluation
        int expectedBit = parameters.get("expectedBit");
        MyByte bufferActualState = buffer.get(stream);
        int positionActualState = position.get(stream);

        Context context = builder.actualState(
            TestInformation.builder()
                .add("buffer", bufferActualState)
                .add("position", positionActualState)
                .build()
        ).build();

        Assertions2.assertEquals(expectedBit, actualBit, context,
            comment -> "Return value is not correct.");
    }

    @DisplayName("Die Methode readBit() liest das nächste Bit korrekt, falls wir bereits alle Bits des vorherigen Bytes gelesen haben.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadBitNextByte.json", customConverters = CUSTOM_CONVERTERS)
    void testReadBitNextByte(JsonParameterSet parameters) throws Throwable {
        assertReadBit(parameters);
    }

    @DisplayName("Die Methode readBit() gibt im Falle von Lesen des ersten Bit das korrekte Bit zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadBitBufferStart.json", customConverters = CUSTOM_CONVERTERS)
    void testReadBitByteStart(JsonParameterSet parameters) throws Throwable {
        assertReadBit(parameters);
    }

    @DisplayName("Die Methode readBit() gibt im Falle von Lesen eines mittleren Bit das korrekte Bit zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadBitBufferMiddle.json", customConverters = CUSTOM_CONVERTERS)
    void testReadBitByteMiddle(JsonParameterSet parameters) throws Throwable {
        assertReadBit(parameters);
    }

    @DisplayName("Die Methode readBit() gibt im Falle von Lesen des letzen Bit eines Bytes das korrekte Bit zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadBitBufferEnd.json", customConverters = CUSTOM_CONVERTERS)
    void testReadBitByteEnd(JsonParameterSet parameters) throws Throwable {
        assertReadBit(parameters);
    }

    @DisplayName("Die Methode readBit() gibt im Falle von EOF das korrekte Bit zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadBitEOF.json", customConverters = CUSTOM_CONVERTERS)
    void testReadBitEOF(JsonParameterSet parameters) throws Throwable {
        assertReadBit(parameters);
    }

    /**
     * Asserts that the method read() returns the correct result.
     *
     * @param parameters the parameters for the test
     *
     * @throws Throwable if an error occurs
     */
    private void assertRead(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("read");

        // Test setup
        TestInformation.TestInformationBuilder builder = initTest(method, parameters);

        // Test execution
        int actualByte = method.invoke(stream);

        // Test evaluation
        int expectedByte = parameters.get("expectedByte");
        MyByte bufferActualState = buffer.get(stream);
        int positionActualState = position.get(stream);

        Context context = builder.actualState(
            TestInformation.builder()
                .add("buffer", bufferActualState)
                .add("position", positionActualState)
                .build()
        ).build();

        Assertions2.assertEquals(expectedByte, actualByte, context, comment -> "Return value is not correct.");
    }

    @DisplayName("Die Methode read() gibt das korrekte Ergebnis zurück, falls wir am Ende des Streams sind.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadEnd.json", customConverters = CUSTOM_CONVERTERS)
    void testReadEnd(JsonParameterSet parameters) throws Throwable {
        assertRead(parameters);
    }

    @DisplayName("Die Methode read() gibt das korrekte Teilergebnis zurück, falls der Stream keine 8 Bits mehr enthält.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testReadPartial.json", customConverters = CUSTOM_CONVERTERS)
    void testReadPartial(JsonParameterSet parameters) throws Throwable {
        assertRead(parameters);
    }

    @DisplayName("Die Methode read() gibt in allen anderen Fallen das korrekte Ergebnis zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_1_1_testRead.json", customConverters = CUSTOM_CONVERTERS)
    void testRead(JsonParameterSet parameters) throws Throwable {
        assertRead(parameters);
    }
}
