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

@DoNotTouch
public final class HuffmanCoding {

    @StudentImplementationRequired("H12.3.1")
    public Map<Character, Integer> buildFrequencyTable(String text) {
        // TODO H12.3.1
        return text.chars()
               .mapToObj(c -> (char) c)
               .collect(Collectors.toMap(Function.identity(), c -> 1, Integer::sum));
    }

    @StudentImplementationRequired("H12.3.2")
    <T> T removeMin(Collection<? extends T> elements, Comparator<? super T> cmp) {
        // TODO H12.3.2
        T min = elements.stream().min(cmp).orElseThrow();
        elements.remove(min);
        return min;
    }

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

    @DoNotTouch
    public EncodingTable buildEncodingTable(Map<Character, Integer> frequencyTable) {
        return new EncodingTable(buildTree(frequencyTable));
    }
}
