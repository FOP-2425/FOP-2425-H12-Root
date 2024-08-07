package h12.util;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

/**
 * A utility class which provides operations to work with bits.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
@DoNotTouch
public class Bits {

    /**
     * Prevent instantiation of this utility class.
     */
    private Bits() {
    }

    /**
     * Returns the bit at the given position in the value.
     * <p>
     * For example, the input
     * <pre>{@code
     *     0000_00001
     *     }</pre>
     * which represents the number 1, would return 1 for position 0 and 0 for position 1.
     *
     * @param value    the value to get the bit from
     * @param position the position of the bit to get
     * @return the bit at the given position in the value
     */
    public static int get(int value, int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Position must be non-negative: %d".formatted(position));
        }
        return (value >> position) & 1;
    }

    /**
     * Sets the bit at the given position in the value.
     * <p>
     * For example, the input
     * <pre>{@code
     *     0000_00001, 0, 0
     *     }</pre>
     * would return 0000_00000.
     *
     * @param value    the value to set the bit in
     * @param position the position of the bit to set
     * @param bit      the bit to set
     * @return the value with the bit set at the given position
     */
    public static int set(int value, int position, int bit) {
        if (position < 0) {
            throw new IllegalArgumentException("Position must be non-negative: %d".formatted(position));
        }
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1: %d".formatted(bit));
        }
        return (byte) (bit == 1 ? value | (1 << position) : value & ~(1 << position));
    }
}
