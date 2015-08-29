package minestra.collection;

import static minestra.collection.TestUtils.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.Test;

public final class ImmArrayImplTest {

    @SafeVarargs
    static <T> ImmArrayImpl<T> arr0(T... a) {
        return new ImmArrayImpl<>(true, a);
    }

    @Test
    public void testAt() {
        // fail("Not yet implemented");
    }

    @Test
    public void testEqualsObject() {
        ImmArray<String> arr1 = ImmArray.of("11", "22");
        ImmArray<String> arr2 = ImmArray.of("11", "22");
        assertEquals(arr1, arr1);
        assertEquals(arr1, arr2);
        assertEquals(arr1, ImmArray.of("11", "22"));
        assertNotEquals(arr1, null);
        assertNotEquals(arr1, "");
        assertNotEquals(arr1, arr0("11", "22", "33"));
        assertNotEquals(arr1, arr0("11", "33"));
    }

    @Test
    public void testFold() {
        // fail("Not yet implemented");
    }

    @Test
    public void testHashCode() {
        assertNotEquals(ImmArray.of(11).hashCode(), ImmArray.of(11, 22).hashCode());
    }

    @Test
    public void testIterator() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = arr0("11", "22").iterator(); it.hasNext();) {
            sb.append("AA").append(it.next()).append("ZZ/");
        }
        assertEquals("AA11ZZ/AA22ZZ/", sb.toString());
    }

    @Test
    public void testMap() {
        assertEquals(ImmArray.of("==11::", "==22::"), arr0("11", "22").map(x -> "==" + x + "::"));
    }

    @Test
    public void testMapToInt() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMapToIntAsArray() {
        // fail("Not yet implemented");
    }

    // @Test
    // public void testFilter() {
    // Sequence<String> arr = arr("java", "scala", "perl", "ruby", "python");
    // assertEquals(arr("java", "perl", "ruby"), arr.filter(x -> x.length() ==
    // 4));
    // }
    //
    // @Test
    // public void testHead() {
    // assertEquals(Optional.of("java"), arr("java", "scala", "perl", "ruby",
    // "python").head());
    // assertEquals(Optional.of("scala"), arr("scala", "perl", "ruby",
    // "python").head());
    // assertEquals(Optional.empty(), arr().head());
    // }
    //
    // @Test
    // public void testTail() {
    // assertEquals(arr("scala", "perl", "ruby"), arr("java", "scala", "perl",
    // "ruby").tail());
    // assertEquals(arr("perl", "ruby"), arr("java", "scala", "perl",
    // "ruby").tail().tail());
    // assertEquals(arr(), arr("scala").tail().tail());
    // }

    @Test
    public void testFlatten0() throws Exception {
        List<String> a1 = new ArrayList<>();
        List<String> a2 =
                ImmArrayImpl.flatten0(arr0("1", Arrays.asList("2"), Stream.of("3", "A"), Optional.of("4"), arr0("5")), a1);
        assertSame(a1, a2);
        assertArrayEquals(arr0("1", "2","3","A", "4","5").toArray(), a2.toArray());
    }

    @Test
    public void testMapToStringAsArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfCollectionOfE() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfEArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfStreamOfE() {
        // fail("Not yet implemented");
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
        // fail("Not yet implemented");
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public void testStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSubSequence() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToArray() {
        assertArrayEquals(arr("11", "22"), arr0("11", "22").toArray());
    }

    @Test
    public void testToArrayIntFunctionOfE() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToList() {
        // fail("Not yet implemented");
    }

    // @Test
    // public void testToMapWithKey() {
    // assertEquals("{python=6, java=4, scala=5, perl=4, ruby=4}", // -
    // arr("java", "scala", "perl", "ruby",
    // "python").toMapWithKey(String::length).toString());
    // assertEquals("{1=1, 2=2, 3=3}", arr("1", "2",
    // "3").toMapWithKey(String::toUpperCase).toString());
    // }
    //
    // @Test
    // public void testToMapWithValue() {
    // assertEquals("{JAVA=java, SCALA=scala, PERL=perl, RUBY=ruby, PYTHON=python}",
    // arr("java", "scala", "perl", "ruby",
    // "python").toMapWithValue(String::toUpperCase).toString());
    // assertEquals("{1=1, 2=2, 3=3}", arr("1", "2",
    // "3").toMapWithValue(String::toUpperCase).toString());
    // }

    @Test
    public void testToSet() {
        assertEquals(new HashSet<>(Arrays.asList("11", "22")), arr0("11", "22").toSet());
    }

    @Test
    public void testToString() {
        assertEquals("[11, 22]", ImmArray.of(11, 22).toString());
        assertEquals("[11]", ImmArray.of(11).toString());
        assertEquals("[]", ImmArray.of().toString());
        assertEquals("[[1, 2], [A, B]]", ImmArray.of(Arrays.asList("1", "2"), Arrays.asList("A", "B")).toString());
    }

    @Test
    public void testToStringArray() {
        assertArrayEquals(arr("11", "22"), arr0("11", "22").toStringArray());
    }

}
