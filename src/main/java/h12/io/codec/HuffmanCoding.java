package h12.io.codec;

import h12.util.TreeNode;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class HuffmanCoding {

    @StudentImplementationRequired("H4.1")
    public Map<Character, Double> buildFrequencyTable(String text) {
        double relativeFactor = 1.0 / text.length();
        Map<Character, Double> frequency = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0.0) + relativeFactor);
        }
        return frequency;
    }

    @StudentImplementationRequired("H4.2")
    public TreeNode<Character> buildTree(Map<Character, Double> frequency) {
        Queue<HuffmanTreeNode> builder = new PriorityQueue<>();

        for (Map.Entry<Character, Double> entry : frequency.entrySet()) {
            builder.add(new HuffmanTreeNode(entry.getKey(), entry.getValue()));
        }

        while (builder.size() > 1) {
            HuffmanTreeNode left = builder.poll();
            HuffmanTreeNode right = builder.poll();
            assert right != null;
            HuffmanTreeNode parent = new HuffmanTreeNode(left, right, left.getFrequency() + right.getFrequency());
            left.setParent(parent);
            right.setParent(parent);
            builder.add(parent);
        }
        return builder.poll();
    }

    @StudentImplementationRequired("H4.3")
    public Map<Character, String> buildEncodingTable(TreeNode<Character> root) {
        Map<Character, String> encodingTable = new HashMap<>();
        StringBuilder builder = new StringBuilder();

        if (root.isLeaf()) {
            encodingTable.put(root.getValue(), "0");
        } else {
            buildEncodingTable(root.getLeft(), encodingTable, builder);
            buildEncodingTable(root.getRight(), encodingTable, builder);
        }

        return encodingTable;
    }

    @SolutionOnly("H4.3")
    void buildEncodingTable(TreeNode<Character> node, Map<Character, String> encodingTable, StringBuilder path) {
        StringBuilder newPath = new StringBuilder(path);

        if (node == node.getParent().getLeft()) {
            newPath.append("0");
        } else {
            newPath.append("1");
        }

        if (node.isLeaf()) {
            encodingTable.put(node.getValue(), newPath.toString());
            return;
        }

        buildEncodingTable(node.getLeft(), encodingTable, newPath);
        buildEncodingTable(node.getRight(), encodingTable, newPath);
    }

    private static class HuffmanTreeNode extends TreeNode<Character> implements Comparable<HuffmanTreeNode> {

        private final double frequency;

        public HuffmanTreeNode(TreeNode<Character> left, TreeNode<Character> right, double frequency) {
            super(left, right, null);
            this.frequency = frequency;
        }


        public HuffmanTreeNode(Character value, double frequency) {
            super(value);
            this.frequency = frequency;
        }

        public double getFrequency() {
            return frequency;
        }

        @Override
        public int compareTo(@NotNull HuffmanTreeNode o) {
            return Double.compare(frequency, o.frequency);
        }
    }
}
