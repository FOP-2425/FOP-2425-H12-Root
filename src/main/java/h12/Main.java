package h12;

import h12.io.compress.HuffmanCodingCompressor;
import h12.io.compress.HuffmanCodingDecompressor;
import h12.util.Bytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test() throws Exception {
        String text = "a";
        System.out.printf("Text: %s%n", text);
        ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HuffmanCodingCompressor compressor = new HuffmanCodingCompressor(in, out);
        compressor.compress();
        System.out.printf("Compressed: %s%n", Bytes.toBitsRepresentation(out.toByteArray()));
        in = new ByteArrayInputStream(out.toByteArray());
        out = new ByteArrayOutputStream();
        HuffmanCodingDecompressor decompressor = new HuffmanCodingDecompressor(in, out);
        decompressor.decompress();
        System.out.printf("Decompressed: %s%n", out);
    }
}
