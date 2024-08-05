package h12.io.codec;

import h12.io.BitInputStream;
import h12.util.Bytes;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HuffmanCodingDecompressor implements Decompressor {

    private final BitInputStream in;

    private final OutputStream out;

    public HuffmanCodingDecompressor(InputStream in, OutputStream out) {
        this.in = new BitInputStream(in);
        this.out = out;
    }

    @Override
    public void decompress() throws IOException {
        skip();
        TreeNode<Character> root = decodeTree();
        decodeContent(root);
        out.flush();
    }

    @StudentImplementationRequired("H6.1")
    void skip() throws IOException {
        int value = 0;
        for (int i = Bytes.NUMBER_OF_BITS - 1; i >= 0; i--) {
            value = Bytes.setBit(value, i, in.readBit());
        }
        for (int i = 0; i < value; i++) {
            in.readBit();
        }
    }

    @StudentImplementationRequired("H6.2")
    TreeNode<Character> decodeTree() throws IOException {
        if (in.readBit() == 1) {
            byte[] bytes = new byte[2];
            return new TreeNode<>((char) in.read(bytes));
        }
        TreeNode<Character> left = decodeTree();
        TreeNode<Character> right = decodeTree();
        TreeNode<Character> parent = new TreeNode<>(left, right, null);
        left.setParent(parent);
        right.setParent(parent);
        return parent;
    }

    @StudentImplementationRequired("H6.3")
    void decodeContent(TreeNode<Character> root) throws IOException {
        int bit;
        while ((bit = in.readBit()) != -1) {
            TreeNode<Character> current = root;
            while (!current.isLeaf()) {
                if (bit == 0) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
                bit = in.readBit();
            }
            out.write(Bytes.toBytes(current.getValue()));
        }
    }
}
