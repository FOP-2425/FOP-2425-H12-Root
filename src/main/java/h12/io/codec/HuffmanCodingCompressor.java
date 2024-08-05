package h12.io.codec;

import h12.io.BitOutputstream;
import h12.util.Bytes;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

public class HuffmanCodingCompressor implements Compressor {

    private final BufferedReader reader;
    private final BitOutputstream out;

    public HuffmanCodingCompressor(InputStream in, OutputStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.out = new BitOutputstream(out);
    }

    @StudentImplementationRequired("H5.1")
    @Override
    public void compress() throws IOException {
        HuffmanCoding huffman = new HuffmanCoding();
        String content = getContent();
        Map<Character, Double> encodingTable = huffman.buildFrequencyTable(content);
        TreeNode<Character> root = huffman.buildTree(encodingTable);
        Map<Character, String> encoded = huffman.buildEncodingTable(root);

        int bits = getEncodingSize(root, content, encoded);
        fill(Bytes.getFillBits(bits));
        encodeTree(root);
        encodeContent(content, encoded);
        out.flush();
    }

    @StudentImplementationRequired("H5.1")
    String getContent() throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    @StudentImplementationRequired("H5.2")
    int getEncodingSize(TreeNode<Character> node, String text, Map<Character, String> encoded) {
        return getTreeSize(node) + getContentSize(text, encoded);
    }

    @SolutionOnly("H5.2")
    private int getTreeSize(TreeNode<Character> node) {
        if (node.isLeaf()) {
            return 1 + 32;
        } else {
            return 1 + +getTreeSize(node.getLeft()) + getTreeSize(node.getRight());
        }
    }

    @SolutionOnly("H5.2")
    private int getContentSize(String text, Map<Character, String> encoded) {
        int bits = 0;
        for (char c : text.toCharArray()) {
            bits += encoded.get(c).length();
        }
        return bits;
    }

    void fill(int n) throws IOException {
        out.write(n);
        for (int i = 0; i < n; i++) {
            out.writeBit(0);
        }
    }

    @StudentImplementationRequired("H5.3")
    void encodeTree(TreeNode<Character> node) throws IOException {
        if (node.isLeaf()) {
            out.writeBit(1);
            out.write(Bytes.toBytes(node.getValue()));
        } else {
            out.writeBit(0);
            encodeTree(node.getLeft());
            encodeTree(node.getRight());
        }
    }

    @StudentImplementationRequired("H5.4")
    void encodeContent(String text, Map<Character, String> encoded) throws IOException {
        for (char c : text.toCharArray()) {
            for (char bit : encoded.get(c).toCharArray()) {
                out.writeBit(bit == '1' ? 1 : 0);
            }
        }
    }

    @Override
    public void close() throws Exception {
        reader.close();
        out.close();
    }
}
