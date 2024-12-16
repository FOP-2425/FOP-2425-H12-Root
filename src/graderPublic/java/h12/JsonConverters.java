package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.lang.MyByte;
import h12.mock.MockBitInputStream;
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
}
