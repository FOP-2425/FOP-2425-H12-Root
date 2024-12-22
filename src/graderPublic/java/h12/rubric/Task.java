package h12.rubric;

import org.jetbrains.annotations.NotNull;
import org.sourcegrade.jagr.api.rubric.Criterion;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a task in a rubric.
 *
 * @param description a short description of the task
 * @param subtasks    the subtasks that must be met to complete the task
 *
 * @author Nhan Huynh
 */
public record Task(String description, List<Subtask> subtasks) implements Criteriable {

    /**
     * Returns a new {@link TaskCriteriaBuilder} to build a {@link Task}.
     *
     * @return a new task subtasks builder
     */
    public static TaskCriteriaBuilder builder() {
        return new TaskCriteriaBuilderImpl();
    }

    @Override
    public Criterion getCriterion() {
        return Criterion.builder()
            .shortDescription(description)
            .addChildCriteria(subtasks.stream().map(Subtask::getCriterion).toArray(Criterion[]::new))
            .build();
    }

    /**
     *
     */
    public interface TaskCriteriaBuilder extends Builder<Task> {

        /**
         * Sets the description of the task.
         *
         * @param description the description of the task
         *
         * @return this builder instance with the description set
         */
        TaskCriteriaBuilder description(String description);

        /**
         * Adds subtasks to the task.
         *
         * @param subtasks the subtasks to add
         *
         * @return this builder instance with the subtasks added
         */
        TaskCriteriaBuilder subtasks(Subtask... subtasks);

        /**
         * Adds a criterion to the task.
         *
         * @param criterion the criterion to add
         *
         * @return this builder instance with the criterion added
         */
        default TaskCriteriaBuilder subtask(Subtask criterion) {
            return subtasks(criterion);
        }
    }

    /**
     * A builder for building {@link Task} instances.
     */
    private static class TaskCriteriaBuilderImpl implements TaskCriteriaBuilder {

        /**
         * The description of the task.
         */
        private @NotNull String description = "";

        /**
         * The subtasks of the task.
         */
        private final List<Subtask> criteria = new ArrayList<>();

        @Override
        public TaskCriteriaBuilder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        @Override
        public TaskCriteriaBuilder subtasks(Subtask... subtasks) {
            this.criteria.addAll(List.of(subtasks));
            return this;
        }

        @Override
        public Task build() {
            if (description.isBlank()) {
                throw new IllegalArgumentException("Description cannot be blank!");
            }
            return new Task(description, criteria);
        }
    }
}
