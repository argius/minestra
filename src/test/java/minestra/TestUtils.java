package minestra;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * This is a package global utilities class for tests.
 */
public final class TestUtils {

    private TestUtils() {
    }

    public static <T, R> Function<T, R> getStaticMethodInvokerWithForcedAccessibility(Class<?> c, String methodName,
            Class<?>... types) {
        final Method m;
        try {
            m = c.getDeclaredMethod(methodName, types);
            m.setAccessible(true);
        } catch (IllegalArgumentException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        Function<T, R> f = x -> {
            try {
                @SuppressWarnings("unchecked")
                final R r = (R) m.invoke(m, x);
                return r;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
        return f;
    }

}
