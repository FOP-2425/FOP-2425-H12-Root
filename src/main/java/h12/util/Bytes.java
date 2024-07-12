package h12.util;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

@DoNotTouch
public class Bytes {

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
}
