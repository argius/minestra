package minestra.text;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;
import junit.framework.AssertionFailedError;

final class PackagePrivateTestUtils {

    private PackagePrivateTestUtils() {
        //
    }

    static Map<String, String> mapOf(String... a) {
        Map<String, String> m = new HashMap<>();
        for (int i = 0; i < a.length;) {
            String k = a[i++];
            String v = a[i++];
            m.put(k, v);
        }
        return m;
    }

    static Properties propsOf(String... a) {
        Properties props = new Properties();
        props.putAll(mapOf(a));
        return props;
    }

    static Supplier<? extends Error> fail(String message) {
        return () -> new AssertionFailedError(message);
    }

}
