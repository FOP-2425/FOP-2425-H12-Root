package h12.io.codec;

import h12.io.BitInputStream;
import h12.io.BitOutputstream;
import h12.util.Bytes;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

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

    @StudentImplementationRequired("H3.1")
    @Override
    public void compress(InputStream in, OutputStream out) throws IOException {
        BitInputStream bis = new BitInputStream(in);
        BitOutputstream bos = new BitOutputstream(out);

        int bit = bis.readBit();
        while (bit != -1) {
            Map.Entry<Integer, Integer> rle = count(bis, bit);

            // Store count as 4 bytes
            bos.write(Bytes.toBytes(rle.getKey()));

            // Store bit
            bos.writeBit(bit);

            // Since we are reading the next bit in the loop, we need to remember it
            bit = rle.getValue();
        }
    }

    /**
     * Count the number of consecutive bits of the same value.
     *
     * @param in  the bits to read
     * @param bit the bit value to count
     * @return a map entry with the count as the key and the next bit as the value
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H3.2")
    Map.Entry<Integer, Integer> count(BitInputStream in, int bit) throws IOException {
        int count = 1;
        int next;
        while ((next = in.readBit()) == bit) {
            count++;
        }
        return Map.entry(count, next);
    }
}

