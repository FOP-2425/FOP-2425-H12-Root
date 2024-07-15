package h12.io.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A decompressor that decompresses an input stream into an output stream. Decompression is the process of restoring a
 * compressed file to its original size and content.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public interface Decompressor {

    /**
     * Decompresses the given input stream into the output stream. The decompressed data is written to the output
     * stream.
     *
     * @param in  the input stream to decompress
     * @param out the output stream to write the decompressed data to
     * @throws IOException if an I/O error occurs
     */
    void decompress(InputStream in, OutputStream out) throws IOException;
}
