package h12.io.codec;

import h12.io.BitInputStream;
import h12.io.BitOutputstream;
import h12.util.Bytes;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class BitRunningLengthEncoder implements Encoder {

    @StudentImplementationRequired("H3.1")
    @Override
    public void encode(InputStream in, OutputStream out) throws IOException {
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

