package h12.io;

import h12.lang.MyBit;
import h12.lang.MyByte;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.OutputStream;

@DoNotTouch
public final class BitOutputStream extends OutputStream {

    @SolutionOnly
    private static final int INVALID = -1;

    @DoNotTouch
    private final OutputStream underlying;

    private MyByte buffer = new MyByte();

    private int position = MyByte.NUMBER_OF_BITS - 1;

    @DoNotTouch
    public BitOutputStream(OutputStream underlying) {
        this.underlying = underlying;
    }


    @StudentImplementationRequired("H12.1.2")
    void flushBuffer() throws IOException {
        // TODO H12.1.2
        // Flush the buffer if it is not empty
        if (position != MyByte.NUMBER_OF_BITS - 1) {
            assert buffer != null;
            underlying.write(buffer.getValue());
            buffer = new MyByte();
            position = MyByte.NUMBER_OF_BITS - 1;
        }
    }

    @StudentImplementationRequired("H12.1.2")
    public void writeBit(MyBit bit) throws IOException {
        // TODO H12.1.2

        // If buffer is full, flush it
        if (position < 0) {
            flushBuffer();
        }

        assert buffer != null;
        buffer.set(position--, bit);
    }

    @StudentImplementationRequired("H12.1.2")
    @Override
    public void write(int b) throws IOException {
        // TODO H12.1.2
        MyByte byteToWrite = new MyByte(b);
        for (int i = MyByte.NUMBER_OF_BITS - 1; i >= 0; i--) {
            writeBit(byteToWrite.get(i));
        }
    }

    @DoNotTouch
    @Override
    public void write(byte @NotNull [] b, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }

    @DoNotTouch
    @Override
    public void flush() throws IOException {
        flushBuffer();
        underlying.flush();
    }

    @DoNotTouch
    @Override
    public void close() throws IOException {
        flush();
        underlying.close();
    }
}
