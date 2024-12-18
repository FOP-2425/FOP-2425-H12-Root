package h12.mock;

import h12.io.compress.EncodingTable;

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
    public boolean containsCharacter(Character character) {
        return encodings.containsKey(character);
    }

    @Override
    public boolean containsCode(String code) {
        return encodings.containsValue(code);
    }

    @Override
    public boolean containsCode(Iterable<Integer> iterable) {
        return containsCode(StreamSupport.stream(iterable.spliterator(), false)
            .map(String::valueOf)
            .reduce("", String::concat));
    }

    @Override
    public String getCode(Character character) {
        return encodings.get(character);
    }

    @Override
    public Character getCharacter(String code) {
        return encodings.entrySet().stream()
            .filter(entry -> entry.getValue().equals(code))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow();
    }

    @Override
    public Character getCharacter(Iterable<Integer> iterable) {
        return getCharacter(StreamSupport.stream(iterable.spliterator(), false)
            .map(String::valueOf)
            .reduce("", String::concat));
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MockHuffmanEncodingTable that && encodings.equals(that.encodings);
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
