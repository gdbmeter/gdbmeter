package ch.ethz.ast.gdbmeter.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class Randomization {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static <T> void shuffleList(List<T> list) {
        Collections.shuffle(list, random);
    }

    public static int nextInt(int lower, int upper) {
        return random.nextInt(lower, upper);
    }

    public static int nextInt() {
        return random.nextInt();
    }

    public static long nextLong() {
        return random.nextLong();
    }

    public static short getShort() {
        if (smallBiasProbability()) {
            return Randomization.fromOptions((short) 0, Short.MAX_VALUE, Short.MIN_VALUE);
        }

        return (short) random.nextInt(Short.MAX_VALUE + 1);
    }

    public static byte getByte() {
        if (smallBiasProbability()) {
            return Randomization.fromOptions((byte) 0, Byte.MAX_VALUE, Byte.MIN_VALUE);
        }

        return (byte) random.nextInt(Byte.MAX_VALUE + 1);
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

    public static double nextDouble(double lower, double upper) {
        return random.nextDouble(lower, upper);
    }

    public static int smallNumber() {
        return (int) (Math.abs(random.nextGaussian()) * 2);
    }

    public static boolean smallBiasProbability() {
        return random.nextInt(100) == 1;
    }

    public static long getInteger() {
        if (smallBiasProbability()) {
            return Randomization.fromOptions(-1L, Long.MAX_VALUE, Long.MIN_VALUE, 1L, 0L);
        } else {
            return random.nextLong();
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

    public static <T> T fromSet(Set<T> set) {
        return new ArrayList<>(set).get((int) random.nextLong(0, set.size()));
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

    public static String getStringOfAlphabet(String alphabet) {
        int chars;

        if (Randomization.getBoolean()) {
            chars = Randomization.smallNumber();
        } else {
            chars = random.nextInt(0, 10);
        }

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
        return getStringOfAlphabet("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#<>/.,~-+'*()[]{} ^*?%_\t\n\r|&\\");
    }
    
    public static <E> E generateUniqueElement(Set<E> elements, Supplier<E> generator) {
        E element;

        do {
            element = generator.get();
        } while (elements.contains(element));

        return element;
    }

}
