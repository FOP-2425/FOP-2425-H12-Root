package h12.io.compress.huffman;

import h12.util.TreeNode;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.Objects;

/**
 * A node in a Huffman tree which can be either a leaf node with a character value or an internal node with a frequency.
 * The frequency is the sum of the frequencies of its children and is used to build the Huffman tree.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class HuffmanTreeNode extends TreeNode<Character> implements Comparable<HuffmanTreeNode> {

    /**
     * The frequency of this node.
     */
    @DoNotTouch
    private final int frequency;

    /**
     * Creates a new internal node with the given left and right children and frequency.
     *
     * @param left      the left child
     * @param right     the right child
     * @param frequency the frequency of this node
     */
    @DoNotTouch
    public HuffmanTreeNode(TreeNode<Character> left, TreeNode<Character> right, int frequency) {
        super(left, right, null);
        this.frequency = frequency;
    }

    /**
     * Creates a new leaf node with the given value and frequency.
     *
     * @param value     the value of this node
     * @param frequency the frequency of this node
     */
    @DoNotTouch
    public HuffmanTreeNode(Character value, int frequency) {
        super(value);
        this.frequency = frequency;
    }

    /**
     * Returns the frequency of this node.
     *
     * @return the frequency of this node
     */
    @DoNotTouch
    public int getFrequency() {
        return frequency;
    }

    @DoNotTouch
    @Override
    public int compareTo(@NotNull HuffmanTreeNode o) {
        return Integer.compare(frequency, o.frequency);
    }

    @DoNotTouch
    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof HuffmanTreeNode that && frequency == that.frequency;
    }

    @DoNotTouch
    @Override
    public int hashCode() {
        return Objects.hashCode(frequency);
    }
}

