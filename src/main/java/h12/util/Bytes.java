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
     * The number of bits in a byte.
     */
    public static final int NUMBER_OF_BITS = 8;

    /**
     * A buffer to convert integers to bytes and vice versa.
     */
    private static final ByteBuffer buffer = ByteBuffer.allocate(4);

    /**
     * Prevent instantiation of this utility class.
     */
    private Bytes() {
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
    public static int getBit(int value, int position) {
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
     * Converts the given bytes to an integer.
     *
     * @param bytes the bytes to convert
     * @return the integer value of the bytes
     */
    public static int toInt(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * Converts the given integer to bytes.
     *
     * @param value the integer to convert
     * @return the bytes of the integer
     */
    public static byte[] toBytes(int value) {
        buffer.clear();
        return buffer.putInt(value).array();
    }

    /**
     * Converts the given bytes into a bits representation.
     *
     * @param bytes the bytes to convert
     * @return the bits representation of the bytes
     */
    public static String toBits(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            for (int i = NUMBER_OF_BITS - 1; i >= 0; i--) {
                sb.append(getBit(b, i));
            }
        }
        return sb.toString();
    }

    /**
     * Returns the number of bits needed to fill the last byte.
     *
     * @param length the length of the current bits
     * @return the number of bits needed to fill the last byte
     */
    public static int getFillBits(int length) {
        return length % (NUMBER_OF_BITS - 1);
    }
}
