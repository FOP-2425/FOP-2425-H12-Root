package h12.rubric;

import org.sourcegrade.jagr.api.rubric.Criterion;

/**
 * An object that implements this interface indicates that it can be converted to a {@link Criterion} which means
 * it is gradable.
 *
 * @author Nhan Huynh
 */
public interface Criteriable {

    /**
     * Returns the criterion that represents this object.
     *
     * @return the criterion that represents this object
     */
    Criterion getCriterion();
}
