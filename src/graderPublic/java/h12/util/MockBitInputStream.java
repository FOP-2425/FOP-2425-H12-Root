package h12.util;

import h12.io.BitInputStream;
import h12.lang.MyBit;
import h12.lang.MyByte;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MockBitInputStream extends BitInputStream {

    private final List<Integer> bits;
    private final Iterator<Integer> read;

    public MockBitInputStream(List<Integer> bits) {
        this.bits = bits;
        this.read = bits.iterator();
    }

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

    public List<Integer> getBits() {
        return bits;
    }
}
