package minestra.text;

import static minestra.text.TestUtils.*;
import static org.junit.Assert.*;
import java.util.OptionalInt;
import org.junit.Test;

public final class AggregatedPropsRefTest {

    @Test
    public void testStringOpt() {
        PropsRef r1 = PropsRef.wrap(propsOf("name", "pXX"));
        PropsRef r2 = PropsRef.wrap(mapOf("name", "Q", "Name", "qX"));
        PropsRef r3 = ResourceSheaf.create(getClass());
        PropsRef ar1 = PropsRef.aggregate(r1, r2, r3);
        assertEquals("pXX", ar1.string("name", ""));
        assertEquals("qX", ar1.string("Name", ""));
        assertEquals("c", ar1.string("NAME", ""));
        PropsRef ar2 = PropsRef.aggregate(r2, r3, r1);
        assertEquals("Q", ar2.string("name", ""));
        assertEquals("qX", ar2.string("Name", ""));
        assertEquals("c", ar2.string("NAME", ""));
        PropsRef ar3 = PropsRef.aggregate(r3, r1, r2);
        assertEquals("a", ar3.string("name", ""));
        assertEquals("b", ar3.string("Name", ""));
        assertEquals("c", ar3.string("NAME", ""));
    }

    @Test
    public void testIntegerOpt() {
        PropsRef r1 = PropsRef.wrap(propsOf("num", "13", "fallThrough", "String"));
        PropsRef r2 = PropsRef.wrap(mapOf("num", "55", "Val", "47", "fallThrough", "31"));
        PropsRef r3 = ResourceSheaf.create(getClass());
        PropsRef ar1 = PropsRef.aggregate(r1, r2, r3);
        assertEquals(13, ar1.integer("num"));
        assertEquals(47, ar1.integer("Val"));
        assertEquals(OptionalInt.empty(), ar1.integerOpt("dummy"));
        PropsRef ar2 = PropsRef.aggregate(r2, r3, r1);
        assertEquals(55, ar2.integer("num"));
        assertEquals(47, ar2.integer("Val"));
        assertEquals(OptionalInt.empty(), ar2.integerOpt("dummy"));
        PropsRef ar3 = PropsRef.aggregate(r3, r1, r2);
        assertEquals(84, ar3.integer("num"));
        assertEquals(62, ar3.integer("Val"));
        assertEquals(31, ar3.integer("fallThrough"));
        assertEquals(OptionalInt.empty(), ar3.integerOpt("dummy"));
    }

}
