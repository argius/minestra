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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import java.util.stream.StreamSupport;

/**
 * An immutable array of <code>T</code>.
 * @param <T> the type of the array elements
 */
public interface ImmArray<T> extends Iterable<T> {

    /**
     * Returns a new ImmArray of the specified values.
     * @param <T> the type of the array elements
     * @param a values as an array
     * @return the new ImmArray
     */
    @SafeVarargs
    static <T> ImmArray<T> of(T... a) {
        return new ImmArrayImpl<>(a);
    }

    /**
     * Returns a new ImmArray of the specified collection.
     * @param <T> the type of the array elements
     * @param list a values as a collection
     * @return the new ImmArray
     */
    static <T> ImmArray<T> of(Collection<T> list) {
        return new ImmArrayImpl<>(list);
    }

    /**
     * Returns a new ImmArray of the specified stream.
     * @param <T> the type of the array elements
     * @param stream a values as a stream
     * @return the new ImmArray
     */
    static <T> ImmArray<T> of(Stream<T> stream) {
        return new ImmArrayImpl<>(stream.collect(Collectors.toList()));
    }

    /**
     * Returns the concatenated array of all specified arrays.
     * @param first the first array
     * @param rest the rest of arrays
     * @return the concatenated array
     */
    @SuppressWarnings("unchecked")
    default ImmArray<T> concat(ImmArray<? extends T> first, ImmArray<? extends T>... rest) {
        int sizes = size() + first.size();
        for (int i = 0; i < rest.length; i++) {
            sizes += rest[i].size();
        }
        List<T> a = new ArrayList<>(sizes);
        Collections.addAll(a, toArray());
        Collections.addAll(a, first.toArray());
        for (ImmArray<? extends T> o : rest) {
            Collections.addAll(a, o.toArray());
        }
        return of(a);
    }

    /**
     * Returns the number of elements in this array.
     * @return the number of elements
     */
    default int size() {
        return toArray().length;
    }

    /**
     * Returns the element at the specified index in this array.
     * @param index index of an desired element
     * @return the element
     */
    default T at(int index) {
        return toArray()[index];
    }

    /**
     * Returns an empty array.
     * @param <T> the type of the array elements
     * @return the empty array
     */
    static <T> ImmArray<T> empty() {
        @SuppressWarnings("unchecked")
        ImmArray<T> o = (ImmArray<T>) ImmArrayImpl.EMPTY;
        return o;
    }

