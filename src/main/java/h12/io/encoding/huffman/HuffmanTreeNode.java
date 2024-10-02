package h12.io.encoding.huffman;

import h12.util.TreeNode;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.Objects;

@DoNotTouch
public final class HuffmanTreeNode extends TreeNode<Character> implements Comparable<HuffmanTreeNode> {

    @DoNotTouch
    private final int frequency;

    @DoNotTouch
    public HuffmanTreeNode(TreeNode<Character> left, TreeNode<Character> right, int frequency) {
        super(left, right, null);
        this.frequency = frequency;
    }

    @DoNotTouch
    public HuffmanTreeNode(Character value, int frequency) {
        super(value);
        this.frequency = frequency;
    }

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

