package h12.lang;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class for converting between bytes and other types.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class MyBytes {

    /**
     * Buffer used for converting between bytes and other types.
     */
    @DoNotTouch
    private static final ByteBuffer buffer = ByteBuffer.allocate(4);

    /**
     * Prevent instantiation of this utility class.
     */
    @DoNotTouch
    private MyBytes() {
    }

    /**
     * Convert an integer to a 4-byte array.
     *
     * @param value the integer to convert
     *
     * @return the 4-byte array representing the integer
     */
    @DoNotTouch
    public static byte[] toBytes(int value) {
        return buffer.clear().putInt(value).array();
    }

    /**
     * Convert a character to a 4-byte array.
     *
     * @param bytes the character to convert
     *
     * @return the 4-byte array representing the character
     */
    @DoNotTouch
    public static int toInt(byte[] bytes) {
        return buffer.clear().put(bytes).flip().getInt();
    }

    /**
     * Convert a 4-byte array to a character.
     *
     * @param bytes the 4-byte array to convert
     *
     * @return the character represented by the 4-byte array
     */
    @DoNotTouch
    public static char toChar(byte[] bytes) {
        return (char) toInt(bytes);
    }

    /**
     * Convert a byte array to a bit sequence array.
     *
     * @param bytes the byte array to convert
     *
     * @return the bit sequence array representing the byte array
     */
    public static MyBit[] toBits(byte[] bytes) {
        MyBit[] bits = new MyBit[bytes.length * MyByte.NUMBER_OF_BITS];
        int offset = -java.lang.Byte.MIN_VALUE;
        int index = 0;
        for (byte b : bytes) {
            MyByte value = new MyByte(b + offset);
            for (int i = MyByte.NUMBER_OF_BITS - 1; i >= 0; i--) {
                bits[index++] = value.get(i);
            }
        }
        return bits;
    }

    /**
     * Returns a string representation of the byte array in binary format.
     *
     * @param bytes the byte array to convert
     *
     * @return the string representation of the byte array in binary format
     */
    public static String toBinaryString(byte[] bytes) {
        return Arrays.stream(toBits(bytes))
            .map(MyBit::intValue)
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    /**
     * Returns the number of missing bits to fill the byte.
     *
     * <p>E.g. if the length is 5, then the number of missing bits is 3 to fill the byte.
     *
     * @param length the length of the byte array
     *
     * @return the number of missing bits to fill the byte
     */
    public static int computeMissingBits(int length) {
        return (MyByte.NUMBER_OF_BITS - (length % MyByte.NUMBER_OF_BITS)) % MyByte.NUMBER_OF_BITS;
    }
}
