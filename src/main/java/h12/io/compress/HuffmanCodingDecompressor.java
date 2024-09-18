package h12.io.compress;

import h12.io.BitInputStream;
import h12.util.Bits;
import h12.util.Bytes;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Decompressor that uses Huffman coding to decompress the input.
 * <p>
 * For example, the input
 * <pre>{@code
 *     00000110 | 000000 | 1 00000000 00000000 00000000 01100001 0
 * }</pre>
 * would be decompressed to
 * <pre>{@code
 *    a
 * } </pre>
 *
 * @author Nhan Huynh, Per Goettlicher
 * @see HuffmanCodingDecompressor
 */
public class HuffmanCodingDecompressor implements Decompressor {

    /**
     * The input stream to read the compressed data from.
     */
    private final BitInputStream in;

    /**
     * The output stream to write the decompressed data to.
     */
    private final Writer out;

    /**
     * Creates a new decompressor that reads compressed data from the given input stream and writes the decompressed.
     *
     * @param in  the input stream to read the compressed data from
     * @param out the output stream to write the decompressed data to
     */
    public HuffmanCodingDecompressor(InputStream in, OutputStream out) {
        this.in = new BitInputStream(in);
        this.out = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    @Override
    public void decompress() throws IOException {
        skipBits();
        TreeNode<Character> root = decodeTree();
        decodeContent(root);
        out.flush();
    }

    /**
     * Skips the filled bits.
     *
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.2")
    void skipBits() throws IOException {
        // TODO H12.4.2
        int value = 0;
        for (int i = Bytes.NUMBER_OF_BITS - 1; i >= 0; i--) {
            value = Bits.set(value, i, in.readBit());
        }
        for (int i = 0; i < value; i++) {
            in.readBit();
        }
    }

    /**
     * Decodes the tree from the input stream.
     *
     * @return the root of the tree
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.2")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    TreeNode<Character> decodeTree() throws IOException {
        // TODO H12.4.2
        if (in.readBit() == 1) {
            byte[] bytes = new byte[4];
            in.read(bytes);
            return new TreeNode<>(Bytes.toChar(bytes));
        }
        TreeNode<Character> left = decodeTree();
        TreeNode<Character> right = decodeTree();
        TreeNode<Character> parent = new TreeNode<>(left, right, null);
        left.setParent(parent);
        right.setParent(parent);
        return parent;
    }

    /**
     * Decodes the content from the input stream.
     *
     * @param root the root of the tree
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.2")
    @SuppressWarnings("ConstantConditions")
    void decodeContent(TreeNode<Character> root) throws IOException {
        // TODO H12.4.2
        int bit;
        while ((bit = in.readBit()) != -1) {
            TreeNode<Character> current = root;

            while (!current.isLeaf()) {
                if (bit == 0) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
                if (current.isLeaf()) {
                    break;
                }
                bit = in.readBit();
            }

            out.write(current.getValue());
        }
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
    }
}
