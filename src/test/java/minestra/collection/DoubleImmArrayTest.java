package minestra.collection;

import static minestra.collection.DoubleImmArray.*;
import static minestra.collection.TestUtils.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.function.DoubleFunction;
import java.util.stream.DoubleStream;
import org.junit.Test;

public class DoubleImmArrayTest {

    private static final double DELTA = 0.00001d;

    static DoubleImmArray arr(double... a) {
        return DoubleImmArray.of(a);
    }

    static DoubleImmArray arr(Collection<Double> collection) {
        return ImmArray.of(collection).mapToDouble(Double::doubleValue);
    }

    static DoubleImmArray arr(DoubleStream stream) {
        return DoubleImmArray.of(stream);
    }

    @Test
    public void testAt() {
        // fail("Not yet implemented");
    }

    @Test
    public void testAverage() {
        assertEquals(28.52571d, DoubleImmArrayTest.arr(42.08d, 27.37d, 36.63d, 41.03d, 12.11d, 50.10d, -9.64d).average(), 0.001d);
    }

    @Test
    public void testBoxed() {
        DoubleFunction<Double> f = Double::valueOf;
        assertEquals(ImmArray.of(f.apply(41.1d), f.apply(52.1d), f.apply(43.1d)), arr(41.1d, 52.1d, 43.1d).boxed());
        assertEquals(ImmArray.of(f.apply(345.1d)), arr(345.1d).boxed());
    }

    @Test
    public void testConcat() {
        assertEquals(DoubleImmArrayTest.arr(15.3d, -4.2, 0.3, 3.3d, -0.5, 2.3, -1.5),
            DoubleImmArrayTest.arr(15.3d, -4.2, 0.3).concat(DoubleImmArrayTest.arr(3.3d, -0.5), DoubleImmArrayTest.arr(2.3, -1.5)));
    }

    @Test
    public void testDistinct() {
        assertEquals(DoubleImmArrayTest.arr(8d, 3, 2, 12, 18), DoubleImmArrayTest.arr(8d, 3, 2, 12, 3, 8, 18).distinct());
    }

    @Test
    public void testDrop() {
        assertEquals(DoubleImmArrayTest.arr(-1.34d, 1.92d, 29.95d), DoubleImmArrayTest.arr(14.59d, 24.80d, 34.88d, -1.34d, 1.92d, 29.95d).drop(3));
        assertEquals(DoubleImmArrayTest.arr(), DoubleImmArrayTest.arr(14.59d, 24.80d, 34.88d, -1.34d, 1.92d, 29.95d).drop(8));
        assertEquals(DoubleImmArrayTest.arr(), DoubleImmArrayTest.arr(32.52d, 30.51d, -5.33d, 7.60d, 46.04d, 25.82d).drop(16));
        assertEquals(DoubleImmArrayTest.arr(), DoubleImmArrayTest.arr().drop(3));
    }

    @Test
    public void testDropWhile() {
        assertEquals(arr(-1.34d, 1.92d, 29.95d), arr(14.59d, 24.80d, 34.88d, -1.34d, 1.92d, 29.95d).dropWhile(x -> x > 3.0d));
        assertEquals(arr(-1.34d, 1.92d, 29.95d), arr(14.59d, 24.80d, 34.88d, -1.34d, 1.92d, 29.95d).dropWhile(x -> x > 0.0d));
        assertEquals(arr(32.52d, 30.51d, -5.33d, 7.60d), arr(32.52d, 30.51d, -5.33d, 7.60d).dropWhile(x -> x > 32.6d));
        assertEquals(arr(), arr().dropWhile(x -> x > 3.0d));
    }

    @Test
    public void testEmpty() {
        assertEquals(DoubleImmArrayTest.arr(), DoubleImmArray.empty());
    }

    @Test
    public void testEqualsObject() {
        DoubleImmArray arr1 = DoubleImmArrayTest.arr(1.1, 2.2);
        DoubleImmArray arr2 = DoubleImmArrayTest.arr(1.1, 2.2);
        assertEquals(arr1, arr1);
        assertEquals(arr1, arr2);
        assertEquals(arr1, DoubleImmArrayTest.arr(1.1, 2.2));
    }

    @Test
    public void testExists() {
        assertTrue(DoubleImmArrayTest.arr(50.11d, 46.33d, 22.36d, -10.62d, 46.58d, 10.29d, 17.21d, -4.43d, 13.62d).exists(x -> x < 10.6d));
        assertFalse(DoubleImmArrayTest.arr(-0.61d, 21.05d, 18.48d, 14.65d, 18.69d, -10.12d, -8.16d, 3.89d, 19.61d).exists(x -> x == 18.5d));
    }

