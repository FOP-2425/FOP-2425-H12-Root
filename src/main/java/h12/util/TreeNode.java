package h12.util;

import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

@DoNotTouch
public class TreeNode<T> {

    @DoNotTouch
    private @Nullable TreeNode<T> left;

    @DoNotTouch
    private @Nullable TreeNode<T> right;

    @DoNotTouch
    private @Nullable TreeNode<T> parent;

    @DoNotTouch
    private final @Nullable T value;

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

    @DoNotTouch
    public TreeNode(TreeNode<T> left, TreeNode<T> right, T value) {
        this(left, right, null, value);
    }

    @DoNotTouch
    public TreeNode(TreeNode<T> left, TreeNode<T> right) {
        this(left, right, null);
    }

    @DoNotTouch
    public TreeNode(@Nullable T value) {
        this(null, null, value);
    }

    @DoNotTouch
    public @Nullable TreeNode<T> getLeft() {
        return left;
    }

    @DoNotTouch
    public void setLeft(@Nullable TreeNode<T> left) {
        this.left = left;
    }

    @DoNotTouch
    public @Nullable TreeNode<T> getRight() {
        return right;
    }

    @DoNotTouch
    public void setRight(@Nullable TreeNode<T> right) {
        this.right = right;
    }

    @DoNotTouch
    public @Nullable TreeNode<T> getParent() {
        return parent;
    }

    @DoNotTouch
    public void setParent(@Nullable TreeNode<T> parent) {
        this.parent = parent;
    }

    @DoNotTouch
    public @Nullable T getValue() {
        return value;
    }

    @DoNotTouch
    public boolean isLeaf() {
        return left == null && right == null;
    }
}
