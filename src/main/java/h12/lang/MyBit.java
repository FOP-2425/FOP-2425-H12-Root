package h12.lang;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.Objects;

@DoNotTouch
public final class MyBit {

    @DoNotTouch
    public static final MyBit ZERO = new MyBit(0);

    @DoNotTouch
    public static final MyBit ONE = new MyBit(1);

    @DoNotTouch
    private final int value;

    @DoNotTouch
    private MyBit(int value) {
        this.value = value;
    }

    @DoNotTouch
    public static MyBit fromInt(int value) {
        if (value != 0 && value != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1: %d".formatted(value));
        }
        return value == ONE.getValue() ? ONE : ZERO;
    }

    @DoNotTouch
    public int getValue() {
        return value;
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
