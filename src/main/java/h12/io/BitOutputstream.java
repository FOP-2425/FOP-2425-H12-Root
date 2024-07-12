package h12.io;

import h12.util.Bytes;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputstream extends OutputStream {

    private final ByteArrayOutputStream stream;

    private byte buffer;
    private int position = 7;

    public BitOutputstream(ByteArrayOutputStream stream) {
        this.stream = stream;
    }

    @StudentImplementationRequired("H2.2")
    @Override
    public void write(int b) {
        buffer = Bytes.setBit(buffer, position--, b);
        if (position < 0) {
            stream.write(buffer);
            buffer = 0;
            position = 7;
        }
    }

    @StudentImplementationRequired("H2.2")
    @Override
    public void flush() {
        if (position >= 0) {
            stream.write(buffer);
        }
        buffer = 0;
        position = 7;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
