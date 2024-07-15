package h12.io.codec;

import h12.io.BitInputStream;
import h12.io.BitOutputstream;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    @StudentImplementationRequired("H3.2")
    void writeCount(BitOutputstream out, int count, int bit) throws IOException {
        for (int i = 0; i < count; i++) {
            out.writeBit(bit);
        }
    }
}
