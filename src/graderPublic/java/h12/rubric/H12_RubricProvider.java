package h12.rubric;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Provides the rubric for H10.
 *
 * @author Nhan Huynh
 */
public abstract class H12_RubricProvider implements RubricProvider {

    /**
     * Defines the subtask H12.1.1 for task H12.1.
     */
    private static final Subtask H12_1_1 = Subtask.builder()
        .description("H12.1.1 | Bits lesen")
        .testClassName("h12.H12_1_1_Tests")
        .criterion("Die Methode fetch() aktualisiert den Puffer und die Position korrekt.", Map.of(
            "testFetchNotEOF", List.of(JsonParameterSet.class),
            "testFetchEOF", List.of(JsonParameterSet.class)
        ))
        .criterion("Die Methode readBit() liest das nächste Byte korrekt, falls wir bereits alle Bits des vorherigen Bytes gelesen haben", "testReadBitNextByte", JsonParameterSet.class)
        .criterion("Die Methode readBit() gibt in allen anderen Fällen das korrekte Bit zurück.", Map.of(
            "testReadBitByteStart", List.of(JsonParameterSet.class),
            "testReadBitByteMiddle", List.of(JsonParameterSet.class),
            "testReadBitByteEnd", List.of(JsonParameterSet.class),
            "testReadBitEOF", List.of(JsonParameterSet.class)
        ))
        .criterion("Die Methode read() gibt das korrekte Ergebnis zurück, falls wir am Ende des Streams sind.", "testReadEnd", JsonParameterSet.class)
        .criterion("Die Methode read() gibt das korrekte Teilergebnis zurück, falls der Stream keine 8 Bits mehr enthält.", "testReadPartial", JsonParameterSet.class)
        .criterion("Die Methode read() gibt in allen anderen Fallen das korrekte Ergebnis zurück.", "testRead", JsonParameterSet.class)
        .build();

    /**
     * Defines the subtask H12.1.2 for task H12.1.
     */
    private static final Subtask H12_1_2 = Subtask.builder()
        .description("H12.1.2 | Bits schreiben")
        .testClassName("h12.H12_1_2_Tests")
        .criterion("Die Methode flushBuffer() aktualisiert den Puffer und Position korrekt, wenn nötig.", false, Map.of(
            "testFlushBufferUpdateYes", List.of(JsonParameterSet.class),
            "testFlushBufferUpdateNo", List.of(JsonParameterSet.class)
        ))
        .criterion("Die Methode flushBuffer() schreibt das Zeichen in den internen OutputStream korrekt.", false, "testFlushBufferWrite", JsonParameterSet.class)
        .criterion("Die Methode writeBit(Bit bit) schreibt das Zeichen in den internen OutputStream, falls der Puffer voll ist.", false, Map.of(
            "testWriteBitFlushYes", List.of(JsonParameterSet.class),
            "testWriteBitFlushNo", List.of(JsonParameterSet.class)
        ))
        .criterion("Die Methode writeBit(Bit bit) schreibt ein Bit korrekt.", false, "testWriteBit", JsonParameterSet.class)
        .criterion("Die Methode write(int b) schreibt ein Byte korrekt.", false, "testWrite", JsonParameterSet.class)
        .criterion("Die Methode write(int b) wirft eine IllegalArgumentException, falls die Eingabe kein Byte ist.", false, "testWriteIllegalArgumentException", JsonParameterSet.class)
        .build();

    /**
     * Defines the task H12.1.
     */
    private static final Task H12_1 = Task.builder()
        .description("H12.1 | Mit Bits jonglieren: Eingabe und Ausgabe im Detail")
        .subtasks(H12_1_1, H12_1_2)
        .build();

    /**
     * Defines the subtask H12.2.1 for task H12.2.
     */
    private static final Subtask H12_2_1 = Subtask.builder()
        .description("H12.2.1 | BitRunningLengthCompressor")
        .testClassName("h12.H12_2_1_Tests")
        .criterion("Die Methode getBitCount(int bit) gibt die korrekte Anzahl an aufeinanderfolgenden wiederholenden Bits zurück, für den Fall, dass die Anzahl der Bits nicht maximal ist.", "testGetBitCountNotMax", JsonParameterSet.class)
        .criterion("Die Methode getBitCount(int bit) gibt die korrekte Anzahl an aufeinanderfolgenden wiederholenden Bits zurück, für den Fall, dass die Anzahl der Bits maximal ist.", "testGetBitCountMax", JsonParameterSet.class)
        .criterion("Die Methode compress() komprimiert korrekt.", "testCompress", JsonParameterSet.class)
        .build();

    /**
     * Defines the subtask H12.2.2 for task H12.2.
     */
    private static final Subtask H12_2_2 = Subtask.builder()
        .description("H12.2.2 | BitRunningLengthDecompressor")
        .testClassName("h12.H12_2_2_Tests")
        .criterion("Die Methode writeBit(int count, Bit bit) schreibt die Anzahl an aufeinanderfolgenden wiederholenden Bits korrekt.", false, "testWriteBit", JsonParameterSet.class)
        .criterion("Die Methode decompress() liest die Anzahl an aufeinanderfolgenden wiederholenden Bits.", false, "testDecompressBitCount", JsonParameterSet.class)
        .criterion("Die Methode decompress() dekomprimiert korrekt.", false, "testDecompress", JsonParameterSet.class)
        .build();

