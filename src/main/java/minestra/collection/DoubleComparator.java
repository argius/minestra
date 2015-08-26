package minestra.collection;

/**
 * A comparator for natural ordering of primitive <code>double</code>.
 */
@FunctionalInterface
public interface DoubleComparator {

    /**
     * The instance of this comparator.
     */
    DoubleComparator NATURAL = new DoubleComparator() {
        @Override
        public int compare(double a, double b) {
            return (a == b) ? 0 : (a < b) ? -1 : 1;
        }
    };

    /**
     * The instance of this comparator for the reversed ordering.
     */
    DoubleComparator REVERSE = new DoubleComparator() {
        @Override
        public int compare(double a, double b) {
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
    int compare(double a, double b);

    /**
     * Return <code>true</code> if <code>A</code> is greater than <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean gt(double a, double b) {
        return compare(a, b) > 0;
    }

    /**
     * Return <code>true</code> if <code>A</code> is equals to <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean eq(double a, double b) {
        return compare(a, b) == 0;
    }

    /**
     * Return <code>true</code> if <code>A</code> is less than <code>B</code>.
     * @param a value A
     * @param b value B
     * @return result
     */
    default boolean lt(double a, double b) {
        return compare(a, b) < 0;
    }

}
