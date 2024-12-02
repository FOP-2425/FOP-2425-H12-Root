package h12;

import h12.assertions.TestConstants;
import h12.io.BufferedBitOutputStream;
import h12.io.compression.rle.BitRunningLengthDecompressor;
import h12.rubric.H12_Tests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;

/**
 * Defines the private tests for H12.2.2.
 *
 * @author Nhan Huynh
 */

@TestForSubmission
@DisplayName("H12.2.2 | BitRunningLengthDecompressor")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H12_2_2_TestsPrivate extends H12_Tests {

    @Override
    public Class<?> getClassType() {
        return BitRunningLengthDecompressor.class;
    }
}
