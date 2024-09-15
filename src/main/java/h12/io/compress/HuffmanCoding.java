package h12.io.compress;

import h12.util.TreeNode;
import org.jetbrains.annotations.NotNull;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Huffman coding is a lossless data compression algorithm. The idea is to assign variable-length codes to input
 * characters. The length of the assigned code depends on the frequency of the character. The most frequent character
 * gets the smallest code and the least frequent character gets the largest code.
 * <p>
 * The Huffman coding algorithm consists of the following steps:
 * <ol>
 *     <li>Build a frequency table of the input characters.</li>
 *     <li>Build a Huffman tree from the frequency table.</li>
 *     <li>Build an encoding table from the Huffman tree.</li>
 *     </ol>
 * <p>
 *     The encoding table is a map that assigns a binary code to each character. The binary code is constructed by
 *     traversing the Huffman tree from the root to the leaf node. The left child is assigned the code "0" and the right
 *     child is assigned the code "1".
 * <p>
 *         For example, consider the following frequency table:
 *         <pre>{@code
 *         a: 0.5
 *         b: 0.25
 *         c: 0.25
 *         }</pre>
 *         The Huffman tree would look like this:
 *         <pre>{@code
 *         root
 *         /    \
 *        a    node
 *            /    \
 *           b      c
 *         }</pre>
 *         The encoding table would look like this:
 *         <pre>{@code
 *         a: 0
 *         b: 10
 *         c: 11
 *         }</pre>
 * <p>
 *         The encoded text "aabc" would be "010011".
 *
 * @author Nhan Huynh, Per Goettlicher
 */
public class HuffmanCoding {

    /**
     * Builds a relative frequency table of the input text. The relative frequency of a character is the number of times
     * it appears in the text divided by the total number of characters in the text.
     *
     * @param text the input text to build the frequency table from
     * @return a map that assigns a relative frequency to each character
     */
    @StudentImplementationRequired("H12.3.1")
    public Map<Character, Double> buildFrequencyTable(String text) {
        // TODO H12.3.1
        double relativeFactor = 1.0 / text.length();
        Map<Character, Double> frequency = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0.0) + relativeFactor);
        }
        return frequency;
    }

    public static void main(String[] args) {
        HuffmanCoding huffmanCoding = new HuffmanCoding();
        Map<Character, Double> frequency = huffmanCoding.buildFrequencyTable("abracadabra");
        System.out.println(frequency);
        TreeNode<Character> root = huffmanCoding.buildTree(frequency);
        System.out.println(root);
        System.out.println(huffmanCoding.buildEncodingTable(root));
    }

    /**
     * Builds a Huffman tree from the frequency table.
     *
     * @param frequency the frequency table to build the tree from
     * @return the root node of the Huffman tree
     */
    @StudentImplementationRequired("H12.3.2")
    @SuppressWarnings("ConstantConditions")
    public TreeNode<Character> buildTree(Map<Character, Double> frequency) {
        // TODO H12.3.2
        Queue<HuffmanTreeNode> builder = new PriorityQueue<>();

        for (Map.Entry<Character, Double> entry : frequency.entrySet()) {
            builder.add(new HuffmanTreeNode(entry.getKey(), entry.getValue()));
        }

        while (builder.size() > 1) {
            HuffmanTreeNode left = builder.poll();
            HuffmanTreeNode right = builder.poll();

            HuffmanTreeNode parent = new HuffmanTreeNode(left, right, left.getFrequency() + right.getFrequency());
            left.setParent(parent);
            right.setParent(parent);
            builder.add(parent);
        }
        return builder.poll();
    }

    /**
     * Builds an encoding table from the Huffman tree. The encoding table is a map that assigns a binary code to each
     * character.
     *
     * @param root the root node of the Huffman tree to build the encoding table fromo
     * @return a map that assigns a binary code to each character
     */
    @SuppressWarnings("ConstantConditions")
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

    /**
     * Recursively builds the encoding table from the Huffman tree.
     *
     * @param node          the current node to build the encoding table from
     * @param encodingTable the encoding table to store the binary codes
     * @param path          the current path to the node
     */
    @SuppressWarnings("ConstantConditions")
    private void buildEncodingTable(
        TreeNode<Character> node,
        Map<Character, String> encodingTable,
        StringBuilder path
    ) {
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

    /**
     * A node in the Huffman tree that stores the frequency of the character.
     *
     * @see TreeNode
     */
    private static class HuffmanTreeNode extends TreeNode<Character> implements Comparable<HuffmanTreeNode> {

        /**
         * The frequency of the character.
         */
        private final double frequency;

        /**
         * Creates a new Huffman tree node with the given left and right children and frequency.
         *
         * @param left      the left child of the node
         * @param right     the right child of the node
         * @param frequency the frequency of the character
         */
        public HuffmanTreeNode(TreeNode<Character> left, TreeNode<Character> right, double frequency) {
            super(left, right, null);
            this.frequency = frequency;
        }

        /**
         * Creates a new Huffman tree node with the given value and frequency.
         *
         * @param value     the value stored in the node
         * @param frequency the frequency of the character
         */
        public HuffmanTreeNode(Character value, double frequency) {
            super(value);
            this.frequency = frequency;
        }

        /**
         * Returns the frequency of the character.
         *
         * @return the frequency of the character
         */
        public double getFrequency() {
            return frequency;
        }

        @Override
        public int compareTo(@NotNull HuffmanTreeNode o) {
            return Double.compare(frequency, o.frequency);
        }
    }
}
