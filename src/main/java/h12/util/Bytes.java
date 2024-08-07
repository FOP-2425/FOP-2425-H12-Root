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
     * Converts the given bytes to an integer.
     *
     * @param bytes the bytes to convert
     * @return the integer value of the bytes
     */
    public static int toInt(byte[] bytes) {
        return buffer.clear().put(bytes).flip().getInt();
    }

    /**
     * Converts the given integer to bytes.
     *
     * @param value the integer to convert
     * @return the bytes of the integer
     */
    public static byte[] toBytes(int value) {
        return buffer.clear().putInt(value).array();
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
                sb.append(Bits.get(b, i));
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
    public static int computeMissingBits(int length) {
        return length % (NUMBER_OF_BITS - 1);
    }
}
