package h12.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream which allows additionally reading bits.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
public abstract class BitInputStream extends InputStream {

    /**
     * Reads a single bit from the input stream.
     *
     * @return the bit read from the input stream or -1 if the end of the stream has been reached
     * @throws IOException if an I/O error occurs
     */
    public abstract int readBit() throws IOException;
}
