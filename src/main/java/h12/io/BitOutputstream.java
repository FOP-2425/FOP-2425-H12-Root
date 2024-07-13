package h12.io;

import h12.util.Bytes;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputstream extends OutputStream {

    private static final int MAX_POSITION = 7;

    private static final int MIN_POSITION = 0;

    private static final int INVALID = -1;

    private static final int DEFAULT_BUFFER = 0;

    private final OutputStream delegate;

    private byte buffer = DEFAULT_BUFFER;

    private int position = MAX_POSITION;

    public BitOutputstream(OutputStream delegate) {
        this.delegate = delegate;
    }

    private void resetBuffer() {
        buffer = DEFAULT_BUFFER;
        position = MAX_POSITION;
    }

    @StudentImplementationRequired("H2.2")
    public void writeBit(int bit) throws IOException {
        buffer = (byte) Bytes.setBit(buffer, position--, bit);
        if (position < MIN_POSITION) {
            delegate.write(buffer);
            resetBuffer();
        }
    }

    @StudentImplementationRequired("H2.2")
    @Override
    public void write(int b) throws IOException {
        for (int i = MAX_POSITION; i >= MIN_POSITION; i--) {
            writeBit(Bytes.getBit(b, i));
        }
    }

    @Override
    public void write(byte @NotNull [] b, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }

    @StudentImplementationRequired("H2.2")
    @Override
    public void flush() throws IOException {
        if (position >= MIN_POSITION) {
            delegate.write(buffer);
        }
        resetBuffer();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
