package h12.io.codec;

import h12.io.BitInputStream;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HuffmanCodingDecoder implements Decoder {

    @Override
    public void decode(InputStream in, OutputStream out) throws IOException {
        BitInputStream bIn = new BitInputStream(in);
        TreeNode<Character> root = decodeTree(bIn);
        decodeContent(bIn, out, root);
    }

    @StudentImplementationRequired("H6.1")
    TreeNode<Character> decodeTree(BitInputStream in) throws IOException {
        if (in.readBit() == 1) {
            return new TreeNode<>((char) in.read());
        }
        TreeNode<Character> left = decodeTree(in);
        TreeNode<Character> right = decodeTree(in);
        TreeNode<Character> parent = new TreeNode<>(left, right, null);
        left.setParent(parent);
        right.setParent(parent);
        return parent;
    }

    @StudentImplementationRequired("H6.2")
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
                bit = in.readBit();
            }
            out.write(current.getValue());
        }
        out.flush();
    }
}
