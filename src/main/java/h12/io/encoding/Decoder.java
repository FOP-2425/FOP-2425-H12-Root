package h12.io.encoding;

import java.io.IOException;

/**
 * A decompressor that decompresses an input stream into an output stream. Decompression is the process of restoring a
 * compressed file to its original size and content.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public interface Decoder extends AutoCloseable {

    /**
     * Decompresses the input stream into the output stream.
     *
     * @throws IOException if an I/O error occurs
     */
    void decode() throws IOException;
}
