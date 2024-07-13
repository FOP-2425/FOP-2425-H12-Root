package h12.util;

import java.util.Objects;

public class TreeNode<T> {
    private TreeNode<T> left;

    private TreeNode<T> right;

    private TreeNode<T> parent;

    private final T value;

    public TreeNode(TreeNode<T> left, TreeNode<T> right, TreeNode<T> parent, T value) {
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.value = value;
    }

    public TreeNode(TreeNode<T> left, TreeNode<T> right, T value) {
        this(left, right, null, value);
    }

    public TreeNode(T value) {
        this(null, null, value);
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<T> left) {
        this.left = left;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    public void setRight(TreeNode<T> right) {
        this.right = right;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public T getValue() {
        return value;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
