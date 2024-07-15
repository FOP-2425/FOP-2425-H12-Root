package h12.util;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a node in a binary tree.
 *
 * @param <T> the type of the value stored in the node
 */
public class TreeNode<T> {

    /**
     * The left child of the node.
     */
    private @Nullable TreeNode<T> left;

    /**
     * The right child of the node.
     */
    private @Nullable TreeNode<T> right;

    /**
     * The parent of the node.
     */

    private @Nullable TreeNode<T> parent;

    /**
     * The value stored in the node.
     */
    private final @Nullable T value;

    /**
     * Creates a new node with the given left and right children, parent and value.
     *
     * @param left   the left child of the node
     * @param right  the right child of the node
     * @param parent the parent of the node
     * @param value  the value stored in the node
     */
    public TreeNode(TreeNode<T> left, TreeNode<T> right, TreeNode<T> parent, T value) {
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.value = value;
    }

    /**
     * Creates a new node with the given left and right children and value.
     *
     * @param left  the left child of the node
     * @param right the right child of the node
     * @param value the value stored in the node
     */
    public TreeNode(TreeNode<T> left, TreeNode<T> right, T value) {
        this(left, right, null, value);
    }

    /**
     * Creates a new leaf node with the given value.
     *
     * @param value the value stored in the node
     */
    public TreeNode(@Nullable T value) {
        this(null, null, value);
    }

    /**
     * Returns the left child of the node.
     *
     * @return the left child of the node
     */
    public TreeNode<T> getLeft() {
        return left;
    }

    /**
     * Sets the left child of the node.
     *
     * @param left the left child of the node
     */
    public void setLeft(@Nullable TreeNode<T> left) {
        this.left = left;
    }

    /**
     * Returns the right child of the node.
     *
     * @return the right child of the node
     */
    public TreeNode<T> getRight() {
        return right;
    }

    /**
     * Sets the right child of the node.
     *
     * @param right the right child of the node
     */
    public void setRight(@Nullable TreeNode<T> right) {
        this.right = right;
    }

    /**
     * Returns the parent of the node.
     *
     * @return the parent of the node
     */
    public TreeNode<T> getParent() {
        return parent;
    }

    /**
     * Sets the parent of the node.
     *
     * @param parent the parent of the node
     */
    public void setParent(@Nullable TreeNode<T> parent) {
        this.parent = parent;
    }

    /**
     * Returns the value stored in the node.
     *
     * @return the value stored in the node
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns whether the node is a leaf node.
     *
     * @return whether the node is a leaf node
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }
}
