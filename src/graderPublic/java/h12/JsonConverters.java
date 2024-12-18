package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.io.compress.EncodingTable;
import h12.lang.MyBit;
import h12.lang.MyByte;
import h12.mock.MockBitInputStream;
import h12.mock.MockHuffmanEncodingTable;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for JSON converters.
 *
 * @author Nhan Huynh
 */
public final class JsonConverters extends org.tudalgo.algoutils.tutor.general.json.JsonConverters {

    /**
     * Prevent instantiation of this utility class.
     */
    private JsonConverters() {

    }

    public static @Nullable MyByte toMyByte(JsonNode node) {

        if (!node.isInt() && !node.isNull()) {
            throw new IllegalStateException("JSON node is not an integer");
        }
        if (node.isNull()) {
            return null;
        }
        return new MyByte(node.asInt());
    }

    /**
     * Converts the given JSON node to a bit input stream.
     *
     * @param node the JSON node to convert
     *
     * @return the bit input stream
     */
    public static MockBitInputStream toBitInputStream(JsonNode node) {
        if (!node.isArray()) {
            throw new IllegalStateException("JSON node is not an array");
        }
        return new MockBitInputStream(toList(node, JsonNode::asInt));
    }

    /**
     * Converts the given JSON node to an encoding table.
     *
     * @param node the JSON node to convert
     *
     * @return the encoding table
     */
    public static EncodingTable toEncodingTable(JsonNode node) {
        if (!node.isObject()) {
            throw new IllegalStateException("JSON node is not an object");
        }
        return new MockHuffmanEncodingTable(toMap(node, keyMapper -> keyMapper.charAt(0), JsonNode::asText));
    }

    /**
     * Converts a JSON node to a byte array.
     *
     * @param node the JSON node containing the bytes
     *
     * @return the byte array
     */
    public static byte[] toByteArray(JsonNode node) {
        if (!node.isArray()) {
            throw new IllegalStateException("JSON node is not an array");
        }
        byte[] bytes = new byte[node.size()];
        for (int i = 0; i < node.size(); i++) {
            bytes[i] = (byte) node.get(i).asInt();
        }
        return bytes;
    }

    /**
     * Converts a JSON node to a bit
     *
     * @param node the JSON node containing the bit
     *
     * @return the bit
     */
    public static MyBit toBit(JsonNode node) {
        if (!node.isInt()) {
            throw new IllegalStateException("JSON node is not an int");
        }
        return MyBit.fromInt(node.asInt());
    }
}
