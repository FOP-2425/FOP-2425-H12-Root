package h12.io.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A compressor that compresses an input stream into an output stream. Compression is the process of reducing the size
 * of a file by encoding its content in a more efficient way.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public interface Compressor {

    /**
     * Compresses the given input stream into the output stream. The compressed data is written to the output stream.
     *
     * @param in  the input stream to compress
     * @param out the output stream to write the compressed data to
     * @throws IOException if an I/O error occurs
     */
    void compress(InputStream in, OutputStream out) throws IOException;
}
