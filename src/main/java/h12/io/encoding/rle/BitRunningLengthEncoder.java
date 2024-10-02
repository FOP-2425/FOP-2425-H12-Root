package h12.io.encoding.rle;

import h12.io.BitInputStream;
import h12.io.BitOutputStream;
import h12.io.encoding.Encoder;
import h12.lang.Bytes;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@DoNotTouch
public final class BitRunningLengthEncoder implements Encoder {

    @DoNotTouch
    private final BitInputStream in;

    @DoNotTouch
    private final BitOutputStream out;

    private int lastRead = -1;

    @DoNotTouch
    public BitRunningLengthEncoder(InputStream in, OutputStream out) {
        this.in = in instanceof BitInputStream bitIn ? bitIn : new BitInputStream(in);
        this.out = out instanceof BitOutputStream bitOut ? bitOut : new BitOutputStream(out);
    }

    @StudentImplementationRequired("H12")
    int getBitCount(int bit) throws IOException {
        // TODO H12
        int count = 1;
        while ((lastRead = in.readBit()) == bit) {
            count++;
        }
        return count;
    }

    @StudentImplementationRequired("H12")
    @Override
    public void encode() throws IOException {
        // TODO H12
        int bit = in.readBit();
        while (bit != -1) {
            int count = getBitCount(bit);
            // Store count as 4 bytes
            out.write(Bytes.toBytes(count));
            // Store bit
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
