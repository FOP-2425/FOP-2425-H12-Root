package h12.io.compression.huffman;

import h12.util.TreeNode;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Huffman coding algorithm to compress and decompress data.
 *
 * <p> It is used to compress data by encoding characters with variable-length codes based on their frequency.
 * <p> The Huffman tree is built from the frequency table of characters and used to build the encoding table.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public final class HuffmanCoding {

    /**
     * Returns the frequency table of characters in the given text.
     *
     * @param text the text to build the frequency table from
     * @return the frequency table of characters in the text
     */
    @StudentImplementationRequired("H12.3.1")
    public Map<Character, Integer> buildFrequencyTable(String text) {
        // TODO H12.3.1
        return text.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toMap(Function.identity(), c -> 1, Integer::sum));
    }

    /**
     * Removes and returns the minimum element from the given collection using the given comparator.
     *
     * @param elements the collection to remove the minimum element from
     * @param cmp      the comparator to compare elements
     * @param <T>      the type of elements in the collection
     * @return the minimum element
     */
    @StudentImplementationRequired("H12.3.2")
    <T> T removeMin(Collection<? extends T> elements, Comparator<? super T> cmp) {
        // TODO H12.3.2
        T min = elements.stream().min(cmp).orElseThrow();
        elements.remove(min);
        return min;
    }

    /**
     * Builds indirectly a tree from the given frequency table of characters using the given functions and comparator.
     *
     * @param frequency the frequency table of characters
     * @param f         maps a character and its frequency an element representing both values
     * @param g         combines two elements into a new element
     * @param cmp       the comparator to compare elements and remove the minimum element
     * @param <T>       the type of elements to build the tree from
     * @return the Huffman tree built from the frequency table
     */
    @StudentImplementationRequired("H12")
    <T> T build(
        Map<Character, Integer> frequency,
        BiFunction<Character, Integer, T> f,
        BiFunction<T, T, T> g,
        Comparator<? super T> cmp
    ) {
        // TODO H12
        Collection<T> builder = new ArrayList<>();
        frequency.forEach((character, freq) -> builder.add(f.apply(character, freq)));
        while (builder.size() > 1) {
            T left = removeMin(builder, cmp);
            T right = removeMin(builder, cmp);
            builder.add(g.apply(left, right));
        }
        return removeMin(builder, cmp);
    }

    /**
     * Builds the Huffman tree from the given frequency table of characters.
     *
     * @param frequency the frequency table of characters
     * @return the Huffman tree built from the frequency table
     */
    @DoNotTouch
    private TreeNode<Character> buildTree(Map<Character, Integer> frequency) {
        return build(
            frequency,
            HuffmanTreeNode::new,
            (left, right) -> {
                HuffmanTreeNode parent = new HuffmanTreeNode(left, right, left.getFrequency() + right.getFrequency());
                left.setParent(parent);
                right.setParent(parent);
                return parent;
            },
            Comparator.comparingInt(HuffmanTreeNode::getFrequency)
        );
    }

    /**
     * Returns the encoding table of characters in the given text.
     *
     * @param frequencyTable the frequency table of characters
     * @return the encoding table of characters in the text
     */
    @DoNotTouch
    public EncodingTable buildEncodingTable(Map<Character, Integer> frequencyTable) {
        return new EncodingTable(buildTree(frequencyTable));
    }
}
