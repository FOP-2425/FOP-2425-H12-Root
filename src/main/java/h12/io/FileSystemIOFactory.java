package h12.io;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A filesystem-based implementation of the {@link IOFactory} interface which allows creating readers and writers for files
 * on the filesystem.
 *
 * <p>For this implementation, the {@link #supportsReader()} and {@link #supportsWriter()} methods should always return {@code true}.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public class FileSystemIOFactory implements IOFactory {

    @StudentImplementationRequired("H1")
    @Override
    public boolean supportsReader() {
        // TODO H1
        return true;
    }

    @StudentImplementationRequired("H1")
    @Override
    public BufferedReader createReader(String path) throws FileNotFoundException {
        // TODO H1
        return new BufferedReader(new FileReader(path));
    }

    @StudentImplementationRequired("H1")
    @Override
    public boolean supportsWriter() {
        // TODO H1
        return true;
    }

    @StudentImplementationRequired("H1")
    @Override
    public BufferedWriter createWriter(String path) throws IOException {
        // TODO H1
        return new BufferedWriter(new FileWriter(path));
    }
}
