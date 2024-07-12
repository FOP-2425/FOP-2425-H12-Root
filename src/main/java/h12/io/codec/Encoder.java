package h12.io.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Encoder {

    void encode(InputStream in, OutputStream out) throws IOException;
}
