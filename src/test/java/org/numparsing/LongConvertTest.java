package org.numparsing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongConvertTest {
    @ParameterizedTest
    @CsvFileSource(resources = {"integerNumbers.csv"})
    void shouldParseStringAppropriately(String numberString, int radix) {
        var underTest = new LongConvert();
        assertEquals(Long.parseLong(numberString, radix), underTest.parseLong(numberString, radix));
    }
}
