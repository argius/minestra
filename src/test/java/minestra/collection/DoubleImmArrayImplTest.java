package minestra.collection;

import static minestra.collection.TestUtils.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public final class DoubleImmArrayImplTest {

    private static final double DELTA = 0.00001d;

    static DoubleImmArrayImpl arr(double... a) {
        return new DoubleImmArrayImpl(a);
    }

    @Test
    public void testAt() {
        assertEquals(2.2d, arr(1.1d, 2.2, 3.3).at(1), 0.1d);
    }

    @Test
    public void testEqualsObject() {
        DoubleImmArray arr1 = arr(1.1d, 2.2);
        DoubleImmArray arr2 = arr(1.1d, 2.2);
        assertEquals(arr1, arr1);
        assertEquals(arr1, arr2);
        assertEquals(arr1, arr(1.1, 2.2));
        assertNotEquals(arr1, null);
        assertNotEquals(arr1, "");
        assertNotEquals(arr1, arr(11, 22, 33));
        assertNotEquals(arr1, arr(11, 33));
    }

    @Test
    public void testFilter() {
        // fail("Not yet implemented");
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
        assertNotEquals(arr(1.1).hashCode(), arr(1.1, 2.2).hashCode());
    }

    @Test
    public void testHead() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMap() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMapToObj() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMax() {
        assertEquals(33.1, arr(4d, 23, 33.1, 1, 5, 19).max().getAsDouble(), DELTA);
        assertEquals(343.1, arr(-1d, 3, 8, 343.1, -53, 134).max().getAsDouble(), DELTA);
        assertTrue(arr(-1).max().isPresent());
        assertFalse(arr().max().isPresent());
    }

    @Test
    public void testMin() {
        assertEquals(1d, arr(4d, 23, 33, 1, 5, 19).min().getAsDouble(), DELTA);
        assertEquals(-53d, arr(-1d, 3, 8, 343, -53, 134).min().getAsDouble(), DELTA);
        assertTrue(arr(-1f).min().isPresent());
        assertFalse(arr().min().isPresent());
    }

    @Test
    public void testOfCollectionOfDouble() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfDoubleArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfDoubleStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testProduct() {
        assertEquals(720.0d, arr(2, 3, 4, 5, 6).product(), DELTA);
        assertEquals(2.0d, arr(2).product(), DELTA);
        assertEquals(0.0d, arr().product(), DELTA);
    }

    @Test
    public void testReverse() {
        assertEquals(arr(-1, 3, 8, 343, -53, 134), arr(134, -53, 343, 8, 3, -1).reverse());
        assertEquals(arr(-1, 3), arr(3, -1).reverse());
        assertEquals(arr(-1), arr(-1).reverse());
        assertEquals(arr(), arr().reverse());
    }

    @Test
    public void testSeqCollectionOfDouble() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqDoubleArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqDoubleStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSize() {
        assertEquals(3, arr(1.1d, 2.2, 3.3).size());
    }

    @Test
    public void testSort() {
        double[] arg = darr(134, -53, 343, 8, 3, -1);
        double[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(arr(expected), arr(arg).sort());
    }

    @Test
    public void testSortWith() {
        assertEquals(arr(343, 134, 8, 3, -1, -53), arr(134d, -53, 343, 8, 3, -1).sortWith(DoubleComparator.REVERSE));
        assertEquals(arr(134, -53), arr(-53, 134d).sortWith(DoubleComparator.REVERSE));
    }

    @Test
    public void testStream() {
        assertEquals(1, arr(1).stream().count());
    }

    @Test
    public void testSubSequence() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSum() {
        assertEquals(6.6d, arr(1.1d, 2.2, 3.3).sum(), DELTA);
        assertEquals(1.1d, arr(1.1f).sum(), DELTA);
        assertEquals(0d, arr().sum(), DELTA);
    }

    @Test
    public void testTail() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        assertEquals("[1.1, 2.2]", arr(1.1, 2.2).toString());
        assertEquals("[1.1]", arr(1.1).toString());
        assertEquals("[]", arr().toString());
    }

}
