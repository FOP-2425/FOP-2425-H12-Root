package h12.lang;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@DoNotTouch
public final class Byte {

    @DoNotTouch
    public static final int NUMBER_OF_BITS = 8;

    @DoNotTouch
    public static final int MAX_VALUE = 255;

    @DoNotTouch
    public static final int MIN_VALUE = 0;

    @DoNotTouch
    private final Bit[] bits = new Bit[NUMBER_OF_BITS];

    @DoNotTouch
    private int value;

    @DoNotTouch
    public Byte() {
        this(0);
    }

    @DoNotTouch
    public Byte(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(
                "Value must be between %s and %s: %s".formatted(MIN_VALUE, MAX_VALUE, value)
            );
        }
        this.value = value;
        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            // Reverse the bit indexing here
            bits[i] = Bit.fromInt((value >> (NUMBER_OF_BITS - i - 1)) & 1);
        }
    }

    @DoNotTouch
    public int getValue() {
        return value;
    }

    @DoNotTouch
    public Bit[] getBits() {
        return bits;
    }

    @DoNotTouch
    public Byte decrease(int n) {
        if (value - n < MIN_VALUE) {
            throw new IllegalStateException("Value cannot be decreased below %s".formatted(MIN_VALUE));
        }
        value -= n;
        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            bits[i] = Bit.fromInt((value >> (NUMBER_OF_BITS - i - 1)) & 1);
        }
        return this;
    }

    @DoNotTouch
    public Byte decrease() {
        return decrease(1);
    }

    @DoNotTouch
    public Byte increase(int n) {
        if (value + n > MAX_VALUE) {
            throw new IllegalStateException("Value cannot be increased above %s".formatted(MAX_VALUE));
        }
        value += n;
        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            // Reverse bit indexing here as well
            bits[i] = Bit.fromInt((value >> (NUMBER_OF_BITS - i - 1)) & 1);
        }
        return this;
    }

    @DoNotTouch
    public Byte increase() {
        return increase(1);
    }

    @DoNotTouch
    public Bit get(int index) {
        if (index < 0 || index >= NUMBER_OF_BITS) {
            throw new IllegalArgumentException("Index must be between 0 and %s: %s".formatted(NUMBER_OF_BITS - 1, index));
        }
        // Reverse index when accessing the bit
        return bits[NUMBER_OF_BITS - index - 1];
    }

    @DoNotTouch
    public void set(int index, Bit bit) {
        if (index < 0 || index >= NUMBER_OF_BITS) {
            throw new IllegalArgumentException("Index must be between 0 and %s: %s".formatted(NUMBER_OF_BITS - 1, index));
        }
        // Reverse index when setting the bit
        Bit old = bits[NUMBER_OF_BITS - index - 1];
        bits[NUMBER_OF_BITS - index - 1] = bit;
        if (old == bit) {
            return;
        }
        if (bit == Bit.ZERO) {
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
        return this == o || o instanceof Byte aByte && value == aByte.value;
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
                .map(Bit::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining())
        );
    }
}
