package minestra.collection;

import static minestra.collection.IntImmArray.*;
import static minestra.collection.TestUtils.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

public class IntImmArrayTest {

    static IntImmArray arr(int... a) {
        return IntImmArray.of(a);
    }

    static IntImmArray arr0(int... a) {
        return new IntImmArray() {

            @Override
            public int at(int index) {
                throw new UnsupportedOperationException();
            }

            @Override
            public OptionalInt max() {
                throw new UnsupportedOperationException();
            }

            @Override
            public OptionalInt min() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int product() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int size() {
                return a.length;
            }

            @Override
            public IntImmArray sortWith(int fromIndex, int toIndex, IntComparator cmp) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int sum() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int[] toArray() {
                return Arrays.copyOf(a, a.length);
            }

        };
    }

    @Test
    public void testAt() {
        assertEquals(1, arr(4, 23, 33, 1, 5, 19).at(3));
        assertEquals(34, arr(23, 34, 1, 5, 19).at(1));
    }

    @Test
    public void testAverage() {
        assertEquals(17.4285714285714, arr(22, 40, 2, 35, 37, -5, -9).average(), 0.00001d);
    }

    @Test
    public void testBoxed() {
        IntFunction<Integer> f = Integer::valueOf;
        assertEquals(ImmArray.of(f.apply(41), f.apply(52), f.apply(43)), arr(41, 52, 43).boxed());
        assertEquals(ImmArray.of(f.apply(345)), arr(345).boxed());
    }

    @Test
    public void testConcat() {
        assertEquals(arr(8, 3, 2, 12, -3, 6, 18), arr(8, 3, 2).concat(arr(12), arr(-3, 6, 18)));
    }

    @Test
    public void testContains() {
        assertTrue(arr(32, 42, 28, 20, -2).contains(28));
        assertFalse(arr(15, 25, 10, -3, -13).contains(3));
    }

    @Test
    public void testDistinct() {
        assertEquals(arr(8, 3, 2, 12, 18), arr(8, 3, 2, 12, 3, 8, 18).distinct());
    }

    @Test
    public void testDrop() {
        assertEquals(arr(12, 3, 8, 18), arr(8, 3, 2, 12, 3, 8, 18).drop(3));
        assertEquals(arr(), arr(8, 3, 2, 12, 3, 8, 18).drop(12));
        assertEquals(arr(5, 3, 8, 18), arr(5, 3, 8, 18).drop(0));
        assertEquals(arr(), arr(8, 3, 2, 12, 3, 8, 18).drop(8));
    }

    @Test
    public void testDropWhile() {
        assertEquals(arr(100, 121, 122), arr(111, 112, 113, 100, 121, 122).dropWhile(x -> x > 100));
        assertEquals(arr(1, 2, 3), arr(1, 2, 3).dropWhile(x -> x > 9));
        assertEquals(arr(), arr(1, 2, 3).dropWhile(x -> x < 9));
    }

    @Test
    public void testEmpty() {
        IntImmArray empty1 = IntImmArray.empty();
        IntImmArray empty2 = IntImmArray.empty();
        assertEquals(arr(), empty1);
        assertSame(empty1, empty2);
    }

    @Test
    public void testExists() {
        assertTrue(arr(23, 8, 30, 16, 22, 36, 0, 51, 36).exists(x -> x > 0));
        assertFalse(arr(47, 13, -8, 2, 34, 20, 0, 6, 40).exists(x -> x < -10));
    }

    @Test
    public void testFilter() {
        assertEquals(arr(2, 8, 6), arr(3, 2, 8, 5, 6).filter(x -> x % 2 == 0));
    }

    @Test
    public void testFind() {
        assertEquals(OptionalInt.of(-16), arr(3, -11, 9, -5, -16, 2, 16).find(x -> x % 2 == 0));
        assertEquals(OptionalInt.of(-3), arr(-7, -10, 4, -3, 15, 13, 8).find(x -> x < 0, 3));
        assertEquals(OptionalInt.empty(), arr(-7, -10, 4, -3, 15, 13, 8).find(x -> x < 0, 5));
    }

    @Test
    public void testFold() {
        assertEquals(11, arr(1, 2, 3).fold(5, (x, y) -> x + y));
        assertEquals(5, arr().fold(5, (x, y) -> x + y));
    }

    @Test
    public void testForEach() {
        StringBuilder sb = new StringBuilder();
        arr(3, 2, 8, 5, 6).forEach(x -> sb.append(":").append(x));
        assertEquals(":3:2:8:5:6", sb.toString());
    }

