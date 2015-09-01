package minestra.collection;

import static minestra.collection.TestUtils.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.Test;

public final class ImmArrayTest {

    @SafeVarargs
    static <T> ImmArray<T> arr(T... a) {
        return ImmArray.of(a);
    }

    static <T> ImmArray<T> arr(List<T> collection) {
        return ImmArray.of(collection);
    }

    static <T> ImmArray<T> arr(Stream<T> stream) {
        return ImmArray.of(stream);
    }

    @Test
    public void testAt() {
        ImmArray<String> arr = arr("Java", "Scala", "Perl", "Ruby", "Python");
        assertEquals("Scala", arr.at(1));
        assertEquals("Ruby", arr.at(3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConcat() {
        StringBuilder sb = new StringBuilder("X");
        assertEquals(arr("a", "b", "c", "d", "e"), arr("a", "b").concat(arr("c"), arr("d", "e")));
        assertEquals(arr(sb, "b", "c", "d", "e"), arr(sb, "b").concat(arr("c"), arr("d", "e")));
        // Stream.concat(a, b);
    }

    @Test
    public void testContains() {
        assertTrue(arr("java", "scala", "perl", "ruby", "python").contains("perl"));
        assertFalse(arr("java", "scala", "perl", "ruby", "python").contains("haskell"));
        assertFalse(arr().contains("java"));
    }

    @Test
    public void testDistinct() {
        String[] a = oarr("java", "scala", "perl", "java", "python");
        assertEquals(arr("java", "scala", "perl", "python"), arr(a).distinct());
    }

    @Test
    public void testDrop() {
        assertEquals(arr("perl", "ruby", "python"), arr("java", "scala", "perl", "ruby", "python").drop(2));
        assertEquals(arr(), arr("java", "scala", "perl", "ruby", "python").drop(8));
        assertEquals(arr(), arr().drop(1));
    }

    @Test
    public void testDropWhile() {
        assertEquals(arr("aba", "abb"), arr("aaa", "aab", "aac", "aba", "abb").dropWhile(x -> x.startsWith("aa")));
        assertEquals(arr("aaa1", "aab2", "aac3"), arr("aaa1", "aab2", "aac3").dropWhile(x -> x.startsWith("xx")));
        assertEquals(arr(), arr("aaa1", "aab2", "aac3").dropWhile(x -> x.startsWith("a")));
    }

    @Test
    public void testExists() {
        assertTrue(arr("java", "scala", "perl", "ruby", "python").exists(x -> x.length() == 6));
        assertFalse(arr("java", "scala", "perl", "ruby", "python").exists(x -> x.length() == 7));
        assertFalse(arr("").tail().exists(x -> x.isEmpty()));
    }

    @Test
    public void testFilter() {
        ImmArray<String> arr = arr("java", "scala", "perl", "ruby", "python");
        assertEquals(arr("java", "perl", "ruby"), arr.filter(x -> x.length() == 4));
    }

    @Test
    public void testFind() {
        ImmArray<String> arr = arr("java", "scala", "perl", "ruby", "python");
        assertEquals(Optional.of("scala"), arr.find(x -> x.length() == 5));
        assertEquals(Optional.of("python"), arr.find(x -> x.length() > 4, 2));
        assertEquals(Optional.empty(), arr.find(x -> x.endsWith("a"), 2));
    }

    @Test
    public void testFold() {
        assertEquals("FACE", arr("A", "C", "E").fold("F", (x, y) -> x + y));
        assertEquals("FA", arr("A").fold("F", (x, y) -> x + y));
        assertEquals("F", arr("").tail().fold("F", (x, y) -> x + y));
    }

    @Test
    public void testForEach() {
        List<String> a = new ArrayList<>();
        ImmArray<String> arr = arr("Java", "Scala", "Perl", "Ruby", "Python");
        arr.forEach(x -> {
            a.add(x.toUpperCase());
            a.add(x.toLowerCase());
        });
        arr.forEach(x -> {
            a.add(x.toLowerCase());
            a.add(x.toUpperCase());
        });
        assertEquals(
            arr("JAVA", "java", "SCALA", "scala", "PERL", "perl", "RUBY", "ruby", "PYTHON", "python", "java", "JAVA",
                "scala", "SCALA", "perl", "PERL", "ruby", "RUBY", "python", "PYTHON"), ImmArray.of(a));
    }

    @Test
    public void testHead() {
        assertEquals("java", arr("java", "scala", "perl", "ruby", "python").head());
        assertEquals("scala", arr("scala", "perl", "ruby", "python").head());
        try {
            arr().head();
            fail();
        } catch (NoSuchElementException e) {
            assertEquals("head", e.getMessage());
        }
    }

    @Test
    public void testIndexOf() {
        assertEquals(2, arr("java", "scala", "perl", "ruby", "python").indexOf("perl"));
        assertEquals(-1, arr("java", "scala", "perl", "ruby", "python").indexOf("haskell"));
        assertEquals(-1, arr().indexOf("java"));
    }

    @Test
    public void testIndexWhere() {
        assertEquals(4, arr("java", "scala", "perl", "ruby", "python").indexWhere(x -> x.length() == 6));
        assertEquals(-1, arr("java", "scala", "perl", "ruby", "python").indexWhere(x -> x.length() == 7));
        assertEquals(-1, arr("").tail().indexWhere(x -> x.isEmpty()));
    }

    @Test
    public void testLast() throws Exception {
        assertEquals("python", arr("java", "scala", "perl", "ruby", "python").last());
        assertEquals("0X0", arr("0X0").last());
        ImmArray<String> a = arr("Z*9");
        assertEquals(a.head(), a.last());
        try {
            arr().last();
            fail();
        } catch (NoSuchElementException e) {
            assertEquals("last", e.getMessage());
        }
    }

    @Test
    public void testMap() {
        assertEquals(arr("JAVA", "SCALA", "PERL"), arr(oarr("java", "scala", "perl")).map(String::toUpperCase));
    }

    @Test
    public void testMapToDouble() {
        assertEquals(DoubleImmArray.of(100.1, 2.3, 33.5), arr(oarr("100.1", "2.3", "33.5")).mapToDouble(Double::parseDouble));
    }

    @Test
    public void testMapToInt() {
        assertEquals(IntImmArray.of(100, 2, 33), arr(oarr("100", "2", "33")).mapToInt(Integer::parseInt));
    }

    @Test
    public void testMapToLong() {
        assertEquals(LongImmArray.of(100L, 2L, 33L), arr(oarr("100", "2", "33")).mapToLong(Long::parseLong));
    }

    @Test
    public void testOfCollectionOfE() {
        assertEquals(arr("A", "B"), arr(Arrays.asList("A", "B")));
    }

    @Test
    public void testOfEArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfStreamOfE() {
        assertEquals(arr("A", "B"), arr(Stream.of("A", "B")));
    }

    @Test
    public void testReduce() {
        assertEquals("ACE", ImmArray.of("A", "C", "E").reduce((x, y) -> x + y).get());
        assertEquals("FACE", ImmArray.of("A", "C", "E").reduce("F", (x, y) -> x + y));
        assertEquals("c(b(a))", ImmArray.of("a", "b", "c").reduce((x, y) -> String.format("%s(%s)", y, x)).get());
        assertEquals("c(b(a(x)))", ImmArray.of("a", "b", "c").reduce("x", (x, y) -> String.format("%s(%s)", y, x)));
        assertEquals(Optional.<String> empty(), ImmArray.of("").tail().reduce((x, y) -> x + y));
        assertEquals("X", ImmArray.of("").tail().reduce("X", (x, y) -> x + y));
    }

    @Test
    public void testReverse() {
        String[] arg = oarr("java", "scala", "perl", "ruby", "python");
        List<String> a = new ArrayList<>();
        Collections.addAll(a, arg);
        Collections.reverse(a);
        assertEquals(ImmArray.of(a), ImmArray.of(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfE() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqEArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqStreamOfE() {
        String[] a = oarr("java", "scala", "perl", "java", "python");
        assertEquals(ImmArray.of(a), ImmArray.of(Stream.of(a)));
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSlice() {
        assertEquals(ImmArray.of("scala", "perl", "ruby"), arr("java", "scala", "perl", "ruby", "python").slice(1, 3));
    }

    @Test
    public void testSort() {
        String[] a = oarr("java", "scala", "perl", "ruby", "python");
        String[] expected = Arrays.copyOf(a, a.length);
        Arrays.sort(expected);
        assertEquals(ImmArray.of(expected), arr(a).sort());
    }

    @Test
    public void testSortWith() {
        // fail("Not yet implemented");
    }

    @Test
    public void testStream() {
        assertEquals("@@a1@@bb@@ccc", ImmArray.of("a1", "bb", "ccc").stream().reduce("", (x, y) -> x + "@@" + y));
    }

    @Test
    public void testTail() {
        assertEquals(ImmArray.of("scala", "perl", "ruby"), ImmArray.of("java", "scala", "perl", "ruby").tail());
        assertEquals(ImmArray.of("perl", "ruby"), ImmArray.of("java", "scala", "perl", "ruby").tail().tail());
        assertEquals(ImmArray.of(), ImmArray.of("scala").tail().tail());
    }

    @Test
    public void testTake() {
        assertEquals(ImmArray.of("java", "scala", "perl"), ImmArray.of("java", "scala", "perl", "ruby").take(3));
        assertEquals(ImmArray.of(), ImmArray.of("scala").take(0));
    }

    @Test
    public void testTakeWhile() {
        ImmArray<String> i1 = ImmArray.of("java", "scala", "perl", "ruby");
        assertEquals(ImmArray.of("java", "scala"), i1.takeWhile(x -> !x.equals("perl")));
        assertEquals(ImmArray.of("java"), i1.takeWhile(x -> !x.equals("scala")));
        assertEquals(ImmArray.of(), i1.takeWhile(x -> x.equals("perl")));
    }

    @Test
    public void testToArrayIntFunctionOfE() {
        assertArrayEquals(oarr("11", "22"), arr("11", "22").toArray(String[]::new));
    }

    @Test
    public void testToList() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToMapWithKey() {
        assertEquals("{python=6, java=4, scala=5, perl=4, ruby=4}", //
            ImmArray.of("java", "scala", "perl", "ruby", "python").toMapWithKey(String::length).toString());
        assertEquals("{1=1, 2=2, 3=3}", arr("1", "2", "3").toMapWithKey(String::toUpperCase).toString());
    }

    @Test
    public void testToMapWithValue() {
        assertEquals("{JAVA=java, SCALA=scala, PERL=perl, RUBY=ruby, PYTHON=python}",
            ImmArray.of("java", "scala", "perl", "ruby", "python").toMapWithValue(String::toUpperCase).toString());
        assertEquals("{1=1, 2=2, 3=3}", arr("1", "2", "3").toMapWithValue(String::toUpperCase).toString());
    }

    @Test
    public void testToSet() {
        assertEquals(new HashSet<>(Arrays.asList("11", "22")), arr("11", "22").toSet());
    }

    @Test
    public void testToStringArray() {
        assertArrayEquals(oarr("11", "22"), arr("11", "22").toStringArray());
    }

}
