package h12.util;

import h12.io.compression.EncodingTable;

import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * A mock implementation of the {@link EncodingTable} interface for testing purposes.
 *
 * @author Nhan Huynh
 */
public class MockHuffmanEncodingTable implements EncodingTable {

    /**
     * The encodings of characters.
     */
    private final Map<Character, String> encodings;

    /**
     * Creates a new mock encoding table with the given encodings.
     *
     * @param encodings the encodings of characters
     */
    public MockHuffmanEncodingTable(Map<Character, String> encodings) {
        this.encodings = encodings;
    }

    @Override
    public boolean contains(Character character) {
        return encodings.containsKey(character);
    }

    @Override
    public boolean contains(String code) {
        return encodings.containsValue(code);
    }

    @Override
    public boolean contains(Iterable<Integer> iterable) {
        return contains(StreamSupport.stream(iterable.spliterator(), false)
            .map(String::valueOf)
            .reduce("", String::concat));
    }

    @Override
    public String get(Character character) {
        return encodings.get(character);
    }

    @Override
    public Character get(String code) {
        return encodings.entrySet().stream()
            .filter(entry -> entry.getValue().equals(code))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow();
    }

    @Override
    public Character get(Iterable<Integer> iterable) {
        return get(StreamSupport.stream(iterable.spliterator(), false)
            .map(String::valueOf)
            .reduce("", String::concat));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MockHuffmanEncodingTable that)) return false;
        return Objects.equals(encodings, that.encodings);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(encodings);
    }

    @Override
    public String toString() {
        return encodings.toString();
    }
}
