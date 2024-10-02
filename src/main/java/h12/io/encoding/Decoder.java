package h12.io.encoding;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.IOException;

@DoNotTouch
public interface Decoder extends AutoCloseable {

    @DoNotTouch
    void decode() throws IOException;
}
