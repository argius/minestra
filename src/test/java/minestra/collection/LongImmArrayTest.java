package minestra.collection;

import static minestra.collection.LongImmArray.*;
import static minestra.collection.TestUtils.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongFunction;
import java.util.stream.LongStream;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public final class LongImmArrayTest {

    static LongImmArray arr(long... a) {
        return of(a);
    }

    static LongImmArray arr(Collection<Long> collection) {
        return ImmArray.of(collection).mapToLong(Long::intValue);
    }

    static LongImmArray arr(LongStream stream) {
        return of(stream.toArray());
    }

    @Test
    public void testAverage() {
        assertEquals(20.1428571428571d, arr(20L, 3L, 42L, 47L, 15L, -9L, 23L).average(), 0.00001d);
    }

    @Test
    public void testBoxed() {
        LongFunction<Long> f = Long::valueOf;
        assertEquals(ImmArray.of(f.apply(41L), f.apply(52L), f.apply(43L)), arr(41L, 52L, 43L).boxed());
        assertEquals(ImmArray.of(f.apply(345L)), arr(345L).boxed());
    }

    @Test
    public void testConcat() {
        assertEquals(arr(8L, 3, 2, 12, -3, 6, 18), arr(8L, 3, 2).concat(arr(12L), arr(-3L, 6, 18)));
    }

    @Test
    public void testContains() {
        assertTrue(arr(39L, 33L, 29L, 24L, 32L, 48L, 27L, 30L, 38L).contains(33L));
        assertFalse(arr(9L, 47L, -1L, -8L, 45L, 34L, 13L, -2L, 45L).contains(46L));
    }

    @Test
    public void testDistinct() {
        assertEquals(arr(8, 3, 2, 12, 18), arr(8, 3, 2, 12, 3, 8, 18).distinct());
    }

    @Test
    public void testDrop() {
        assertEquals(arr(15L, -9L, 23L), arr(20L, 3L, 42L, 47L, 15L, -9L, 23L).drop(4));
        assertEquals(arr(7L, 15L, -9L, 23L), arr(7L, 15L, -9L, 23L).drop(0));
        assertEquals(arr(), arr(7L, 15L, -9L, 23L).drop(8));
        assertEquals(arr(), arr().drop(1));
    }

    @Test
    public void testDropWhile() {
        assertEquals(arr(-9L, 23L), arr(20L, 3L, 42L, 47L, 15L, -9L, 23L).dropWhile(x -> x >= 3L));
        assertEquals(arr(7L, 15L, -9L, 23L), arr(7L, 15L, -9L, 23L).dropWhile(x -> x < -10L));
        assertEquals(arr(), arr(7L, 15L, -9L, 23L).dropWhile(x -> x > -10L));
    }

    @Test
    public void testEmpty() {
        assertEquals(arr(), LongImmArray.empty());
    }

    @Test
    public void testExists() {
        assertTrue(arr(27L, 31L, 10L, 0L, 11L, 41L, 34L, -4L, 0L).exists(x -> x == 10));
        assertFalse(arr(16L, 8L, -2L, 5L, 36L, -8L, 39L, 5L, 38L).exists(x -> x == 1));
    }

    @Test
    public void testFilter() {
        assertEquals(arr(6L), arr(1, 6, 2).filter(x -> x > 3));
    }

    @Test
    public void testFind() {
        assertEquals(OptionalLong.of(7L), arr(-9L, 7L, -3L, 18L, 3L, 19L, 12L).find(x -> x > 1L));
        assertEquals(OptionalLong.of(-6L), arr(8L, -7L, 0L, 3L, 13L, -6L, -19L).find(x -> x % 2 == 0, 3));
        assertEquals(OptionalLong.empty(), arr(-15L, -11L, 17L, -19L, -13L, 5L, 7L).find(x -> x % 2 == 0, 3));
    }

    @Test
    public void testFold() {
        assertEquals(12L, arr(1, 6, 2).fold(3, (x, y) -> x + y));
        assertEquals(3L, arr().fold(3, (x, y) -> x + y));
    }

    @Test
    public void testForEach() {
        StringBuilder sb = new StringBuilder();
        arr(3L, 5, 8).forEach(x -> {
            sb.append("--").append(x);
        });
        assertEquals("--3--5--8", sb.toString());
    }

    @Test
    public void testGenerate() {
        assertEquals(arr(7L, 7, 7), generate(3, () -> 7L));
        AtomicLong along = new AtomicLong(-5L);
        assertEquals(arr(-5L, -4, -3, -2, -1), generate(5, () -> along.getAndAdd(1)));
    }

    @Test
    public void testHead() {
        assertEquals(OptionalLong.of(4), arr(4, 23, 33, 1, 5, 19).head());
        assertEquals(OptionalLong.of(23), arr(23, 33, 1, 5, 19).head());
        assertEquals(OptionalLong.empty(), arr().head());
    }

    @Test
    public void testIndexOf() {
        assertEquals(5, arr(14L, -9L, 10L, 26L, -4L, 30L, 0L, 24L, 5L).indexOf(30L));
        assertEquals(-1, arr(16L, 2L, 48L, 51L, 5L, -7L, 5L, 50L, -6L).indexOf(3));
    }

    @Test
    public void testIndexWhere() {
        assertEquals(1, arr(15L, -6L, -8L, -5L, 8L, -9L, 36L, 4L, 24L).indexWhere(x -> x < 0L));
        assertEquals(-1, arr(10L, 15L, 39L, 23L, 27L, 33L, 23L, -3L, -2L).indexWhere(x -> x < -3L));
    }

    @Test
    public void testMap() {
        assertEquals(arr(26, 37, 4, 8, 22), arr(23, 34, 1, 5, 19).map(x -> x + 3));
    }

    @Test
    public void testMapToDouble() {
        assertArrayEquals(darr(2.857d, 3.143d, 0.714d, 0.143d, 0.143d, 1.429d), //
            arr(20L, 22L, 5L, 1L, 1L, 10L).mapToDouble(x -> x / 7d).toArray(), 0.001d);
    }

    @Test
    public void testMapToInt() {
        assertArrayEquals(iarr(36, -6, 42, -24, 60, -6), //
            arr(12L, -2L, 14L, -8L, 20L, -2L).mapToInt(x -> (int) (x * 3)).toArray());
    }

    @Test
    public void testMapToObj() {
        assertEquals(ImmArray.of("AB3CD", "AB5CD", "AB7CD"), arr(3, 5, 7).mapToObj(x -> "AB" + x + "CD"));
    }

    @Test
    public void testOfCollectionOfLong() {
        assertEquals(arr(44L, -9L, 18L, 39L, 51L, 19L), of(Arrays.asList(44L, -9L, 18L, 39L, 51L, 19L)));
    }

    @Test
    public void testOfLongArray() {
        assertEquals(arr(1, 2, 3), of(1, 2, 3));
    }

    @Test
    public void testOfLongStream() {
        assertEquals(arr(48L, -5L, 35L, 43L, -2L, 7L), of(LongStream.of(48L, -5L, 35L, 43L, -2L, 7L)));
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 1000; i++) {
            final int size = 13;
            final long min = -8L;
            final long max = 12L;
            LongImmArray arr = random(size, min, max);
            assertEquals(size, arr.size());
            assertEquals(0, arr.filter(x -> x < min || x > max).size());
        }
    }

    @Test
    public void testReduce() {
        assertEquals(105L, arr(3, 5, 7).reduce((x, y) -> x * y).getAsLong());
        assertEquals(420L, arr(3, 5, 7).reduce(4L, (x, y) -> x * y));
        assertEquals(OptionalLong.empty(), arr(0L).tail().reduce((x, y) -> x * y));
        assertEquals(4L, arr().reduce(4L, (x, y) -> x * y));
    }

    @Test
    public final void testReverse() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        long[] expected0 = Arrays.copyOf(arg, arg.length);
        ArrayUtils.reverse(expected0);
        LongImmArray expected = arr(expected0);
        assertEquals(expected, arr(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfLong() {
        assertEquals(of(38L, 47L, 8L, 33L, 35L, 24L), arr(Arrays.asList(38L, 47L, 8L, 33L, 35L, 24L)));
    }

    @Test
    public void testSeqLongArray() {
        assertEquals(of(20L, 51L, 0L, 3L, 50L, -6L), arr(20L, 51L, 0L, 3L, 50L, -6L));
    }

    @Test
    public void testSeqLongStream() {
        assertEquals(of(-11L, 31L, 5L, 9L, -2L, 40L), arr(LongStream.of(-11L, 31L, 5L, 9L, -2L, 40L)));
    }

    @Test
    public void testSlice() {
        assertEquals(arr(1, 6, 2), arr(1, 6, 2).slice(0, 2));
        assertEquals(arr(1, 6, 2), arr(1, 6, 2).slice(0, 3));
        assertEquals(arr(6, 2), arr(1, 6, 2).slice(1, 2));
        assertEquals(arr(1, 6), arr(1, 6, 2).slice(0, 1));
        assertEquals(arr(), arr(1, 6, 2).slice(3, 4));
    }

    @Test
    public final void testSort() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        long[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(arr(expected), arr(arg).sort());
    }

    @Test
    public void testStream() {
        final LongImmArray a = arr(2, 3, 4);
        try (LongStream st = a.stream()) {
            assertEquals(OptionalLong.of(24), st.reduce((x, y) -> x * y));
        }
    }

    @Test
    public void testTail() {
        assertEquals(arr(34, 1, 5, 19), arr(23, 34, 1, 5, 19).tail());
    }

    @Test
    public void testTake() {
        assertEquals(arr(23L, 34, 1), arr(23L, 34, 1, 5, 19).take(3));
        assertEquals(arr(), arr(23L, 34, 1, 5, 19).take(0));
    }

    @Test
    public void testTakeWhile() {
        assertEquals(arr(23L, 34), arr(23L, 34, 1, 5, 19).takeWhile(x -> x > 3));
        assertEquals(arr(23L), arr(23L, 34, 1, 5, 19).takeWhile(x -> x < 30));
        assertEquals(arr(), arr(23L, 34, 1, 5, 19).takeWhile(x -> x < 3));
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

}
