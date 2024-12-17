package h12.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.assertions.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfFail;

/**
 * Utility class for tutor assertions.
 *
 * @author Nhan Huynh
 */
public final class TutorAssertions {

    /**
     * Prevent instantiation of this utility class.
     */
    private TutorAssertions() {

    }

    /**
     * Copies the byte array to the object array.
     *
     * @param src  the source byte array
     * @param dest the destination object array
     */
    private static void copyArray(byte[] src, Object[] dest) {
        int offset = -Byte.MIN_VALUE;
        for (int i = 0; i < src.length; i++) {
            dest[i] = (int) src[i] + offset;
        }
    }

    /**
     * Asserts that the two byte arrays are equal.
     *
     * @param expected           the expected byte array
     * @param actual             the actual byte array
     * @param context            the context
     * @param preCommentSupplier the pre-comment supplier
     */
    public static void assertArrayEquals(
        byte[] expected,
        byte[] actual,
        Context context,
        PreCommentSupplier<? super ResultOfFail> preCommentSupplier
    ) {
        Object[] expectedObjects = new Object[expected.length];
        copyArray(expected, expectedObjects);
        Object[] actualObjects = new Object[actual.length];
        copyArray(actual, actualObjects);
        Assertions2.assertArrayEquals(expectedObjects, actualObjects, context, preCommentSupplier);
    }
}
