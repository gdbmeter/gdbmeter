package ch.ethz.ast.gdblancer.util;

import java.util.concurrent.ThreadLocalRandom;

public class Randomization {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static int nextInt(int lower, int upper) {
        return random.nextInt(lower, upper);
    }

    public static float nextFloat() {
        return random.nextFloat();
    }

    public static int smallNumber() {
        return (int) (Math.abs(random.nextGaussian()) * 2);
    }

    private static boolean smallBiasProbability() {
        return random.nextInt(100) == 1;
    }

    public static long getInteger() {
        if (smallBiasProbability()) {
            return Randomization.fromOptions(-1L, Long.MAX_VALUE, Long.MIN_VALUE, 1L, 0L);
        } else {
            return random.nextInt();
        }
    }

    public static boolean getBoolean() {
        return random.nextBoolean();
    }

    @SafeVarargs
    public static <T> T fromOptions(T... options) {
        return options[nextInt(0, options.length)];
    }

    private static final String ALPHANUMERIC_SPECIALCHAR_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#<>/.,~-+'*()[]{} ^*?%_\t\n\r|&\\";
    private static final String ALPHANUMERIC_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC_ALPHABET = "0123456789";

    private static int getStringLength() {
        int chars;

        if (Randomization.getBoolean()) {
            chars = Randomization.smallNumber();
        } else {
            chars = random.nextInt(0, 10);
        }

        return chars;
    }

    public static String getStringOfAlphabet(String alphabet) {
        int chars = getStringLength();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < chars; i++) {
            sb.append(getCharacterFromAlphabet(alphabet));
        }

        return sb.toString();
    }

    public static char getCharacterFromAlphabet(String alphabet) {
        return alphabet.charAt(nextInt(0, alphabet.length()));
    }

    public static String getString() {
        return getStringOfAlphabet(ALPHANUMERIC_SPECIALCHAR_ALPHABET);
    }

}