    @Test
    public void testFilter() {
        assertEquals(DoubleImmArrayTest.arr(8.1d, 12, 8, 18), DoubleImmArrayTest.arr(8.1d, 3.6, 2, 12, -3, 8, 18).filter(x -> x > 5));
    }

    @Test
    public void testFind() {
        assertEquals(OptionalDouble.of(-6.42d),
            DoubleImmArrayTest.arr(21.54d, -6.42d, -0.67d, -4.31d, -1.29d, 12.39d, 1.93d).find(x -> x < 0.1d));
        assertEquals(OptionalDouble.of(14.34d),
            DoubleImmArrayTest.arr(20.31d, 14.04d, 0.82d, 3.14d, 14.34d, -19.58d, -0.00d).find(x -> x > 9.1d, 3));
        assertEquals(OptionalDouble.empty(),
            DoubleImmArrayTest.arr(-3.85d, -7.46d, 15.06d, 13.29d, -12.29d, -7.37d, 3.53d).find(x -> x > 14.1d, 3));
    }

    @Test
    public void testFold() {
        assertEquals(435.6d, DoubleImmArrayTest.arr(134.3d, -53, 343, 8, 3, -1).fold(1.3d, (x, y) -> x + y), DELTA);
        assertEquals(-52.1d, DoubleImmArrayTest.arr(-53.8d).fold(1.7d, (x, y) -> x + y), DELTA);
        assertEquals(-3.1d, DoubleImmArrayTest.arr().fold(-3.1d, (x, y) -> x + y), DELTA);
    }

    @Test
    public void testForEach() {
        StringBuilder sb = new StringBuilder();
        DoubleImmArrayTest.arr().forEach(x -> {
            sb.append(":").append(x);
        });
        assertEquals("", sb.toString());
        DoubleImmArrayTest.arr(4.3d, 8, 2, -3.1).forEach(x -> {
            sb.append(":").append(x);
        });
        assertEquals(":4.3:8.0:2.0:-3.1", sb.toString());
    }

    @Test
    public void testGenerate() {
        assertEquals(DoubleImmArrayTest.arr(0.81d, 0.81d, 0.81d), generate(3, () -> 0.81d));
        DoubleAdder dadder = new DoubleAdder();
        assertEquals(DoubleImmArrayTest.arr(0.57d, 1.14d, 1.71d, 2.28d), generate(4, () -> {
            dadder.add(0.57d);
            return dadder.sum();
        }));
    }

    @Test
    public void testHead() {
        assertEquals(OptionalDouble.of(8.2d), DoubleImmArrayTest.arr(8.2d, 3, 12, -8).head());
        assertEquals(OptionalDouble.empty(), DoubleImmArrayTest.arr().head());
    }

    @Test
    public void testIndexWhere() {
        assertEquals(6,
            DoubleImmArrayTest.arr(7.74d, 35.33d, -1.04d, 14.77d, -10.91d, -7.21d, 25.87d, 8.36d, 49.55d).indexWhere(x -> ((int)x) == 25));
        assertEquals(-1,
            DoubleImmArrayTest.arr(51.02d, 9.25d, 49.79d, 3.56d, -0.32d, 49.38d, 29.28d, 39.90d, 9.53d).indexWhere(x -> x < -1));
    }

    @Test
    public void testMap() {
        assertArrayEquals(DoubleImmArrayTest.arr(4.0d, 1.5d, 6d, -4d).toArray(), DoubleImmArrayTest.arr(8d, 3, 12, -8).map(x -> x / 2).toArray(), DELTA);
    }

    @Test
    public void testMapToInt() {
        assertArrayEquals(iarr(36, -20, 70, 27, 49, -12, 37, 0),
            DoubleImmArrayTest.arr(9.07d, -5.08d, 17.73d, 6.97d, 12.40d, -3.05d, 9.38d, 0.0d).mapToInt(x -> (int)(x * 4)).toArray());
    }

    @Test
    public void testMapToLong() {
        assertArrayEquals(larr(19L, 66L, -12L, -4L, 23L, 59L, 0L),
            DoubleImmArrayTest.arr(6.58d, 22.14d, -4.05d, -1.58d, 7.98d, 19.89d, -0.24d).mapToLong(x -> (long)(x * 3)).toArray());
    }

    @Test
    public void testMapToObj() {
        assertEquals(ImmArray.of("43.5%", "100.0%", "8.0%"),
            DoubleImmArrayTest.arr(0.435d, 1.0d, 0.08d).mapToObj(x -> String.format("%.1f%%", x * 100)));
    }

