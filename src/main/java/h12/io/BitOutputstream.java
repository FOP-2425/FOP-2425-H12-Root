package h12.io;

import h12.util.Bytes;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A BitOutputStream allows writing individual bits from an underlying OutputStream.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public class BitOutputstream extends OutputStream {

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
    private final OutputStream underlying;

    /**
     * The current byte to write bits to.
     */
    private int buffer = DEFAULT_BUFFER;

    /**
     * The current position in the buffer to write the next bit to. The value is always in the range of
     * {@value Bytes#MIN_POSITION} and {@value Bytes#MAX_POSITION} or {@value  #INVALID} if the buffer is full.
     * Attention: We always start at the highest bit (from left to right).
     */
    private int position = Bytes.MAX_POSITION;

    /**
     * Creates a BitOutputStream that writes bits to the given underlying OutputStream.
     *
     * @param underlying the underlying OutputStream to write bytes to
     */
    public BitOutputstream(OutputStream underlying) {
        this.underlying = underlying;
    }

    /**
     * Resets the buffer and its position to the default values.
     */
    private void resetBuffer() {
        buffer = DEFAULT_BUFFER;
        position = Bytes.MAX_POSITION;
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
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Bit must be 0 or 1: %d".formatted(bit));
        }
        buffer = Bytes.setBit(buffer, position--, bit);

        // if the buffer is full, write it to the underlying OutputStream and reset the buffer
        if (position < Bytes.MIN_POSITION) {
            underlying.write(buffer);
            resetBuffer();
        }
    }

    /**
     * Writes the specified byte to this output stream. The general contract for write is that one byte is written to
     *
     * @param b the {@code byte}.
     * @throws IOException              if an I/O error occurs.
     * @throws IllegalArgumentException if the byte is not in the range of 0 to 255
     */
    @StudentImplementationRequired("H2.2")
    @Override
    public void write(int b) throws IOException {
        if (b < 0 || b > 255) {
            throw new IllegalArgumentException("Byte must be in the range of 0 to 255: %d".formatted(b));
        }
        // Always start at the highest bit (from left to right)
        for (int i = Bytes.MAX_POSITION; i >= Bytes.MIN_POSITION; i--) {
            writeBit(Bytes.getBit(b, i));
        }
    }

    /**
     * Writes {@code len} bytes from the specified byte array starting at offset {@code off} to this output stream.
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
     * Flushes this output stream and forces any buffered output bits to be written out.
     *
     * @throws IOException if an I/O error occurs.
     */
    @StudentImplementationRequired("H2.2")
    @Override
    public void flush() throws IOException {
        if (position >= Bytes.MIN_POSITION) {
            underlying.write(buffer);
        }
        resetBuffer();
    }

    @Override
    public void close() throws IOException {
        underlying.close();
    }
}
