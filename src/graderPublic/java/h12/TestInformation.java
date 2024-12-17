package h12;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.Property;
import org.tudalgo.algoutils.tutor.general.assertions.basic.BasicProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Context information for a test. THis class extends the {@link Context} interface and provide optional pre-, post- and
 * actual states of the test.
 *
 * @author Nhan Huynh
 */
public class TestInformation implements Context {

    /**
     * The subject of the test.
     */
    private final @Nullable Object subject;

    /**
     * The properties of the test.
     */
    private final Collection<Property> properties;

    /**
     * Constructs new test information with the specified subject and properties.
     *
     * @param subject     the subject of the test
     * @param preState    the pre-state of the test
     * @param postState   the post-state of the test
     * @param actualState the actual state of the test
     * @param properties  the properties of the test
     */
    public TestInformation(
        @Nullable Object subject,
        @Nullable TestInformation preState,
        @Nullable TestInformation postState,
        @Nullable TestInformation actualState,
        Collection<Property> properties) {


        Context.Builder<?> builder = Assertions2.contextBuilder()
            .subject(subject);

        if (preState != null) {
            addState(builder, "Pre state", preState);
        }
        if (postState != null) {
            addState(builder, "Post state", postState);
        }
        if (actualState != null) {
            addState(builder, "Actual state", actualState);
        }
        builder.add(properties.toArray(Property[]::new));
        Context context = builder.build();
        this.subject = context.subject();
        this.properties = context.properties();
    }

    /**
     * Adds the state to the builder.
     *
     * @param builder the builder to add the sotate to
     * @param name    the name of the state
     * @param state   the state to add
     */
    private void addState(Builder<?> builder, String name, TestInformation state) {
        if (state != null) {
            builder.add(
                name,
                Assertions2.contextBuilder()
                    .add(state.properties.toArray(Property[]::new))
                    .build()
            );
        }
    }

    /**
     * Returns a new test information builder.
     *
     * @return a new test information builder
     */
    public static TestInformationBuilder builder() {
        return new TestInformationBuilderImpl();
    }

    @Override
    public Object subject() {
        return subject;
    }

    @Override
    public Collection<Property> properties() {
        return properties;
    }

    /**
     * Builder for {@link TestInformation}.
     */
    public interface TestInformationBuilder extends h12.rubric.Builder<TestInformation> {

        /**
         * Sets the subject of the test.
         *
         * @param subject the subject of the test
         *
         * @return this builder after setting the subject
         */
        TestInformationBuilder subject(Object subject);

        /**
         * Sets the pre-state of the test.
         *
         * @param preState the pre-state of the test
         *
         * @return this builder after setting the pre-state
         */
        TestInformationBuilder preState(TestInformation preState);

        /**
         * Sets the post-state of the test.
         *
         * @param postState the post-state of the test
         *
         * @return this builder after setting the post-state
         */
        TestInformationBuilder postState(TestInformation postState);

        /**
         * Sets the actual state of the test.
         *
         * @param actualState the actual state of the test
         *
         * @return this builder after setting the actual state
         */
        TestInformationBuilder actualState(TestInformation actualState);

        /**
         * Adds a property to the test which provides context information.
         *
         * @param key   the key of the property
         * @param value the value of the property
         *
         * @return this builder after adding the property
         */
        TestInformationBuilder add(String key, Object value);
    }

    /**
     * Implementation of the {@link TestInformationBuilder} interface.
     */
    private static class TestInformationBuilderImpl implements TestInformationBuilder {

        /**
         * The subject of the test.
         */
        private @Nullable Object subject;

        /**
         * The pre-state of the test.
         */
        private @Nullable TestInformation preState = null;

        /**
         * The post-state of the test.
         */
        private @Nullable TestInformation postState = null;

        /**
         * The actual state of the test.
         */
        private @Nullable TestInformation actualState = null;

        /**
         * The properties of the test.
         */
        private final List<Property> properties = new ArrayList<>();

        @Override
        public TestInformationBuilder subject(Object subject) {
            this.subject = subject;
            return this;
        }

        @Override
        public TestInformationBuilder preState(TestInformation preState) {
            this.preState = preState;
            return this;
        }

        @Override
        public TestInformationBuilder postState(TestInformation postState) {
            this.postState = postState;
            return this;
        }

        @Override
        public TestInformationBuilder actualState(TestInformation actualState) {
            this.actualState = actualState;
            return this;
        }

        @Override
        public TestInformationBuilder add(String key, Object value) {
            ListIterator<Property> iterator = properties.listIterator();
            while (iterator.hasNext()) {
                Property property = iterator.next();
                if (property.key().equals(key)) {
                    iterator.set(new BasicProperty(key, value));
                    return this;
                }
            }
            properties.add(new BasicProperty(key, value));
            return this;
        }

        @Override
        public TestInformation build() {
            return new TestInformation(
                subject,
                preState,
                postState,
                actualState,
                properties);
        }
    }
}
