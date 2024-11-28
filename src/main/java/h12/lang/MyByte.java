package h12.lang;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@DoNotTouch
public final class MyByte {

    @DoNotTouch
    public static final int NUMBER_OF_BITS = 8;

    @DoNotTouch
    public static final int MAX_VALUE = 255;

    @DoNotTouch
    public static final int MIN_VALUE = 0;

    @DoNotTouch
    private final MyBit[] bits = new MyBit[NUMBER_OF_BITS];

    @DoNotTouch
    private int value;

    @DoNotTouch
    public MyByte() {
        this(0);
    }

    @DoNotTouch
    public MyByte(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(
                "Value must be between %s and %s: %s".formatted(MIN_VALUE, MAX_VALUE, value)
            );
        }
        this.value = value;
        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            // Reverse the bit indexing here
            bits[i] = MyBit.fromInt((value >> (NUMBER_OF_BITS - i - 1)) & 1);
        }
    }

    @DoNotTouch
    public int getValue() {
        return value;
    }

    @DoNotTouch
    public MyBit[] getBits() {
        return bits;
    }

    @DoNotTouch
    public MyByte decrease(int n) {
        if (value - n < MIN_VALUE) {
            throw new IllegalStateException("Value cannot be decreased below %s".formatted(MIN_VALUE));
        }
        value -= n;
        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            bits[i] = MyBit.fromInt((value >> (NUMBER_OF_BITS - i - 1)) & 1);
        }
        return this;
    }

    @DoNotTouch
    public MyByte decrease() {
        return decrease(1);
    }

    @DoNotTouch
    public MyByte increase(int n) {
        if (value + n > MAX_VALUE) {
            throw new IllegalStateException("Value cannot be increased above %s".formatted(MAX_VALUE));
        }
        value += n;
        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            // Reverse bit indexing here as well
            bits[i] = MyBit.fromInt((value >> (NUMBER_OF_BITS - i - 1)) & 1);
        }
        return this;
    }

    @DoNotTouch
    public MyByte increase() {
        return increase(1);
    }

    @DoNotTouch
    public MyBit get(int index) {
        if (index < 0 || index >= NUMBER_OF_BITS) {
            throw new IllegalArgumentException("Index must be between 0 and %s: %s".formatted(NUMBER_OF_BITS - 1, index));
        }
        // Reverse index when accessing the bit
        return bits[NUMBER_OF_BITS - index - 1];
    }

    @DoNotTouch
    public void set(int index, MyBit bit) {
        if (index < 0 || index >= NUMBER_OF_BITS) {
            throw new IllegalArgumentException("Index must be between 0 and %s: %s".formatted(NUMBER_OF_BITS - 1, index));
        }
        // Reverse index when setting the bit
        MyBit old = bits[NUMBER_OF_BITS - index - 1];
        bits[NUMBER_OF_BITS - index - 1] = bit;
        if (old == bit) {
            return;
        }
        if (bit == MyBit.ZERO) {
            // Clear the bit at the correct index
            value = value & ~(1 << index);
        } else {
            // Set the bit at the correct index
            value = value | (1 << index);
        }
    }

    @DoNotTouch
    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MyByte aMyByte && value == aMyByte.value;
    }

    @DoNotTouch
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @DoNotTouch
    @Override
    public String toString() {
        return "Byte{value=%s, bits=%s}".formatted(
            value,
            Arrays.stream(bits)
                .map(MyBit::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining())
        );
    }
}
