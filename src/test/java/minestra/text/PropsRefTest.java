package minestra.text;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.junit.Test;

public class PropsRefTest {

    @Test
    public void testWrap() {
        Properties props = new Properties();
        props.setProperty("k", "AAA");
        PropsRef pr = PropsRef.wrap(props);
        assertEquals("AAA", pr.s("k"));
        props.setProperty("k", "xxxYYYz99");
        assertEquals("xxxYYYz99", pr.s("k"));
    }

    @Test
    public void testCopyOf_Properties() {
        Properties props = new Properties();
        props.setProperty("k", "AAA");
        PropsRef pr = PropsRef.copyOf(props);
        assertEquals("AAA", pr.s("k"));
        props.setProperty("k", "xxxYYYz99");
        assertEquals("AAA", pr.s("k"));
    }

    @Test
    public void testCopyOf_Map() {
        Map<String, String> m = new HashMap<>();
        m.put("s", "XXX");
        m.put("r", "12%");
        PropsRef pr = PropsRef.copyOf(m);
        assertEquals("XXX", pr.s("s"));
        assertEquals("12%", pr.s("r"));
    }

    @Test
    public void testOthers() {
        // sample code

        // resource file:
        // minestra/messages_en
        //   key1=en
        // minestra/messages
        //   key1=no-suffix
        //   key2=MSG
        ResourceSheaf res = ResourceSheaf.create(getClass()).withLocation("minestra/").withMessages().withLocales(Locale.ENGLISH);
        Properties props = new Properties();
        props.setProperty("key1", "A");
        props.setProperty("key2", "B");
        props.setProperty("key3", "C");
        PropsRef pr = PropsRef.aggregate(res, PropsRef.wrap(props));
        pr.getProperty("key1"); // => en
        pr.string("key1"); // => en
        pr.string("key2"); // => MSG
        pr.string("key3"); // => C
    }
}
