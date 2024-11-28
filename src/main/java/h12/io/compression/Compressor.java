package h12.io.compression;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.IOException;

/**
 * Represents a compressor that can compress a file.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public interface Compressor extends AutoCloseable {

    /**
     * Compresses the file.
     *
     * @throws IOException if an I/O error occurs
     */
    @DoNotTouch
    void compress() throws IOException;
}
