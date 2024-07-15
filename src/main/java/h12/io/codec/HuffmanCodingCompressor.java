package h12.io.codec;

import h12.io.BitOutputstream;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

public class HuffmanCodingCompressor implements Compressor {

    @StudentImplementationRequired("H5.1")
    @Override
    public void compress(InputStream in, OutputStream out) throws IOException {
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

        encodeTree(root, bOut);
        encodeContent(bOut, text, encoded);
    }

    @StudentImplementationRequired("H5.2")
    void encodeTree(TreeNode<Character> node, BitOutputstream out) throws IOException {
        if (node.isLeaf()) {
            out.writeBit(1);
            out.write(node.getValue());
        } else {
            out.writeBit(0);
            encodeTree(node.getLeft(), out);
            encodeTree(node.getRight(), out);
        }
    }

    @StudentImplementationRequired("H5.3")
    void encodeContent(BitOutputstream out, String text, Map<Character, String> encoded) throws IOException {
        for (char c : text.toCharArray()) {
            for (char bit : encoded.get(c).toCharArray()) {
                out.writeBit(bit == '1' ? 1 : 0);
            }
        }
    }
}
