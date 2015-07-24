package minestra.collection;

import static minestra.collection.TestUtils.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.OptionalLong;
import org.junit.Test;

public class LongImmArrayImplTest {

    static LongImmArrayImpl arr(long... a) {
        return new LongImmArrayImpl(false, a);
    }

    static LongImmArrayImpl arr0(long... a) {
        return new LongImmArrayImpl(true, a);
    }

    @Test
    public final void benchSort() {
        long[] a = new long[10000];
        Arrays.fill(a, 1000, 5000, 8);
        Arrays.fill(a, 6000, 9000, 13);
        arr(a).sort();
    }

    @Test
    public final void benchSortWith() {
        long[] a = new long[1000];
        Arrays.fill(a, 100, 500, 8);
        Arrays.fill(a, 600, 900, 13);
        arr(a).sortWith(LongComparator.NATURAL);
    }

    @Test
    public void testAt() {
        assertEquals(2, arr(1, 2, 3).at(1));
    }

    @Test
    public void testEqualsObject() {
        LongImmArray arr1 = arr(11, 22);
        LongImmArray arr2 = arr(11, 22);
        assertEquals(arr1, arr1);
        assertEquals(arr1, arr2);
        assertEquals(arr1, arr(11, 22));
        assertNotEquals(arr1, null);
        assertNotEquals(arr1, "");
        assertNotEquals(arr1, arr0(11, 22, 33));
        assertNotEquals(arr1, arr0(11, 33));
    }

    @Test
    public void testFilter() {
        assertEquals(arr(6L), arr(1, 6, 2).filter(x -> x > 3));
    }

    @Test
    public void testMax() {
        assertEquals(33, arr(4, 23, 33, 1, 5, 19).max().getAsLong());
        assertEquals(343, arr(-1, 3, 8, 343, -53, 134).max().getAsLong());
        assertTrue(arr(-1).max().isPresent());
        assertFalse(arr().max().isPresent());
    }

    @Test
    public void testMin() {
        assertEquals(1L, arr(4, 23, 33, 1, 5, 19).min().getAsLong());
        assertEquals(-53L, arr(-1, 3, 8, 343, -53, 134).min().getAsLong());
        assertTrue(arr(-1L).min().isPresent());
        assertFalse(arr().min().isPresent());
    }

    @Test
    public void testFold() {
        // fail("Not yet implemented");
    }

    @Test
    public void testForEach() {
        // fail("Not yet implemented");
    }

    @Test
    public void testHashCode() {
        assertEquals(2347, arr(11, 22).hashCode());
        assertEquals(1034, arr(11).hashCode());
        assertEquals(962, arr().hashCode());
    }

    @Test
    public void testHead() {
        assertEquals(OptionalLong.of(4), arr(4, 23, 33, 1, 5, 19).head());
        assertEquals(OptionalLong.of(23), arr(23, 33, 1, 5, 19).head());
        assertEquals(OptionalLong.empty(), arr().head());
    }

    @Test
    public void testMap() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMapToObj() {
        assertEquals(ImmArray.of("AB3CD", "AB5CD", "AB7CD"), arr(3, 5, 7).mapToObj(x -> "AB" + x + "CD"));
    }

    @Test
    public void testOfCollectionOfLong() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfLongArray() {
        assertArrayEquals(larr(4L, 12, 3, 25, 0, 1), arr(4L, 12, 3, 25, 0, 1).toArray());
    }

    @Test
    public void testOfLongStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testProduct() {
        assertEquals(720, arr(2, 3, 4, 5, 6).product());
        assertEquals(2, arr(2).product());
        assertEquals(0, arr().product());
    }

    @Test
    public final void testReverse() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        LongImmArray expected = arr(-1, 3, 8, 343, -53, 134);
        assertEquals(expected, arr(arg).reverse());
    }

    @Test
    public void testarrCollectionOfLong() {
        // fail("Not yet implemented");
    }

    @Test
    public void testarrLongArray() {
        assertArrayEquals(larr(1, 2, 5, 8), arr(1, 2, 5, 8).toArray());
    }

    @Test
    public void testarrLongStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public final void testSort() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        long[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(arr(expected), arr(arg).sort());
    }

    @Test
    public final void testSortWith() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        arr(arg).sort().reverse().equals(arr0(arg).sortWith(LongComparator.REVERSE));
        assertEquals(arr(2, 8).sort().reverse(), arr0(2, 8).sortWith(LongComparator.REVERSE));
    }

    @Test
    public void testStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSubarruence() {
        assertEquals(arr(1, 6, 2), arr(1, 6, 2).subSequence(0, 2));
        assertEquals(arr(1, 6, 2), arr(1, 6, 2).subSequence(0, 3));
        assertEquals(arr(6, 2), arr(1, 6, 2).subSequence(1, 2));
        assertEquals(arr(1, 6), arr(1, 6, 2).subSequence(0, 1));
        assertEquals(arr(), arr(1, 6, 2).subSequence(3, 4));
    }

    @Test
    public void testSum() {
        assertEquals(434L, arr(134L, -53, 343, 8, 3, -1).sum());
        assertEquals(134L, arr(134L).sum());
        assertEquals(0, arr().sum());
    }

    @Test
    public void testTail() {
        assertEquals(arr(34, 1, 5, 19), arr(23, 34, 1, 5, 19).tail());
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        assertEquals("[11, 22]", arr(11, 22).toString());
        assertEquals("[11]", arr(11).toString());
        assertEquals("[]", arr().toString());
    }

}
