package minestra.file;

import static minestra.file.PathString.*;
import static org.junit.Assert.*;
import java.nio.file.Paths;
import org.junit.Test;

public class PathStringTest {

    @Test
    public void testExtension() {
        assertEquals("java", extension("xxx.java").orElse(""));
        assertFalse(extension("java").isPresent());
        assertEquals("classpath", extension(".classpath").orElse(""));
    }

    @Test
    public void testHasExtension() {
        PathString o = new PathString(Paths.get("xxx.java"));
        assertTrue(o.hasExtension("java"));
        assertTrue(o.hasExtension("Java"));
        assertFalse(o.hasExtension("jar"));
    }

    @Test(expected = NullPointerException.class)
    public void testHasExtensionNPE() {
        PathString o = new PathString(Paths.get("xxx.java"));
        o.hasExtension(null);
    }

}
