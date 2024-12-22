package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.assertions.TestConstants;
import h12.io.compress.huffman.HuffmanCoding;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Defines the public tests for H12.3.2.
 *
 * @author Nhan Huynh
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
        "elementsPreState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "elementsPostState", node -> JsonConverters.toList(node, JsonNode::asInt),
        "minimumElement", JsonNode::asInt
    );

    @Override
    public Class<?> getTestClass() {
        return HuffmanCoding.class;
    }

    @DisplayName("Die Methode removeMin(Collection<? extends T> elements, Comparator<? super T> cmp) entfernt das Minimum und gibt diesen korrekt zurück.")
    @ParameterizedTest
    @JsonParameterSetTest(value = "H12_3_2_Tests_testRemoveMin.json", customConverters = CUSTOM_CONVERTERS)
    public void testRemoveMin(JsonParameterSet parameters) throws Throwable {
        // Access method to test
        MethodLink method = getMethod("removeMin", Collection.class, Comparator.class);

        // Test setup
        List<Integer> elementsPreState = new ArrayList<>(parameters.get("elementsPreState"));
        List<Integer> elementsPostState = new ArrayList<>(parameters.get("elementsPostState"));

        TestInformation.TestInformationBuilder builder = testInformation(method)
            .preState(
                TestInformation.builder()
                    .add("elements", elementsPreState)
                    .add("cmp", COMPARATOR)
                    .build()
            )
            .postState(
                TestInformation.builder()
                    .add("elements", elementsPostState)
                    .build()
            );


        // Test execution
        HuffmanCoding coding = new HuffmanCoding();
        List<Integer> elementsActualState = new ArrayList<>(elementsPreState);
        Integer removed = method.invoke(coding, elementsActualState, COMPARATOR);

        // Test evaluation
        Context context = builder.actualState(
            TestInformation.builder()
                .add("elements", elementsPreState)
                .build()
        ).build();

        int minimumElement = parameters.getInt("minimumElement");
        Assertions2.assertEquals(minimumElement, removed, context, comment -> "Removed element is incorrect.");
        Assertions2.assertEquals(elementsPostState, elementsActualState, context,
            comment -> "Remaining elements are incorrect.");
    }

    @DisplayName("Die Methode build(Map<Character, Integer> frequency, BiFunction<Character, Integer, T> f, BiFunction<T, T, T> g, Comparator<? super T> cmp) erstellt die Elemente mit der Funktion f korrekt.")
    @Test
    void testBuildFunctionF() throws Throwable {
        // Access method to test
        MethodLink method = getMethod("build", Map.class, BiFunction.class, BiFunction.class, Comparator.class);

        // Test setup
        Map<Character, Integer> frequency = Map.of('a', 1, 'b', 2, 'c', 3);
        List<Character> visited = new ArrayList<>();
        BiFunction<Character, Integer, Character> f = (c, i) -> {
            visited.add(c);
            return c;
        };
        BiFunction<Character, Character, Character> g = (a, b) -> a;

        Context context = testInformation(method).preState(
            TestInformation.builder()
                .add("frequency", frequency)
                .add("f", "f: (Character, Integer) -> Mark character as visited")
                .add("g", "g: (Character, Character) -> Character")
                .add("cmp", COMPARATOR)
                .build()
        ).build();

        // Test execution
        HuffmanCoding coding = new HuffmanCoding();
        Exception ex = null;
        try {
            method.invoke(coding, frequency, f, g, COMPARATOR);
        } catch (Exception e) {
            ex = e;
        }

        // Test evaluation
        try {
            Assertions2.assertEquals(frequency.keySet().stream().toList(), visited, context,
                comment -> "Function f was not called with the correct elements.");
        } catch (AssertionFailedError e) {
            if (ex != null) {
                throw ex;
            } else {
                throw e;
            }
        }
    }

    @DisplayName("Die Methode build(Map<Character, Integer> frequency, BiFunction<Character, Integer, T> f, BiFunction<T, T, T> g, Comparator<? super T> cmp) wendet die Funktion g mit den beiden Minimumelementen korrekt an.")
    @Test
    void testBuildFunctionG() throws Throwable {
        // Access the method to test
        MethodLink method = getMethod("build", Map.class, BiFunction.class, BiFunction.class, Comparator.class);

        // Test setup
        Map<Character, Integer> frequency = Map.of('a', 1, 'b', 2, 'c', 3);
        List<Map.Entry<Character, Character>> visited = new ArrayList<>();
        BiFunction<Character, Integer, Character> f = (c, i) -> c;
        BiFunction<Character, Character, Character> g = (a, b) -> {
            visited.add(Map.entry(a, b));
            return a;
        };
        List<Map.Entry<Character, Character>> expected = List.of(Map.entry('a', 'b'), Map.entry('a', 'c'));

        Context context = testInformation(method).preState(
            TestInformation.builder()
                .add("frequency", frequency)
                .add("f", "f: (Character, Integer) -> Mark pair (Character, Character) as visited")
                .add("g", "g: (Character, Character) -> (Character, Character)")
                .add("cmp", COMPARATOR)
                .build()
        ).build();


        // Test execution
        HuffmanCoding coding = new HuffmanCoding();
        Exception ex = null;
        try {
            method.invoke(coding, frequency, f, g, Comparator.naturalOrder());
        } catch (Exception e) {
            ex = e;
        }

        // Test evaluation
        try {
            Assertions2.assertEquals(expected, visited, context,
                comment -> "Function g was not called with the correct elements.");
        } catch (AssertionFailedError e) {
            if (ex != null) {
                throw ex;
            } else {
                throw e;
            }
        }
    }
}
