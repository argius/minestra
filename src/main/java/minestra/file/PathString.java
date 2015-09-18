package minestra.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.Optional;

/**
 * PathString is an extension of {@link Path} and privides features about path string.
 */
public final class PathString implements Path {

    private final Path path;

    /**
     * Ctor.
     * @param path path
     */
    public PathString(Path path) {
        this.path = path;
    }

    /**
     * Returns an instance of PathString.
     * @param path path
     * @return the instance
     */
    public static PathString of(Path path) {
        return new PathString(path);
    }

    /**
     * Returns an instance of PathString.
     * @param s path as string
     * @return the instance
     */
    public static PathString of(String s) {
        return new PathString(Paths.get(s));
    }

    /**
     * Returns the name of specified path.
     * This name should be same as the result of {@link File#getName()}.
     * {@link File#getName()}
     * @param path path
     * @return the name of path
     */
    public static String name(Path path) {
        return path.toFile().getName();
    }

    /**
     * Returns the name of this path.
     * This name should be same as the result of {@link File#getName()}.
     * @return the name of path
     */
    public String name() {
        return name(path);
    }

    /**
     * Returns the extension string of specified path.
     * @param filename file name
     * @return the extension string
     */
    public static Optional<String> extension(CharSequence filename) {
        final String s = String.valueOf(filename);
        final int index = s.lastIndexOf('.');
        if (index >= 0) {
            return Optional.of(s.substring(index + 1));
        }
        return Optional.empty();
    }

    /**
     * Returns the extension string of specified path.
     * @param path a path
     * @return the extension string
     */
    public static Optional<String> extension(Path path) {
        return extension(path.toString());
    }

    /**
     * Returns whether this path has specified extension.
     * @param extension extension string
     * @return <code>true</code> if this path has specified path
     */
    public boolean hasExtension(String extension) {
        if (extension == null) {
            throw new NullPointerException();
        }
        Optional<String> optExt = extension(path);
        if (optExt.isPresent()) {
            return optExt.get().equalsIgnoreCase(extension);
        }
        return false;
    }

    @Override
    public int compareTo(Path other) {
        return path.compareTo(other);
    }

    @Override
    public boolean endsWith(Path other) {
        return path.endsWith(other);
    }

    @Override
    public boolean endsWith(String other) {
        return path.endsWith(other);
    }

    @Override
    public boolean equals(Object other) {
        return path.equals(other);
    }

    @Override
    public Path getFileName() {
        return path.getFileName();
    }

    @Override
    public FileSystem getFileSystem() {
        return path.getFileSystem();
    }

    @Override
    public Path getName(int index) {
        return path.getName(index);
    }

    @Override
    public int getNameCount() {
        return path.getNameCount();
    }

    @Override
    public Path getParent() {
        return path.getParent();
    }

    @Override
    public Path getRoot() {
        return path.getRoot();
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean isAbsolute() {
        return path.isAbsolute();
    }

    @Override
    public Iterator<Path> iterator() {
        return path.iterator();
    }

    @Override
    public Path normalize() {
        return path.normalize();
    }

    @Override
    public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException {
        return path.register(watcher, events);
    }

    @Override
    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
        return path.register(watcher, events, modifiers);
    }

    @Override
    public Path relativize(Path other) {
        return path.relativize(other);
    }

    @Override
    public Path resolve(Path other) {
        return path.resolve(other);
    }

    @Override
    public Path resolve(String other) {
        return path.resolve(other);
    }

    @Override
    public Path resolveSibling(Path other) {
        return path.resolveSibling(other);
    }

    @Override
    public Path resolveSibling(String other) {
        return path.resolveSibling(other);
    }

    @Override
    public boolean startsWith(Path other) {
        return path.startsWith(other);
    }

    @Override
    public boolean startsWith(String other) {
        return path.startsWith(other);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return path.subpath(beginIndex, endIndex);
    }

    @Override
    public Path toAbsolutePath() {
        return path.toAbsolutePath();
    }

    @Override
    public File toFile() {
        return path.toFile();
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return path.toRealPath(options);
    }

    @Override
    public String toString() {
        return path.toString();
    }

    @Override
    public URI toUri() {
        return path.toUri();
    }

}
