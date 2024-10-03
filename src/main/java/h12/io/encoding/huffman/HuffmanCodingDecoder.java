package h12.io.encoding.huffman;

import h12.io.BitInputStream;
import h12.io.encoding.Decoder;
import h12.lang.Bit;
import h12.lang.Byte;
import h12.lang.Bytes;
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
public final class HuffmanCodingDecoder implements Decoder {

    @DoNotTouch
    private final BitInputStream in;

    @DoNotTouch
    private final Writer out;

    @DoNotTouch
    public HuffmanCodingDecoder(InputStream in, OutputStream out) {
        this.in = in instanceof BitInputStream bitIn ? bitIn : new BitInputStream(in);
        this.out = new OutputStreamWriter(out, StandardCharsets.UTF_8);
    }

    @StudentImplementationRequired("H12")
    void skipBits() throws IOException {
        // TODO H12
        Byte value = new Byte();
        for (int i = Byte.NUMBER_OF_BITS - 1; i >= 0; i--) {
            value.set(i, Bit.fromInt(in.readBit()));
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
            return new TreeNode<>(Bytes.toChar(bytes));
        }
        TreeNode<Character> left = decodeTree();
        TreeNode<Character> right = decodeTree();
        TreeNode<Character> parent = new TreeNode<>(left, right);
        left.setParent(parent);
        right.setParent(parent);
        return parent;
    }

    @StudentImplementationRequired("H12")
    char decodeCharacter(int startBit, EncodingTable encodingTable) throws IOException {
        // TODO H12
        List<Integer> code = new ArrayList<>();
        code.add(startBit);
        while (!encodingTable.contains(code)) {
            code.add(in.readBit());
        }
        return encodingTable.get(code);
    }

    @StudentImplementationRequired("H12")
    void decodeContent(EncodingTable encodingTable) throws IOException {
        // TODO H12
        int bit;
        while ((bit = in.readBit()) != -1) {
            out.write(decodeCharacter(bit, encodingTable));
        }
    }

    @StudentImplementationRequired("H12")
    @Override
    public void decode() throws IOException {
        // TODO H12
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
