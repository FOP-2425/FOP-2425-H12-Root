package h12.io;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystemIOFactory implements IOFactory {

    @StudentImplementationRequired("H1")
    @Override
    public boolean supportsReader() {
        // TODO H1
        return true;
    }

    @StudentImplementationRequired("H1")
    @Override
    public BufferedReader createReader(String ioName) throws FileNotFoundException {
        // TODO H1
        return new BufferedReader(new FileReader(ioName));
    }

    @StudentImplementationRequired("H1")
    @Override
    public boolean supportsWriter() {
        // TODO H1
        return true;
    }

    @StudentImplementationRequired("H1")
    @Override
    public BufferedWriter createWriter(String ioName) throws IOException {
        // TODO H1
        return new BufferedWriter(new FileWriter(ioName));
    }
}
