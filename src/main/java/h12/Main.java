package h12;

import h12.io.codec.HuffmanCodingCompressor;
import h12.io.codec.HuffmanCodingDecompressor;
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
        String text = "abc";
        ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HuffmanCodingCompressor compressor = new HuffmanCodingCompressor();
        compressor.compress(in, out);
        System.out.println(Bytes.toBits(out.toByteArray()));
        in = new ByteArrayInputStream(out.toByteArray());
        out = new ByteArrayOutputStream();
        HuffmanCodingDecompressor decompressor = new HuffmanCodingDecompressor();
        decompressor.decompress(in, out);
        System.out.println(out);
    }
}
