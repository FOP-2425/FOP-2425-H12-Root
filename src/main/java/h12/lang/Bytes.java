package h12.lang;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;

@DoNotTouch
public final class Bytes {

    @DoNotTouch
    private static final ByteBuffer buffer = ByteBuffer.allocate(4);

    @DoNotTouch
    private Bytes() {
    }

    @DoNotTouch
    public static byte[] toBytes(int value) {
        return buffer.clear().putInt(value).array();
    }

    @DoNotTouch
    public static int toInt(byte[] bytes) {
        return buffer.clear().put(bytes).flip().getInt();
    }

    public static Bit[] toBits(byte[] bytes) {
        Bit[] bits = new Bit[bytes.length * Byte.NUMBER_OF_BITS];
        int offset = -java.lang.Byte.MIN_VALUE;
        int index = 0;
        for (byte b : bytes) {
            Byte value = new Byte(b + offset);
            for (int i = Byte.NUMBER_OF_BITS - 1; i >= 0; i--) {
                bits[index++] = value.get(i);
            }
        }
        return bits;
    }

    public static String toBinaryString(byte[] bytes) {
        return Arrays.stream(toBits(bytes))
                .map(Bit::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }


    public static int computeMissingBits(int length) {
        return (Byte.NUMBER_OF_BITS - (length % Byte.NUMBER_OF_BITS)) % Byte.NUMBER_OF_BITS;
    }
}
