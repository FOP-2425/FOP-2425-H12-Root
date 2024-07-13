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
        int value = 0;
        for (int i = MAX_POSITION; i >= MIN_POSITION; i--) {
            int bit = readBit();
            if (bit == INVALID) {
                return value;
            }
            value = Bytes.setBit(value, i, bit);
        }
        return value;
    }

    @Override
    public int read(byte @NotNull [] b, int off, int len) throws IOException {
        int read = 0;
        for (int i = 0; i < len; i++) {
            int value = read();
            if (value == INVALID && i == 0) {
                return -1;
            } else if (value == INVALID) {
                return read;
            }
            b[off + i] = (byte) value;
            read++;
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
