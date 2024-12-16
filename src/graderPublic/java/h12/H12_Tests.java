package h12;

import h12.assertions.Links;
import h12.assertions.TestConstants;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Defines a test skeleton for the H10 assignment.
 *
 * <p>Use the following schema:
 * <pre>{@code
 *     public class TestClass extends H12_Test {
 *
 *          public static final Map<String, Function<JsonNode, ?>> CUSTOM_CONVERTERS = Map.of(
 *              ...
 *          );
 *
 *          @Override
 *          public Class<?> getClassType() {
 *              return ...
 *          }
 *
 *          @ParameterizedTest
 *          @JsonParameterSetTest(value = "path-to-json-data.json", customConverters = CUSTOM_CONVERTERS)
 *          void testXYZ(JsonParameterSet parameters) {
 *              ...
 *          }
 *   }
 * }</pre>
 *
 * @author Nhan Huynh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestForSubmission
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public abstract class H12_Tests {

    /**
     * The attribute name for custom converters in the JSON parameter set test annotation.
     */
    public static final String CUSTOM_CONVERTERS = "CONVERTERS";

    /**
     * The custom comparator for comparing integers.
     */
    public static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {

        private static final Comparator<Integer> delegate = Comparator.naturalOrder();

        @Override
        public int compare(Integer o1, Integer o2) {
            return delegate.compare(o1, o2);
        }

        @Override
        public String toString() {
            return "o1 <= o2";
        }
    };

    /**
     * The type of the class under test.
     */
    private @Nullable TypeLink type;

    /**
     * Configuration for all tests.
     */
    @BeforeAll
    protected void globalSetup() {
        Assertions.assertNotNull(
            getClass().getAnnotation(TestForSubmission.class),
            "The test class is not annotated with @TestForSubmission which is needed for Jagr to work"
        );
        this.type = Links.getType(getTestClass());
    }

    /**
     * Returns the class type of the class under test.
     *
     * @return the class type of the class under test
     */
    public abstract Class<?> getTestClass();

    /**
     * Returns the method under test.
     *
     * @param methodName       the name of the method
     * @param parameterClasses the parameter classes of the method
     *
     * @return the method under test
     */
    public MethodLink getMethod(String methodName, Class<?>... parameterClasses) {
        List<TypeLink> parameterTypes = Arrays.stream(parameterClasses).<TypeLink>map(BasicTypeLink::of).toList();
        return Links.getMethod(
            type,
            methodName,
            Matcher.of(method -> method.typeList().equals(parameterTypes))
        );
    }

    /**
     * Returns the type of the class under test.
     *
     * @return the type of the class under test
     */
    public TypeLink getType() {
        if (type == null) {
            throw new IllegalStateException("Class of the test method not set");
        }
        return type;
    }

    /**
     * Returns a context builder with the method under test as the subject.
     *
     * @param methodLink the method under test
     *
     * @return a context builder with the method under test as the subject
     */
    public TestInformation.TestInformationBuilder testInformation(MethodLink methodLink) {
        return TestInformation.builder()
            .subject(methodLink.reflection());
    }
}
