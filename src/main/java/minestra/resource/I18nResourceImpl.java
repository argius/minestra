package minestra.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.function.Function;

public class I18nResourceImpl implements I18nResource {

    private static final String EXT = ".txt";

    private List<Map<String, String>> list;
    private Optional<I18nResource> parent;

    I18nResourceImpl(List<Map<String, String>> list) {
        this(list, Optional.empty());
    }

    private I18nResourceImpl(List<Map<String, String>> list, Optional<I18nResource> parent) {
        this.list = list;
        this.parent = parent;
    }

    @Override
    public boolean contains(String key) {
        for (Map<String, String> map : list) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setParent(I18nResource parent) {
        if (parent == this) {
            throw new IllegalArgumentException("same object");
        }
        this.parent = Optional.of(parent);
    }

    @Override
    public Optional<String> stringOpt(String key) {
        for (Map<String, String> m : list) {
            String v = m.get(key);
            if (v != null) {
                return Optional.of(v);
            }
        }
        return parent.flatMap(x -> x.stringOpt(key));
    }

    @Override
    public OptionalInt integerOpt(String key) {
        for (Map<String, String> m : list) {
            if (m.containsKey(key)) {
                try {
                    return OptionalInt.of(Integer.valueOf(m.get(key)).intValue());
                } catch (NumberFormatException e) { // ignore
                }
            }
        }
        if (parent.isPresent()) {
            return parent.get().integerOpt(key);
        }
        return OptionalInt.empty();
    }

    @Override
    public int integer(String key, int defaultValue) {
        for (Map<String, String> m : list) {
            if (m.containsKey(key)) {
                try {
                    return Integer.valueOf(m.get(key)).intValue();
                } catch (NumberFormatException e) { //
                }
            }
        }
        if (parent.isPresent()) {
            return parent.get().integer(key, defaultValue);
        }
        return defaultValue;
    }

    static Map<String, String> createTable(String location, String suffix) {
        return createTable(Object.class, location, suffix);
    }

    static Map<String, String> createTable(Class<?> c, String location, String suffix) {
        final String path = location + suffix + EXT;
        try (InputStream is = c.getResourceAsStream(path)) {
            if (is == null) {
                return Collections.emptyMap();
            }
            Function<String, String> fKey = x -> (x.contains("=")) ? x.replaceFirst("=.*", "").trim() : x.trim();
            Function<String, String> fVal = x -> (x.contains("="))
                    ? x.replaceFirst(".+=", "").trim().replaceFirst("\\\\$", " ").replace("\\ ", " ") : x.trim();
            Map<String, String> m = new HashMap<>();
            lines(is).stream().filter(x -> !x.trim().isEmpty()).forEach(x -> m.put(fKey.apply(x), fVal.apply(x)));
            return Collections.unmodifiableMap(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    private static List<String> lines(InputStream is) {
        List<String> lines = new ArrayList<String>();
        StringBuilder buffer = new StringBuilder();
        @SuppressWarnings("resource")
        Scanner r = new Scanner(is, StandardCharsets.UTF_8.name());
        while (r.hasNextLine()) {
            final String s = r.nextLine();
            if (s.matches("^\\s*#.*")) {
                continue;
            }
            buffer.append(s.replace("\\t", "\t").replace("\\n", "\n").replace("\\=", "="));
            if (s.endsWith("\\")) {
                buffer.setLength(buffer.length() - 1);
                continue;
            }
            lines.add(buffer.toString());
            buffer.setLength(0);
        }
        if (buffer.length() > 0) {
            lines.add(buffer.toString());
        }
        return lines;
    }

}