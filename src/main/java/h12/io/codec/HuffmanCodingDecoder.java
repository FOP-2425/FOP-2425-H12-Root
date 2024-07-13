package h12.io.codec;

import h12.io.BitInputStream;
import h12.io.BitOutputstream;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class HuffmanCodingDecoder implements Decoder {

    void writeHeader(BitOutputstream out, Map<Character, String> encodingTable) throws IOException {
        // TODO: We need to figure out how to write the encoding table to the output stream
    }

    void writeContent(BitOutputstream out, String text, Map<Character, String> encoded) throws IOException {
        for (char c : text.toCharArray()) {
            for (char bit : encoded.get(c).toCharArray()) {
                out.writeBit(bit == '1' ? 1 : 0);
            }
        }
    }

    @StudentImplementationRequired("H6.1")
    @Override
    public void decode(InputStream in, OutputStream out) throws IOException {
        HuffmanCoding huffman = new HuffmanCoding();
        BitInputStream bIn = new BitInputStream(in);
        TreeNode<Character> root = decodeHeader(bIn);
        decodeContent(bIn, out, root);
    }

    @StudentImplementationRequired("H6.2")
    TreeNode<Character> decodeHeader(BitInputStream in) throws IOException {
        return null; // TODO: Depends on the implementation of the header
    }

    @StudentImplementationRequired("H6.3")
    void decodeContent(BitInputStream in, OutputStream out, TreeNode<Character> root) throws IOException {
        int bit;
        while ((bit = in.readBit()) != -1) {
            TreeNode<Character> current = root;
            while (!current.isLeaf()) {
                if (bit == 0) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
            }
            out.write(current.getValue());
        }
        out.flush();
    }
}
