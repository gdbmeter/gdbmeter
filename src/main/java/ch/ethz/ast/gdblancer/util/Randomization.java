package ch.ethz.ast.gdblancer.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Randomization {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static <T> List<T> shuffleList(List<T> list) {
        Collections.shuffle(list, random);
        return list;
    }

    public static int nextInt(int lower, int upper) {
        return random.nextInt(lower, upper);
    }

    public static float nextFloat() {
        return random.nextFloat();
    }

    public static double getDouble() {
        if (smallBiasProbability()) {
            return Randomization.fromOptions(0.0, -0.0, Double.MAX_VALUE, -Double.MAX_VALUE);
        }

        return random.nextDouble();
    }

    public static int smallNumber() {
        return (int) (Math.abs(random.nextGaussian()) * 2);
    }

    public static boolean smallBiasProbability() {
        return random.nextInt(100) == 1;
    }

    public static boolean getBooleanWithRatherLowProbability() {
        return random.nextInt(10) == 1;
    }

    public static long getInteger() {
        if (smallBiasProbability()) {
            return Randomization.fromOptions(-1L, Long.MAX_VALUE, Long.MIN_VALUE, 1L, 0L);
        } else {
            return random.nextInt();
        }
    }

    public static long getPositiveInteger() {
        if (smallBiasProbability()) {
            return Randomization.fromOptions(0L, Long.MAX_VALUE, 1L);
        } else {
            return random.nextLong(0, Long.MAX_VALUE);
        }
    }

    public static long getPositiveInt() {
        if (smallBiasProbability()) {
            return Randomization.fromOptions(0, Integer.MAX_VALUE, 1);
        } else {
            return random.nextInt(0, Integer.MAX_VALUE);
        }
    }

    public static boolean getBoolean() {
        return random.nextBoolean();
    }

    @SafeVarargs
    public static <T> T fromOptions(T... options) {
        return options[nextInt(0, options.length)];
    }

    public static <T> T fromList(List<T> list) {
        return list.get((int) random.nextLong(0, list.size()));
    }

    public static <T> Set<T> nonEmptySubset(Set<T> set) {
        int nr = 1 + random.nextInt(0, set.size());
        return new HashSet<>(getNEntries(new ArrayList<>(set), nr));
    }

    private static <T> List<T> getNEntries(List<T> list, int amount) {
        List<T> selectedColumns = new ArrayList<>();
        List<T> remainingColumns = new ArrayList<>(list);

        for (int i = 0; i < amount; i++) {
            selectedColumns.add(remainingColumns.remove(random.nextInt(0, remainingColumns.size())));
        }

        return selectedColumns;
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
