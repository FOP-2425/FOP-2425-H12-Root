package h12.mock;

import h12.io.BitOutStream;
import h12.lang.MyBit;
import h12.lang.MyByte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A mock bit output stream for testing purposes. This implementation allows us to access the written bits directly.
 *
 * @author Nhan Huynh
 */
public class MockBitOutputStream extends BitOutStream {

    /**
     * The bits written to the stream.
     */
    private final List<Integer> bits = new ArrayList<>();

    /**
     * Whether the stream has been flushed.
     */
    private boolean flushed;

    @Override
    public void writeBit(MyBit bit) throws IOException {
        bits.add(bit.intValue());
    }

    @Override
    public void write(int b) throws IOException {
        MyByte value = new MyByte(b);
        for (int i = MyByte.NUMBER_OF_BITS - 1; i >= 0; i--) {
            bits.add(value.get(i).intValue());
        }
    }

    /**
     * Returns the bits written to the stream.
     *
     * @return the bits written to the stream
     */
    public List<Integer> getBits() {
        if (!flushed) {
            return List.of();
        }
        return bits;
    }

    /**
     * Returns whether the stream has been flushed.
     *
     * @return whether the stream has been flushed
     */
    public boolean isFlushed() {
        return flushed;
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        flushed = true;
    }

    @Override
    public void close() throws IOException {
        super.close();
        flush();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getBits());
    }

    @Override
    public String toString() {
        return bits.toString();
    }
}
