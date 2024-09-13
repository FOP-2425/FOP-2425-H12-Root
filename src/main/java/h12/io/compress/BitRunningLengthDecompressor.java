package h12.io.compress;

import h12.io.BitInputStream;
import h12.io.BitOutputStream;
import h12.util.Bytes;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Decompressor that uses a simple run-length encoding scheme to decompress the input. The input is read bit by bit and
 * the number of consecutive bits of the same value is stored as a 4-byte integer followed by the bit value.
 * <p>
 * For example, the input
 * <pre>{@code
 *     8 1 8 0 8 1
 *     }</pre>
 * would be decompressed to
 * <pre>{@code
 *     111111110000000011111111
 *     }</pre>
 * where the first number is the count of the following bit value.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public class BitRunningLengthDecompressor implements Decompressor {

    /**
     * The input stream to read the compressed data from.
     */
    private final BitInputStream in;

    /**
     * The output stream to write the decompressed data to.
     */
    private final BitOutputStream out;

    /**
     * Creates a new decompressor that reads compressed data from the given input stream and writes the decompressed.
     *
     * @param in  the input stream to read the compressed data from
     * @param out the output stream to write the decompressed data to
     */
    public BitRunningLengthDecompressor(InputStream in, OutputStream out) {
        this.in = new BitInputStream(in);
        this.out = new BitOutputStream(out);
    }

    @StudentImplementationRequired("H3.2")
    @Override
    public void decompress() throws IOException {
        // TODO H3.2
        byte[] bytes = new byte[4];
        int read = in.read(bytes);
        while (read != -1) {
            writeBit(Bytes.toInt(bytes), in.read());
            read = in.read(bytes);
        }
        out.flush();
    }

    /**
     * Write the given bit count times to the output stream.
     *
     * @param count the number of times to write the bit
     * @param bit   the bit to write
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H3.2")
    void writeBit(int count, int bit) throws IOException {
        // TODO H3.2
        for (int i = 0; i < count; i++) {
            out.writeBit(bit);
        }
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
    }
}
