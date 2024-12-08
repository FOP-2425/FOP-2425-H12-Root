package h12;

import h12.assertions.TestConstants;
import h12.io.compression.huffman.HuffmanCoding;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Defines the private tests for H12.3.2.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H12.3.2 | Huffman-Baum")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_3_2_TestsPrivate extends H12_3_2_TestsPublic {

    @DisplayName("Die Methode build(Map<Character, Integer> frequency, BiFunction<Character, Integer, T> f, BiFunction<T, T, T> g, Comparator<? super T> cmp) ist vollständig und korrekt.")
    @Test
    void testResult() throws Throwable {
        // Access test method
        MethodLink method = getMethod("build", Map.class, BiFunction.class,
            BiFunction.class, java.util.Comparator.class);

        // Test data
        Map<Character, Integer> frequency = Map.of('a', 1, 'b', 2, 'c', 3, 'd', 4);
        BiFunction<Character, Integer, Map.Entry<Character, Integer>> f = Map::entry;
        BiFunction<Map.Entry<Character, Integer>, Map.Entry<Character, Integer>, Map.Entry<Character, Integer>> g =
            (e1, e2) -> Map.entry(e1.getKey(), e1.getValue() + e2.getValue());
        Comparator<Map.Entry<Character, Integer>> cmp = Comparator.comparingInt(Map.Entry::getValue);

        // Context information
        Context context = contextBuilder(method)
            .add("frequency", frequency)
            .add("f", "(Character, Integer) -> Map.Entry<Character, Integer>")
            .add("g", "(Map.Entry<Character, Integer>, Map.Entry<Character, Integer>) -> Map.Entry<Character, Integer>")
            .add("cmp", "Integer <= Integer")
            .build();

        // Test method
        HuffmanCoding coding = new HuffmanCoding();
        Map.Entry<Character, Integer> actual = method.invoke(coding, frequency, f, g, cmp);
        Map.Entry<Character, Integer> expected = Map.entry('d', 10);

        Assertions2.assertEquals(expected, actual, context, comment -> "Wrong result");
    }
}
