package minestra.text;

import static minestra.text.TestUtils.fail;
import static org.junit.Assert.*;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import org.junit.Test;

public final class ResourceSheafTest {

    @Test
    public void testGetPathList() {
        ResourceSheaf res;
        res = ResourceSheaf.create();
        assertEquals("[]", res.getPathList().toString());
        res = res.withName("rename");
        assertEquals("[rename]", res.getPathList().toString());
        res = ResourceSheaf.create(getClass());
        assertEquals("[minestra/text/ResourceSheafTest]", res.getPathList().toString());
        res = res.withName("test");
        assertEquals("[minestra/text/test]", res.getPathList().toString());
    }

    @Test
    public void testReadTable() {
        final String resName = getClass().getName().replace('.', '/') + "_readTable";
        ResourceSheaf res = ResourceSheaf.create().withName(resName);
        assertEquals("test", res.stringOpt("key1").orElseThrow(fail("empty")));
        assertEquals("skip-comment", res.stringOpt("key2").orElseThrow(fail("empty")));
        assertEquals("key3", res.stringOpt("key3").orElseThrow(fail("empty")));
        assertEquals("AB", res.stringOpt("eof").orElseThrow(fail("empty")));
    }

    @Test
    public void testCreate() {
        ResourceSheaf res;
        res = ResourceSheaf.create();
        assertEquals(Optional.empty(), res.stringOpt("key1"));
        res = ResourceSheaf.create().withName("default").withExtension("txt");
        assertEquals("value1", res.stringOpt("key1").orElseThrow(fail("empty")));
        res = ResourceSheaf.create(getClass());
        assertEquals("overwritten-value1", res.stringOpt("key1").orElseThrow(fail("empty")));
        res = res.withLocales(Locale.JAPANESE);
        assertEquals("\u3042", res.stringOpt("key1").orElseThrow(fail("empty")));
        res = ResourceSheaf.create(getClass()).withLocales(Locale.ENGLISH);
        assertEquals("the", res.stringOpt("key1").orElseThrow(fail("empty")));
        res = res.withExtension("txt");
        assertEquals("ofText", res.stringOpt("key1").orElseThrow(fail("empty")));
        res = ResourceSheaf.create().withLocation("default").withLocales(Locale.JAPAN).withExtension("txt");
        assertEquals("value1ja", res.stringOpt("key1").orElseThrow(fail("empty")));
    }

    @Test
    public void testWithLocation() {
        ResourceSheaf res;
        res = ResourceSheaf.create().withLocation("minestra/");
        assertEquals("[minestra/]", res.getPathList().toString());
        res = res.withMessages();
        assertEquals("[minestra/messages]", res.getPathList().toString());
        res = res.withClass(getClass());
        assertEquals("[minestra/text/ResourceSheafTest]", res.getPathList().toString());
        System.out.println(res);
    }

    @Test
    public void testWithLocales() {
        ResourceSheaf res;
        res = ResourceSheaf.create(getClass()).withLocales(Locale.ENGLISH);
        assertEquals("[minestra/text/ResourceSheafTest_en, minestra/text/ResourceSheafTest]",
            res.getPathList().toString());
    }

    @Test
    public void testWithExtension() {
        ResourceSheaf res;
        res = ResourceSheaf.create(getClass()).withExtension("txt");
        assertEquals("[minestra/text/ResourceSheafTest.txt]", res.getPathList().toString());
        res = res.withName("rename");
        assertEquals("[minestra/text/rename]", res.getPathList().toString());
    }

    @Test
    public void testWithClass() {
    }

    @Test
    public void testWithName() {
    }

    @Test
    public void testWithMessages() {
        ResourceSheaf res;
        res = ResourceSheaf.create().withMessages();
        assertEquals("/messages", res.stringOpt("name").orElseThrow(fail("empty")));
    }

    @Test
    public void testDerive() {
        ResourceSheaf parent = ResourceSheaf.create().withMessages();
        ResourceSheaf res = parent.derive().withClass(getClass()).withLocales(Locale.ENGLISH);
        assertEquals("the", res.stringOpt("key1").orElseThrow(fail("empty")));
        assertEquals("parent(/messages)", res.stringOpt("parentName").orElseThrow(fail("empty")));
    }

    @Test
    public void testToResourceBundle() {
        ResourceSheaf res = ResourceSheaf.create(getClass());
        ResourceBundle rb = res.toResourceBundle();
        List<String> keys = Collections.list(rb.getKeys());
        Collections.sort(keys);
        assertEquals("[key1, key2, key3a, key3b, key3c]", keys.toString());
        for (String key : keys) {
            assertEquals(res.string(key), rb.getString(key));
        }
    }

    @Test
    public void testStringOpt() {
    }

    @Test
    public void testIntegerOpt() {
    }

}
