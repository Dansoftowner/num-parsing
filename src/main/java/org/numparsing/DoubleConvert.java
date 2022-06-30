package org.numparsing;

import java.util.stream.IntStream;

public final class DoubleConvert extends Convert {

    private static final char POINT = '.';

    private final LongConvert longConvert = new LongConvert();

    public String toString(double number, int radix) {
        long integerPart = (long)number;
        double fractionPart = Math.abs(number) - Math.abs(integerPart);
        while (fractionPart % 1 != 0) {
            fractionPart *= radix;
        }
        return longConvert.toString(integerPart, radix) + "." + longConvert.toString((long)fractionPart, radix);
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

        double fraction = IntStream.range(0, fractionPortion.length())
                .mapToDouble(i -> getRealValueAtPos(fractionPortion, i, radix))
                .sum();

        double signum = integer == 0 ? 1 : Math.signum((double)integer);
        return signum * (Math.abs(integer) + fraction);
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
