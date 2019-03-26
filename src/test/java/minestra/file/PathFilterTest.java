package minestra.file;

import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public final class PathFilterTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Path path1;

    @Before
    public void createFile() throws IOException {
        this.path1 = tmpFolder.newFile("PathFilterTest1.txt").toPath();
        byte[] bytes = "123".getBytes();
        Files.write(path1, bytes);
        Files.setLastModifiedTime(path1, FileTime.from(t("2018-06-15T00:00:00Z")));
    }

    @Test
    public void testSizeEQ() {
        assertFalse(PathFilter.sizeEQ(2L).test(path1));
        assertTrue(PathFilter.sizeEQ(3L).test(path1));
        assertFalse(PathFilter.sizeEQ(4L).test(path1));
    }

    @Test
    public void testSizeLT() {
        assertFalse(PathFilter.sizeLT(2L).test(path1));
        assertFalse(PathFilter.sizeLT(3L).test(path1));
        assertTrue(PathFilter.sizeLT(4L).test(path1));
    }

    @Test
    public void testSizeLE() {
        assertFalse(PathFilter.sizeLE(2L).test(path1));
        assertTrue(PathFilter.sizeLE(3L).test(path1));
        assertTrue(PathFilter.sizeLE(4L).test(path1));
    }

    @Test
    public void testSizeGT() {
        assertTrue(PathFilter.sizeGT(2L).test(path1));
        assertFalse(PathFilter.sizeGT(3L).test(path1));
        assertFalse(PathFilter.sizeGT(4L).test(path1));
    }

    @Test
    public void testSizeGE() {
        assertTrue(PathFilter.sizeGE(2L).test(path1));
        assertTrue(PathFilter.sizeGE(3L).test(path1));
        assertFalse(PathFilter.sizeGE(4L).test(path1));
    }

    @Test
    public void testSizeBetween() {
        assertFalse(PathFilter.sizeBetween(0L, 2L).test(path1));
        assertTrue(PathFilter.sizeBetween(1L, 3L).test(path1));
        assertTrue(PathFilter.sizeBetween(2L, 4L).test(path1));
        assertTrue(PathFilter.sizeBetween(3L, 5L).test(path1));
        assertFalse(PathFilter.sizeBetween(4L, 6L).test(path1));
    }

    @Test
    public void testMtimeBefore() {
        assertFalse(PathFilter.mtimeBefore(t("2018-06-14T00:00:00Z")).test(path1));
        assertFalse(PathFilter.mtimeBefore(t("2018-06-15T00:00:00Z")).test(path1));
        assertTrue(PathFilter.mtimeBefore(t("2018-06-15T00:00:01Z")).test(path1));
        assertTrue(PathFilter.mtimeBefore(t("2018-06-16T00:00:00Z")).test(path1));
    }

    @Test
    public void testMtimeAfter() {
        assertTrue(PathFilter.mtimeAfter(t("2018-06-14T00:00:00Z")).test(path1));
        assertTrue(PathFilter.mtimeAfter(t("2018-06-14T23:59:59Z")).test(path1));
        assertFalse(PathFilter.mtimeAfter(t("2018-06-15T00:00:00Z")).test(path1));
        assertFalse(PathFilter.mtimeAfter(t("2018-06-16T00:00:00Z")).test(path1));
    }

    @Test
    public void testMtimeBetween() {
        assertFalse(PathFilter.mtimeBetween(t("2018-06-14T23:59:59Z"), t("2018-06-14T23:59:59Z")).test(path1));
        assertTrue(PathFilter.mtimeBetween(t("2018-06-14T23:59:59Z"), t("2018-06-15T00:00:00Z")).test(path1));
        assertTrue(PathFilter.mtimeBetween(t("2018-06-15T00:00:00Z"), t("2018-06-15T00:00:00Z")).test(path1));
        assertTrue(PathFilter.mtimeBetween(t("2018-06-15T00:00:00Z"), t("2018-06-15T00:00:01Z")).test(path1));
        assertFalse(PathFilter.mtimeBetween(t("2018-06-15T00:00:01Z"), t("2018-06-15T00:00:01Z")).test(path1));
    }

    @Test
    public void testMtimeBetween_IllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(Matchers.containsString("2018-06-14T23:59:59Z is after 2018-06-14T23:59:58Z"));
        PathFilter.mtimeBetween(t("2018-06-14T23:59:59Z"), t("2018-06-14T23:59:58Z"));
    }

    static Instant t(String timeFormat) {
        return Instant.parse(timeFormat);
    }
}
