package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.util.MockBitInputStream;

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

    /**
     * Converts a JSON node to a mock bit input stream.
     *
     * @param node the JSON node containing the bits
     *
     * @return the mock bit input stream
     */
    public static MockBitInputStream toBitInputStream(JsonNode node) {
        if (!node.isArray()) {
            throw new IllegalStateException("JSON node is not an array");
        }
        return new MockBitInputStream(toList(node, JsonNode::asInt));
    }

    /**
     * Converts a JSON node to an integer array.
     *
     * @param node the JSON node containing the integers
     *
     * @return the integer array
     */
    public static int[] toIntArray(JsonNode node) {
        if (!node.isArray()) {
            throw new IllegalStateException("JSON node is not an array");
        }
        return toList(node, JsonNode::asInt).stream().mapToInt(i -> i).toArray();
    }
}
