package minestra.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

/**
 * PathFilter provides several methods to generate predicates for filtering objects of <code>java.nio.file.Path</code>.
 */
public final class PathFilter {

    private PathFilter() {
    }

    /**
     * Returns a predicate that checks if the file size equals the specified size.
     * @param size the specified size
     * @return a predicate as a size filter
     */
    public static Predicate<Path> sizeEQ(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("size=" + size);
        }
        return x -> {
            long v = size(x);
            return v >= 0 && v == size;
        };
    }

    /**
     * Returns a predicate that checks if the file size is less than the specified size.
     * @param size the specified size
     * @return a predicate as a size filter
     */
    public static Predicate<Path> sizeLT(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("size=" + size);
        }
        return x -> {
            long v = size(x);
            return v >= 0 && v < size;
        };
    }

    /**
     * Returns a predicate that checks if the file size is less than or equals the specified size.
     * @param size the specified size
     * @return a predicate as a size filter
     */
    public static Predicate<Path> sizeLE(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("size=" + size);
        }
        return x -> {
            long v = size(x);
            return v >= 0 && v <= size;
        };
    }

    /**
     * Returns a predicate that checks if the file size is greater than the specified size.
     * @param size the specified size
     * @return a predicate as a size filter
     */
    public static Predicate<Path> sizeGT(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("size=" + size);
        }
        return x -> size(x) > size;
    }

    /**
     * Returns a predicate that checks if the file size is greater than or equals the specified size.
     * @param size the specified size
     * @return a predicate as a size filter
     */
    public static Predicate<Path> sizeGE(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("size=" + size);
        }
        return x -> size(x) >= size;
    }

    /**
     * Returns a predicate that checks if the file size is between two specified sizes.
     * @param size1 the first specified size (inclusive)
     * @param size2 the second specified size (inclusive)
     * @return a predicate as a size filter
     */
    public static Predicate<Path> sizeBetween(long size1, long size2) {
        if (size1 < 0) {
            throw new IllegalArgumentException("size1=" + size1);
        }
        if (size2 < 0) {
            throw new IllegalArgumentException("size2=" + size2);
        }
        return x -> {
            long v = size(x);
            return v >= 0 && v >= size1 && v <= size2;
        };
    }

    private static long size(Path path) {
        if (Files.isDirectory(path)) {
            return 0L;
        }
        try {
            return Files.size(path);
        } catch (IOException e) {
        }
        return Long.MIN_VALUE;
    }

    /**
     * Returns a predicate that checks if the mtime of the file is before the specified time point.
     * @param time the spacified time (exclusive)
     * @return a predicate as a mtime filter
     */
    public static Predicate<Path> mtimeBefore(Instant time) {
        return x -> {
            Instant v = mtime(x);
            return v != null && v.isBefore(time);
        };
    }

    /**
     * Returns a predicate that checks if the mtime of the file is after the specified time point.
     * @param time the spacified time (exclusive)
     * @return a predicate as a mtime filter
     */
    public static Predicate<Path> mtimeAfter(Instant time) {
        return x -> {
            Instant v = mtime(x);
            return v != null && v.isAfter(time);
        };
    }

    /**
     * Returns a predicate that checks if the mtime of the file is between two specified time points.
     * @param time1 the first spacified time (inclusive)
     * @param time2 the second spacified time (inclusive)
     * @return a predicate as a mtime filter
     */
    public static Predicate<Path> mtimeBetween(Instant time1, Instant time2) {
        if (time1.isAfter(time2)) {
            throw new IllegalArgumentException(String.format("%s is after %s", time1, time2));
        }
        Instant t1 = time1.minus(1L, ChronoUnit.MILLIS);
        Instant t2 = time2.plus(1L, ChronoUnit.MILLIS);
        return x -> {
            Instant v = mtime(x);
            return v != null && v.isAfter(t1) && v.isBefore(t2);
        };
    }

    private static Instant mtime(Path path) {
        try {
            return Files.getLastModifiedTime(path).toInstant();
        } catch (IOException e) {
        }
        return null;
    }
}
