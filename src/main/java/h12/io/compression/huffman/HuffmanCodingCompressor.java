package h12.io.compression.huffman;

import h12.io.BitOutputStream;
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

@DoNotTouch
public final class HuffmanCodingCompressor implements Compressor {

    @DoNotTouch
    private final BufferedReader in;

    @DoNotTouch
    private final BitOutputStream out;

    @DoNotTouch
    public HuffmanCodingCompressor(InputStream in, OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out instanceof BitOutputStream bitOut ? bitOut : new BitOutputStream(out);
    }

    @StudentImplementationRequired("H12.4.1")
    String getText() throws IOException {
        // TODO H12.4.1
        return in.lines().collect(Collectors.joining());
    }

    @DoNotTouch
    int computeFillBits(String text, EncodingTable encodingTable) {
        return MyBytes.computeMissingBits(computeHeaderSize(encodingTable) + computeTextSize(text, encodingTable));
    }

    @DoNotTouch
    private int computeHeaderSize(EncodingTable encodingTable) {
        return computeHeaderSize(encodingTable.getRoot());
    }

    @SuppressWarnings("ConstantConditions")
    private int computeHeaderSize(TreeNode<Character> node) {
        if (node.isLeaf()) {
            return 1 + 32;
        } else {
            return 1 + computeHeaderSize(node.getLeft()) + computeHeaderSize(node.getRight());
        }
    }

    @StudentImplementationRequired("H12.4.1")
    int computeTextSize(String text, EncodingTable encodingTable) {
        // TODO H12.4.1
        return text.chars().map(c -> encodingTable.get((char) c).length()).sum();
    }

    @StudentImplementationRequired("H12.4.1")
    void fillBits(int count) throws IOException {
        // TODO H12.4.1
        out.write(count);
        for (int i = 0; i < count; i++) {
            out.writeBit(MyBit.ZERO);
        }
    }

    @DoNotTouch
    private void encodeHeader(EncodingTable encodingTable) throws IOException {
        encodeHeader(encodingTable.getRoot());
    }

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
