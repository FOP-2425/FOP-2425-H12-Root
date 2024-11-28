package h12.rubric;

import h12.assertions.Links;
import h12.assertions.TestConstants;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;
import java.util.List;

/**
 * Defines a test skeleton for the H10 assignment.
 *
 * <p>Use the following schema:
 * <pre>{@code
 *     public class TestClass extends H12_Test {
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
     * The type of the class under test.
     */
    private @Nullable TypeLink type;

    /**
     * Configuration for all tests.
     */
    @BeforeAll
    void globalSetup() {
        Assertions.assertNotNull(
            getClass().getAnnotation(TestForSubmission.class),
            "The test class is not annotated with @TestForSubmission."
        );

        this.type = Links.getType(getClassType());
    }

    /**
     * Returns the class type of the class under test.
     *
     * @return the class type of the class under test
     */
    public abstract Class<?> getClassType();

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
            throw new IllegalStateException("Type not initialized");
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
    public Context.Builder<?> contextBuilder(MethodLink methodLink) {
        return Assertions2.contextBuilder().subject(methodLink.reflection());
    }
}
