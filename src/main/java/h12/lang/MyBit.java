package h12.lang;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.Objects;

/**
 * Represents a bit with the value of 0 or 1.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class MyBit {

    /**
     * The bit with the value of 0.
     */
    @DoNotTouch
    public static final MyBit ZERO = new MyBit(0);

    /**
     * The bit with the value of 1.
     */
    @DoNotTouch
    public static final MyBit ONE = new MyBit(1);

    /**
     * The value of the bit.
     */
    @DoNotTouch
    private final int value;

    /**
     * Constructs a new bit with the specified value.
     *
     * @param value the value of the bit
     */
    @DoNotTouch
    private MyBit(int value) {
        this.value = value;
    }

    /**
     * Returns the bit with the specified value.
     *
     * @param value the value of the bit
     *
     * @return the bit with the specified value
     * @throws IllegalArgumentException if the value is not 0 or 1
     */
    @DoNotTouch
    public static MyBit fromInt(int value) {
        if (value != 0 && value != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1: %d".formatted(value));
        }
        return value == ONE.intValue() ? ONE : ZERO;
    }

    /**
     * Returns the value of the bit as an integer.
     *
     * @return the value of the bit as an integer
     */
    @DoNotTouch
    public int intValue() {
        return value;
    }

    /**
     * Flips this bit.
     *
     * @return the flipped bit
     */
    public MyBit flip() {
        return value == 0 ? ONE : ZERO;
    }

    @DoNotTouch
    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MyBit bit && value == bit.value;
    }

    @DoNotTouch
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @DoNotTouch
    @Override
    public String toString() {
        return "Bit{value=%s}".formatted(value);
    }
}
