package minestra.collection;

/**
 * A comparator for natural ordering of primitive <code>long</code>.
 */
@FunctionalInterface
public interface LongComparator {

    /**
     * The instance of this comparator.
     */
    LongComparator NATURAL = new LongComparator() {
        @Override
        public int compareTo(long a, long b) {
            return (a == b) ? 0 : (a < b) ? -1 : 1;
        }
    };

    /**
     * The instance of this comparator for the reversed ordering.
     */
    LongComparator REVERSE = new LongComparator() {
        @Override
        public int compareTo(long a, long b) {
            return (a == b) ? 0 : (a > b) ? -1 : 1;
        }
    };

    /**
     * Compares <code>A</code> and <code>B</code> along the order of this instance.
     * @param a value A
     * @param b value B
     * @return result of comparing
     * @see java.util.Comparator
     */
    int compareTo(long a, long b);

    /**
     * Return <code>true</code> if <code>A</code> is greater than <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean gt(long a, long b) {
        return compareTo(a, b) > 0;
    }

    /**
     * Return <code>true</code> if <code>A</code> is equals to <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean eq(long a, long b) {
        return compareTo(a, b) == 0;
    }

    /**
     * Return <code>true</code> if <code>A</code> is less than <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean lt(long a, long b) {
        return compareTo(a, b) < 0;
    }

}
