package h12.io;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

@DoNotTouch
public interface IOFactory {

    boolean supportsReader();

    BufferedReader createReader(String ioName) throws IOException;

    boolean supportsWriter();

    BufferedWriter createWriter(String ioName) throws IOException;
}
