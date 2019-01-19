package minestra.text;

import static org.junit.Assert.*;
import java.util.Locale;
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
    public void testCopied() {
        Properties props = new Properties();
        props.setProperty("k", "AAA");
        PropsRef pr = PropsRef.copyOf(props);
        assertEquals("AAA", pr.s("k"));
        props.setProperty("k", "xxxYYYz99");
        assertEquals("AAA", pr.s("k"));
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
