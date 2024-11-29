package h12.io.compression.rle;

import h12.io.BitInputStream;
import h12.io.BitOutStream;
import h12.io.BufferedBitInputStream;
import h12.io.BufferedBitOutputStream;
import h12.io.compression.Compressor;
import h12.lang.MyBytes;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A simple compressor that uses the running length encoding algorithm to compress data.
 *
 * <p>E.g. the input 111111110000111111 would be compressed to 8 1 4 0 6 1 to represent 8 ones, 4 zeros, and 6 ones.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class BitRunningLengthCompressor implements Compressor {

    /**
     * The input stream to read from.
     */
    @DoNotTouch
    private final BitInputStream in;

    /**
     * The output stream to write to.
     */
    @DoNotTouch
    private final BitOutStream out;

    /**
     * The last read bit.
     */
    private int lastRead = -1;

    /**
     * Creates a new compressor with the given input to compress and output to write to.
     *
     * @param in  the input stream to read from
     * @param out the output stream to write to
     */
    @DoNotTouch
    public BitRunningLengthCompressor(InputStream in, OutputStream out) {
        this.in = in instanceof BitInputStream bitIn ? bitIn : new BufferedBitInputStream(in);
        this.out = out instanceof BitOutStream bitOut ? bitOut : new BufferedBitOutputStream(out);
    }

    /**
     * Returns the number of bits that are the same as the given bit.
     *
     * @param bit the bit to count
     *
     * @return the number of bits that are the same as the given bit
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.2.1")
    int getBitCount(int bit) throws IOException {
        // TODO H12.2.1
        int count = 1;
        while ((lastRead = in.readBit()) == bit) {
            count++;
        }
        return count;
    }

    @StudentImplementationRequired("H12.2.1")
    @Override
    public void compress() throws IOException {
        // TODO H12.2.1
        int bit = in.readBit();
        while (bit != -1) {
            int count = getBitCount(bit);
            // Store count as 4 bytes
            out.write(MyBytes.toBytes(count));
            // Store bit as 1 byte since Java can only read/write bytes
            out.write(bit);
            // Since we are reading the next bit in the loop from getBitCount, we need to remember it
            bit = lastRead;
        }
        out.flush();
    }

    @DoNotTouch
    @Override
    public void close() throws Exception {
        in.close();
        out.close();
    }
}
