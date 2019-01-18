package minestra.text;

import static org.junit.Assert.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import minestra.collection.ImmArray;

public final class LetterCaseConverterTest {

    private static final String[] commonSamples = { "", "camelCase", "ABC", "As", "A", "a", "world-wide-web", "under_A",
            "Abc0xyZ", "CaseConv#toString", "hello(str1, strX)", };

    private AnotherConverter o = new AnotherConverter();

    @Test
    public void testSplitWords() {
        assertNull(LetterCaseConverter.splitWords(null));
    }

    @Test
    public void testCapitalize() {
        String[] samples = { "toString", };
        for (String s : getSamples(samples)) {
            assertEquals(o.toCapitalCase(s), LetterCaseConverter.capitalize(s));
        }
    }

    @Test
    public void testUncapitalize() {
        String[] samples = { "AAA", };
        for (String s : getSamples(samples)) {
            assertEquals(StringUtils.uncapitalize(s), LetterCaseConverter.uncapitalize(s));
        }
    }

    @Test
    public void testToCamelCase() {
        String[] samples = { "toString", };
        for (String s : getSamples(samples)) {
            assertEquals(o.toCamelCase(s), LetterCaseConverter.toCamelCase(s));
        }
    }

    @Test
    public void testToPascalCase() {
        String[] samples = { "CaseConverter", };
        for (String s : getSamples(samples)) {
            assertEquals(o.toPascalCase(s), LetterCaseConverter.toPascalCase(s));
        }
    }

    @Test
    public void testToSnakeCase() {
        String[] samples = { "get_value", };
        for (String s : getSamples(samples)) {
            assertEquals(o.toSnakeCase(s), LetterCaseConverter.toSnakeCase(s));
        }
    }

    @Test
    public void testToChainCase() {
        String[] samples = { "-X", };
        for (String s : getSamples(samples)) {
            assertEquals(o.toChainCase(s), LetterCaseConverter.toChainCase(s));
        }
    }

    private static String[] getSamples(String... samples) {
        Stream<String> concat = Stream.concat(Stream.of(samples), Stream.of(commonSamples));
        return ImmArray.of(concat).toStringArray();
    }

    static final class AnotherConverter implements ConverterSupports {
        //
    }

    interface ConverterSupports {

        default String[] splitWords(String s) {
            if (s.contains("_") || s.contains("-")) {
                return s.split("_|-");
            }
            else {
                // assume camel case
                return StringUtils.splitByCharacterTypeCamelCase(s);
            }
        }

        default String toCapitalCase(String s) {
            return StringUtils.capitalize(s);
        }

        default String toCamelCase(String s) {
            return StringUtils.uncapitalize(toPascalCase(s));
        }

        default String toPascalCase(String s) {
            return Stream.of(splitWords(s)).map(String::toLowerCase).map(this::toCapitalCase)
                    .collect(Collectors.joining());
        }

        default String toSnakeCase(String s) {
            return String.join("_", splitWords(s)).toLowerCase();
        }

        default String toChainCase(String s) {
            return String.join("-", splitWords(s)).toLowerCase();
        }
    }

}
