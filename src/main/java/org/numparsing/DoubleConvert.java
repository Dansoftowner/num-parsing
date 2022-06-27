package org.numparsing;

import java.util.stream.IntStream;

public final class DoubleConvert extends Convert {

    private static final char POINT = '.';

    private final LongConvert longConvert;

    public DoubleConvert() {
        longConvert = new LongConvert();
    }

    public double parseDouble(String numberString) {
        return parseDouble(numberString, 10);
    }

    public double parseDouble(String numberString, int radix) {
        if (hasInvalidFormat(numberString))
            throw new NumberFormatException("Multiple points found!");

        String integerPortion = getIntegerPortion(numberString);
        String fractionPortion = getFractionPortion(numberString);

        long integer = longConvert.parseLong(integerPortion, radix);
        if (fractionPortion.isEmpty()) {
            if (isFraction(numberString))
                throw new NumberFormatException(); // TODO: message
            return integer;
        }

        double fraction = IntStream.range(0, numberString.length())
                .mapToDouble(i -> getRealValueAtPos(numberString, i, radix))
                .sum();

        return Math.signum(integer) * (Math.abs(integer) + fraction);
    }

    public double getRealValueAtPos(String numberString, int index, int radix) {
        char ch = numberString.charAt(index);
        int pos = -(index + 1);

        double placeValue = Math.pow(radix, pos);
        int formalValue = getNumericValue(ch, radix);
        if (formalValue == -1)
            throw new NumberFormatException(String.format("Invalid character found: '%s'", ch));
        return formalValue * placeValue;
    }

    private String getIntegerPortion(String numberString) {
        int pointIndex = numberString.indexOf(POINT);
        if (pointIndex < 0) return numberString;

        String intPortion = numberString.substring(0, pointIndex);
        return intPortion.isEmpty() ? "0" : intPortion;
    }

    private String getFractionPortion(String numberString) {
        int pointIndex = numberString.indexOf(POINT);
        if (pointIndex < 0) return "";
        return numberString.substring(pointIndex + 1);
    }

    private boolean isFraction(String numberString) {
        return numberString.contains(Character.toString(POINT));
    }

    private boolean hasInvalidFormat(String numberString) {
        return numberString.chars().filter(it -> (char) it == POINT).count() > 1;
    }
}
