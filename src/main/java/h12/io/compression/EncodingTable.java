package h12.io.compression;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.NoSuchElementException;

/**
 * A table that maps characters to their codes.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public interface EncodingTable {

    /**
     * Returns whether the encoding table contains the given character.
     *
     * @param character the character to check
     *
     * @return true if the encoding table contains the character, false otherwise
     */
    boolean contains(Character character);

    /**
     * Returns whether the encoding table contains the given code.
     *
     * @param code the code to check
     *
     * @return true if the encoding table contains the code, false otherwise
     */
    boolean contains(String code);

    /**
     * Returns whether the encoding table contains the given code.
     *
     * @param iterable the Huffman code to check
     *
     * @return {@code true} if the encoding table contains the code, {@code false} otherwise
     */
    boolean contains(Iterable<Integer> iterable);

    /**
     * Returns the Huffman code of the given character.
     *
     * @param character the character to get the code for
     *
     * @return the Huffman code of the character
     * @throws NoSuchElementException if the character is not in the encoding table
     */
    String get(Character character);

    /**
     * Returns the character of the given Huffman code.
     *
     * @param code the Huffman code to get the character for
     *
     * @return the character of the code
     * @throws NoSuchElementException if the code is not in the encoding table
     */
    Character get(String code);

    /**
     * Returns the character of the given Huffman code.
     *
     * @param iterable the Huffman code to get the character for
     *
     * @return the character of the code
     * @throws NoSuchElementException if the code is not in the encoding table
     */
    Character get(Iterable<Integer> iterable);
}
