package minestra.text;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Properties;

/**
 * PropsRef provides an interface of read-only String map.
 *
 * "Props" means <code>java.util.Properties</code> class as a map.
 */
public interface PropsRef {

    /**
     * Returns the Optional string of specified key.
     * @param key key for the value
     * @return the Optional string value
     */
    Optional<String> stringOpt(String key);

    /**
     * Returns the string of specified key.
     * @param key key for the value
     * @return the string value
     */
    default String getProperty(String key) {
        return string(key, "");
    }

    /**
     * Returns the string of specified key.
     * @param key key for the value
     * @param defaultValue default string value if it contains key
     * @return the string value
     */
    default String getProperty(String key, String defaultValue) {
        return stringOpt(key).orElse(defaultValue);
    }

    /**
     * Returns the string of specified key.
     * @param key key for the value
     * @return the string value
     */
    default String string(String key) {
        return string(key, "");
    }

    /**
     * Returns the string of specified key.
     * @param key key for the value
     * @param defaultValue default string value if it contains key
     * @return the string value
     */
    default String string(String key, String defaultValue) {
        return stringOpt(key).orElse(defaultValue);
    }

    /**
     * This is an alias of <code>string(String)</code>.
     * @param key key for the value
     * @return the string value
     */
    default String s(String key) {
        return string(key, "");
    }

    /**
     * Returns the {@link Optional} value of formatted string with {@link MessageFormat}.
     * @param key message key
     * @param args message arguments for format
     * @return formatted string
     */
    default Optional<String> formatOpt(String key, Object... args) {
        return stringOpt(key).map(x -> MessageFormat.format(x, args));
    }

    /**
     * Returns the formatted string with {@link MessageFormat}.
     * @param key key for the value
     * @return the string value
     */
    default String format(String key, Object... args) {
        return formatOpt(key, args).orElse("");
    }

    /**
     * Returns the int of specified key.
     * @param key key for the value
     * @return the OptionalInt value
     */
    default OptionalInt integerOpt(String key) {
        Optional<String> x = stringOpt(key);
        if (x.isPresent()) {
            return OptionalInt.of(Integer.parseInt(x.get()));
        }
        return OptionalInt.empty();
    }

    /**
     * Returns the int value of specified key.
     * @param key key for the value
     * @return the int value
     */
    default int integer(String key) {
        return integer(key, 0);
    }

    /**
     * Returns the int value of specified key.
     * @param key key for the value
     * @param defaultValue default int value if it contains key
     * @return the int value
     */
    default int integer(String key, int defaultValue) {
        return integerOpt(key).orElse(defaultValue);
    }

    /**
     * This is an alias of <code>integer(String)</code>.
     * @param key key for the value
     * @return the int value
     */
    default int i(String key) {
        return integer(key, 0);
    }

    /**
     * Returns a bool opt value of specified key.
     * @param key key for the value
     * @return the Optional Boolean value
     */
    default Optional<Boolean> boolOpt(String key) {
        return stringOpt(key).map(Boolean::valueOf);
    }

    /**
     * Returns boolean value of specified key.
     * @param key key for the value
     * @return the boolean value
     */
    default boolean isTrue(String key) {
        return boolOpt(key).orElse(false);
    }

    static PropsRef wrap(Properties props) {
        return new PropertiesHeldPropsRef(props);
    }

    static PropsRef wrap(Map<String, String> props) {
        return new StringMapHeldPropsRef(props);
    }

    static PropsRef copyOf(Properties props) {
        Properties copied = new Properties();
        copied.putAll(props);
        return new PropertiesHeldPropsRef(copied);
    }

    static PropsRef copyOf(Map<String, String> props) {
        Map<String, String> copied = new HashMap<>();
        copied.putAll(props);
        return new StringMapHeldPropsRef(copied);
    }

    static PropsRef aggregate(PropsRef... refs) {
        return new AggregatedPropsRef(refs);
    }

    static PropsRef aggregate(Collection<PropsRef> refs) {
        return new AggregatedPropsRef(refs);
    }

}
