package h12.io.compression.huffman;

import h12.io.BitInputStream;
import h12.io.compression.Decompressor;
import h12.lang.MyBit;
import h12.lang.MyByte;
import h12.lang.MyBytes;
import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@DoNotTouch
public final class HuffmanCodingDecompressor implements Decompressor {

    @DoNotTouch
    private final BitInputStream in;

    @DoNotTouch
    private final Writer out;

    @DoNotTouch
    public HuffmanCodingDecompressor(InputStream in, OutputStream out) {
        this.in = in instanceof BitInputStream bitIn ? bitIn : new BitInputStream(in);
        this.out = new OutputStreamWriter(out, StandardCharsets.UTF_8);
    }

    @StudentImplementationRequired("H12.4.2")
    void skipBits() throws IOException {
        // TODO H12.4.2
        MyByte value = new MyByte();
        for (int i = MyByte.NUMBER_OF_BITS - 1; i >= 0; i--) {
            value.set(i, MyBit.fromInt(in.readBit()));
        }
        for (int i = 0; i < value.getValue(); i++) {
            in.readBit();
        }
    }

    @DoNotTouch
    private EncodingTable decodeHeader() throws IOException {
        return new EncodingTable(decodeTree());
    }

    @DoNotTouch
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private TreeNode<Character> decodeTree() throws IOException {
        if (in.readBit() == 1) {
            byte[] bytes = new byte[4];
            in.read(bytes);
            return new TreeNode<>(MyBytes.toChar(bytes));
        }
        TreeNode<Character> left = decodeTree();
        TreeNode<Character> right = decodeTree();
        TreeNode<Character> parent = new TreeNode<>(left, right);
        left.setParent(parent);
        right.setParent(parent);
        return parent;
    }

    @StudentImplementationRequired("H12.4.2")
    char decodeCharacter(int startBit, EncodingTable encodingTable) throws IOException {
        // TODO H12.4.2
        List<Integer> code = new ArrayList<>();
        code.add(startBit);
        while (!encodingTable.contains(code)) {
            code.add(in.readBit());
        }
        return encodingTable.get(code);
    }

    @StudentImplementationRequired("H12.4.2")
    void decodeContent(EncodingTable encodingTable) throws IOException {
        // TODO H12.4.2
        int bit;
        while ((bit = in.readBit()) != -1) {
            out.write(decodeCharacter(bit, encodingTable));
        }
    }

    @StudentImplementationRequired("H12.4.2")
    @Override
    public void decompress() throws IOException {
        // TODO H12.4.2
        skipBits();
        EncodingTable encodingTable = decodeHeader();
        decodeContent(encodingTable);
        out.flush();
    }

    @DoNotTouch
    @Override
    public void close() throws Exception {
        in.close();
        out.close();
    }
}
