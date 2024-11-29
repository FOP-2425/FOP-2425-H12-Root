package h12.io.compression.huffman;

import h12.io.BitOutStream;
import h12.io.BufferedBitOutputStream;
import h12.io.compression.Compressor;
import h12.lang.MyBit;
import h12.lang.MyBytes;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A compressor that uses the Huffman coding algorithm to compress data.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class HuffmanCodingCompressor implements Compressor {

    /**
     * The input stream to read the text from.
     */
    @DoNotTouch
    private final BufferedReader in;

    /**
     * The output stream to write the compressed data to.
     */
    @DoNotTouch
    private final BitOutStream out;

    /**
     * Creates a new compressor with the given input to compress and output to write to.
     *
     * @param in  the input stream to read the text from
     * @param out the output stream to write the compressed data to
     */
    @DoNotTouch
    public HuffmanCodingCompressor(InputStream in, OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out instanceof BitOutStream bitOut ? bitOut : new BufferedBitOutputStream(out);
    }

    /**
     * Reads the text from the input stream.
     *
     * @return the text read from the input stream
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.1")
    String getText() throws IOException {
        // TODO H12.4.1
        return in.lines().collect(Collectors.joining());
    }

    /**
     * Computes the number of fill bits needed to fill the first byte of the compressed data.
     *
     * @param text          the text to compress
     * @param encodingTable the encoding table to use
     *
     * @return the number of fill bits needed
     */
    @DoNotTouch
    int computeFillBits(String text, EncodingTable encodingTable) {
        return MyBytes.computeMissingBits(computeHeaderSize(encodingTable) + computeTextSize(text, encodingTable));
    }

    /**
     * Computes the size of the header in bits needed to encode the Huffman tree.
     *
     * @param encodingTable the encoding table to use
     *
     * @return the size of the header in bitso
     */
    @DoNotTouch
    private int computeHeaderSize(EncodingTable encodingTable) {
        return computeHeaderSize(encodingTable.getRoot());
    }

    /**
     * Computes the size of the header in bits needed to encode the Huffman tree.
     *
     * @param node the node to compute the size from
     *
     * @return the size of the header in bits
     */
    @SuppressWarnings("ConstantConditions")
    private int computeHeaderSize(TreeNode<Character> node) {
        if (node.isLeaf()) {
            return 1 + 32;
        } else {
            return 1 + computeHeaderSize(node.getLeft()) + computeHeaderSize(node.getRight());
        }
    }

    /**
     * Computes the size of the text in bits needed to encode the text.
     *
     * @param text          the text to compress
     * @param encodingTable the encoding table to use
     *
     * @return the size of the text in bits
     */
    @StudentImplementationRequired("H12.4.1")
    int computeTextSize(String text, EncodingTable encodingTable) {
        // TODO H12.4.1
        return text.chars().map(c -> encodingTable.get((char) c).length()).sum();
    }

    /**
     * Fills the last byte with the given number of bits.
     *
     * @param count the number of bits to fill
     *
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.1")
    void fillBits(int count) throws IOException {
        // TODO H12.4.1
        out.write(count);
        for (int i = 0; i < count; i++) {
            out.writeBit(MyBit.ZERO);
        }
    }

    /**
     * Encodes the header of the compressed data using the given encoding table.
     *
     * @param encodingTable the encoding table to use
     *
     * @throws IOException if an I/O error occurs
     */
    @DoNotTouch
    private void encodeHeader(EncodingTable encodingTable) throws IOException {
        encodeHeader(encodingTable.getRoot());
    }

    /**
     * Encodes the header of the compressed data using the given node until a leaf node is reached.
     *
     * @param node the node to encode
     *
     * @throws IOException if an I/O error occurs
     */
    @DoNotTouch
    @SuppressWarnings("ConstantConditions")
    private void encodeHeader(TreeNode<Character> node) throws IOException {
        if (node.isLeaf()) {
            out.writeBit(MyBit.ONE);
            out.write(MyBytes.toBytes(node.getValue()));
        } else {
            out.writeBit(MyBit.ZERO);
            encodeHeader(node.getLeft());
            encodeHeader(node.getRight());
        }
    }

    /**
     * Encodes the content of the compressed data using the given text and encoding table.
     *
     * @param text          the text to compress
     * @param encodingTable the encoding table to use
     *
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.4.1")
    void encodeContent(String text, EncodingTable encodingTable) throws IOException {
        // TODO H12.4.1
        for (char c : text.toCharArray()) {
            for (char bit : encodingTable.get(c).toCharArray()) {
                out.writeBit(bit == '1' ? MyBit.ONE : MyBit.ZERO);
            }
        }
    }

    @StudentImplementationRequired("H12.4.1")
    @Override
    public void compress() throws IOException {
        // TODO H12.4.1
        String text = getText();
        HuffmanCoding huffman = new HuffmanCoding();
        Map<Character, Integer> frequencyTable = huffman.buildFrequencyTable(text);
        EncodingTable encodingTable = huffman.buildEncodingTable(frequencyTable);
        fillBits(computeFillBits(text, encodingTable));
        encodeHeader(encodingTable);
        encodeContent(text, encodingTable);
        out.flush();
    }

    @DoNotTouch
    @Override
    public void close() throws Exception {
        in.close();
        out.close();
    }
}
