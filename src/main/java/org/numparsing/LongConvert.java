package org.numparsing;

import java.util.stream.IntStream;

public final class LongConvert extends Convert {

    public String toString(long number, int radix) {
        var stringBuilder = new StringBuilder();
        long n = Math.abs(number);
        while (n != 0) {
            stringBuilder.insert(0, getCharForNumber((int) (n % radix), radix, false));
            n /= radix;
        }
        // in case of 0:
        if (stringBuilder.isEmpty()) stringBuilder.append(getCharForNumber((int) n, radix, false));
        stringBuilder.insert(0, number < 0 ? "-" : "");
        return stringBuilder.toString();
    }

    public long parseLong(String numberString) {
        return parseLong(numberString, 10);
    }

    public long parseLong(String numberString, int radix) {
        if (hasSign(numberString))
            return getSignValue(numberString) * parseLong(removeSigns(numberString), radix);
        return IntStream.range(0, numberString.length())
                .mapToLong(i -> getRealValueAtPos(numberString, i, radix))
                .sum();
    }

    public long getRealValueAtPos(String numberString, int index, int radix) {
        char ch = numberString.charAt(index);
        int pos = numberString.length() - 1 - index;

        long placeValue = (long) Math.pow(radix, pos);
        int formalValue = getNumericValue(ch, radix);
        if (formalValue == -1)
            throw new NumberFormatException("Invalid character found: '%s'".formatted(ch));
        return formalValue * placeValue;
    }
}
