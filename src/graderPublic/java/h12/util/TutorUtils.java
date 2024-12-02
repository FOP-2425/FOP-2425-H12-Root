package h12.util;

import h12.lang.MyBit;
import h12.lang.MyByte;

import java.util.List;

/**
 * Utility class for tutor tests.
 *
 * @author Nhan Huynh
 */
public final class TutorUtils {

    /**
     * The number of bits in an integer value.
     */
    public static final int INTEGER_BITS_LENGTH = 32;

    /**
     * Prevent instantiation of this utility class.
     */
    private TutorUtils() {
    }

    /**
     * Converts a list of bits into a byte.
     *
     * @param bits the list of bits
     *
     * @return the byte value
     * @throws IllegalArgumentException if the number of bits is not 8
     */
    public static byte toByte(List<Integer> bits) {
        if (bits.size() != MyByte.NUMBER_OF_BITS) {
            throw new IllegalArgumentException("The number of bits must be %d.".formatted(MyByte.NUMBER_OF_BITS));
        }
        MyByte value = new MyByte();
        for (int i = 0; i < MyByte.NUMBER_OF_BITS; i++) {
            value.set(MyByte.NUMBER_OF_BITS - i - 1, MyBit.fromInt(bits.get(i)));
        }
        return (byte) value.getValue();
    }

    /**
     * Converts a list of bits into a byte array.
     *
     * @param bits the list of bits
     *
     * @return the byte array
     * @throws IllegalArgumentException if the number of bits is not a multiple of 8
     */
    public static byte[] toBytes(List<Integer> bits) {
        if (bits.size() % MyByte.NUMBER_OF_BITS != 0) {
            throw new IllegalArgumentException("The number of bits must be a multiple of %d.".formatted(MyByte.NUMBER_OF_BITS));
        }
        byte[] bytes = new byte[bits.size() / MyByte.NUMBER_OF_BITS];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = toByte(bits.subList(i * MyByte.NUMBER_OF_BITS, (i + 1) * MyByte.NUMBER_OF_BITS));
        }
        return bytes;
    }
}