    @Test
    public void testGenerate() {
        assertEquals(arr(4, 4, 4), generate(3, () -> 4));
        AtomicInteger aint = new AtomicInteger(-3);
        assertEquals(arr(-3, -2, -1, 0, 1), generate(5, () -> aint.getAndAdd(1)));
    }

    @Test
    public void testHead() {
        assertEquals(4, arr(4, 23, 33, 1, 5, 19).head().getAsInt());
        assertTrue(arr(4).head().isPresent());
        assertFalse(arr().head().isPresent());
    }

    @Test
    public void testIndexOf() {
        assertEquals(3, arr(34, 20, 13, 46, 20, 7, 0, 3, -5).indexOf(46));
        assertEquals(-1, arr(49, 16, 25, 44, 27, -7, 41, 19, 51).indexOf(-6));
    }

    @Test
    public void testIndexWhere() {
        assertEquals(2, arr(41, 11, 17, 21, 48, 31, 11, 4, 42).indexWhere(x -> x >= 16 && x < 30));
        assertEquals(-1, arr(40, 3, 37, 27, 0, 50, 9, 22, 21).indexWhere(x -> x < 0));
    }

    @Test
    public void testMap() {
        assertArrayEquals(iarr(24, 2, 6, 20), arr(23, 1, 5, 19).map(x -> x + 1).toArray());
    }

    @Test
    public void testMapToDouble() {
        assertArrayEquals(darr(1.33d, -1.33d, 4.67d, 6.67d, 1.00d, 1.00d, 6.67d), //
            arr(4, -4, 14, 20, 3, 3, 20).mapToDouble(x -> x / 3d).toArray(), 0.01d);
    }

    @Test
    public void testMapToLong() {
        assertArrayEquals(larr(17L, 2L, 20L, 22L, -2L, 24L), arr(15, 0, 18, 20, -4, 22).mapToLong(x -> x + 2).toArray());
    }

    @Test
    public void testMapToObj() {
        assertArrayEquals(ImmArray.of("A1", "A2", "A3").toArray(), arr(1, 2, 3).mapToObj(x -> "A" + x).toArray());
    }

