package minestra.collection;

import static org.junit.Assert.*;
import java.util.stream.Stream;
import org.junit.Test;

public final class ImmArrayMetaTest {

    static final String[] methodList1 = { "at", "concat", "distinct", "drop", "dropWhile", "empty", "exists", "filter",
        "find", "fold", "forEach", "head", "indexWhere", "map", "of", "reduce", "reverse", "size", "slice", "sort",
        "sortWith", "stream", "tail", "take", "takeWhile", "toArray", };

    static final String[] methodListForT = { "toList", "toSet", "toMapWithKey", "toMapWithValue", };
    static final String[] methodListForNumber = { "average", "boxed", "mapToObj", "max", "min", "sum", };
    static final String[] methodListExceptInt = { "mapToInt", };
    static final String[] methodListOnlyInt = { "random", "range", };
    static final String[] methodListExceptLong = { "mapToLong", };
    static final String[] methodListExceptDouble = { "contains", "indexOf", "mapToDouble", };

    static void checkMethods(Class<?> c, String[]... lists) {
        for (String[] list : lists) {
            assert Stream.of(list).distinct().count() == list.length : "list not unique";
            for (final String methodName : list) {
                if (!TestUtils.hasMethod(c, methodName)) {
                    fail(String.format("class %s does not have [%s] method", c.getSimpleName(), methodName));
                }
            }
        }
    }

    @Test
    public void testImmArrayImpl() {
        Class<?> c = ImmArray.class;
        checkMethods(c, methodList1);
        checkMethods(c, methodListForT);
        checkMethods(c, methodListExceptInt);
        checkMethods(c, methodListExceptLong);
        checkMethods(c, methodListExceptDouble);
    }

    @Test
    public void testIntImmArrayImpl() {
        Class<?> c = IntImmArray.class;
        checkMethods(c, methodList1);
        checkMethods(c, methodListForNumber);
        checkMethods(c, methodListExceptLong);
        checkMethods(c, methodListExceptDouble);
        checkMethods(c, methodListOnlyInt);
    }

    @Test
    public void testLongImmArrayImpl() {
        Class<?> c = LongImmArray.class;
        checkMethods(c, methodList1);
        checkMethods(c, methodListForNumber);
        checkMethods(c, methodListExceptInt);
        checkMethods(c, methodListExceptDouble);
    }

    @Test
    public void testDoubleImmArrayImpl() {
        Class<?> c = DoubleImmArray.class;
        checkMethods(c, methodList1);
        checkMethods(c, methodListForNumber);
        checkMethods(c, methodListExceptInt);
        checkMethods(c, methodListExceptLong);
    }

}
