package h12.io.compress;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.IOException;

/**
 * Represents a decompressor that can decompress a file.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public interface Decompressor extends AutoCloseable {

    /**
     * Decompresses the file.
     *
     * @throws IOException if an I/O error occurs
     */
    @DoNotTouch
    void decompress() throws IOException;
}
