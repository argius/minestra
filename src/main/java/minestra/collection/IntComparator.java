package minestra.collection;

/**
 * A comparator for natural ordering of primitive <code>int</code>.
 */
@FunctionalInterface
public interface IntComparator {

    /**
     * The instance of this comparator.
     */
    IntComparator NATURAL = new IntComparator() {
        @Override
        public int compare(int a, int b) {
            return (a == b) ? 0 : (a < b) ? -1 : 1;
        }
    };

    /**
     * The instance of this comparator for the reversed ordering.
     */
    IntComparator REVERSE = new IntComparator() {
        @Override
        public int compare(int a, int b) {
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
    int compare(int a, int b);

    /**
     * Return <code>true</code> if <code>A</code> is greater than <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean gt(int a, int b) {
        return compare(a, b) > 0;
    }

    /**
     * Return <code>true</code> if <code>A</code> is equals to <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean eq(int a, int b) {
        return compare(a, b) == 0;
    }

    /**
     * Return <code>true</code> if <code>A</code> is less than <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean lt(int a, int b) {
        return compare(a, b) < 0;
    }

}
