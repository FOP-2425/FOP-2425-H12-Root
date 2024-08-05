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
     * A buffer to convert integers to bytes.
     */
    private static final ByteBuffer buffer = ByteBuffer.allocate(4);

    /**
     * Prevent instantiation of this utility class.
     */
    private Bytes() {
    }

    public static int getBit(int value, int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Position must be non-negative: %d".formatted(position));
        }
        return (value >> position) & 1;
    }

    public static int setBit(int value, int position, int bit) {
        if (position < 0) {
            throw new IllegalArgumentException("Position must be non-negative: %d".formatted(position));
        }
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1: %d".formatted(bit));
        }
        return (byte) (bit == 1 ? value | (1 << position) : value & ~(1 << position));
    }


    public static byte[] toBytes(int value) {
        return buffer.putInt(value).array();
    }

    public static String toBits(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            for (int i = NUMBER_OF_BITS - 1; i >= 0; i--) {
                sb.append(getBit(b, i));
            }
        }
        return sb.toString();
    }

    public static int getFillBits(int length) {
        return length % (NUMBER_OF_BITS - 1);
    }
}
