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

/**
 * A table that maps characters to their Huffman codes.
 *
 * <p>It is used to encode and decode data using the Huffman coding algorithm.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class EncodingTable {

    /**
     * The root of the Huffman tree.
     */
    @DoNotTouch
    private final TreeNode<Character> root;

    /**
     * The map that stores the encodings of characters.
     */
    @DoNotTouch
    private @Nullable Map<Character, String> encodings = null;

    /**
     * Creates a new encoding table with the given root of the Huffman tree to build the table from.
     *
     * @param root the root of the Huffman tree
     */
    @DoNotTouch
    public EncodingTable(TreeNode<Character> root) {
        this.root = root;
    }

    /**
     * Returns the root of the Huffman tree.
     *
     * @return the root of the Huffman tree
     */
    @DoNotTouch
    public TreeNode<Character> getRoot() {
        return root;
    }

    /**
     * Builds the encoding table from the Huffman tree.
     */
    @DoNotTouch
    private void buildEncodingTable() {
        encodings = new HashMap<>();
        buildEncodingTable(root, "");
        if (encodings.size() == 1) {
            encodings.put(root.getValue(), "1");
        }
    }

    /**
     * Builds the encoding table recursively from the given node.
     *
     * @param root    the current node
     * @param builder the current encoding built so far
     */
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

    /**
     * Returns whether the encoding table contains the given character.
     *
     * @param character the character to check
     *
     * @return true if the encoding table contains the character, false otherwise
     */
    @DoNotTouch
    public boolean contains(Character character) {
        if (encodings == null) {
            buildEncodingTable();
        }
        return encodings != null && encodings.containsKey(character);
    }

    /**
     * Returns whether the encoding table contains the given code.
     *
     * @param code the code to check
     *
     * @return true if the encoding table contains the code, false otherwise
     */
    @DoNotTouch
    public boolean contains(String code) {
        try {
            get(code);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns whether the encoding table contains the given code.
     *
     * @param iterable the Huffman code to check
     *
     * @return {@code true} if the encoding table contains the code, {@code false} otherwise
     */
    @DoNotTouch
    public boolean contains(Iterable<Integer> iterable) {
        return contains(StreamSupport.stream(iterable.spliterator(), false)
                .map(String::valueOf)
                .collect(Collectors.joining()));
    }

    /**
     * Returns the Huffman code of the given character.
     *
     * @param character the character to get the code for
     *
     * @return the Huffman code of the character
     */
    @DoNotTouch
    public String get(Character character) {
        if (encodings == null) {
            buildEncodingTable();
        }
        return encodings.get(character);
    }

    /**
     * Returns the character of the given Huffman code.
     *
     * @param code the Huffman code to get the character for
     *
     * @return the character of the code
     */
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

    /**
     * Returns the character of the given Huffman code.
     *
     * @param iterable the Huffman code to get the character for
     *
     * @return the character of the code
     */
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
