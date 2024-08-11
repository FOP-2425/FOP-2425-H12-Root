package h12.io;

import h12.util.Bits;
import h12.util.Bytes;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * A BitOutputStream allows writing individual bits from an underlying
 * OutputStream.
 */
public class BitOutputStream extends OutputStream {

    /**
     * A marker to indicate that the buffer is full.
     */
    private static final int INVALID = -1;

    /**
     * The default buffer value after a reset.
     */
    private static final int DEFAULT_BUFFER = 0;

    /**
     * The underlying OutputStream to write bytes to.
     */
    final OutputStream underlying;

    /**
     * The current byte to write bits to.
     */
    private int buffer = DEFAULT_BUFFER;

    /**
     * The position of the next bit to write to the buffer.
     */
    private int position = Bytes.NUMBER_OF_BITS - 1;

    /**
     * Creates a BitOutputStream that writes bits to the given underlying
     * OutputStream.
     *
     * @param underlying the underlying OutputStream to write bytes to
     */
    public BitOutputStream(OutputStream underlying) {
        this.underlying = underlying;
    }

    /**
     * Writes a single bit to the buffer.
     *
     * @param bit the bit to write
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if the bit is not 0 or 1
     */
    @StudentImplementationRequired("H2.2")
    public void writeBit(int bit) throws IOException {
        // TODO H2.2
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1: %d".formatted(bit));
        }

        // If buffer is full, flush it
        if (position < 0) {
            flushBuffer();
        }

        buffer = Bits.set(buffer, position--, bit);
    }

    /**
     * Writes the specified byte to this output stream.
     *
     * @param b the {@code byte}.
     * @throws IOException              if an I/O error occurs.
     * @throws IllegalArgumentException if the byte is not in the range of 0 to 255
     */
    @StudentImplementationRequired("H2.2")
    @Override
    public void write(int b) throws IOException {
        // TODO H2.2
        if (b < 0 || b > 255) {
            throw new IllegalArgumentException("Byte must be in the range of 0 to 255: %d".formatted(b));
        }
        for (int i = Bytes.NUMBER_OF_BITS - 1; i >= 0; i--) {
            writeBit(Bits.get(b, i));
        }
    }

    /**
     * Writes {@code len} bytes from the specified byte array starting at offset
     * {@code off} to this output stream.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(byte @NotNull [] b, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }

    /**
     * Writes the specified string to this output stream.
     *
     * @param s the string to write
     * @throws IOException if an I/O error occurs
     */
    public void writeString(String s) throws IOException {
        write(s.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Flushes the buffer to the underlying OutputStream.
     */
    @SolutionOnly("H2.2")
    private void flushBuffer() throws IOException {
        // Flush the buffer if it is not empty
        if (position != Bytes.NUMBER_OF_BITS - 1) {
            underlying.write(buffer);
            buffer = DEFAULT_BUFFER;
            position = Bytes.NUMBER_OF_BITS - 1;
        }
    }

    /**
     * Flushes this output stream and forces any buffered output bits to be written
     * out.
     *
     * @throws IOException if an I/O error occurs.
     */
    @StudentImplementationRequired("H2.2")
    @Override
    public void flush() throws IOException {
        flushBuffer();
        underlying.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        underlying.close();
    }
}
