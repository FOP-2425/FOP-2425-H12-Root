package h12.io;

import h12.util.Bytes;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends InputStream {

    private static final int MAX_POSITION = 7;

    private static final int MIN_POSITION = 0;

    private static final int INVALID = -1;

    private final InputStream delegate;

    private int buffer;

    private int position = INVALID;

    public BitInputStream(InputStream delegate) {
        this.delegate = delegate;
    }

    @SolutionOnly
    private void fetchNextByte() throws IOException {
        buffer = delegate.read();
        position = MAX_POSITION;
    }

    @StudentImplementationRequired("H2.1")
    public int readBit() throws IOException {
        if (buffer == INVALID) {
            return INVALID;
        }
        if (position < MIN_POSITION) {
            fetchNextByte();
        }

        return Bytes.getBit(buffer, position--);
    }

    @StudentImplementationRequired("H2.1")
    @Override
    public int read() throws IOException {
        if (position != MAX_POSITION) {
            fetchNextByte();
        }
        position = INVALID;
        return buffer;
    }

    @Override
    public int read(byte @NotNull [] b, int off, int len) throws IOException {
        position = INVALID;
        return delegate.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
