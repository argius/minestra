Minestra
========
Minestra is a small utilities library for Java8 and later.


The word 'minestra' means soup in Italian. I love soups and there is no particular meaning for this name.


See "Examples" section if you want to know more about features in this library.

* Minestra has been compiled with compact1 profile.


site: http://argius.net/minestra/


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

See also test classes.



### ImmArray

```
// import java.util.*;
// import minestra.collection.*;

ImmArray<String> a = ImmArray.of(Arrays.asList("1"), Optional.of("2"), ImmArray.of("3", "A"),
    Optional.empty(), Arrays.asList("5"), "B", Stream.of("6", "7"))
    .flatten();
// => [1, 2, 3, A, 5, B, 6, 7]
```



### PathIterator

```
// import java.nio.file.*;
// import minestra.file.*;

PathIterator.streamOf(Paths.get("/tmp"))
    .filter(x -> x.toFile().length() > 1024L * 100)
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
I18nResource pkgBase = I18nResource.create("/yourpkg/", Locale.JAPAN);
I18nResource res = rootBase.derive(YourClass.class);
I18nResource resJa = rootBase.derive(YourClass.class, Locale.JAPAN);

String s = res.string("key1");
int i = res.integer("key2");
boolean b = res.isTrue("key3");
```



License
-------

Apache 2.0 License.
