package minestra.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.LongStream;

public interface LongImmArray {

    static LongImmArray of(long... a) {
        return new LongImmArrayImpl(a);
    }

    static LongImmArray of(Collection<Long> collection) {
        return ImmArray.of(collection).mapToLong(Long::longValue);
    }

    static LongImmArray of(LongStream stream) {
        return of(stream.toArray());
    }

    static LongImmArray generate(int size, LongSupplier generator) {
        long[] a = new long[size];
        for (int i = 0; i < size; i++) {
            a[i] = generator.getAsLong();
        }
        return of(a);
    }

    static LongImmArray random(int size, long min, long max) {
        // if you need SecureRandom, use generate(int, LongSupplier)
        final long distance = max - min + 1;
        Random r = new Random(System.currentTimeMillis());
        long[] a = new long[size];
        for (int i = 0; i < size; i++) {
            a[i] = min + (int) (r.nextDouble() * distance);
        }
        return of(a);
    }

    default LongImmArray concat(LongImmArray first, LongImmArray... rest) {
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (LongImmArray o : rest) {
            newLength += o.size();
        }
        // XXX copy twice
        long[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (LongImmArray o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return of(a);
    }

    int size();

    long at(int index);

    static LongImmArray empty() {
        return LongImmArrayImpl.EMPTY;
    }

    default void forEach(LongConsumer action) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            action.accept(at(i));
        }
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default boolean exists(LongPredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return true;
            }
        }
        return false;
    }

    default boolean contains(long value) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (at(i) == value) {
                return true;
            }
        }
        return false;
    }

    default OptionalLong find(LongPredicate pred) {
        return find(pred, 0);
    }

    default OptionalLong find(LongPredicate pred, int start) {
        final int n = size();
        for (int i = start; i < n; i++) {
            final long value = at(i);
            if (pred.test(value)) {
                return OptionalLong.of(value);
            }
        }
        return OptionalLong.empty();
    }

    default int indexOf(long value) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (at(i) == value) {
                return i;
            }
        }
        return -1;
    }

    default int indexWhere(LongPredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return i;
            }
        }
        return -1;
    }

    default OptionalLong head() {
        return (size() == 0) ? OptionalLong.empty() : OptionalLong.of(at(0));
    }

    default LongImmArray tail() {
        return subSequence(1, Integer.MAX_VALUE);
    }

    default LongImmArray take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default LongImmArray takeWhile(LongPredicate pred) {
        final int index = indexWhere(pred.negate());
        return (index > 0) ? subSequence(0, index - 1) : empty();
    }

    default LongImmArray drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default LongImmArray subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return new LongImmArrayImpl(true, Arrays.copyOfRange(toArray(), from, to0 + 1));
    }

    default LongImmArray map(LongUnaryOperator mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsLong(at(i));
        }
        return of(a);
    }

    default <R> ImmArray<R> mapToObj(LongFunction<R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            a.add(mapper.apply(at(i)));
        }
        return ImmArray.of(a);
    }

    default IntImmArray mapToInt(LongToIntFunction mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsInt(at(i));
        }
        return IntImmArray.of(a);
    }

    default DoubleImmArray mapToDouble(LongToDoubleFunction mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsDouble(at(i));
        }
        return DoubleImmArray.of(a);
    }

    default OptionalLong reduce(LongBinaryOperator op) {
        final int n = size();
        if (n == 0) {
            return OptionalLong.empty();
        }
        long result = at(0);
        for (int i = 1; i < n; i++) {
            result = op.applyAsLong(result, at(i));
        }
        return OptionalLong.of(result);
    }

    default long reduce(long identity, LongBinaryOperator op) {
        final int n = size();
        if (n == 0) {
            return identity;
        }
        long result = identity;
        for (int i = 0; i < n; i++) {
            result = op.applyAsLong(result, at(i));
        }
        return result;
    }

    default long fold(long value, LongBinaryOperator f) {
        switch (size()) {
            case 0:
                return value;
            case 1:
                return f.applyAsLong(value, at(0));
            default:
                return f.applyAsLong(value, tail().fold(at(0), f));
        }
    }

    default LongImmArray distinct() {
        return of(stream().distinct());
    }

    default LongImmArray filter(LongPredicate pred) {
        final int n = size();
        int p = 0;
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            long x = at(i);
            if (pred.test(x)) {
                a[p++] = x;
            }
        }
        return new LongImmArrayImpl(Arrays.copyOf(a, p));
    }

    long sum();

    long product();

    default double average() {
        return sum() * 1d / size();
    }

    OptionalLong max();

    OptionalLong min();

    default LongImmArray sort() {
        long[] a = toArray();
        Arrays.sort(a);
        return of(a);
    }

    default LongImmArray sortWith(LongComparator cmp) {
        return sortWith(0, size() - 1, cmp);
    }

    LongImmArray sortWith(int fromIndex, int toIndex, LongComparator cmp);

    default LongImmArray reverse() {
        long[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            long x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return of(a);
    }

    long[] toArray();

    default LongStream stream() {
        return LongStream.of(toArray());
    }

}
