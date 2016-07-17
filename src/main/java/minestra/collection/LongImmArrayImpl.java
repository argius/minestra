package minestra.collection;

import java.util.Arrays;
import java.util.OptionalLong;
import java.util.function.LongPredicate;

final class LongImmArrayImpl implements LongImmArray {

    static final LongImmArray EMPTY = new LongImmArrayImpl();

    final long[] values;

    LongImmArrayImpl(long... a) {
        this(false, a);
    }

    LongImmArrayImpl(boolean withoutCopying, long... a) {
        this.values = withoutCopying ? a : Arrays.copyOf(a, a.length);
    }

    @Override
    public long at(int index) {
        return values[index];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public LongImmArray filter(LongPredicate pred) {
        final int n = size();
        int p = 0;
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            final long x = values[i];
            if (pred.test(x)) {
                a[p++] = x;
            }
        }
        return new LongImmArrayImpl(Arrays.copyOf(a, p));
    }

    @Override
    public long sum() {
        final int n = size();
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return values[0];
        }
        long sum = values[0];
        for (int i = 1; i < n; i++) {
            sum += values[i];
        }
        return sum;
    }

    @Override
    public long product() {
        final int n = size();
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return values[0];
        }
        long product = values[0];
        for (int i = 1; i < n; i++) {
            product *= values[i];
        }
        return product;
    }

    @Override
    public OptionalLong max() {
        final int n = size();
        if (n == 0) {
            return OptionalLong.empty();
        }
        if (n == 1) {
            return OptionalLong.of(values[0]);
        }
        long max = values[0];
        for (int i = 1; i < n; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return OptionalLong.of(max);
    }

    @Override
    public OptionalLong min() {
        final int n = size();
        if (n == 0) {
            return OptionalLong.empty();
        }
        if (n == 1) {
            return OptionalLong.of(values[0]);
        }
        long min = values[0];
        for (int i = 1; i < n; i++) {
            if (values[i] < min) {
                min = values[i];
            }
        }
        return OptionalLong.of(min);
    }

    @Override
    public LongImmArray sortWith(int fromIndex, int toIndex, LongComparator cmp) {
        long[] a = toArray();
        sortWith0(a, fromIndex, toIndex, cmp);
        return new LongImmArrayImpl(a);
    }

    static void sortWith0(long[] a, int fromIndex, int toIndex, LongComparator cmp) {
        final int length = toIndex - fromIndex + 1;
        if (length < 2) {
            return;
        }
        if (length == 2) {
            if (cmp.gt(a[fromIndex], a[toIndex])) {
                long x = a[fromIndex];
                a[fromIndex] = a[toIndex];
                a[toIndex] = x;
            }
            return;
        }
        // FIXME bad performance
        final long pivot = a[fromIndex];
        int p1 = 0;
        int p2 = 0;
        long[] a1 = new long[length];
        long[] a2 = new long[length];
        for (int i = fromIndex + 1; i <= toIndex; i++) {
            final long v = a[i];
            if (cmp.lt(v, pivot)) {
                a1[p1++] = v;
            }
            else {
                a2[p2++] = v;
            }
        }
        int p = fromIndex;
        for (int i = 0; i < p1; i++) {
            a[p++] = a1[i];
        }
        a[p++] = pivot;
        for (int i = 0; i < p2; i++) {
            a[p++] = a2[i];
        }
        if (p1 > 0) {
            sortWith0(a, fromIndex, fromIndex + p1 - 1, cmp);
        }
        if (p2 > 0) {
            sortWith0(a, fromIndex + p1 + 1, toIndex, cmp);
        }
    }

    @Override
    public long[] toArray() {
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
        LongImmArrayImpl other = (LongImmArrayImpl) obj;
        if (!Arrays.equals(values, other.values)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

}
