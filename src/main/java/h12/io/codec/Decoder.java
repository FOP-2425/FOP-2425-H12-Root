package h12.io.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Decoder {

    void decode(InputStream in, OutputStream out) throws IOException;
}
