package minestra.resource;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  Internationalization (i18n) resource.
 *
 *  <p>
 *  text resource internationalized by locales.
 *  </p>
 */
public interface I18nResource {

    /**
     * Returns an I18nResource.
     * @param locales target locales
     * @return the I18nResource
     */
    static I18nResource create(Locale... locales) {
        return create("/", locales);
    }

    /**
     * Returns an I18nResource.
     * @param c target class
     * @param locales target locales
     * @return the I18nResource
     */
    static I18nResource create(Class<?> c, Locale... locales) {
        return create(toResourceName(c), locales);
    }

    /**
     * Returns an I18nResource.
     * @param location target location
     * @param locales target locales
     * @return the I18nResource
     */
    static I18nResource create(String location, Locale... locales) {
        Stream<Locale> tmp = (locales.length == 0) ? Stream.of(Locale.getDefault()) : Stream.of(locales);
        Stream<String> st = tmp.flatMap(x -> Stream.of("_" + x, "_" + x.getLanguage())).distinct();
        String location0 = location + ((location.endsWith("/")) ? "default" : "");
        List<Map<String, String>> tables = Stream.concat(st, Stream.of(""))
                .map(x -> I18nResourceImpl.createTable(location0, x)).collect(Collectors.toList());
        return new I18nResourceImpl(Collections.unmodifiableList(tables));
    }

    /**
     * Returns the resource name which converts from the specified Class.
     * @param c target class
     * @return the resource name
     */
    static String toResourceName(Class<?> c) {
        return "/" + c.getName().replace('.', '/');
    }

    /**
     * Returns the new I18nResource derived from this I18nResource.
     * @param location target location
     * @param locales target locales
     * @return the I18nResource
     */
    default I18nResource derive(String location, Locale... locales) {
        I18nResource o = create(location, locales);
        o.setParent(this);
        return o;
    }

    /**
     * Returns the new I18nResource derived from this I18nResource.
     * @param c target class
     * @param locales target locales
     * @return the I18nResource
     */
    default I18nResource derive(Class<?> c, Locale... locales) {
        return derive(toResourceName(c), locales);
    }

    /**
     * Returns whether this resources contains the key.
     * @param key key for value
     * @return true if it contains the key
     */
    default boolean contains(String key) {
        return false;
    }

    /**
     * Sets an I18nResource as its parent.
     * @param parent parent I18nResource
     */
    void setParent(I18nResource parent);

    /**
     * Returns the Optional string of specified key.
     * @param key key for the value
     * @return the Optional string value
     */
    public Optional<String> stringOpt(String key);

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
     * Returns the int of specified key.
     * @param key key for the value
     * @return the OptionalInt value
     */
    OptionalInt integerOpt(String key);

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
    int integer(String key, int defaultValue);

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

}
