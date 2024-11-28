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

/**
 * An input stream that reads bits from an underlying input stream.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class BitInputStream extends InputStream {

    /**
     * The value returned when the stream has reached the end or the beginning of the stream.
     */
    @SolutionOnly
    private static final int INVALID = -1;

    /**
     * The underlying input stream.
     */
    @DoNotTouch
    private final InputStream underlying;

    /**
     * The buffer used for reading bits.
     */
    private @Nullable MyByte buffer;

    /**
     * The position of the next bit to read in the buffer.
     */
    private int position = INVALID;

    /**
     * Constructs a new bit input stream with the specified underlying input stream.
     *
     * @param underlying the underlying input stream
     */
    @DoNotTouch
    public BitInputStream(InputStream underlying) {
        this.underlying = underlying;
    }

    /**
     * Fetches the next byte from the underlying input stream to the buffer.
     *
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Reads a single bit from the input stream.
     *
     * @return the bit read from the stream
     * @throws IOException if an I/O error occurs
     */
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
    @Override
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

    /**
     * Reads up to {@code len} bytes of data from the input stream into an array of bytes.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array {@code b}
     *            at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of the
     * stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
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
