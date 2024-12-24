package h12.util;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

/**
 * Represents a node in a binary tree.
 *
 * @param <T> the type of the value stored in the node
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public class TreeNode<T> {

    /**
     * The left child of this node.
     */
    @DoNotTouch
    private @Nullable TreeNode<T> left;

    /**
     * The right child of this node.
     */
    @DoNotTouch
    private @Nullable TreeNode<T> right;

    /**
     * The parent of this node.
     */
    @DoNotTouch
    private @Nullable TreeNode<T> parent;

    /**
     * The value stored in this node.
     */
    @DoNotTouch
    private final @Nullable T value;

    /**
     * Constructs a new tree node with the given children, parent and value.
     *
     * @param left   the left child of the node
     * @param right  the right child of the node
     * @param parent the parent of the node
     * @param value  the value stored in the node
     */
    @DoNotTouch
    public TreeNode(
        @Nullable TreeNode<T> left,
        @Nullable TreeNode<T> right,
        @Nullable TreeNode<T> parent,
        @Nullable T value
    ) {
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.value = value;
    }

    /**
     * Constructs a new tree node with the given children and value.
     *
     * @param left  the left child of the node
     * @param right the right child of the node
     * @param value the value stored in the node
     */
    @DoNotTouch
    public TreeNode(TreeNode<T> left, TreeNode<T> right, T value) {
        this(left, right, null, value);
    }

    /**
     * Constructs a new tree node with the given children and parent.
     *
     * @param left  the left child of the node
     * @param right the right child of the node
     */
    @DoNotTouch
    public TreeNode(TreeNode<T> left, TreeNode<T> right) {
        this(left, right, null);
    }

    /**
     * Constructs a new tree node with the given value.
     *
     * @param value the value stored in the node
     */
    @DoNotTouch
    public TreeNode(@Nullable T value) {
        this(null, null, value);
    }

    /**
     * Returns the left child of this node.
     *
     * @return the left child of this node
     */
    @DoNotTouch
    public @Nullable TreeNode<T> getLeft() {
        return left;
    }

    /**
     * Sets the left child of this node.
     *
     * @param left the left child of this node
     */
    @DoNotTouch
    public void setLeft(@Nullable TreeNode<T> left) {
        this.left = left;
    }

    /**
     * Returns the right child of this node.
     *
     * @return the right child of this node
     */
    @DoNotTouch
    public @Nullable TreeNode<T> getRight() {
        return right;
    }

    /**
     * Sets the right child of this node.
     *
     * @param right the right child of this node
     */
    @DoNotTouch
    public void setRight(@Nullable TreeNode<T> right) {
        this.right = right;
    }

    /**
     * Returns the parent of this node.
     *
     * @return the parent of this node
     */
    @DoNotTouch
    public @Nullable TreeNode<T> getParent() {
        return parent;
    }

    /**
     * Sets the parent of this node.
     *
     * @param parent the parent of this node
     */
    @DoNotTouch
    public void setParent(@Nullable TreeNode<T> parent) {
        this.parent = parent;
    }

    /**
     * Returns the value stored in this node.
     *
     * @return the value stored in this node
     */
    @DoNotTouch
    public @Nullable T getValue() {
        return value;
    }

    /**
     * Returns whether this node is a leaf node.
     *
     * @return {@code true} if this node is a leaf node, {@code false} otherwise
     */
    @DoNotTouch
    public boolean isLeaf() {
        return left == null && right == null;
    }
}
