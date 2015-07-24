package minestra.file;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class PathIterator implements Iterator<Path>, Iterable<Path> {

    private final int rootDepth;
    private final int maxDepth;
    private final Queue<Path> q;
    private final Queue<Path> dirs;

    PathIterator(Path root, int maxDepth) {
        this.rootDepth = root.getNameCount();
        this.maxDepth = maxDepth;
        this.q = new LinkedList<>();
        this.dirs = new LinkedList<>();
        q.offer(root);
        dirs.offer(root);
    }

    @Override
    public boolean hasNext() {
        if (!dirs.isEmpty()) {
            traverse(128);
        }
        return !q.isEmpty();
    }

    @Override
    public Path next() {
        return q.poll();
    }

    @Override
    public Iterator<Path> iterator() {
        return this;
    }

    public static PathIterator of(Path root) {
        return of(root, Integer.MAX_VALUE);
    }

    public static PathIterator of(Path path, int maxDepth) {
        return new PathIterator(path, maxDepth);
    }

    public static Stream<Path> streamOf(Path root) {
        return streamOf(root, Integer.MAX_VALUE);
    }

    public static Stream<Path> streamOf(Path root, int maxDepth) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new PathIterator(root, maxDepth), 0), false);
    }

    void traverse(int requiredSize) {
        while (q.size() < requiredSize) {
            if (dirs.isEmpty()) {
                break;
            }
            Path dir = dirs.poll();
            try {
                Files.newDirectoryStream(dir).forEach(x -> {
                    if ((x.getNameCount() - rootDepth) <= maxDepth) {
                        q.offer(x);
                        if (Files.isDirectory(x)) {
                            dirs.offer(x);
                        }
                    }
                });
            } catch (IOException e) {
                err(e, dir);
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
        System.err.printf("potf: '%s': %s%n", path, msg);
    }

}
