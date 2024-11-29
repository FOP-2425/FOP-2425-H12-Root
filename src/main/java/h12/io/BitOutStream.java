package h12.io;

import h12.lang.MyBit;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An input stream which allows additionally reading bits.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
public abstract class BitOutStream extends OutputStream {

    /**
     * Writes a single bit to the output stream.
     *
     * @param bit the bit to write
     *
     * @throws IOException if an I/O error occurs
     */
    public abstract void writeBit(MyBit bit) throws IOException;
}
