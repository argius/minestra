package minestra.resource;

import static org.junit.Assert.*;
import java.util.Locale;
import org.junit.Test;

public class I18nResourceTest {

    static final String rootPackageName = getRootPackage();
    static final Locale JA = Locale.JAPANESE;
    static final I18nResource rootBase = I18nResource.create(Locale.JAPAN);
    static final I18nResource pkgBase = I18nResource.create("/" + rootPackageName + "/", JA);
    static final I18nResource res = rootBase.derive(I18nResourceTest.class, JA);
    static final I18nResource resEn = rootBase.derive(I18nResourceTest.class, Locale.ENGLISH, JA);

    static String getRootPackage() {
        return I18nResourceTest.class.getPackage().getName().replaceFirst("\\..+", "");
    }

    @Test
    public void testX() {
        // base
        assertEquals("value1ja", rootBase.string("key1"));
        assertEquals("value1ja", rootBase.s("key1"));
        assertEquals(336, rootBase.integer("key2"));
        assertEquals(336, rootBase.i("key2"));
        // pkg-base
        assertEquals("pkg-default", pkgBase.string("key1"));
        assertEquals(910, pkgBase.integer("key2"));
        // res
        assertEquals("true", res.string("key3a"));
        assertEquals("false", res.string("key3b"));
        assertEquals("TRUE", res.string("key3c"));
        assertEquals("あ", res.string("key1"));
        assertEquals(687, res.integer("key2"));
        assertEquals(true, res.isTrue("key3a"));
        assertEquals(false, res.isTrue("key3b"));
        assertEquals(true, res.isTrue("key3c"));
        assertEquals("", res.string("no-key"));
        assertEquals("x", res.string("no-key", "x"));
        assertEquals(0, res.integer("no-key"));
        assertEquals(-1, res.integer("no-key", -1));
        // res-en
        assertEquals("the", resEn.string("key1"));
    }

}
