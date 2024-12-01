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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Defines the public tests for H12.3.2.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@TestForSubmission
@DisplayName("H12.3.2 | Huffman-Baum")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_3_2_TestsPublic extends H12_Tests {

    /**
     * The custom converters for the JSON parameter set test annotation.
     */
    public static final Map<String, Function<JsonNode, ?>> CONVERTERS = Map.of(
        "ints", node -> JsonConverters.toList(node, JsonNode::asInt),
        "minList", node -> JsonConverters.toList(node, JsonNode::asInt),
        "min", JsonNode::asInt
    );

    @Override
    public Class<?> getClassType() {
        return HuffmanCoding.class;
    }

    @DisplayName("Die Methode removeMin(Collection<? extends T> elements, Comparator<? super T> cmp) entfernt das Minimum und gibt diesen korrekt zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_3_2_Tests_testRemoveMin.json", customConverters = CUSTOM_CONVERTERS)
    public void testRemoveMin(JsonParameterSet parameters) throws Throwable {
        // Access the method to test
        MethodLink removeMin = getMethod("removeMin", List.class, Comparator.class);

        // Get the parameters for the test
        List<Integer> ints = new ArrayList<>(parameters.get("ints"));
        List<Integer> expected = new ArrayList<>(parameters.get("minlist"));
        int min = parameters.getInt("min");
        Comparator<Integer> cmp = Comparator.naturalOrder();

        // Context information
        Context context = contextBuilder(removeMin)
            .add("Elements", ints)
            .add("Comparator", "a <= b")
            .add("Expected list", expected)
            .add("Expected removed element", min)
            .build();

        // Start the test
        HuffmanCoding coding = new HuffmanCoding();
        Integer removed = removeMin.invoke(coding, ints, cmp);

        // Validate the output
        Assertions2.assertEquals(min, removed, context, comment -> "Expected the minimum element to be removed.");
        Assertions2.assertEquals(expected, ints, context,
            comment -> "Expected the list to remove the first occurrence of the minimum element.");
    }
}
