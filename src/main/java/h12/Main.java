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
        String text = "ab";
        ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HuffmanCodingCompressor compressor = new HuffmanCodingCompressor(in, out);
        compressor.compress();
        // 00000001 0110001001011000011011000111001100000000
        System.out.println(Bytes.toBits(out.toByteArray()));
        in = new ByteArrayInputStream(out.toByteArray());
        out = new ByteArrayOutputStream();
        HuffmanCodingDecompressor decompressor = new HuffmanCodingDecompressor(in, out);
        decompressor.decompress();
        System.out.println(out.toString());
    }
}
