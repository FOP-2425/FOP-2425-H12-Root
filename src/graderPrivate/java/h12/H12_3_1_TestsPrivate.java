package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compression.huffman.HuffmanCoding;
import h12.rubric.H12_Tests;
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

import java.util.HashSet;
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
        "keys", node -> new HashSet<>(JsonConverters.toList(node, JsonNode::asText)),
        "frequency", node -> JsonConverters.toMap(node, key -> key.charAt(0), JsonNode::asInt)
    );


    @Override
    public Class<?> getClassType() {
        return HuffmanCoding.class;
    }

    @DisplayName("Die Methode buildFrequencyTable(String text) erstellt die Häufigkeitstabelle mit allen Zeichen als Schlüssel korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_3_1_testBuildFrequencyTableKeys.json", customConverters = CUSTOM_CONVERTERS)
    void testBuildFrequencyTableKeys(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink method = getMethod("buildFrequencyTable", String.class);

        // Test data
        String text = parameters.get("text");

        // Context information
        Context context = contextBuilder(method)
            .add("text", text)
            .build();

        // Test the method
        HuffmanCoding coding = new HuffmanCoding();
        Map<Character, Integer> frequency = method.invoke(coding, text);

        // Validate the output
        Set<String> keys = parameters.get("keys");
        Assertions2.assertEquals(keys, frequency.keySet(), context,
            comment -> "The keys of the frequency table are not correct.");
    }

    @DisplayName("Die Methode buildFrequencyTable(String text) erstellt die Häufigkeitstabelle mt den Häufigkeiten korrekt.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_3_1_testResult.json", customConverters = CUSTOM_CONVERTERS)
    void testResult(JsonParameterSet parameters) throws Throwable {
        // Access the method
        MethodLink method = getMethod("buildFrequencyTable", String.class);

        // Test data
        String text = parameters.get("text");

        // Context information
        Context context = contextBuilder(method)
            .add("text", text)
            .build();

        // Test the method
        HuffmanCoding coding = new HuffmanCoding();
        Map<Character, Integer> frequency = method.invoke(coding, text);

        // Validate the output
        Map<Character, Integer> expected = parameters.get("frequency");
        Assertions2.assertEquals(expected, frequency.keySet(), context,
            comment -> "The frequency table is not correct.");
    }
}
