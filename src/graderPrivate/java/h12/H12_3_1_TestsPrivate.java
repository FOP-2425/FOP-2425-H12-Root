package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compress.huffman.HuffmanCoding;
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

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Defines the private tests for H12.3.1.
 *
 * @author Nhan Huynh
 */

@TestForSubmission
@DisplayName("H12.3.1 | Häufigkeitstabelle")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_3_1_TestsPrivate extends H12_Tests {

    /**
     * The custom converters for the JSON parameter set test annotation.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "text", JsonNode::asText,
        "frequency", node -> JsonConverters.toMap(node, key -> key.charAt(0), JsonNode::asInt)
    );


    @Override
    public Class<?> getTestClass() {
        return HuffmanCoding.class;
    }

    private TestInformation.TestInformationBuilder initTest(JsonParameterSet parameters) {
        // Access method to test
        MethodLink method = getMethod("buildFrequencyTable", String.class);

        // Test setup
        String text = parameters.get("text");

        return testInformation(method).preState(
            TestInformation.builder()
                .add("text", text)
                .build()
        );
    }

    @DisplayName("Die Methode buildFrequencyTable(String text) erstellt die Häufigkeitstabelle mit allen Zeichen als Schlüssel korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_3_1_testBuildFrequencyTable.json", customConverters = CUSTOM_CONVERTERS)
    void testBuildFrequencyTableKeys(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("buildFrequencyTable", String.class);

        // Test setup
        String text = parameters.get("text");

        TestInformation.TestInformationBuilder builder = testInformation(method).preState(
            TestInformation.builder()
                .add("text", text)
                .build()
        );

        // Test execution
        HuffmanCoding coding = new HuffmanCoding();
        Map<Character, Integer> actualFrequency = method.invoke(coding, text);

        // Test evaluation
        Map<Character, Integer> frequency = parameters.get("frequency");
        Set<Character> keys = frequency.keySet();

        Context context = builder.build();
        Assertions2.assertEquals(keys, actualFrequency.keySet(), context,
            comment -> "The keys of the built frequency table are not correct.");
    }

    @DisplayName("Die Methode buildFrequencyTable(String text) erstellt die Häufigkeitstabelle mt den Häufigkeiten korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_3_1_testBuildFrequencyTable.json", customConverters = CUSTOM_CONVERTERS)
    void testResult(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("buildFrequencyTable", String.class);

        // Test setup
        String text = parameters.get("text");

        TestInformation.TestInformationBuilder builder = testInformation(method).preState(
            TestInformation.builder()
                .add("text", text)
                .build()
        );

        // Test execution
        HuffmanCoding coding = new HuffmanCoding();
        Map<Character, Integer> actualFrequency = method.invoke(coding, text);

        // Test evaluation
        Map<Character, Integer> frequency = parameters.get("frequency");
        Context context = builder.build();
        Assertions2.assertEquals(frequency, actualFrequency, context,
            comment -> "The built frequency table is not correct.");
    }
}
