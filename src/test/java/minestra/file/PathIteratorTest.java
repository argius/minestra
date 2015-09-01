package minestra.file;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import minestra.collection.ImmArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

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
        assertEquals("potf: '.': test1 (IOException)", StringUtils.chomp(bos.toString()));
        bos.reset();
        x.err(new AccessDeniedException("test2"), Paths.get("."));
        assertEquals("potf: '.': access denied", StringUtils.chomp(bos.toString()));
        bos.reset();
        x.err(new NoSuchFileException("test3"), Paths.get("."));
        assertEquals("potf: '.': no such file or directory", StringUtils.chomp(bos.toString()));
        // restore STDERR
        System.setErr(err);
    }

}
