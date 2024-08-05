package h12.io.codec;

import java.io.IOException;

/**
 * A compressor that compresses an input stream into an output stream. Compression is the process of reducing the size
 * of a file by encoding its content in a more efficient way.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public interface Compressor extends AutoCloseable {

    void compress() throws IOException;
}
