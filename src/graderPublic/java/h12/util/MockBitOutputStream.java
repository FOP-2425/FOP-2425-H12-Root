package h12.util;

import h12.io.BitOutStream;
import h12.lang.MyBit;
import h12.lang.MyByte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A mock bit output stream for testing purposes. This implementation allows us to access the written bits directly.
 *
 * @author Nhan Huynh
 */
public class MockBitOutputStream extends BitOutStream {

    /**
     * The bits written.
     */
    private final List<Integer> bits = new ArrayList<>();

    @Override
    public void writeBit(MyBit bit) throws IOException {
        bits.add(bit.getValue());
    }

    @Override
    public void write(int b) throws IOException {
        MyByte value = new MyByte(b);
        for (int i = MyByte.NUMBER_OF_BITS - 1; i >= 0; i--) {
            bits.add(value.get(i).getValue());
        }
    }

    /**
     * Returns the bits written.
     *
     * @return the bits written
     */
    public List<Integer> getBits() {
        return bits;
    }
}
