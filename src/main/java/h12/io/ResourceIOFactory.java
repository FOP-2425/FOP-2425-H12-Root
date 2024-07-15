package h12.io;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * A resource-based implementation of the {@link IOFactory} interface which allows creating readers for files on the
 * classpath.
 *
 * <p>For this implementation, the {@link #supportsReader()} method should always return {@code true} and the
 * {@link #supportsWriter()} method should always return {@code false}.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
@DoNotTouch
public class ResourceIOFactory implements IOFactory {

    @Override
    public boolean supportsReader() {
        return true;
    }

    @Override
    public BufferedReader createReader(String path) throws IOException {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(path);
        if (resourceStream == null) {
            throw new FileNotFoundException("Resource does not exist: %s".formatted(path));
        }
        return new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
    }

    @Override
    public boolean supportsWriter() {
        return false;
    }

    @Override
    public BufferedWriter createWriter(String path) throws IOException {
        throw new IOException("Cannot write to resource directory");
    }
}
