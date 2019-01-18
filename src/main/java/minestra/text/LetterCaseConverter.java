package minestra.text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An utility class on letter-case conversion.
 * @since 1.1
 */
public final class LetterCaseConverter {

    private LetterCaseConverter() {
    }

    /**
     * Splits a string by words.
     * Some conversions in this class depend on this method.
     * @param s a string
     * @return the string array splitted by words
     */
    public static String[] splitWords(String s) {
        if (s == null) {
            return null;
        }
        if (s.contains("_") || s.contains("-")) {
            return s.split("_|-");
        }
        else {
            return splitWordsAsCamelCase(s); // assume camel case
        }
    }

    private static String[] splitWordsAsCamelCase(String s) {
        assert s != null;
        if (s.isEmpty()) {
            return new String[0];
        }
        if (s.length() == 1) {
            return new String[] { s };
        }
        List<String> a = new ArrayList<>();
        int p = 0;
        char[] c = s.toCharArray();
        int t1 = Character.getType(c[0]);
        for (int i = 1, n = c.length; i < n; i++) {
            final int t2 = Character.getType(c[i]);
            if (t2 != t1 && !(t1 == Character.UPPERCASE_LETTER && t2 == Character.LOWERCASE_LETTER)) {
                a.add(s.substring(p, i));
                p = i;
            }
            t1 = t2;
        }
        a.add(s.substring(p));
        return a.toArray(new String[a.size()]);
    }

    /**
     * Converts a string to capital case.
     * @param s a string
     * @return the string converted to capital case
     */
    public static String capitalize(String s) {
        if (s.length() <= 1) {
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Converts a string to uncapital case.
     * @param s a string
     * @return the string converted to uncapital case
     */
    public static String uncapitalize(String s) {
        if (s.length() <= 1) {
            return s.toLowerCase();
        }
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * Converts a string to camel case.
     * @param s a string
     * @return the string converted to camel case
     */
    public static String toCamelCase(String s) {
        return uncapitalize(toPascalCase(s));
    }

    /**
     * Converts a string to pascal case.
     * @param s a string
     * @return the string converted to pascal case
     */
    public static String toPascalCase(String s) {
        return Stream.of(splitWords(s)).map(x -> capitalize(x.toLowerCase())).collect(Collectors.joining());
    }

    /**
     * Converts a string to snake case.
     * @param s a string
     * @return the string converted to snake case
     */
    public static String toSnakeCase(String s) {
        return String.join("_", splitWords(s)).toLowerCase();
    }

    /**
     * Converts a string to chain case.
     * @param s a string
     * @return the string converted to chain case
     */
    public static String toChainCase(String s) {
        return String.join("-", splitWords(s)).toLowerCase();
    }

}
