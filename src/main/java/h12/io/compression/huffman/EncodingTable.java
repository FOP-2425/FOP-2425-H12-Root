package h12.io.compression.huffman;

import h12.util.TreeNode;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@DoNotTouch
public final class EncodingTable {

    @DoNotTouch
    private final TreeNode<Character> root;

    @DoNotTouch
    private @Nullable Map<Character, String> encodings = null;

    @DoNotTouch
    public EncodingTable(TreeNode<Character> root) {
        this.root = root;
    }

    @DoNotTouch
    public TreeNode<Character> getRoot() {
        return root;
    }

    @DoNotTouch
    private void buildEncodingTable() {
        encodings = new HashMap<>();
        buildEncodingTable(root, "");
        if (encodings.size() == 1) {
            encodings.put(root.getValue(), "1");
        }
    }

    @DoNotTouch
    private void buildEncodingTable(TreeNode<Character> root, String builder) {
        if (root.isLeaf()) {
            assert root.getValue() != null;
            assert encodings != null;
            encodings.put(root.getValue(), builder);
        } else {
            assert root.getLeft() != null;
            assert root.getRight() != null;
            buildEncodingTable(root.getLeft(), builder + "0");
            buildEncodingTable(root.getRight(), builder + "1");
        }
    }

    @DoNotTouch
    public boolean contains(Character character) {
        if (encodings == null) {
            buildEncodingTable();
        }
        return encodings != null && encodings.containsKey(character);
    }

    @DoNotTouch
    public boolean contains(String code) {
        try {
            get(code);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @DoNotTouch
    public boolean contains(Iterable<Integer> iterable) {
        return contains(StreamSupport.stream(iterable.spliterator(), false)
            .map(String::valueOf)
            .collect(Collectors.joining()));
    }

    @DoNotTouch
    public String get(Character character) {
        if (encodings == null) {
            buildEncodingTable();
        }
        return encodings.get(character);
    }

    @DoNotTouch
    public Character get(String code) {
        TreeNode<Character> current = root;
        if (current.isLeaf()) {
            return current.getValue();
        }
        for (char c : code.toCharArray()) {
            if (c == '0') {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
            assert current != null;
            if (current.isLeaf()) {
                return current.getValue();
            }
        }
        throw new NoSuchElementException(code);
    }

    @DoNotTouch
    public Character get(Iterable<Integer> iterable) {
        return get(StreamSupport.stream(iterable.spliterator(), false)
            .map(String::valueOf)
            .collect(Collectors.joining()));
    }

    @DoNotTouch
    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof EncodingTable that && Objects.equals(root, that.root);
    }

    @DoNotTouch
    @Override
    public int hashCode() {
        return Objects.hashCode(root);
    }

    @DoNotTouch
    @Override
    public String toString() {
        if (encodings == null) {
            buildEncodingTable();
        }
        return encodings.toString();
    }
}
