package minestra.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

public interface DoubleImmArray {

    static DoubleImmArray of(double... a) {
        return new DoubleImmArrayImpl(a);
    }

    static DoubleImmArray of(Collection<Double> collection) {
        return ImmArray.of(collection).mapToDouble(Double::doubleValue);
    }

    static DoubleImmArray of(DoubleStream stream) {
        return of(stream.toArray());
    }

    static DoubleImmArray generate(int size, DoubleSupplier generator) {
        double[] a = new double[size];
        for (int i = 0; i < size; i++) {
            a[i] = generator.getAsDouble();
        }
        return of(a);
    }

    static DoubleImmArray random(int size, double min, double max) {
        // if you need SecureRandom, use generate(int, DoubleSupplier)
        final double distance = max - min + 0.000000000d;
        Random r = new Random(System.currentTimeMillis());
        double[] a = new double[size];
        for (int i = 0; i < size; i++) {
            a[i] = min + (int) (r.nextDouble() * distance);
        }
        return of(a);
    }

    default DoubleImmArray concat(DoubleImmArray first, DoubleImmArray... rest) {
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (DoubleImmArray o : rest) {
            newLength += o.size();
        }
        // XXX copy twice
        double[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (DoubleImmArray o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return of(a);
    }

    int size();

    double at(int index);

    static DoubleImmArray empty() {
        return DoubleImmArrayImpl.EMPTY;
    }

    default void forEach(DoubleConsumer action) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            action.accept(at(i));
        }
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default boolean exists(DoublePredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return true;
            }
        }
        return false;
    }

    default OptionalDouble find(DoublePredicate pred) {
        return find(pred, 0);
    }

    default OptionalDouble find(DoublePredicate pred, int start) {
        final int n = size();
        for (int i = start; i < n; i++) {
            final double value = at(i);
            if (pred.test(value)) {
                return OptionalDouble.of(value);
            }
        }
        return OptionalDouble.empty();
    }

    default int indexWhere(DoublePredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return i;
            }
        }
        return -1;
    }

    default OptionalDouble head() {
        return (size() == 0) ? OptionalDouble.empty() : OptionalDouble.of(at(0));
    }

    default DoubleImmArray tail() {
        return subSequence(1, Integer.MAX_VALUE);
    }

    default DoubleImmArray take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default DoubleImmArray takeWhile(DoublePredicate pred) {
        final int index = indexWhere(pred.negate());
        return (index > 0) ? subSequence(0, index - 1) : empty();
    }

    default DoubleImmArray drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default DoubleImmArray subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return new DoubleImmArrayImpl(true, Arrays.copyOfRange(toArray(), from, to0 + 1));
    }

    default DoubleImmArray map(DoubleUnaryOperator mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsDouble(at(i));
        }
        return of(a);
    }

    default <R> ImmArray<R> mapToObj(DoubleFunction<R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            a.add(mapper.apply(at(i)));
        }
        return ImmArray.of(a);
    }

    default IntImmArray mapToInt(DoubleToIntFunction mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsInt(at(i));
        }
        return IntImmArray.of(a);
    }

    default LongImmArray mapToLong(DoubleToLongFunction mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsLong(at(i));
        }
        return LongImmArray.of(a);
    }

    default OptionalDouble reduce(DoubleBinaryOperator op) {
        final int n = size();
        if (n == 0) {
            return OptionalDouble.empty();
        }
        double result = at(0);
        for (int i = 1; i < n; i++) {
            result = op.applyAsDouble(result, at(i));
        }
        return OptionalDouble.of(result);
    }

    default double reduce(double identity, DoubleBinaryOperator op) {
        final int n = size();
        if (n == 0) {
            return identity;
        }
        double result = identity;
        for (int i = 0; i < n; i++) {
            result = op.applyAsDouble(result, at(i));
        }
        return result;
    }

    default double fold(double value, DoubleBinaryOperator f) {
        switch (size()) {
            case 0:
                return value;
            case 1:
                return f.applyAsDouble(value, at(0));
            default:
                return f.applyAsDouble(value, tail().fold(at(0), f));
        }
    }

    default DoubleImmArray distinct() {
        return of(stream().distinct());
    }

    default DoubleImmArray filter(DoublePredicate pred) {
        final int n = size();
        int p = 0;
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            double x = at(i);
            if (pred.test(x)) {
                a[p++] = x;
            }
        }
        return new DoubleImmArrayImpl(true, Arrays.copyOf(a, p));
    }

    double sum();

    double product();

    default double average() {
        return sum() / size();
    }

    OptionalDouble max();

    OptionalDouble min();

    default DoubleImmArray sort() {
        double[] a = toArray();
        Arrays.sort(a);
        return of(a);
    }

    default DoubleImmArray sortWith(DoubleComparator cmp) {
        return sortWith(0, size() - 1, cmp);
    }

    DoubleImmArray sortWith(int fromIndex, int toIndex, DoubleComparator cmp);

    default DoubleImmArray reverse() {
        double[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            double x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return of(a);
    }

    double[] toArray();

    default DoubleStream stream() {
        return DoubleStream.of(toArray());
    }

}
