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

    void decompress() throws IOException;
}
