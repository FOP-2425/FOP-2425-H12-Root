package h12;

import com.fasterxml.jackson.databind.JsonNode;
import h12.io.BitInputStream;
import org.mockito.Mockito;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static Map.Entry<List<Integer>, BitInputStream> toBitReadStream(JsonNode node) {
        List<Integer> bits = new ArrayList<>(toList(node, JsonNode::asInt));
        bits.add(-1);
        BitInputStream stream = Mockito.mock(BitInputStream.class);
        try {
            Mockito.when(stream.readBit()).thenReturn(bits.getFirst(), bits.subList(1, bits.size()).toArray(Integer[]::new));
        } catch (IOException e) {
            Assertions2.fail(Assertions2.emptyContext(),
                comment -> "An error occurred while mocking the BitInputStream. Please contact the tutor.");
        }
        return Map.entry(bits, stream);
    }
}
