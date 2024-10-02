package h12.io.encoding.rle;

import h12.io.BitInputStream;
import h12.io.BitOutputStream;
import h12.io.encoding.Decoder;
import h12.lang.Bit;
import h12.lang.Bytes;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@DoNotTouch
public final class BitRunningLengthDecoder implements Decoder {

    @DoNotTouch
    private final BitInputStream in;

    @DoNotTouch
    private final BitOutputStream out;

    @DoNotTouch
    public BitRunningLengthDecoder(InputStream in, OutputStream out) {
        this.in = in instanceof BitInputStream bitIn ? bitIn : new BitInputStream(in);
        this.out = out instanceof BitOutputStream bitOut ? bitOut : new BitOutputStream(out);
    }

    @StudentImplementationRequired("H12")
    void writeBit(int count, Bit bit) throws IOException {
        // TODO H12
        for (int i = 0; i < count; i++) {
            out.writeBit(bit);
        }
    }

    @StudentImplementationRequired("H12")
    @Override
    public void decode() throws IOException {
        // TODO H12
        byte[] bytes = new byte[4];
        while (in.read(bytes) != -1) {
            writeBit(Bytes.toChar(bytes), Bit.fromInt(in.read()));
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
