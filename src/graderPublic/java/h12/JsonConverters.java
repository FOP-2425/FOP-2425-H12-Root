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

    public static MockBitInputStream toBitInputStream(JsonNode node) {
        if (!node.isArray()) {
            throw new IllegalStateException("JSON node is not an array");
        }
        return new MockBitInputStream(toList(node, JsonNode::asInt));
    }
}
