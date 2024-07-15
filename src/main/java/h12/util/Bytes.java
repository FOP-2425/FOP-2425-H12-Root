package h12.util;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.nio.ByteBuffer;

/**
 * A utility class which provides operations to work with bytes.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
@DoNotTouch
public class Bytes {

    /**
     * The position of the least significant bit in a byte.
     */
    public static final int MIN_POSITION = 0;

    /**
     * The position of the most significant bit in a byte.
     */
    public static final int MAX_POSITION = 7;

    /**
     * A buffer to convert integers to bytes.
     */
    private static final ByteBuffer buffer = ByteBuffer.allocate(4);

    /**
     * Prevent instantiation of this utility class.
     */
    private Bytes() {
    }

    /**
     * Returns the bit at the given position in the given value.
     *
     * @param value    the value to get the bit from
     * @param position the position of the bit to get
     * @return the bit at the given position in the given value
     * @throws IllegalArgumentException if the position is negative
     */
    public static int getBit(int value, int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Position must be non-negative: %d".formatted(position));
        }
        return (value >> position) & 1;
    }

    /**
     * Sets the bit at the given position in the given value to the given bit.
     *
     * @param value    the value to set the bit in
     * @param position the position of the bit to set
     * @param bit      the bit to set
     * @return the value with the bit at the given position set to the given bit
     * @throws IllegalArgumentException if the position is negative or the bit is not 0 or 1
     */
    public static int setBit(int value, int position, int bit) {
        if (position < 0) {
            throw new IllegalArgumentException("Position must be non-negative: %d".formatted(position));
        }
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1: %d".formatted(bit));
        }
        return (byte) (bit == 1 ? value | (1 << position) : value & ~(1 << position));
    }

    /**
     * Converts an integer to a representation of 4 bytes.
     *
     * @param value the integer to convert
     * @return the byte representation of the integer
     */
    public static byte[] toBytes(int value) {
        return buffer.putInt(value).array();
    }

    /**
     * Converts a byte array to a string of bits.
     *
     * @param bytes the byte array to convert
     * @return the string of bits
     */
    public static String toBits(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            for (int i = MAX_POSITION; i >= MIN_POSITION; i--) {
                sb.append(getBit(b, i));
            }
        }
        return sb.toString();
    }
}
