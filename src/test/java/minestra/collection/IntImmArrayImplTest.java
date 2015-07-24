package minestra.collection;

import static minestra.collection.TestUtils.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

public class IntImmArrayImplTest {

    static IntImmArray arr(int... a) {
        return IntImmArray.of(a); // without copying
    }

    static IntImmArrayImpl arr0(int... a) {
        return new IntImmArrayImpl(false, a); // with copying
    }

    static IntImmArrayImpl arr1(int... a) {
        return new IntImmArrayImpl(false, a); // with copying ???
    }

    @Test
    public void testAt() {
        // fail("Not yet implemented");
    }

    @Test
    public void testCount() {
        // fail("Not yet implemented");
    }

    @Test
    public void testEqualsObject() {
        IntImmArray arr1 = arr(11, 22);
        IntImmArray arr2 = arr(11, 22);
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
        assertEquals(arr(2, 8, 6), arr(3, 2, 8, 5, 6).filter(x -> x % 2 == 0));
    }

    @Test
    public void testFold() {
        // fail("Not yet implemented");
    }

    @Test
    public void testHashCode() {
        assertEquals(2347, arr(11, 22).hashCode());
        assertEquals(1034, arr1(11).hashCode());
        assertEquals(962, arr().hashCode());
    }

    @Test
    public void testMapToObj() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMax() {
        assertEquals(33, arr(4, 23, 33, 1, 5, 19).max().getAsInt());
        assertEquals(343, arr(-1, 3, 8, 343, -53, 134).max().getAsInt());
        assertTrue(arr1(-1).max().isPresent());
        assertFalse(arr().max().isPresent());
    }

    @Test
    public void testMin() {
        assertEquals(1, arr(4, 23, 33, 1, 5, 19).min().getAsInt());
        assertEquals(-53, arr(-1, 3, 8, 343, -53, 134).min().getAsInt());
        assertTrue(arr1(-1).min().isPresent());
        assertFalse(arr().min().isPresent());
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

    // @Test
    // public void testHead() {
    // assertEquals(4, arr(4, 23, 33, 1, 5, 19).head().getAsInt());
    // }
    //
    // @Test
    // public void testTail() {
    // assertArrayEquals(arr(34, 1, 5, 19).toArray(), arr(23, 34, 1, 5,
    // 19).tail().toArray());
    // }

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
    public void testProduct() {
        assertEquals(720, arr(2, 3, 4, 5, 6).product());
        assertEquals(2, arr1(2).product());
        assertEquals(0, arr().product());
    }

    @Test
    public void testReverse() {
        int[] arg = iarr(134, -53, 343, 8, 3, -1);
        IntImmArray expected = arr(-1, 3, 8, 343, -53, 134);
        assertEquals(expected, arr1(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfInteger() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqIntArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqIntStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public void testStream() {
        assertEquals(1, arr0(1).stream().count());
    }

    @Test
    public void testSubSequence() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSum() {
        assertEquals(434, arr(134, -53, 343, 8, 3, -1).sum());
        assertEquals(134, arr1(134).sum());
        assertEquals(0, arr().sum());
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        assertEquals("[11, 22]", arr(11, 22).toString());
        assertEquals("[11]", arr1(11).toString());
        assertEquals("[]", arr().toString());
    }

}