    @Test
    public void testOfCollectionOfDouble() {
        assertEquals(DoubleImmArrayTest.arr(47.21d, 40.93d, 7.59d, 11.57d, -10.33d, 44.00d, 37.23d),
            of(Arrays.asList(47.21d, 40.93d, 7.59d, 11.57d, -10.33d, 44.00d, 37.23d)));
    }

    @Test
    public void testOfDoubleArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfDoubleStream() {
        assertEquals(DoubleImmArrayTest.arr(0.37d, 24.98d, 9.46d, 19.55d, 28.90d, -11.07d, 2.75d),
            of(DoubleStream.of(0.37d, 24.98d, 9.46d, 19.55d, 28.90d, -11.07d, 2.75d)));
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 1000; i++) {
            final int size = 14;
            final double min = -1.2d;
            final double max = 3.6d;
            DoubleImmArray arr = random(size, min, max);
            assertEquals(size, arr.size());
            assertEquals(0, arr.filter(x -> x < min || x > max).size());
        }
    }

    @Test
    public void testReduce() {
        assertEquals(434.3d, DoubleImmArrayTest.arr(134.3d, -53, 343, 8, 3, -1).reduce((x, y) -> x + y).getAsDouble(), DELTA);
        assertEquals(435.6d, DoubleImmArrayTest.arr(134.3d, -53, 343, 8, 3, -1).reduce(1.3d, (x, y) -> x + y), DELTA);
        assertEquals(OptionalDouble.empty(), DoubleImmArrayTest.arr(-1d).tail().reduce((x, y) -> x + y));
        assertEquals(1.3d, DoubleImmArrayTest.arr().reduce(1.3d, (x, y) -> x + y), DELTA);
    }

    @Test
    public void testReverse() {
        assertEquals(DoubleImmArrayTest.arr(-1, 3, 8, 343, -53, 134), DoubleImmArrayTest.arr(134, -53, 343, 8, 3, -1).reverse());
        assertEquals(DoubleImmArrayTest.arr(-1, 3), DoubleImmArrayTest.arr(3, -1).reverse());
        assertEquals(DoubleImmArrayTest.arr(-1), DoubleImmArrayTest.arr(-1).reverse());
        assertEquals(DoubleImmArrayTest.arr(), DoubleImmArrayTest.arr().reverse());
    }

    @Test
    public void testSeqCollectionOfDouble() {
        assertEquals(DoubleImmArrayTest.arr(38.19d, 17.03d, 28.75d, 31.61d, 49.02d, 26.22d, -5.20d),
            arr(Arrays.asList(38.19d, 17.03d, 28.75d, 31.61d, 49.02d, 26.22d, -5.20d)));
    }

    @Test
    public void testSeqDoubleArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqDoubleStream() {
        assertEquals(DoubleImmArrayTest.arr(-6.35d, 11.46d, 8.02d, 25.54d, 29.09d, 44.87d, 41.05d),
            arr(DoubleStream.of(-6.35d, 11.46d, 8.02d, 25.54d, 29.09d, 44.87d, 41.05d)));
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSort() {
        double[] arg = darr(134, -53, 343, 8, 3, -1);
        double[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(DoubleImmArrayTest.arr(expected), DoubleImmArrayTest.arr(arg).sort());
    }

    @Test
    public void testStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSubSequence() {
        assertEquals(DoubleImmArrayTest.arr(-53, 343, 8, 3), DoubleImmArrayTest.arr(134, -53, 343, 8, 3, -1).subSequence(1, 4));
    }

    @Test
    public void testTail() {
        // fail("Not yet implemented");
    }

    @Test
    public void testTake() {
        assertEquals(DoubleImmArrayTest.arr(134.2d, -53, 343), DoubleImmArrayTest.arr(134.2d, -53, 343, 8, 3, -1).take(3));
        assertEquals(DoubleImmArrayTest.arr(), DoubleImmArrayTest.arr(0.4d, 23.1, 31.3).take(0));
    }

    @Test
    public void testTakeWhile() {
        assertEquals(DoubleImmArrayTest.arr(134.2d, -53), DoubleImmArrayTest.arr(134.2d, -53, 343, 8, 3, -1).takeWhile(x -> x < 300));
        assertEquals(DoubleImmArrayTest.arr(134.2d), DoubleImmArrayTest.arr(134.2d, -53, 343, 8, 3, -1).takeWhile(x -> x > 3));
        assertEquals(DoubleImmArrayTest.arr(), DoubleImmArrayTest.arr(0.4d, 23.1, 31.3).takeWhile(x -> x <= 0));
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        assertEquals("[1.1, 2.2]", DoubleImmArrayTest.arr(1.1, 2.2).toString());
        assertEquals("[1.1]", DoubleImmArrayTest.arr(1.1).toString());
        assertEquals("[]", DoubleImmArrayTest.arr().toString());
    }

}
