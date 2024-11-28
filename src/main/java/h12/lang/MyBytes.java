package h12.lang;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;

@DoNotTouch
public final class MyBytes {

    @DoNotTouch
    private static final ByteBuffer buffer = ByteBuffer.allocate(4);

    @DoNotTouch
    private MyBytes() {
    }

    @DoNotTouch
    public static byte[] toBytes(int value) {
        return buffer.clear().putInt(value).array();
    }

    @DoNotTouch
    public static int toInt(byte[] bytes) {
        return buffer.clear().put(bytes).flip().getInt();
    }

    @DoNotTouch
    public static char toChar(byte[] bytes) {
        return (char) toInt(bytes);
    }

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

    public static String toBinaryString(byte[] bytes) {
        return Arrays.stream(toBits(bytes))
                .map(MyBit::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }


    public static int computeMissingBits(int length) {
        return (MyByte.NUMBER_OF_BITS - (length % MyByte.NUMBER_OF_BITS)) % MyByte.NUMBER_OF_BITS;
    }
}
