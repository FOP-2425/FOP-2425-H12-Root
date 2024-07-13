package h12.io.codec;

import h12.io.BitOutputstream;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

public class HuffmanCodingEncoder implements Encoder {

    @StudentImplementationRequired("H5.1")
    @Override
    public void encode(InputStream in, OutputStream out) throws IOException {
        HuffmanCoding huffman = new HuffmanCoding();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BitOutputstream bOut = new BitOutputstream(out);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String text = builder.toString();
        Map<Character, Double> encodingTable = huffman.buildFrequencyTable(text);
        TreeNode<Character> root = huffman.buildTree(encodingTable);
        Map<Character, String> encoded = huffman.buildEncodingTable(root);

        writeHeader(bOut, encoded);
        decodeContent(bOut, text, encoded);
    }

    @StudentImplementationRequired("H5.2")
    void writeHeader(BitOutputstream out, Map<Character, String> encodingTable) throws IOException {
        // TODO: We need to figure out how to write the encoding table to the output stream
    }

    @StudentImplementationRequired("H5.3")
    void decodeContent(BitOutputstream out, String text, Map<Character, String> encoded) throws IOException {
        for (char c : text.toCharArray()) {
            for (char bit : encoded.get(c).toCharArray()) {
                out.writeBit(bit == '1' ? 1 : 0);
            }
        }
    }
}