    @Test
    public void testOfCollectionOfInteger() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            List<Integer> initValueList = IntStream.of(initValues).boxed().collect(Collectors.toList());
            IntImmArray expected = new IntImmArrayImpl(initValues);
            IntImmArray actual1 = IntImmArray.of(initValueList);
            IntImmArray actual2 = IntImmArray.of(initValueList);
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testOfIntArray() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            IntImmArray expected = new IntImmArrayImpl(initValues);
            IntImmArray actual1 = IntImmArray.of(initValues);
            IntImmArray actual2 = IntImmArray.of(initValues);
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testOfIntStream() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            IntImmArray expected = new IntImmArrayImpl(initValues);
            IntImmArray actual1 = IntImmArray.of(IntStream.of(initValues));
            IntImmArray actual2 = IntImmArray.of(IntStream.of(initValues));
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 1000; i++) {
            final int size = 12;
            final int min = -2;
            final int max = 4;
            IntImmArray arr = random(size, min, max);
            assertEquals(size, arr.size());
            assertEquals(0, arr.filter(x -> x < min || x > max).size());
        }
    }

    @Test
    public void testRange() {
        assertEquals(arr(2, 3, 4, 5, 6), IntImmArray.range(2, 6));
        try {
            IntImmArray.range(5, 4);
            fail("requires an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal range: 5 to 4", e.getMessage());
        }
        assertEquals(arr(-12, -9, -6, -3, 0, 3), IntImmArray.range(-12, 3, 3));
        // with step
        assertEquals(arr(5, 3, 1, -1, -3), IntImmArray.range(5, -4, -2));
        try {
            IntImmArray.range(5, 4, 1);
            fail("requires an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal range: 5 to 4 step 1", e.getMessage());
        }
        try {
            IntImmArray.range(4, 5, -1);
            fail("requires an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal range: 4 to 5 step -1", e.getMessage());
        }
        try {
            IntImmArray.range(4, 5, 0);
            fail("requires an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal range: 4 to 5 step 0", e.getMessage());
        }
    }

    @Test
    public void testReduce() {
        assertEquals(434, arr(-1, 3, 8, 343, -53, 134).reduce((x, y) -> x + y).getAsInt());
        assertEquals(437, arr(-1, 3, 8, 343, -53, 134).reduce(3, (x, y) -> x + y));
        assertEquals(OptionalInt.empty(), arr(0).tail().reduce((x, y) -> x + y));
        assertEquals(3, arr().reduce(3, (x, y) -> x + y));
    }

    @Test
    public void testReplaceCodePoint() {
        assertEquals("土±𡈽┨士", IntImmArray.asCodePoints("土±𡈽‡士").replaceCodePoint('‡', '┨').toStringAsCodePoints());
        assertEquals("土±𡈽┿士", IntImmArray.asCodePoints("土±𡈽‡士").replaceCodePoint("‡┿").toStringAsCodePoints());
        assertEquals("土±𠥼‡士", IntImmArray.asCodePoints("土±𡈽‡士").replaceCodePoint("𡈽𠥼").toStringAsCodePoints());
    }

    @Test
    public void testReverse() {
        int[] arg = iarr(134, -53, 343, 8, 3, -1);
        IntImmArray expected = arr(-1, 3, 8, 343, -53, 134);
        assertEquals(expected, arr(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfInteger() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            List<Integer> initValueList = IntStream.of(initValues).boxed().collect(Collectors.toList());
            IntImmArray expected = new IntImmArrayImpl(initValues);
            IntImmArray actual1 = IntImmArray.of(initValueList);
            IntImmArray actual2 = IntImmArray.of(initValueList);
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testSeqIntArray() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            IntImmArray expected = new IntImmArrayImpl(initValues);
            IntImmArray actual1 = IntImmArray.of(initValues);
            IntImmArray actual2 = IntImmArray.of(initValues);
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testSeqIntStream() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            IntImmArray expected = new IntImmArrayImpl(initValues);
            IntImmArray actual1 = IntImmArray.of(IntStream.of(initValues));
            IntImmArray actual2 = IntImmArray.of(IntStream.of(initValues));
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testSize() {
        assertEquals(6, arr(4, 23, 33, 1, 5, 19).size());
        assertEquals(5, arr(23, 34, 1, 5, 19).size());
    }

    @Test
    public void testSort() {
        int[] arg = iarr(134, -53, 343, 8, 3, -1);
        int[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(arr(expected), arr(arg).sort());
    }

    @Test
    public void testSortWith() {
        assertEquals(arr(343, 134, 8, 3, -1, -53), arr(iarr(134, -53, 343, 8, 3, -1)).sortWith(IntComparator.REVERSE));
        assertEquals(arr(343, 134, 8, 3, -1, -53), arr(134, -53, 343, 8, 3, -1).sortWith(IntComparator.REVERSE));
        assertEquals(arr(20, 3), arr(3, 20).sortWith(IntComparator.REVERSE));
    }

    @Test
    public void testStream() {
        assertArrayEquals(iarr(16, 38, 48, 20, -5), arr0(16, 38, 48, 20, -5).stream().toArray());
    }

    @Test
    public void testSubSequence() {
        assertEquals(arr(1, 6, 2), arr(1, 6, 2).subSequence(0, 2));
        assertEquals(arr(1, 6, 2), arr(1, 6, 2).subSequence(0, 3));
        assertEquals(arr(6, 2), arr(1, 6, 2).subSequence(1, 2));
        assertEquals(arr(1, 6), arr(1, 6, 2).subSequence(0, 1));
        assertEquals(arr(), arr(1, 6, 2).subSequence(3, 4));
    }

    @Test
    public void testTail() {
        assertArrayEquals(arr(34, 1, 5, 19).toArray(), arr(23, 34, 1, 5, 19).tail().toArray());
    }

    @Test
    public void testTake() {
        assertEquals(arr(23, 34, 1), arr(23, 34, 1, 5, 19).take(3));
        assertEquals(arr(), arr(23, 34, 1, 5, 19).take(0));
    }

    @Test
    public void testTakeWhile() {
        assertEquals(arr(23, 34), arr(23, 34, 1, 5, 19).takeWhile(x -> x > 3));
        assertEquals(arr(23), arr(23, 34, 1, 5, 19).takeWhile(x -> x < 30));
        assertEquals(arr(), arr(23, 34, 1, 5, 19).takeWhile(x -> x < 3));
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToStringAsCodePoints() {
        assertEquals("士‡𡈽±土", IntImmArray.asCodePoints("土±𡈽‡士").reverse().toStringAsCodePoints());
    }

}
