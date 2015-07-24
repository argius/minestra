package minestra.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public interface IntImmArray {

    static IntImmArray of(int... a) {
        return new IntImmArrayImpl(a);
    }

    static IntImmArray of(Collection<Integer> collection) {
        return ImmArray.of(collection).mapToInt(Integer::intValue);
    }

    static IntImmArray of(IntStream stream) {
        return of(stream.toArray());
    }

    static IntImmArray generate(int size, IntSupplier generator) {
        int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = generator.getAsInt();
        }
        return of(a);
    }

    static IntImmArray range(int start, int end) {
        final int length = end - start + 1;
        if (length <= 0) {
            throw new IllegalArgumentException(String.format("illegal range: %d to %d", start, end));
        }
        int[] a = new int[length];
        for (int i = 0; i < length; i++) {
            a[i] = start + i;
        }
        return of(a);
    }

    static IntImmArray range(int start, int end, int step) {
        final int length;
        if (step == 0) {
            length = -1;
        }
        else if (step < 0) {
            length = (start - end) / (-step) + 1;
        }
        else {
            length = (end - start) / step + 1;
        }
        if (length <= 0) {
            throw new IllegalArgumentException(String.format("illegal range: %d to %d step %d", start, end, step));
        }
        int[] a = new int[length];
        for (int i = 0; i < length; i++) {
            a[i] = start + i * step;
        }
        return of(a);
    }

    static IntImmArray random(int size, int min, int max) {
        final int distance = max - min + 1;
        Random r = new Random(System.currentTimeMillis());
        int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = min + (int) (r.nextDouble() * distance);
        }
        return of(a);
    }

    static IntImmArray asCodePoints(CharSequence s) {
        return of(s.codePoints());
    }

    default IntImmArray concat(IntImmArray first, IntImmArray... rest) {
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (IntImmArray o : rest) {
            newLength += o.size();
        }
        int[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (IntImmArray o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return of(a);
    }

    int size();

    int at(int index);

    static IntImmArray empty() {
        return IntImmArrayImpl.EMPTY;
    }

    default void forEach(IntConsumer action) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            action.accept(at(i));
        }
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default boolean exists(IntPredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return true;
            }
        }
        return false;
    }

    default boolean contains(int value) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (at(i) == value) {
                return true;
            }
        }
        return false;
    }

    default OptionalInt find(IntPredicate pred) {
        return find(pred, 0);
    }

    default OptionalInt find(IntPredicate pred, int start) {
        final int n = size();
        for (int i = start; i < n; i++) {
            final int value = at(i);
            if (pred.test(value)) {
                return OptionalInt.of(value);
            }
        }
        return OptionalInt.empty();
    }

    default int indexOf(int value) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (at(i) == value) {
                return i;
            }
        }
        return -1;
    }

    default int indexWhere(IntPredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return i;
            }
        }
        return -1;
    }

    default OptionalInt head() {
        return (size() == 0) ? OptionalInt.empty() : OptionalInt.of(at(0));
    }

    default IntImmArray tail() {
        return subSequence(1, Integer.MAX_VALUE);
    }

    default IntImmArray take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default IntImmArray takeWhile(IntPredicate pred) {
        final int index = indexWhere(pred.negate());
        return (index > 0) ? subSequence(0, index - 1) : empty();
    }

    default IntImmArray drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default IntImmArray subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return of(Arrays.copyOfRange(toArray(), from, to0 + 1));
    }

    default IntImmArray map(IntUnaryOperator mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsInt(at(i));
        }
        return of(a);
    }

    default <R> ImmArray<R> mapToObj(IntFunction<R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            a.add(mapper.apply(at(i)));
        }
        return ImmArray.of(a);
    }

    default LongImmArray mapToLong(IntToLongFunction mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsLong(at(i));
        }
        return LongImmArray.of(a);
    }

    default DoubleImmArray mapToDouble(IntToDoubleFunction mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsDouble(at(i));
        }
        return DoubleImmArray.of(a);
    }

    default OptionalInt reduce(IntBinaryOperator op) {
        final int n = size();
        if (n == 0) {
            return OptionalInt.empty();
        }
        int result = at(0);
        for (int i = 1; i < n; i++) {
            result = op.applyAsInt(result, at(i));
        }
        return OptionalInt.of(result);
    }

    default int reduce(int identity, IntBinaryOperator op) {
        final int n = size();
        if (n == 0) {
            return identity;
        }
        int result = identity;
        for (int i = 0; i < n; i++) {
            result = op.applyAsInt(result, at(i));
        }
        return result;
    }

    default int fold(int value, IntBinaryOperator f) {
        switch (size()) {
            case 0:
                return value;
            case 1:
                return f.applyAsInt(value, at(0));
            default:
                return f.applyAsInt(value, tail().fold(at(0), f));
        }
    }

    default IntImmArray distinct() {
        return of(IntStream.of(toArray()).distinct());
    }

    default IntImmArray filter(IntPredicate pred) {
        final int n = size();
        int p = 0;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            int x = at(i);
            if (pred.test(x)) {
                a[p++] = x;
            }
        }
        return new IntImmArrayImpl(Arrays.copyOf(a, p));
    }

    int sum();

    int product();

    default double average() {
        return sum() * 1d / size();
    }

    OptionalInt max();

    OptionalInt min();

    default IntImmArray sort() {
        int[] a = toArray();
        Arrays.sort(a);
        return of(a);
    }

    default IntImmArray sortWith(IntComparator cmp) {
        return sortWith(0, size() - 1, cmp);
    }

    IntImmArray sortWith(int fromIndex, int toIndex, IntComparator cmp);

    default IntImmArray reverse() {
        int[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            int x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return of(a);
    }

    int[] toArray();

    default IntStream stream() {
        return IntStream.of(toArray());
    }

    default String toStringAsCodePoints() {
        return new String(toArray(), 0, size());
    }

    default IntImmArray replaceCodePoint(char target, char replacement) {
        final int cp0 = target;
        final int cp1 = replacement;
        return map(x -> x == cp0 ? cp1 : x);
    }

    default IntImmArray replaceCodePoint(String chars) {
        int[] codePoints = chars.codePoints().toArray();
        final int cp0 = codePoints[0];
        final int cp1 = codePoints[1];
        return map(x -> x == cp0 ? cp1 : x);
    }

}
