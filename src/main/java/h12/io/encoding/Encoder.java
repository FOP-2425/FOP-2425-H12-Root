package h12.io.encoding;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.IOException;

@DoNotTouch
public interface Encoder extends AutoCloseable {

    @DoNotTouch
    void encode() throws IOException;
}
