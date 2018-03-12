package minestra.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

final class ImmArrayImpl<T> implements ImmArray<T> {

    static final ImmArray<?> EMPTY = new ImmArrayImpl<>();

    final private T[] values;

    @SafeVarargs
    ImmArrayImpl(T... a) {
        this(false, a);
    }

    @SafeVarargs
    ImmArrayImpl(boolean withoutCopying, T... a) {
        this.values = withoutCopying ? a : Arrays.copyOf(a, a.length);
    }

    ImmArrayImpl(Collection<T> a) {
        @SuppressWarnings("unchecked")
        final T[] array = (T[]) a.toArray();
        this.values = array;
    }

    @Override
    public T at(int index) {
        return values[index];
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public ImmArray<T> filter(Predicate<? super T> pred) {
        final int n = size();
        List<T> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            if (pred.test(values[i])) {
                a.add(values[i]);
            }
        }
        return new ImmArrayImpl<>(a);
    }

    static <T, R> List<R> flatten0(Iterable<T> it, List<R> list) {
        for (T o : it) {
            if (o instanceof ImmArray) {
                @SuppressWarnings("unchecked")
                ImmArray<R> x = (ImmArray<R>) o;
                Collections.addAll(list, x.toArray());
            }
            else if (o instanceof Collection) {
                @SuppressWarnings("unchecked")
                Collection<R> x = (Collection<R>) o;
                list.addAll(x);
            }
            else if (o instanceof Stream) {
                try (@SuppressWarnings("unchecked")
                Stream<R> x = (Stream<R>) o) {
                    x.forEachOrdered(list::add);
                }
            }
            else if (o instanceof Optional) {
                @SuppressWarnings("unchecked")
                Optional<R> x = (Optional<R>) o;
                x.ifPresent(list::add);
            }
            else {
                @SuppressWarnings("unchecked")
                R x = (R) o;
                list.add(x);
            }
        }
        return list;
    }

    @Override
    public T[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        ImmArrayImpl other = (ImmArrayImpl) obj;
        if (!Arrays.equals(values, other.values)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    final class IteratorImpl implements Iterator<T> {

        private final int len;
        private int p;

        IteratorImpl() {
            this.len = values.length;
            this.p = -1;
        }

        @Override
        public boolean hasNext() {
            if (p + 1 < len) {
                ++p;
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public T next() {
            return at(p);
        }

    }

}
