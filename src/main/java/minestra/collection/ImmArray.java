package minestra.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ImmArray<T> extends Iterable<T> {

    @SafeVarargs
    static <T> ImmArray<T> of(T... a) {
        return new ImmArrayImpl<>(a);
    }

    static <T> ImmArray<T> of(Collection<T> list) {
        return new ImmArrayImpl<T>(list);
    }

    static <T> ImmArray<T> of(Stream<T> stream) {
        return new ImmArrayImpl<T>(stream.collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    default ImmArray<T> concat(ImmArray<? extends T> first, ImmArray<? extends T>... rest) {
        // XXX varargs
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (ImmArray<? extends T> o : rest) {
            newLength += o.size();
        }
        // XXX copy twice
        T[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (ImmArray<? extends T> o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return of(a);
    }

    default int size() {
        return toArray().length;
    }

    default T at(int index) {
        return toArray()[index];
    }

    static <T> ImmArray<T> empty() {
        @SuppressWarnings("unchecked")
        ImmArray<T> o = (ImmArray<T>) ImmArrayImpl.EMPTY;
        return o;
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            action.accept(at(i));
        }
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default boolean exists(Predicate<T> pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return true;
            }
        }
        return false;
    }

    default boolean contains(T o) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (Objects.equals(at(i), o)) {
                return true;
            }
        }
        return false;
    }

    default Optional<T> find(Predicate<T> pred) {
        return find(pred, 0);
    }

    default Optional<T> find(Predicate<T> pred, int start) {
        for (int i = start, n = size(); i < n; i++) {
            T o = at(i);
            if (pred.test(o)) {
                return Optional.of(o);
            }
        }
        return Optional.empty();
    }

    default int indexOf(T o) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (Objects.equals(at(i), o)) {
                return i;
            }
        }
        return -1;
    }

    default int indexWhere(Predicate<T> pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return i;
            }
        }
        return -1;
    }

    default Optional<T> head() {
        return (size() == 0) ? Optional.empty() : Optional.of(at(0));
    }

    default ImmArray<T> tail() {
        return (size() > 1) ? subSequence(1, Integer.MAX_VALUE) : empty();
    }

    default ImmArray<T> take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default ImmArray<T> takeWhile(Predicate<T> pred) {
        final int index = indexWhere(pred.negate());
        return (index > 0) ? subSequence(0, index - 1) : empty();
    }

    default ImmArray<T> drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default ImmArray<T> subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return of(toList().subList(from, to0 + 1));
    }

    default <R> ImmArray<R> map(Function<? super T, ? extends R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            a.add(mapper.apply(at(i)));
        }
        return ImmArray.of(a);
    }

    default IntImmArray mapToInt(ToIntFunction<? super T> mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsInt(at(i));
        }
        return IntImmArray.of(a);
    }

    default LongImmArray mapToLong(ToLongFunction<? super T> mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsLong(at(i));
        }
        return LongImmArray.of(a);
    }

    default DoubleImmArray mapToDouble(ToDoubleFunction<? super T> mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsDouble(at(i));
        }
        return DoubleImmArray.of(a);
    }

    default <R> ImmArray<R> flatten() {
        return of(ImmArrayImpl.flatten0(toArray(), new ArrayList<>()));
    }

    default <R> ImmArray<R> filterMap(Function<? super T, Optional<? extends R>> mapper) {
        T[] values = toArray();
        final int n = values.length;
        List<R> a = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            mapper.apply(values[i]).ifPresent(a::add);
        }
        return of(a);
    }

    default <S, R> ImmArray<R> flatMap(Function<? super S, ? extends R> mapper) {
        List<S> a = new ArrayList<>();
        return of(ImmArrayImpl.flatten0(toArray(), a)).map(mapper);
    }

    default Optional<T> reduce(BinaryOperator<T> op) {
        final int n = size();
        if (n == 0) {
            return Optional.empty();
        }
        T result = at(0);
        for (int i = 1; i < n; i++) {
            result = op.apply(result, at(i));
        }
        return Optional.of(result);
    }

    default T reduce(T identity, BinaryOperator<T> op) {
        final int n = size();
        if (n == 0) {
            return identity;
        }
        T result = identity;
        for (int i = 0; i < n; i++) {
            result = op.apply(result, at(i));
        }
        return result;
    }

    default T fold(T value, BiFunction<T, T, T> f) {
        switch (size()) {
            case 0:
                return value;
            case 1:
                return f.apply(value, at(0));
            default:
                return f.apply(value, tail().fold(at(0), f));
        }
    }

    default ImmArray<T> distinct() {
        return of(new LinkedHashSet<>(toList()));
    }

    default ImmArray<T> filter(Predicate<? super T> pred) {
        final int n = size();
        List<T> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            T o = at(i);
            if (pred.test(o)) {
                a.add(o);
            }
        }
        return of(a);
    }

    default ImmArray<T> sort() {
        T[] values = toArray();
        Arrays.sort(values);
        return of(values);
    }

    default ImmArray<T> sortWith(Comparator<T> cmp) {
        T[] values = toArray();
        Arrays.sort(values, cmp);
        return of(values);
    }

    default ImmArray<T> reverse() {
        T[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            T x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return of(a);
    }

    T[] toArray();

    default T[] toArray(IntFunction<T[]> generator) {
        final int n = size();
        T[] a = generator.apply(n);
        for (int i = 0; i < n; i++) {
            a[i] = at(i);
        }
        return a;
    }

    default List<T> toList() {
        return Arrays.asList(toArray());
    }

    default Set<T> toSet() {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, toArray());
        return set;
    }

    default <R> Map<T, R> toMapWithKey(Function<? super T, ? extends R> mapper) {
        Map<T, R> m = new HashMap<>();
        for (T k : this) {
            m.put(k, mapper.apply(k));
        }
        return m;
    }

    default <R> Map<R, T> toMapWithValue(Function<? super T, ? extends R> mapper) {
        Map<R, T> m = new HashMap<>();
        for (T k : this) {
            m.put(mapper.apply(k), k);
        }
        return m;
    }

    default Stream<T> stream() {
        return toList().stream();
    }

    default String[] toStringArray() {
        final int n = size();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = Objects.toString(at(i), null);
        }
        return a;
    }

}
