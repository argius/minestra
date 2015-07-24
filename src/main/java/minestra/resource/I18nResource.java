package minestra.resource;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface I18nResource {

    static I18nResource create(Locale... locales) {
        return create("/", locales);
    }

    static I18nResource create(Class<?> c, Locale... locales) {
        return create(toResourceName(c), locales);
    }

    static I18nResource create(String location, Locale... locales) {
        Stream<Locale> tmp = (locales.length == 0) ? Stream.of(Locale.getDefault()) : Stream.of(locales);
        Stream<String> st = tmp.flatMap(x -> Stream.of("_" + x, "_" + x.getLanguage())).distinct();
        String location0 = location + ((location.endsWith("/")) ? "default" : "");
        List<Map<String, String>> tables = Stream.concat(st, Stream.of(""))
                .map(x -> I18nResourceImpl.createTable(location0, x)).collect(Collectors.toList());
        return new I18nResourceImpl(Collections.unmodifiableList(tables));
    }

    static String toResourceName(Class<?> c) {
        return "/" + c.getName().replace('.', '/');
    }

    default I18nResource derive(String location, Locale... locales) {
        I18nResource o = create(location, locales);
        o.setParent(this);
        return o;
    }

    default I18nResource derive(Class<?> c, Locale... locales) {
        return derive(toResourceName(c), locales);
    }

    default boolean contains(String key) {
        return false;
    }

    void setParent(I18nResource parent);

    public Optional<String> stringOpt(String key);

    default String string(String key) {
        return string(key, "");
    }

    default String string(String key, String defaultValue) {
        return stringOpt(key).orElse(defaultValue);
    }

    default String s(String key) {
        return string(key, "");
    }

    OptionalInt integerOpt(String key);

    default int integer(String key) {
        return integer(key, 0);
    }

    int integer(String key, int defaultValue);

    default int i(String key) {
        return integer(key, 0);
    }

    default Optional<Boolean> boolOpt(String key) {
        return stringOpt(key).map(Boolean::valueOf);
    }

    default boolean isTrue(String key) {
        return boolOpt(key).orElse(false);
    }

}
