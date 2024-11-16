package h12.io.compression;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.IOException;

@DoNotTouch
public interface Compressor extends AutoCloseable {

    @DoNotTouch
    void compress() throws IOException;
}
