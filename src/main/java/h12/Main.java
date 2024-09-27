package h12;

import h12.io.encoding.Decoder;
import h12.io.encoding.Encoder;
import h12.io.encoding.huffman.HuffmanCodingDecoder;
import h12.io.encoding.huffman.HuffmanCodingEncoder;
import h12.util.Bytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");

        ByteArrayInputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Encoder encoder = new HuffmanCodingEncoder(in, out);
        encoder.encode();

        System.out.println(Arrays.toString(out.toByteArray()));
        System.out.println(Bytes.toBits(out.toByteArray()));

        in = new ByteArrayInputStream(out.toByteArray());
        out = new ByteArrayOutputStream();
        Decoder decoder = new HuffmanCodingDecoder(in, out);
        decoder.decode();
        System.out.println(out);
    }
}
