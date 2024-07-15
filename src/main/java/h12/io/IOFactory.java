package h12.io;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Factory abstraction for creating readers and writers.
 *
 * @author Nhan Huynh, Per Goettlicher
 */
@DoNotTouch
public interface IOFactory {

    /**
     * Returns {@code true} if this factory supports creating readers.
     *
     * @return {@code true} if this factory supports creating readers
     */
    boolean supportsReader();

    /**
     * Creates a reader for reading the given path.
     *
     * @param path the name of the file to read
     * @return a reader for reading the given path
     * @throws IOException if an I/O error occurs
     */
    BufferedReader createReader(String path) throws IOException;

    /**
     * Returns {@code true} if this factory supports creating writers.
     *
     * @return {@code true} if this factory supports creating writers
     */
    boolean supportsWriter();

    /**
     * Creates a writer for writing to the given path.
     *
     * @param path the name of the file to write
     * @return a writer for writing to the given path
     * @throws IOException if an I/O error occurs
     */
    BufferedWriter createWriter(String path) throws IOException;
}
