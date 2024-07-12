package h12.util;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

@DoNotTouch
public class Bytes {

    private Bytes() {
    }

    public static int getBit(byte value, int position) {
        if (position < 0 || position > 7) {
            throw new IllegalArgumentException("Byte contains only 8 bits: %d".formatted(position));
        }
        return (value >> position) & 1;
    }

    public static byte setBit(byte value, int position, int bit) {
        if (position < 0 || position > 7) {
            throw new IllegalArgumentException("Byte contains only 8 bits: %d".formatted(position));
        }
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1: %d".formatted(bit));
        }
        return (byte) (bit == 1 ? value | (1 << position) : value & ~(1 << position));
    }
}
