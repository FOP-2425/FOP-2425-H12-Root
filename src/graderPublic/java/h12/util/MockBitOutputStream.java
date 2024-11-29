package h12.util;

import h12.io.BitOutStream;
import h12.lang.MyBit;
import h12.lang.MyByte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MockBitOutputStream extends BitOutStream {

    private final List<Integer> bits = new ArrayList<>();

    @Override
    public void writeBit(MyBit bit) throws IOException {
        bits.add(bit.getValue());
    }

    @Override
    public void write(int b) throws IOException {
        MyByte value = new MyByte(b);
        for (int i = 7; i >= 0; i--) {
            bits.add(value.get(i).getValue());
        }
    }

    public List<Integer> getBits() {
        return bits;
    }
}
