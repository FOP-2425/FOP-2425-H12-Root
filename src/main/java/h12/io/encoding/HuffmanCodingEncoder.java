package h12.io.encoding;

import h12.io.BitOutputStream;
import h12.util.Bytes;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Compressor that uses Huffman coding to compress the input.
 * <p>
 * For example, the input
 * <pre>{@code
 *      a
 * }</pre>
 * would be compressed to
 * <pre>{@code
 *      00000110 | 000000 | 1 00000000 00000000 00000000 01100001 0
 * } </pre>
 * where
 * <li>00000110 is the number of bits to skip (used to fill the remaining bits) </li>
 * <li>000000 are the filled bits which should be skipped</li>
 * <li>1 represents the tree and since we encoded a single character, the tree is a leaf</li>
 * <li>00000000 00000000 00000000 01100001 is the byte representation of the character 'a'</li>
 * <li>0 is the encoded content where 'a' is encoded as '0'</li>
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public class HuffmanCodingEncoder implements Encoder {

    /**
     * The input stream to read the data from.
     */
    private final BufferedReader reader;

    /**
     * The output stream to write the compressed data to.
     */
    private final BitOutputStream out;

    /**
     * Creates a new compressor that reads data from the given input stream and writes the compressed data to the given.
     *
     * @param in  the input stream to read the data from
     * @param out the output stream to write the compressed data to
     */
    public HuffmanCodingEncoder(InputStream in, OutputStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = new BitOutputStream(out);
    }

    @Override
    public void encode() throws IOException {
        HuffmanCoding huffman = new HuffmanCoding();
        String content = getContent();
        Map<Character, Integer> encodingTable = huffman.buildFrequencyTable(content);
        TreeNode<Character> root = huffman.buildTree(encodingTable);
        Map<Character, String> encoded = huffman.buildEncodingTable(root);

        int bits = getEncodingSize(root, content, encoded);
        fill(Bytes.computeMissingBits(bits));
        encodeTree(root);
        encodeContent(content, encoded);
        out.flush();
    }

    /**
     * Returns the content of the input stream as a string.
     *
     * @return the content of the input stream as a string
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.1")
    String getContent() throws IOException {
        // TODO H12.4.1
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    /**
     * Returns the number of bits used to encode the data.
     *
     * @param node    the root of the tree to encode
     * @param text    the text to encode
     * @param encoded the encoding table
     * @return the number of bits used to encode the data
     */
    int getEncodingSize(TreeNode<Character> node, String text, Map<Character, String> encoded) {
        return getTreeSize(node) + getContentSize(text, encoded);
    }

    /**
     * Returns the number of bits used to encode the tree.
     *
     * @param node the root of the tree to encode
     * @return the number of bits used to encode the tree
     */
    @SuppressWarnings("ConstantConditions")
    private int getTreeSize(TreeNode<Character> node) {
        if (node.isLeaf()) {
            return 1 + 32;
        } else {
            return 1 + getTreeSize(node.getLeft()) + getTreeSize(node.getRight());
        }
    }

    /**
     * Returns the number of bits used to encode the content.
     *
     * @param text    the text to encode
     * @param encoded the encoding table
     * @return the number of bits used to encode the content
     */
    private int getContentSize(String text, Map<Character, String> encoded) {
        int bits = 0;
        for (char c : text.toCharArray()) {
            bits += encoded.get(c).length();
        }
        return bits;
    }

    /**
     * Fills the output stream with n bits.
     *
     * @param n the number of bits to fill
     * @throws IOException if an I/O error occurs
     */
    void fill(int n) throws IOException {
        out.write(n);
        for (int i = 0; i < n; i++) {
            out.writeBit(0);
        }
    }

    /**
     * Encodes the tree in the output stream.
     *
     * @param node the root of the tree to encode
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.1")
    @SuppressWarnings("ConstantConditions")
    void encodeTree(TreeNode<Character> node) throws IOException {
        // TODO H12.4.1
        if (node.isLeaf()) {
            out.writeBit(1);
            out.write(Bytes.toBytes(node.getValue()));
        } else {
            out.writeBit(0);
            encodeTree(node.getLeft());
            encodeTree(node.getRight());
        }
    }

    /**
     * Encodes the content in the output stream.
     *
     * @param text    the text to encode
     * @param encoded the encoding table
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.1")
    void encodeContent(String text, Map<Character, String> encoded) throws IOException {
        // TODO H12.4.1
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
