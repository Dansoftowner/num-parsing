package org.numparsing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoubleConvertTest {
    @ParameterizedTest
    @CsvFileSource(resources = {"integerNumbers.csv"})
    void shouldParseWholeNumbersAppropriately(String numberString, int radix) {
        var underTest = new DoubleConvert();
        assertEquals((double) Long.parseLong(numberString, radix), underTest.parseDouble(numberString, radix));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"fractionsBase10.csv"})
    void shouldParseFractionsBase10Appropriately(String numberString) {
        var underTest = new DoubleConvert();
        double precision = numberString.substring(numberString.indexOf('.') + 1).length();
        double expected = (double) Math.round(Double.parseDouble(numberString) * precision) / precision;
        double actual = (double) Math.round(underTest.parseDouble(numberString) * precision) / precision;
        assertEquals(expected, actual);
    }
}
