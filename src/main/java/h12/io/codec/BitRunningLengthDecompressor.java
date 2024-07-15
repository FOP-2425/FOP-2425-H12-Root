package h12.io.codec;

import h12.io.BitInputStream;
import h12.io.BitOutputstream;
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

    @StudentImplementationRequired("H3.2")
    @Override
    public void decompress(InputStream in, OutputStream out) throws IOException {
        BitInputStream bis = new BitInputStream(in);
        BitOutputstream bout = new BitOutputstream(out);

        int count = bis.read();
        while (count != -1) {
            writeCount(bout, count, bis.readBit());
            count = bis.read();
        }
    }

    /**
     * Write the given bit count times to the output stream.
     *
     * @param out   the output stream to write to the bit
     * @param count the number of times to write the bit
     * @param bit   the bit to write
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H3.2")
    void writeCount(BitOutputstream out, int count, int bit) throws IOException {
        for (int i = 0; i < count; i++) {
            out.writeBit(bit);
        }
    }
}
