package h12.rubric;

import org.jetbrains.annotations.NotNull;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Gradable;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.tudalgo.algoutils.tutor.general.jagr.RubricUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents a subtask in a rubric.
 *
 * @param description  a short description of the subtask
 * @param criteria     the subtasks that must be met to complete the subtask
 * @param requirements the requirements that must be met to complete the subtask
 *
 * @author Nhan Huynh
 */
public record Subtask(
    String description,
    List<Criterion> criteria,
    List<Criterion> requirements
) implements Criteriable {

    /**
     * Returns a new {@link SubtaskBuilder} to build a {@link Subtask}.
     *
     * @return a new subtask builder
     */
    public static SubtaskBuilder builder() {
        return new SubtaskCriteriaBuilderImpl();
    }

    @Override
    public Criterion getCriterion() {
        return Criterion.builder()
            .shortDescription(description)
            .minPoints(0)
            .addChildCriteria(Stream.concat(criteria.stream(), requirements.stream()).toArray(Criterion[]::new))
            .build();
    }

    /**
     * Returns a new {@link SubtaskBuilder} to build a {@link Subtask}.
     */
    public interface SubtaskBuilder extends Builder<Subtask> {

        /**
         * Sets the description of the subtask.
         *
         * @param description the description of the subtask
         *
         * @return this builder instance with the description set
         */
        SubtaskBuilder description(String description);

        /**
         * Sets the name of the test class that tests this subtask.
         *
         * @param testClassName the name of the test class that tests this subtask
         *
         * @return this builder instance with the test class name set
         */
        SubtaskBuilder testClassName(String testClassName);

        /**
         * Adds a criterion to the subtask.
         *
         * @param description          the description of the criterion
         * @param publicTest           whether the test is public
         * @param testMethodsSignature the signature of the test methods
         *
         * @return this builder instance with the criterion added
         */
        SubtaskBuilder criterion(
            String description,
            boolean publicTest,
            Map<String, List<Class<?>>> testMethodsSignature
        );

        /**
         * Adds a public criterion to the subtask.
         *
         * @param description          the description of the criterion
         * @param testMethodsSignature the signature of the test methods
         *
         * @return this builder instance with the criterion added
         */
        default SubtaskBuilder criterion(String description, Map<String, List<Class<?>>> testMethodsSignature) {
            return criterion(description, true, testMethodsSignature);
        }

        /**
         * Adds a criterion to the subtask.
         *
         * @param description          the description of the criterion
         * @param publicTest           whether the test is public
         * @param testMethodName       the name of the test method
         * @param testMethodParameters the parameters of the test method
         *
         * @return this builder instance with the criterion added
         */
        default SubtaskBuilder criterion(
            String description,
            boolean publicTest,
            String testMethodName,
            Class<?>... testMethodParameters
        ) {
            return criterion(description, publicTest, Map.of(testMethodName, List.of(testMethodParameters)));
        }

        /**
         * Adds a public criterion to the subtask.
         *
         * @param description         the description of the criterion
         * @param testMethodName      the name of the test method
         * @param testMethodSignature the signature of the test method
         *
         * @return this builder instance with the criterion added
         */
        default SubtaskBuilder criterion(String description, String testMethodName, Class<?>... testMethodSignature) {
            return criterion(description, true, testMethodName, testMethodSignature);
        }

        /**
         * Adds a requirement to the subtask.
         *
         * @param description          the description of the requirement
         * @param testMethodsSignature the signature of the test methods
         *
         * @return this builder instance with the requirement added
         */
        SubtaskBuilder requirement(String description, Map<String, List<Class<?>>> testMethodsSignature);

        /**
         * Adds a requirement to the subtask.
         *
         * @param description          the description of the requirement
         * @param testMethodName       the name of the test method
         * @param testMethodParameters the parameters of the test method
         *
         * @return this builder instance with the requirement added
         */
        default SubtaskBuilder requirement(String description, String testMethodName, Class<?>... testMethodParameters) {
            return requirement(description, Map.of(testMethodName, List.of(testMethodParameters)));
        }
    }

    /**
     * A builder for building {@link Subtask} instances.
     */
    private static class SubtaskCriteriaBuilderImpl implements SubtaskBuilder {

        /**
         * The description of the subtask.
         */
        private @NotNull String description = "";

        /**
         * The name of the test class that tests this subtask.
         */
        private @NotNull String testClassName = "";

        /**
         * The subtasks of the subtask.
         */
        private final List<Supplier<Criterion>> criteria = new ArrayList<>();

        /**
         * The requirements of the subtask.
         */
        private final List<Supplier<Criterion>> requirements = new ArrayList<>();

        @Override
        public SubtaskBuilder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        @Override
        public SubtaskBuilder testClassName(String testClassName) {
            this.testClassName = testClassName;
            return this;
        }

        /**
         * Returns the description of the subtask.
         *
         * @return the description of the subtask
         */
        private @NotNull String getDescription() {
            return description;
        }

        /**
         * Returns a pre-configured criterion builder where the description and grader are set.
         *
         * @param description the description of the criterion
         * @param grader      the grader of the criterion
         *
         * @return a pre-configured criterion builder with the description and grader set
         */
        private Criterion.Builder criterionBuilder(String description, Grader grader) {
            return Criterion.builder()
                .shortDescription(description)
                .grader(grader);
        }

        /**
         * Returns a pre-configured criterion builder where the description and test reference are set.
         *
         * @param description the description of the criterion
         * @param testRef     the test reference of the criterion
         *
         * @return a pre-configured criterion builder with the description and test reference set
         */
        private Criterion.Builder criterionBuilder(String description, JUnitTestRef testRef) {
            return criterionBuilder(
                description,
                Grader.testAwareBuilder()
                    .requirePass(testRef)
                    .pointsFailedMin()
                    .pointsPassedMax()
                    .build()
            );
        }

        /**
         * Creates a criterion supplier with the given description, class name, test methods signature, and points.
         * Since the points can only be determined after all subtasks are added, the points are calculated lazily.
         *
         * @param description          the description of the criterion
         * @param className            the name of the test class that tests this subtask
         * @param testMethodsSignature the signature of the test methods
         * @param points               the points of the criterion
         *
         * @return a criterion supplier with the given description, class name, test methods signature, and points
         */
        private Supplier<Criterion> criterion(
            String description,
            String className,
            Map<String, List<Class<?>>> testMethodsSignature,
            Supplier<Integer> points
        ) {
            return () -> {
                Criterion.Builder builder;
                try {
                    List<JUnitTestRef> testRefs = new ArrayList<>(testMethodsSignature.size());
                    for (Map.Entry<String, List<Class<?>>> entry : testMethodsSignature.entrySet()) {
                        Method method = Class.forName(className).getDeclaredMethod(
                            entry.getKey(),
                            entry.getValue().toArray(Class[]::new)
                        );
                        testRefs.add(JUnitTestRef.ofMethod(method));
                    }
                    builder = criterionBuilder(description, JUnitTestRef.and(testRefs.toArray(JUnitTestRef[]::new)));
                } catch (Exception e) {
                    builder = criterionBuilder(description, RubricUtils.graderPrivateOnly());
                }
                int pointsValue = points.get();
                if (pointsValue >= 0) {
                    builder.minPoints(0);
                    builder.maxPoints(pointsValue);
                } else {
                    builder.maxPoints(0);
                    builder.minPoints(pointsValue);
                }
                return builder.build();
            };
        }

        @Override
        public SubtaskBuilder criterion(
            String description,
            boolean publicTest,
            Map<String, List<Class<?>>> testMethodsSignature
        ) {
            criteria.add(
                criterion(
                    description,
                    testClassName + (publicTest ? "Public" : "Private"),
                    testMethodsSignature,
                    () -> 1
                )
            );
            return this;
        }

        @Override
        public SubtaskBuilder requirement(String description, Map<String, List<Class<?>>> testMethodsSignature) {
            requirements.add(
                criterion(
                    description,
                    testClassName + "Private",
                    testMethodsSignature,
                    () -> -criteria.stream()
                        .map(Supplier::get)
                        .mapToInt(Gradable::getMaxPoints)
                        .sum()
                )
            );
            return this;
        }

        @Override
        public Subtask build() {
            if (testClassName.isBlank()) {
                throw new IllegalStateException("Test class name cannot be blank!");
            }
            return new Subtask(
                description,
                criteria.stream().map(Supplier::get).toList(),
                requirements.stream().map(Supplier::get).toList()
            );
        }
    }
}
