package h12.util;

import h12.io.BitInputStream;
import h12.lang.MyBit;
import h12.lang.MyByte;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A mock bit input stream for testing purposes. This implementation allows us to define which bits to read.
 *
 * @author Nhan Huynh
 */
public class MockBitInputStream extends BitInputStream {

    /**
     * The bits to read.
     */
    private final List<Integer> bits;

    /**
     * The iterator for reading the bits.
     */
    private final Iterator<Integer> read;

    /**
     * Creates a new mock bit input stream with the given bits.
     *
     * @param bits the bits to read
     */
    public MockBitInputStream(List<Integer> bits) {
        this.bits = bits;
        this.read = bits.iterator();
    }

    /**
     * Creates a new mock bit input stream with the given bits.
     *
     * @param bits the bits to read
     */
    public MockBitInputStream(Integer... bits) {
        this(Arrays.asList(bits));
    }

    @Override
    public int readBit() throws IOException {
        if (!read.hasNext()) {
            return -1;
        }
        return read.next();
    }

    @Override
    public int read() throws IOException {
        if (!read.hasNext()) {
            return -1;
        }
        MyByte value = new MyByte();
        for (int i = 7; i >= 0 && read.hasNext(); i--) {
            value.set(i, MyBit.fromInt(read.next()));
        }
        return value.getValue();
    }

    /**
     * Returns the bits that this mock bit input stream can read.
     *
     * @return the bits that this mock bit input stream can read
     */
    public List<Integer> getBits() {
        return bits;
    }
}
