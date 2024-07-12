package h12.io;

import h12.util.Bytes;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends InputStream {

    private final ByteArrayInputStream stream;
    private byte buffer;
    private int position = -1;

    public BitInputStream(ByteArrayInputStream stream) {
        this.stream = stream;
    }

    @StudentImplementationRequired("H2.1")
    @Override
    public int read() {
        if (buffer == -1) {
            return -1;
        }
        if (position < 0) {
            fetchNextByte();
        }

        return Bytes.getBit(buffer, position--);
    }

    @SolutionOnly
    private void fetchNextByte() {
        buffer = (byte) stream.read();
        position = 7;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
