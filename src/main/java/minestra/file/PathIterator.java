package minestra.file;

import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * {@code PathIterator} privides a find-like feature.
 *
 * <p>
 * This feature is similar to {@link java.nio.file.Files#find}.
 * However, {@code Files.find} encounts {@code IOException} and stop finding.
 * </p>
 *
 * <p>
 * {@code PathIterator} keep finding with printing the warning by default,
 * when it encounts {@code IOException}.
 * </p>
 */
public final class PathIterator implements Iterator<Path>, Iterable<Path> {

    private final int rootDepth;
    private final int maxDepth;
    private final Queue<Path> q;
    private final Queue<Path> dirs;
    private BiConsumer<Exception, Path> errorHandler;

    PathIterator(Path root, int maxDepth) {
        this.rootDepth = root.getNameCount();
        this.maxDepth = maxDepth;
        this.q = new LinkedList<>();
        this.dirs = new LinkedList<>();
        this.errorHandler = this::err;
        q.offer(root);
        dirs.offer(root);
    }

    /**
     * Returns whether it remains next paths.
     * @return true if it remains next paths, otherwise false
     */
    @Override
    public boolean hasNext() {
        if (!dirs.isEmpty()) {
            traverse(128);
        }
        return !q.isEmpty();
    }

    /**
     * Return a next Path.
     * @return the Path. Returns null if there are no more paths
     */
    @Override
    public Path next() {
        return q.poll();
    }

    /**
     * Returns an PathIterator as Iterator.
     * @return the Iterator of PathIterator
     */
    @Override
    public Iterator<Path> iterator() {
        return this;
    }

    /**
     * Returns a PathIterator.
     * @param root root directory to find
     * @return the PathIterator
     */
    public static PathIterator of(Path root) {
        return of(root, Integer.MAX_VALUE);
    }

    /**
     * Returns a PathIterator.
     * @param root root directory to find
     * @param maxDepth the maximum number of directory levels to find
     * @return the PathIterator
     */
    public static PathIterator of(Path root, int maxDepth) {
        return new PathIterator(root, maxDepth);
    }

    /**
     * Returns a PathIterator as Stream.
     * @param root root directory to find
     * @return the Stream of PathIterator
     */
    public static Stream<Path> streamOf(Path root) {
        return streamOf(root, Integer.MAX_VALUE);
    }

    /**
     * Returns a PathIterator as Stream.
     * @param root root directory to find
     * @param maxDepth the maximum number of directory levels to find
     * @return the Stream of PathIterator
     */
    public static Stream<Path> streamOf(Path root, int maxDepth) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new PathIterator(root, maxDepth), 0), false);
    }

    /**
     * Get a <code>BiConsumer</code> object as an error handler.
     * The default is <code>PathIterator.err</code> (not public).
     * @return <code>BiConsumer</code> object as an error handler
     * @since 1.1
     */
    public BiConsumer<Exception, Path> getErrorHandler() {
        return errorHandler;
    }

    /**
     * Set a <code>BiConsumer</code> object as an error handler.
     * @param errorHandler <code>BiConsumer</code> object as an error handler
     * @since 1.1
     */
    public void setErrorHandler(BiConsumer<Exception, Path> errorHandler) {
        this.errorHandler = errorHandler;
    }

    void traverse(int requiredSize) {
        while (q.size() < requiredSize) {
            if (dirs.isEmpty()) {
                break;
            }
            Path dir = dirs.poll();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                stream.forEach(x -> {
                    if ((x.getNameCount() - rootDepth) <= maxDepth) {
                        q.offer(x);
                        if (Files.isDirectory(x)) {
                            dirs.offer(x);
                        }
                    }
                });
            } catch (Exception e) {
                errorHandler.accept(e, dir);
            }
        }
    }

    void err(Exception e, Path path) {
        final String msg;
        if (e instanceof AccessDeniedException) {
            msg = "access denied";
        }
        else if (e instanceof NoSuchFileException) {
            msg = "no such file or directory";
        }
        else {
            msg = String.format("%s (%s)", e.getMessage(), e.getClass().getSimpleName());
        }
        System.err.printf("%s: '%s': %s%n", getClass().getName(), path, msg);
    }

}
