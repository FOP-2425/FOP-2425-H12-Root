package h12.lang;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a byte with 8 bits.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class MyByte {

    /**
     * The number of bits in a byte.
     */
    @DoNotTouch
    public static final int NUMBER_OF_BITS = 8;

    /**
     * The maximum value of a byte.
     */
    @DoNotTouch
    public static final int MAX_VALUE = 255;

    /**
     * The minimum value of a byte.
     */
    @DoNotTouch
    public static final int MIN_VALUE = 0;

    /**
     * The bit sequence of the byte.
     */
    @DoNotTouch
    private final MyBit[] bits = new MyBit[NUMBER_OF_BITS];

    /**
     * The integer value of the byte.
     */
    @DoNotTouch
    private int value;

    /**
     * Constructs a new byte with the default value of 0.
     */
    @DoNotTouch
    public MyByte() {
        this(0);
    }

    /**
     * Constructs a new byte with the specified value.
     *
     * @param value the value of the byte
     * @throws IllegalArgumentException if the value is not between the {@value MIN_VALUE} and {@value MAX_VALUE}
     */
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

    /**
     * Returns the integer value of the byte.
     *
     * @return the integer value of the byte
     */
    @DoNotTouch
    public int getValue() {
        return value;
    }

    /**
     * Returns the bit sequence of the byte.
     *
     * @return the bit sequence of the byte
     */
    @DoNotTouch
    public MyBit[] getBits() {
        return bits;
    }

    /**
     * Decreases the value of the byte by the specified amount.
     *
     * @param n the amount to decrease the value by
     * @return the decreased byte
     * @throws IllegalArgumentException if the value cannot be decreased below {@value MIN_VALUE}
     */
    @DoNotTouch
    public MyByte decrease(int n) {
        if (value - n < MIN_VALUE) {
            throw new IllegalArgumentException("Value cannot be decreased below %s".formatted(MIN_VALUE));
        }
        value -= n;
        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            bits[i] = MyBit.fromInt((value >> (NUMBER_OF_BITS - i - 1)) & 1);
        }
        return this;
    }

    /**
     * Decreases the value of the byte by 1.
     *
     * @return the decreased byte
     */
    @DoNotTouch
    public MyByte decrease() {
        return decrease(1);
    }

    /**
     * Increases the value of the byte by the specified amount.
     *
     * @param n the amount to increase the value by
     * @return the increased byte
     * @throws IllegalArgumentException if the value cannot be increased above {@value MAX_VALUE}
     */
    @DoNotTouch
    public MyByte increase(int n) {
        if (value + n > MAX_VALUE) {
            throw new IllegalArgumentException("Value cannot be increased above %s".formatted(MAX_VALUE));
        }
        value += n;
        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            // Reverse bit indexing here as well
            bits[i] = MyBit.fromInt((value >> (NUMBER_OF_BITS - i - 1)) & 1);
        }
        return this;
    }

    /**
     * Increases the value of the byte by 1.
     *
     * @return the increased byte
     */
    @DoNotTouch
    public MyByte increase() {
        return increase(1);
    }

    /**
     * Returns the bit at the specified index.
     *
     * @param index the index of the bit to return
     * @return the bit at the specified index
     * @throws IllegalArgumentException if the index is out of bounds
     */
    @DoNotTouch
    public MyBit get(int index) {
        if (index < 0 || index >= NUMBER_OF_BITS) {
            throw new IllegalArgumentException("Index must be between 0 and %s: %s".formatted(NUMBER_OF_BITS - 1, index));
        }
        // Reverse index when accessing the bit
        return bits[NUMBER_OF_BITS - index - 1];
    }

    /**
     * Sets the bit at the specified index.
     *
     * @param index the index of the bit to set
     * @param bit   the bit to set
     * @throws IllegalArgumentException if the index is out of bounds
     */
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
