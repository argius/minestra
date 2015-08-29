Overview
========
Minestra is a small utilities library for Java8 and later.


The word 'minestra' means soup in Italian. I love soups and there is no particular meaning for this name.


See "Examples" section if you want to know more about features in this library.

* Minestra has been compiled with compact1 profile.


<a href="https://github.com/argius/minestra"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://camo.githubusercontent.com/a6677b08c955af8400f44c6298f40e7d19cc5b2d/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f677261795f3664366436642e706e67" alt="Fork me on GitHub" data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_right_gray_6d6d6d.png" /></a>


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
