package minestra.file;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import minestra.collection.ImmArray;

public class PathIteratorTest {

    @Test
    public void testPathIterator() throws IllegalAccessException {
        PathIterator o = new PathIterator(Paths.get(""), 3);
        assertEquals(Integer.valueOf(1), FieldUtils.readDeclaredField(o, "rootDepth", true));
        assertEquals(Integer.valueOf(3), FieldUtils.readDeclaredField(o, "maxDepth", true));
    }

    @Test
    public void testHasNext() {
    }

    @Test
    public void testNext() {
    }

    @Test
    public void testIterator() {
        PathIterator it = PathIterator.of(Paths.get(""));
        for (Path path : it) {
            assertEquals(Paths.get(""), path);
            break;
        }
    }

    @Test
    public void testOfPath() {
    }

    @Test
    public void testOfPathInt() {
    }

    @Test
    public void testStreamOfPath() {
        Stream<Path> st = PathIterator.streamOf(Paths.get(""));
        ImmArray<Path> a = ImmArray.of(st);
        assertEquals(Paths.get(""), a.head());
    }

    @Test
    public void testStreamOfPathInt() {
    }

    @Test
    public void testTraverse() {
    }

    @Test
    public void testErr() {
        // prepare mock STDERR
        PrintStream err = System.err;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setErr(new PrintStream(bos));
        // main
        PathIterator x = PathIterator.of(Paths.get(""));
        x.err(new IOException("test1"), Paths.get("."));
        assertEquals("minestra.file.PathIterator: '.': test1 (IOException)", StringUtils.chomp(bos.toString()));
        bos.reset();
        x.err(new AccessDeniedException("test2"), Paths.get("."));
        assertEquals("minestra.file.PathIterator: '.': access denied", StringUtils.chomp(bos.toString()));
        bos.reset();
        x.err(new NoSuchFileException("test3"), Paths.get("."));
        assertEquals("minestra.file.PathIterator: '.': no such file or directory", StringUtils.chomp(bos.toString()));
        // restore STDERR
        System.setErr(err);
    }

    @Test
    public void testGetErrorHandler() {
        PathIterator o = PathIterator.of(Paths.get(""));
        BiConsumer<Exception, Path> f = (e, path) -> {
            // empty
        };
        o.setErrorHandler(f);
        assertSame(f, o.getErrorHandler());
    }

    @Test
    public void testSetErrorHandler() {
        File f = new File("00000000000000000000.nosuchdir");
        if (f.exists()) {
            return;
        }
        PathIterator o = PathIterator.of(f.toPath());
        List<String> errors = new ArrayList<>();
        o.setErrorHandler((e, path) -> {
            errors.add(String.format("%s_%s", e.getClass().getSimpleName(), PathString.name(path)));
        });
        o.forEach(x -> {
        });
        assertEquals("NoSuchFileException_00000000000000000000.nosuchdir", errors.get(0));
    }

}
