Minestra
========
Minestra is a simple basic utility library for Java8 and later.


The word 'minestra' means soup in Italian. I love soups and there is no particular meaning for this name.


Download
--------

Download the latest Jar file from the release page in GitHub.

https://github.com/argius/minestra/releases



Usage
-----

Not uploaded to Maven yet.
Please download JAR and add it to your classpath.



Examples
--------

### ImmArray

```
// import java.util.*;
// import minestra.collection.*;

ImmArray<String> a = ImmArray.of(Arrays.asList("1"), Optional.of("2"), ImmArray.of("3", "A"), Optional.empty(),
    Arrays.asList("5"), "B", Stream.of("6", "7")).flatten();
// => [1, 2, 3, A, 5, B, 6, 7]
```


### PathIterator

```
// import java.nio.file.*;
// import minestra.file.*;

PathIterator.streamOf(Paths.get("/tmp")).filter(x -> x.toFile().length() > 1024L * 100)
.forEach(System.out::println);
for (Path x : PathIterator.of(Paths.get("/tmp"))) {
    System.out.println(x);
}
```


### I18nResource

```
// import java.util.*;
// import minestra.resource.*;

I18nResource rootBase = I18nResource.create(Locale.JAPAN);
I18nResource pkgBase = I18nResource.create("/yourpkg/", JA);
I18nResource res = rootBase.derive(YourClass.class);
I18nResource resJa = rootBase.derive(YourClass.class, JA);

String s = res.string("key1");
int i = res.integer("key2");
boolean b = res.isTrue("key3");
```


See also test classes.



License
-------

Apache 2.0 License.