package minestra.text;

import static minestra.text.PackagePrivateTestUtils.fail;
import static org.junit.Assert.*;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import org.junit.Test;
import minestra.TestUtils;

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
        ResourceSheaf res, res2;
        res = ResourceSheaf.create(getClass()).withExtension("txt");
        assertEquals("[minestra/text/ResourceSheafTest.txt]", res.getPathList().toString());
        res = res.withName("rename");
        assertEquals("[minestra/text/rename.txt]", res.getPathList().toString());
        res2 = res.derive();
        assertEquals("[minestra/text/rename.txt]", res2.getPathList().toString());
        res2 = res2.withMessages();
        assertEquals("[minestra/text/messages.txt]", res2.getPathList().toString());
        assertEquals("derive_suffix", res2.s("key1"));
        res2 = res2.withLocation("loc/name");
        assertEquals("[loc/name.txt]", res2.getPathList().toString());
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
        ResourceSheaf parent2 = ResourceSheaf.create().withName("default").withExtension("txt");
        ResourceSheaf res2 = parent2.derive().withClass(getClass()).withLocales(Locale.ENGLISH);
        assertEquals("[minestra/text/ResourceSheafTest_en.txt, minestra/text/ResourceSheafTest.txt]",
            res2.getPathList().toString());
        assertEquals("ofText", res2.stringOpt("key1").orElseThrow(fail("empty")));
        ResourceSheaf parent3 = ResourceSheaf.create().withName("default").withExtension("txt");
        ResourceSheaf res3 = parent3.derive().withClass(getClass()).withMessages();
        assertEquals("[minestra/text/messages.txt]", res3.getPathList().toString());
        assertEquals("derive_suffix", res3.stringOpt("key1").orElseThrow(fail("empty")));
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

    @Test
    public void testFormat() {
        ResourceSheaf res = ResourceSheaf.create(getClass()).withLocales(Locale.ENGLISH).withExtension("txt");
        assertEquals("ab-yz", res.format("formatMessage1", "b-y"));
        assertEquals("", res.format("formatMessage2", "A"));
    }

    @Test
    public void testFormatOpt() {
        ResourceSheaf res = ResourceSheaf.create(getClass()).withLocales(Locale.ENGLISH).withExtension("txt");
        assertEquals("a24z", res.formatOpt("formatMessage1", 24).get());
    }

    @Test
    public void testFKey() {
        Function<String, String> f = TestUtils.getStaticMethodInvokerWithForcedAccessibility(ResourceSheaf.class, "fKey", String.class);
        assertEquals("abc", f.apply("abc=def"));
        assertEquals("abc", f.apply("abc =def"));
        assertEquals("abc", f.apply(" abc=def"));
        assertEquals("abc", f.apply(" abc =def"));
        assertEquals("abc", f.apply("abc==def"));
        assertEquals("abcdef", f.apply("abcdef"));
    }

    @Test
    public void testFVal() {
        Function<String, String> f = TestUtils.getStaticMethodInvokerWithForcedAccessibility(ResourceSheaf.class, "fVal", String.class);
        assertEquals("def", f.apply("abc=def"));
        assertEquals("def", f.apply("abc= def"));
        assertEquals("def ", f.apply("abc=def\\"));
        assertEquals(" ", f.apply("abc=\\"));
        assertEquals("def\\g", f.apply("abc=def\\g"));
    }

    @Test
    public void testToString() {
        ResourceSheaf res = ResourceSheaf.create(getClass()).withLocales(Locale.ENGLISH).withExtension("txt");
        assertEquals("ResourceSheaf(parent=None, location=minestra/text/ResourceSheafTest, locales=[en], extension=txt)",
                     res.toString());
        ResourceSheaf child = res.derive().withLocales(Locale.GERMAN).withExtension(".properties");
        assertEquals("ResourceSheaf(parent="
                     + "ResourceSheaf(parent=None, location=minestra/text/ResourceSheafTest, locales=[en], extension=txt),"
                     + " location=minestra/text/ResourceSheafTest, locales=[de], extension=.properties)",
                     child.toString());
    }

}
