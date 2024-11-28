package h12.io.compression.rle;

import h12.io.BitInputStream;
import h12.io.BitOutputStream;
import h12.io.compression.Decompressor;
import h12.lang.MyBit;
import h12.lang.MyBytes;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@DoNotTouch
public final class BitRunningLengthDecompressor implements Decompressor {

    @DoNotTouch
    private final BitInputStream in;

    @DoNotTouch
    private final BitOutputStream out;

    @DoNotTouch
    public BitRunningLengthDecompressor(InputStream in, OutputStream out) {
        this.in = in instanceof BitInputStream bitIn ? bitIn : new BitInputStream(in);
        this.out = out instanceof BitOutputStream bitOut ? bitOut : new BitOutputStream(out);
    }

    @StudentImplementationRequired("H12.2.2")
    void writeBit(int count, MyBit bit) throws IOException {
        // TODO H12.2.2
        for (int i = 0; i < count; i++) {
            out.writeBit(bit);
        }
    }

    @StudentImplementationRequired("H12.2.2")
    @Override
    public void decompress() throws IOException {
        // TODO H12.2.2
        byte[] bytes = new byte[4];
        while (in.read(bytes) != -1) {
            writeBit(MyBytes.toInt(bytes), MyBit.fromInt(in.read()));
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
