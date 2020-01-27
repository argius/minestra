package minestra.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <code>ResourceSheaf</code> provides to build a sheaf of i18nized message resources.
 * The word "sheaf" means "bundle" (<code>ResourceBundle</code>).
 */
public final class ResourceSheaf implements PropsRef {

    private final ClassLoader classLoader;
    private final String location;
    private final Locale[] locales;
    private final String extension;
    private final List<Map<String, String>> tables;

    private ResourceSheaf parent;

    ResourceSheaf(ResourceSheaf parent, ClassLoader classLoader, String location, Locale[] locales,
        String extension) {
        Objects.requireNonNull(classLoader, "classLoader requires non-null value");
        Objects.requireNonNull(location, "location requires non-null value");
        Objects.requireNonNull(locales, "locales requires non-null value");
        Objects.requireNonNull(extension, "extension requires non-null value");
        this.parent = parent;
        this.classLoader = classLoader;
        this.location = location;
        this.locales = locales;
        this.extension = extension;
        this.tables = createTables();
    }

    List<Map<String, String>> createTables() {
        return getPathList().stream().map(this::readTable).filter(x -> !x.isEmpty()).collect(Collectors.toList());
    }

    Map<String, String> readTable(String resourcePath) {
        try (InputStream is = classLoader.getResourceAsStream(resourcePath)) {
            if (is != null) {
                Map<String, String> m = lines(is).stream().filter(x -> !x.trim().isEmpty())
                        .collect(Collectors.toMap(x -> fKey(x), x -> fVal(x)));
                return Collections.unmodifiableMap(m);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return Collections.emptyMap();
    }

    private static List<String> lines(InputStream is) {
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("resource")
        Scanner r = new Scanner(is, StandardCharsets.UTF_8.name());
        while (r.hasNextLine()) {
            final String s = r.nextLine();
            if (s.matches("^\\s*#.*")) {
                continue;
            }
            sb.append(s.replace("\\t", "\t").replace("\\n", "\n").replace("\\=", "="));
            if (s.endsWith("\\")) {
                sb.setLength(sb.length() - 1);
                continue;
            }
            lines.add(sb.toString());
            sb.setLength(0);
        }
        if (sb.length() > 0) {
            lines.add(sb.toString());
        }
        return lines;
    }

    private static String fKey(String x) {
        final int p = x.indexOf('=');
        return (p >= 0 ? x.substring(0, p) : x).trim();
    }

    private static String fVal(String x) {
        final int p = x.indexOf('=');
        if (p >= 0) {
            String s = x.substring(p + 1).trim();
            if (s.endsWith("\\")) {
                s = s.substring(0, s.length() - 1) + ' ';
            }
            return s.replace("\\ ", " ");
        }
        return x.trim();
    }

    /**
     * Returns a list of paths to search for resources.
     * @return a list of paths
     */
    public List<String> getPathList() {
        if (location.isEmpty()) {
            return Collections.emptyList();
        }
        else if (locales.length == 0) {
            return Arrays.asList(toPathString(""));
        }
        else {
            // @formatter:off
            return Stream.of(locales)
                    .flatMap(x -> Stream.of("_" + x, "_" + x.getLanguage(), ""))
                    .distinct()
                    .sorted(Comparator.comparing(String::length).reversed())
                    .map(this::toPathString)
                    .collect(Collectors.toList());
            // @formatter:on
        }
    }

    String toPathString(String suffix) {
        Objects.requireNonNull(suffix);
        return location + suffix + (extension.isEmpty() ? "" : "." + extension);
    }

    static String classToLocation(Class<?> c) {
        return c.getName().replace('.', '/');
    }

    /**
     * Returns an instance created with the default class loader.
     * @return an instance
     */
    public static ResourceSheaf create() {
        return create(ClassLoader.getSystemClassLoader());
    }

    /**
     * Returns an instance created with the specified class loader.
     * @param classLoader class loader
     * @return an instance
     */
    public static ResourceSheaf create(ClassLoader classLoader) {
        return new ResourceSheaf(null, classLoader, "", new Locale[0], "");
    }

    /**
     * Returns an instance created with the specified class and its class loader.
     * @param c class
     * @return an instance
     */
    public static ResourceSheaf create(Class<?> c) {
        Objects.requireNonNull(c, "class requires non-null value");
        return new ResourceSheaf(null, c.getClassLoader(), classToLocation(c), new Locale[0], "");
    }

    /**
     * Returns a new instance replaced with the specified location.
     * @param location location
     * @return an instance
     */
    public ResourceSheaf withLocation(String location) {
        return new ResourceSheaf(parent, classLoader, location, locales, extension);
    }

    /**
     * Returns a new instance replaced with the specified locales.
     * @param locales locales
     * @return new instance
     */
    public ResourceSheaf withLocales(Locale... locales) {
        return new ResourceSheaf(parent, classLoader, location, Arrays.copyOf(locales, locales.length), extension);
    }

    /**
     * Returns a new instance replaced with the default locales.
     * @return an instance
     */
    public ResourceSheaf withDefaultLocale() {
        return withLocales(Locale.getDefault());
    }

    /**
     * Returns a new instance replaced with all of the available locales.
     * @return an instance
     */
    public ResourceSheaf withAllLocales() {
        return new ResourceSheaf(parent, classLoader, location, Locale.getAvailableLocales(), extension);
    }

    /**
     * Returns a new instance replaced with the specified extension.
     * @param extension extension
     * @return new instance
     */
    public ResourceSheaf withExtension(String extension) {
        return new ResourceSheaf(parent, classLoader, location, locales, extension);
    }

    /**
     * Returns an instance replaced with the specified class and its class loader.
     * @param c class
     * @return new instance
     */
    public ResourceSheaf withClass(Class<?> c) {
        Objects.requireNonNull(c, "class requires non-null value");
        return new ResourceSheaf(parent, c.getClassLoader(), classToLocation(c), locales, extension);
    }

    /**
     * Returns an instance replaced with the specified name.
     * @param name name
     * @return new instance
     */
    public ResourceSheaf withName(String name) {
        return new ResourceSheaf(parent, classLoader, replaceName(location, name), locales, extension);
    }

    static String replaceName(String location, String name) {
        if (!location.contains("/")) {
            return name;
        }
        if (location.endsWith("/")) {
            return location + name;
        }
        return location.replaceFirst("/[^/]+$", "/") + name;
    }

    /**
     * Returns an instance replaced with "messages" as a name.
     *
     * <p>
     * This method behaves in the same way as the invocation <code>withName("messages")</code>.
     * </p>
     * @return new instance
     */
    public ResourceSheaf withMessages() {
        return withName("messages");
    }

    /**
     * Returns a copied instance.
     * @return copied new instance
     */
    public ResourceSheaf copy() {
        return new ResourceSheaf(parent, classLoader, location, locales, extension);
    }

    /**
     * Returns a new instance derived as a child.
     * @return new instance
     */
    public ResourceSheaf derive() {
        ResourceSheaf res = copy();
        res.parent = this;
        return res;
    }

    /**
     * Returns an instance of ResourceBundle based on this instance.
     * @return an instance of ResourceBundle
     */
    public ResourceBundle toResourceBundle() {
        List<String> keyList = tables.stream().flatMap(x -> x.keySet().stream()).collect(Collectors.toList());
        Enumeration<String> keys = Collections.enumeration(keyList);
        class ToResourceBundleMethodLocal extends ResourceBundle {
            @Override
            protected Object handleGetObject(String key) {
                return getProperty(key);
            }
            @Override
            public Enumeration<String> getKeys() {
                return keys;
            }
        }
        return new ToResourceBundleMethodLocal();
    }

    /**
     * Returns the Optional string of specified key.
     * @param key key for the value
     * @return the Optional string value
     */
    @Override
    public Optional<String> stringOpt(String key) {
        for (Map<String, String> m : tables) {
            String v = m.get(key);
            if (v != null) {
                return Optional.of(v);
            }
        }
        if (parent != null && parent != this) {
            return parent.stringOpt(key);
        }
        return Optional.empty();
    }

    /**
     * Returns the int of specified key.
     * @param key key for the value
     * @return the OptionalInt value
     */
    @Override
    public OptionalInt integerOpt(String key) {
        for (Map<String, String> m : tables) {
            if (m.containsKey(key)) {
                try {
                    return OptionalInt.of(Integer.valueOf(m.get(key)).intValue());
                } catch (NumberFormatException e) { // ignore
                }
            }
        }
        if (parent != null && parent != this) {
            return parent.integerOpt(key);
        }
        return OptionalInt.empty();
    }

    @Override
    public String toString() {
        return String.format("%s(parent=%s, location=%s, locales=%s, extension=%s)", getClass().getSimpleName(),
            (parent == null) ? "None" : parent, location,
                    (locales.length == Locale.getAvailableLocales().length) ? "ALL" : Arrays.toString(locales), extension);
    }

}