    /**
     * Defines the task H12.2.
     */
    private static final Task H12_2 = Task.builder()
        .description("H12.3 | My Heart Skips Skips Skips, Skips Skips Skips a Bit")
        .subtasks(H12_2_1, H12_2_2)
        .build();

    /**
     * Defines the subtask H12.3.1 for task H12.3.
     */
    private static final Subtask H12_3_1 = Subtask.builder()
        .description("H12.3.1 | Häufigkeitstabelle")
        .testClassName("h12.H12_3_1_Tests")
        .criterion("Die Methode buildFrequencyTable(String text) erstellt die Häufigkeitstabelle mit allen Zeichen als Schlüssel korrekt.", false, "testBuildFrequencyTableKeys", JsonParameterSet.class)
        .criterion("Die Methode buildFrequencyTable(String text) erstellt die Häufigkeitstabelle mt den Häufigkeiten korrekt.", false, "testResult", JsonParameterSet.class)
        .build();

    /**
     * Defines the subtask H12.3.2 for task H12.3.
     */
    private static final Subtask H12_3_2 = Subtask.builder()
        .description("H12.3.2 | Huffman-Baum")
        .testClassName("h12.H12_3_2_Tests")
        .criterion("Die Methode removeMin(Collection<? extends T> elements, Comparator<? super T> cmp) entfernt das Minimum und gibt diesen korrekt zurück.", "testRemoveMin", JsonParameterSet.class)
        .criterion("Die Methode build(Map<Character, Integer> frequency, BiFunction<Character, Integer, T> f, BiFunction<T, T, T> g, Comparator<? super T> cmp) erstellt die Elemente mit der Funktion f korrekt.", "testBuildFunctionF")
        .criterion("Die Methode build(Map<Character, Integer> frequency, BiFunction<Character, Integer, T> f, BiFunction<T, T, T> g, Comparator<? super T> cmp) wendet die Funktion g mit den beiden Minimumelementen korrekt an.", "testBuildFunctionG")
        .criterion("Die Methode build(Map<Character, Integer> frequency, BiFunction<Character, Integer, T> f, BiFunction<T, T, T> g, Comparator<? super T> cmp) ist vollständig und korrekt.", false, "testResult")
        .build();

    /**
     * Defines the task H12.3.
     */
    private static final Task H12_3 = Task.builder()
        .description("H12.3 | Die Kunst des Codes: Huffman-Coding I")
        .subtasks(H12_3_1, H12_3_2)
        .build();

    /**
     * Defines the subtask H12.4.1 for task H12.4.
     */
    private static final Subtask H12_4_1 = Subtask.builder()
        .description("H12.4.1 | Huffman-Komprimierung")
        .testClassName("h12.H12_4_1_Tests")
        .criterion("Die Methode getText() liest den Text korrekt ein.", "testGetText", JsonParameterSet.class)
        .criterion("Die Methode computeTextSize(String text, EncodingTable encodingTable) berechnet die Anzahl an Bits, die für die Komprimierung des Textes nötig ist, korrekt.", "testComputeTextSize", JsonParameterSet.class)
        .criterion("Die Methode encodeText(String text, EncodingTable encodingTable) komprimiert den Text korrekt.", "testEncodeText", JsonParameterSet.class)
        .criterion("Die Methode compress() ist vollständig und korrekt.", "testCompress", JsonParameterSet.class)
        .build();

    /**
     * Defines the subtask H12.4.2 for task H12.2.
     */
    private static final Subtask H12_4_2 = Subtask.builder()
        .description("H12.4.2 | Huffman-Dekomprimierung")
        .testClassName("h12.H12_4_2_Tests")
        .criterion("Die Methode skipBits() überspringt die Füllbits korrekt.", false, "testSkipBits", JsonParameterSet.class)
        .criterion("Die Methode decodeCharacter(int startBit, EncodingTable encodingTable) dekomprimiert einen Zeichen korrekt.", false, "testDecodeCharacter", JsonParameterSet.class)
        .criterion("Die Methode decodeText(EncodingTable encodingTable) Dekomprimiert den Text korrekt.", false, "testDecodeText", JsonParameterSet.class)
        .criterion("Die Methode decompress() ist vollständig und korrekt.", false, "testDecompress", JsonParameterSet.class)
        .build();

    /**
     * Defines the task H12.4.
     */
    private static final Task H12_4 = Task.builder()
        .description("H12.4 | Die Kunst des Codes: Huffman-Coding II")
        .subtasks(H12_4_1, H12_4_2)
        .build();

    /**
     * Whether the private tests are being graded.
     */
    private final boolean publicTests;

    /**
     * Constructs a new rubric provider.
     *
     * @param publicTests whether the private tests are being graded
     */
    public H12_RubricProvider(boolean publicTests) {
        this.publicTests = publicTests;
    }

    /**
     * Constructs a new public rubric provider.
     */
    public H12_RubricProvider() {
        this(true);
    }

    @Override
    public Rubric getRubric() {
        return Rubric.builder()
            .title("H12 | Datenkomprimierung - %s Tests".formatted(publicTests ? "Public" : "Private"))
            .addChildCriteria(
                Stream.of(H12_1, H12_2, H12_3, H12_4)
                    .map(Task::getCriterion)
                    .toArray(Criterion[]::new)
            ).build();
    }
}
