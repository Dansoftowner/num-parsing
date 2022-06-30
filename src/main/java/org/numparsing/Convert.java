package org.numparsing;

import java.util.List;

public abstract sealed class Convert permits LongConvert, DoubleConvert {

    private static final int MAX_RADIX = 36;

    private static final char POSITIVE_SIGN = '+';
    private static final char NEGATIVE_SIGN = '-';

    private static final List<String> SIGNS = List.of(
            Character.toString(POSITIVE_SIGN),
            Character.toString(NEGATIVE_SIGN)
    );

    private void requireAppropriateRadix(int radix) {
        if (radix > MAX_RADIX)
            throw new IllegalArgumentException("Radix '%d' is too high!".formatted(radix));
    }

    protected char getCharForNumber(int num, int radix, boolean preferUppercase) {
        requireAppropriateRadix(radix);
        if (num < 0) throw new IllegalArgumentException();
        if (num >= radix) throw new IllegalArgumentException();

        if (num < 10)
            return (char) (48 + num);
        else {
            int firstLetterCode = preferUppercase ? 65 : 97;
            return (char) (firstLetterCode + num - 10);
        }
    }

    protected int getNumericValue(char ch, int radix) {
        requireAppropriateRadix(radix);

        int val = -1;
        if (isDigit(ch))
            val = (int) ch - 48; // the first number's code in the ASCII table is 48
        if (isLetter(ch))
            if (isUppercaseLetter(ch))
                val = 10 + ((int) ch - 65); // the first uppercase letter's code in ASCII is 65 ('A')
            else
                val = 10 + ((int) ch - 97); // the first uppercase letter's code in ASCII is 97 ('a')

        return val < radix ? val : -1;
    }

    private boolean isDigit(char ch) {
        // Number range in ASCII: 48-57
        return (int) ch > 47 && (int) ch < 58;
    }

    private boolean isLetter(char ch) {
        // Letter ranges in ASCII: A-Z --> 65-90, a-z --> 97-122
        return isUppercaseLetter(ch) || isLowercaseLetter(ch);
    }

    private boolean isUppercaseLetter(char ch) {
        return (int) ch > 64 && (int) ch < 91;
    }

    private boolean isLowercaseLetter(char ch) {
        return (int) ch > 96 && (int) ch < 123;
    }

    protected int getSignValue(String numberString) {
        Integer negativeSignCount = getSignCounts(numberString).second();
        if (isEven(negativeSignCount))
            return 1;
        return -1;
    }

    protected String removeSigns(String numberString) {
        var counts = getSignCounts(numberString);
        int signCount = counts.first() + counts.second();
        return numberString.substring(signCount);
    }

    private Pair<Integer, Integer> getSignCounts(String numberString) {
        int positiveSignCount = 0;
        int negativeSignCount = 0;
        for (char ch : numberString.toCharArray()) {
            if (!isSign(ch))
                break;
            positiveSignCount += (ch == POSITIVE_SIGN) ? 1 : 0;
            negativeSignCount += (ch == NEGATIVE_SIGN) ? 1 : 0;
        }
        return new Pair<>(positiveSignCount, negativeSignCount);
    }

    private boolean isEven(int num) {
        return num % 2 == 0;
    }

    protected boolean hasSign(String numberString) {
        return SIGNS.stream().anyMatch(numberString::startsWith);
    }

    private boolean isSign(char ch) {
        return NEGATIVE_SIGN == ch || POSITIVE_SIGN == ch;
    }
}
