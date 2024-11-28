package h12.io;

import h12.lang.MyBit;
import h12.lang.MyByte;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;

@DoNotTouch
public final class BitInputStream extends InputStream {

    @SolutionOnly
    private static final int INVALID = -1;

    @DoNotTouch
    private final InputStream underlying;

    private @Nullable MyByte buffer;

    private int position = INVALID;

    @DoNotTouch
    public BitInputStream(InputStream underlying) {
        this.underlying = underlying;
    }

    @StudentImplementationRequired("H12.1.1")
    void fetch() throws IOException {
        // TODO H12.1.1
        try {
            buffer = new MyByte(underlying.read());
        } catch (IllegalArgumentException e) {
            // Case when the read value is EOF (-1)
            buffer = null;
            return;
        }
        position = MyByte.NUMBER_OF_BITS - 1;
    }

    @StudentImplementationRequired("H12.1.1")
    public int readBit() throws IOException {
        // TODO H12.1.1
        // If we already read all bits from the buffer, fetch the next byte
        if (position < 0) {
            fetch();
        }

        // If the buffer is empty, it means we reached the end of the stream
        if (buffer == null) {
            return INVALID;
        }

        return buffer.get(position--).getValue();
    }

    @StudentImplementationRequired("H12.1.1")
    public int read() throws IOException {
        // TODO H12.1.1
        MyByte value = new MyByte();

        // The Loop needs to start from the most significant bit (leftmost bit)
        for (int i = MyByte.NUMBER_OF_BITS - 1; i >= 0; i--) {
            int bit = readBit();
            // In case we reached the end of the stream, and the buffer is empty, return -1
            if (bit == INVALID && i == MyByte.NUMBER_OF_BITS - 1) {
                return INVALID;
            }
            // In case we reached the end of the stream, return the value read so far
            if (bit == INVALID) {
                break;
            }
            value.set(i, MyBit.fromInt(bit));
        }
        return value.getValue();
    }

    @DoNotTouch
    public int read(byte @NotNull [] b, int off, int len) throws IOException {
        int read = 0;
        for (int i = 0; i < len; i++) {
            int value = read();
            // In case we reached the end of the stream, and the buffer is empty, return -1
            if (value == INVALID && i == 0) {
                return -1;
            }
            // In case we reached the end of the stream, return the number of values read so far
            if (value == INVALID) {
                return read;
            }
            b[off + i] = (byte) value;
            read++;
        }
        return read;
    }

    @DoNotTouch
    @Override
    public void close() throws IOException {
        underlying.close();
    }
}
