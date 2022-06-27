package org.numparsing;

import java.util.stream.IntStream;

public final class LongConvert extends Convert {

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
            throw new NumberFormatException(String.format("Invalid character found: '%s'", ch));
        return formalValue * placeValue;
    }
}