    /**
     * Performs apply function as an action to each element of this array.
     * @param action action to apply to each element
     */
    @Override
    default void forEach(Consumer<? super T> action) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            action.accept(at(i));
        }
    }

    /**
     * Returns whether this array is empty.
     * @return <code>true</code> if this array is empty.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns whether the element which satisfies predicate exists in this array.
     * @param pred predicate
     * @return <code>true</code> if the element exists in this array
     */
    default boolean exists(Predicate<? super T> pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether this array contains the element which is same as specified element.
     * @param o object to find
     * @return <code>true</code> if this array contains specified element
     */
    default boolean contains(T o) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (Objects.equals(at(i), o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an optional value which satisfies specified predicate from the beginning in this array.
     * @param pred predicate
     * @return the optional value
     */
    default Optional<T> find(Predicate<? super T> pred) {
        return find(pred, 0);
    }

    /**
     * Returns an optional value which satisfies specified predicate after the specified index in this array.
     * @param pred predicate
     * @param start number of beginning index to find
     * @return the element as an optional value
     */
    default Optional<T> find(Predicate<? super T> pred, int start) {
        for (int i = start, n = size(); i < n; i++) {
            T o = at(i);
            if (pred.test(o)) {
                return Optional.of(o);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the index of first element which is same as specified object in this array.
     * @param o object to find
     * @return number of the index, returns <code>-1</code> if not found
     */
    default int indexOf(T o) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (Objects.equals(at(i), o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the index of element which is the first match of the specified predicate in this array.
     * @param pred predicate
     * @return number of the index, returns <code>-1</code> if not found
     */
    default int indexWhere(Predicate<? super T> pred) {
        final int n = size();
        for (int i = 0; i < n; i++) {
            if (pred.test(at(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the first element of this array if it is not empty.
     * @return the first element
     * @throws NoSuchElementException if this array is empty
     */
    default T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head");
        }
        return at(0);
    }

    /**
     * Returns the last element of this array if it is not empty.
     * @return the last element
     * @throws NoSuchElementException if this array is empty
     */
    default T last() {
        if (isEmpty()) {
            throw new NoSuchElementException("last");
        }
        return at(size() - 1);
    }

    /**
     * Returns the tail elements of this array if exists.
     * @return the tail elements as an array
     */
    default ImmArray<T> tail() {
        return (size() > 1) ? slice(1, Integer.MAX_VALUE) : empty();
    }

    /**
     * Returns a new array of elements that taken out as many as specified number from the beginning of this array.
     * @param count count to take
     * @return the array
     */
    default ImmArray<T> take(int count) {
        return (count == 0) ? empty() : slice(0, count - 1);
    }

    /**
     * Returns a new array of elements that taken out only while the specified predicate matches from the beginning of this array.
     * @param pred predicate
     * @return the array
     */
    default ImmArray<T> takeWhile(Predicate<? super T> pred) {
        final int index = indexWhere(pred.negate());
        return (index > 0) ? slice(0, index - 1) : empty();
    }

    /**
     * Returns the rest of the array that drops the specified number of elements in this array.
     * @param count count to drop
     * @return the array
     */
    default ImmArray<T> drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : slice(count, n);
    }

    /**
     * Returns the rest of the array that drops elements which the specified predicate matches in this array.
     * @param pred predicate
     * @return the array
     */
    default ImmArray<T> dropWhile(Predicate<? super T> pred) {
        final int index = indexWhere(pred.negate());
        return (index >= 0) ? drop(index) : empty();
    }

    /**
     * Returns the slice of this array.
     * @param from inclusive index of first
     * @param to exclusive index of end
     * @return the array
     */
    default ImmArray<T> slice(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return of(toList().subList(from, to0 + 1));
    }

    /**
     * Returns an array created by applying a function to each of the elements of this array.
     * @param <R> result type of element
     * @param mapper function as a mapper
     * @return the array
     */
    default <R> ImmArray<R> map(Function<? super T, ? extends R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            a.add(mapper.apply(at(i)));
        }
        return ImmArray.of(a);
    }

    /**
     * Returns a mapped int array created by applying a function to each of the elements of this array.
     * @param mapper function as a mapper
     * @return the mapped array
     */
    default IntImmArray mapToInt(ToIntFunction<? super T> mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsInt(at(i));
        }
        return IntImmArray.of(a);
    }

    /**
     * Returns a mapped long array created by applying a function to each of the elements of this array.
     * @param mapper function as a mapper
     * @return the mapped array
     */
    default LongImmArray mapToLong(ToLongFunction<? super T> mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsLong(at(i));
        }
        return LongImmArray.of(a);
    }

    /**
     * Returns a mapped double array created by applying a function to each of the elements of this array.
     * @param mapper function as a mapper
     * @return the mapped array
     */
    default DoubleImmArray mapToDouble(ToDoubleFunction<? super T> mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = mapper.applyAsDouble(at(i));
        }
        return DoubleImmArray.of(a);
    }

    /**
     * Returns the array of elements to which flattened sub-level containers of this array.
     * This array includes not only containers of sub-level, but flat level elements.
     * @param <R> type of result elements
     * @return the flatten array
     */
    default <R> ImmArray<R> flatten() {
        return of(ImmArrayImpl.flatten0(this, new ArrayList<>()));
    }

    /**
     * Returns an array created by applying a function to each of the filtered elements of this array.
     * @param <R> type of result array elements
     * @param mapper function as a mapper
     * @return the array
     */
    default <R> ImmArray<R> filterMap(Function<? super T, Optional<? extends R>> mapper) {
        T[] values = toArray();
        final int n = values.length;
        List<R> a = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            mapper.apply(values[i]).ifPresent(a::add);
        }
        return of(a);
    }

    /**
     * Returns an array created by applying a function to each of the elements of flatten this array.
     * @param <S> type of input array elements
     * @param <R> type of result array elements
     * @param mapper function as a mapper
     * @return the array
     */
    default <S, R> ImmArray<R> flatMap(Function<? super S, ? extends R> mapper) {
        List<S> a = new ArrayList<>();
        return of(ImmArrayImpl.flatten0(this, a)).map(mapper);
    }

    /**
     * Returns the result that reduce elements of this array with the specified binary operator.
     * @param op binary operator
     * @return the result as optional, or optional empty if this array is empty
     */
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

    /**
     * Returns the result that reduce elements of this array with the specified binary operator.
     * @param identity identity element
     * @param op binary operator
     * @return the result
     */
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

    /**
     * Returns the result of folding the elements of this array by the specified operator.
     * @param value initial value
     * @param op binary operator
     * @return the result
     */
    default T fold(T value, BinaryOperator<T> op) {
        switch (size()) {
            case 0:
                return value;
            case 1:
                return op.apply(value, at(0));
            default:
                return op.apply(value, tail().fold(at(0), op));
        }
    }

    /**
     * Returns a new array which consists with unique elements.
     * @return the array
     */
    default ImmArray<T> distinct() {
        return of(new LinkedHashSet<>(toList()));
    }

    /**
     * Returns the result of filtering the elements of this array by specified predicate.
     * @param pred predicate
     * @return the array
     */
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

    /**
     * Returns new array which is sorted this array in natural order.
     * @return the sorted array
     */
    default ImmArray<T> sort() {
        T[] values = toArray();
        Arrays.sort(values);
        return of(values);
    }

    /**
     * Returns new array which is sorted this array with specified order.
     * @param cmp order
     * @return the sorted array
     */
    default ImmArray<T> sortWith(Comparator<T> cmp) {
        T[] values = toArray();
        Arrays.sort(values, cmp);
        return of(values);
    }

    /**
     * Returns new array which is reversed the order of elements in this array.
     * @return the array
     */
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

    /**
     * Returns this array as a native array.
     * @return the array
     */
    T[] toArray();

    /**
     * Returns this array as a native array.
     * @param generator function to generate native array
     * @return the array
     */
    default T[] toArray(IntFunction<T[]> generator) {
        final int n = size();
        T[] a = generator.apply(n);
        for (int i = 0; i < n; i++) {
            a[i] = at(i);
        }
        return a;
    }

    /**
     * Returns the list which consists same elements in this array.
     * @return the list
     */
    default List<T> toList() {
        return Arrays.asList(toArray());
    }

    /**
     * Returns the set which consists same elements in this array.
     * @return the set
     */
    default Set<T> toSet() {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, toArray());
        return set;
    }

    /**
     * Returns the map that is created with each element as value and the key generated by specified generator.
     * @param <R> type of value in the result map
     * @param gen key generator
     * @return the map
     */
    default <R> Map<T, R> toMapWithKey(Function<? super T, ? extends R> gen) {
        Map<T, R> m = new HashMap<>();
        for (T k : this) {
            m.put(k, gen.apply(k));
        }
        return m;
    }

    /**
     * Returns the map that is created with each element as key and the value generated by specified generator.
     * @param <R> type of key in the result map
     * @param gen value generator
     * @return the map
     */
    default <R> Map<R, T> toMapWithValue(Function<? super T, ? extends R> gen) {
        Map<R, T> m = new HashMap<>();
        for (T v : this) {
            m.put(gen.apply(v), v);
        }
        return m;
    }

    /**
     * Returns the stream which consist same elements in this array.
     * @return the stream
     */
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns this array as a native string array.
     * If an element is <code>null</code>, it converts an empty string.
     * @return the string array
     */
    default String[] toStringArray() {
        final int n = size();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = Objects.toString(at(i), "");
        }
        return a;
    }

}
