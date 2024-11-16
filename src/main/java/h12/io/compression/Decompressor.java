package h12.io.compression;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.IOException;

@DoNotTouch
public interface Decompressor extends AutoCloseable {

    @DoNotTouch
    void decompress() throws IOException;
}
