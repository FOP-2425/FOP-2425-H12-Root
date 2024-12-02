package h12.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.MatcherFactories;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Arrays;

/**
 * Utility class for links to classes and methods.
 *
 * @author Nhan Huynh
 */
public final class Links {

    /**
     * Matcher factory for string matchers.
     */
    private static final MatcherFactories.StringMatcherFactory STRING_MATCHER_FACTORY = BasicStringMatchers::identical;

    /**
     * Prevent instantiation of this utility class.
     */
    private Links() {

    }

    /**
     * Returns the type link for the given class.
     *
     * @param clazz the class to get the type from
     *
     * @return the type link for the given class
     */
    public static TypeLink getType(Class<?> clazz) {
        return Assertions3.assertTypeExists(
            BasicPackageLink.of(clazz.getPackageName()),
            STRING_MATCHER_FACTORY.matcher(clazz.getSimpleName())
        );
    }

    /**
     * Returns the field link for the given type and field name.
     *
     * @param type       the type to get the field from
     * @param methodName the name of the field
     * @param matchers   the matchers for additional checks to retrieve the field
     *
     * @return the field link for the given type and field name
     */
    @SafeVarargs
    public static FieldLink getField(TypeLink type, String methodName, Matcher<FieldLink>... matchers) {
        return Assertions3.assertFieldExists(
            type,
            Arrays.stream(matchers).reduce(STRING_MATCHER_FACTORY.matcher(methodName), Matcher::and)
        );
    }

    /**
     * Returns the method link for the given type and method name.
     *
     * @param type       the type to get the method from
     * @param methodName the name of the method
     * @param matchers   the matchers for additional checks to retrieve the method
     *
     * @return the method link for the given type and method name
     */
    @SafeVarargs
    public static MethodLink getMethod(TypeLink type, String methodName, Matcher<MethodLink>... matchers) {
        return Assertions3.assertMethodExists(
            type,
            Arrays.stream(matchers).reduce(STRING_MATCHER_FACTORY.matcher(methodName), Matcher::and)
        );
    }
}
