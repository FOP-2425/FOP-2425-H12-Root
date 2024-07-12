package h12.io;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@DoNotTouch
public class ResourceIOFactory implements IOFactory {

    @Override
    public boolean supportsReader() {
        return true;
    }

    @Override
    public BufferedReader createReader(String ioName) throws FileNotFoundException {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(ioName);
        if (resourceStream == null) {
            throw new FileNotFoundException("Resource does not exist: %s".formatted(ioName));
        }
        return new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
    }

    @Override
    public boolean supportsWriter() {
        return false;
    }

    @Override
    public BufferedWriter createWriter(String ioName) throws IOException {
        throw new UnsupportedOperationException("Cannot write to resource directory");
    }
}
