package h12.io.compress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import h12.io.BitInputStream;
import h12.io.BitOutputStream;
import h12.util.Bytes;

/**
 * Compressor that uses a simple run-length encoding scheme to compress the input. The input is read bit by bit and
 * the number of consecutive bits of the same value is stored as a 4-byte integer followed by the bit value.
 * <p>
 * For example, the input
 * <pre>{@code
 *     111111110000000011111111
 *     }</pre>
 * would be compressed to
 * <pre>{@code
 *     8 1 8 0 8 1
 *     }</pre>
 * where the first number is the count of the following bit value.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public class BitRunningLengthCompressor implements Compressor {

    /**
     * The input stream to read the data from.
     */
    private final BitInputStream in;

    /**
     * The output stream to write the compressed data to.
     */
    private final BitOutputStream out;

    /**
     * Creates a new compressor that reads data from the given input stream and writes the compressed data to the given.
     *
     * @param in  the input stream to read the data from
     * @param out the output stream to write the compressed data to
     */
    public BitRunningLengthCompressor(InputStream in, OutputStream out) {
        this.in = new BitInputStream(in);
        this.out = new BitOutputStream(out);
    }

    @StudentImplementationRequired("H3.2")
    @Override
    public void compress() throws IOException {
        // TODO H3.2
        int bit = in.readBit();
        while (bit != -1) {
            Map.Entry<Integer, Integer> rle = getBitCount(bit);

            // Store count as 4 bytes
            out.write(Bytes.toBytes(rle.getKey()));

            // Store bit
            out.writeBit(bit);

            // Since we are reading the next bit in the loop, we need to remember it
            bit = rle.getValue();
        }
        out.flush();
    }

    /**
     * Count the number of consecutive bits of the same value.
     *
     * @param bit the bit value to count
     * @return a map entry with the count as the key and the next bit as the value
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H3.1")
    Map.Entry<Integer, Integer> getBitCount(int bit) throws IOException {
        // TODO H3.1
        int count = 1;
        int next;
        while ((next = in.readBit()) == bit) {
            count++;
        }
        return Map.entry(count, next);
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
    }
}
