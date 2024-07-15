package h12.io;

import h12.util.Bytes;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;

/**
 * A BitInputStream allows reading individual bits from an underlying InputStream.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public class BitInputStream extends InputStream {


    /**
     * A marker to indicate that all bits are read from the buffer.
     */
    private static final int INVALID = -1;

    /**
     * The underlying InputStream to read bytes from.
     */
    private final InputStream underlying;

    /**
     * The current byte to read bits from.
     */
    private int buffer;

    /**
     * The current position in the buffer to read the next bit from. The value is always in the range of
     * {@value Bytes#MIN_POSITION} and {@value Bytes#MAX_POSITION} or {@value  #INVALID} if the buffer is empty.
     * Attention: We always start at the highest bit (from left to right).
     */
    private int position = INVALID;

    /**
     * Creates a BitInputStream that reads bits from the given underlying InputStream.
     *
     * @param underlying the underlying InputStream to read bytes from
     */
    public BitInputStream(InputStream underlying) {
        this.underlying = underlying;
    }

    /**
     * Fetches the next byte from the underlying InputStream into the buffer and resets the position to read the
     * next bit from the buffer.
     *
     * @throws IOException if an I/O error occurs
     */
    @SolutionOnly
    private void fetchNextByte() throws IOException {
        buffer = underlying.read();
        position = Bytes.MAX_POSITION;
    }

    /**
     * Reads the next bit from the underlying InputStream.
     *
     * @return the next bit read from the underlying InputStream or {@code -1} if the end of the stream is reached.
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H2.1")
    public int readBit() throws IOException {
        // If we already read all bits from the buffer, fetch the next byte.
        if (position < Bytes.MIN_POSITION) {
            fetchNextByte();
        }

        // If buffer is empty, it means we reached the end of the stream.
        if (buffer == INVALID) {
            return INVALID;
        }
        return Bytes.getBit(buffer, position--);
    }

    /**
     * Reads the next 8 bits from the underlying InputStream and returns the value as an integer. If the end of the
     * stream is reached, return only the bits read so far and fill the remaining bits with zeros or {@code -1} if
     * no bits were read.
     *
     * @return the next 8 bits read from the underlying InputStream as an integer
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H2.1")
    @Override
    public int read() throws IOException {
        int value = 0;

        // Loop needs to start from the most significant bit.
        for (int i = Bytes.MAX_POSITION; i >= Bytes.MIN_POSITION; i--) {
            int bit = readBit();
            // In case we reached the end of the stream and the buffer is empty, return -1.
            if (bit == INVALID && i == Bytes.MAX_POSITION) {
                return -1;
            }
            // In case we reached the end of the stream, return the value read so far.
            if (bit == INVALID) {
                return value;
            }

            value = Bytes.setBit(value, i, bit);
        }
        return value;
    }

    /**
     * Reads up to {@code len} bytes of data from the underlying InputStream into an array of bytes. An attempt is made
     * to read as many bytes as possible, but a smaller number may be read. The number of bytes actually read is
     * returned as an integer.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array {@code b}
     *            at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or {@code -1} if the starting read process is the
     * end of the stream or the total number if bytes read so far
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read(byte @NotNull [] b, int off, int len) throws IOException {
        int read = 0;
        for (int i = 0; i < len; i++) {
            int value = read();
            if (value == INVALID && i == 0) {
                return -1;
            } else if (value == INVALID) {
                return read;
            }
            b[off + i] = (byte) value;
            read++;
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        underlying.close();
    }
}
