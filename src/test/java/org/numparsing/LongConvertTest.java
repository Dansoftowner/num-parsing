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

    @ParameterizedTest
    @CsvFileSource(resources = {"integerNumbers.csv"})
    void shouldConvertNumbersToString(String numberAsString, int radix) {
        long number = Long.parseLong(numberAsString, radix);
        var underTest = new LongConvert();
        assertEquals(Long.toString(number, radix), underTest.toString(number, radix));
    }
}
