Minestra
========
[![Build Status](https://travis-ci.org/argius/minestra.png)](https://travis-ci.org/argius/minestra)

Minestra is a small utilities library for Java8 and later.


The word 'minestra' means soup in Italian. I love soups and there is no particular meaning for this name.


See "Examples" section if you want to know more about features in this library.

* Minestra has been compiled with compact1 profile.


site: http://argius.github.io/minestra/


Dependency
----------

You can download Minestra from Maven Central.

MvnRepository URL:  https://mvnrepository.com/artifact/net.argius/minestra


 - Maven

```
<dependency>
    <groupId>net.argius</groupId>
    <artifactId>minestra</artifactId>
    <version>1.2.1</version>
</dependency>
```

 - Gradle

```
    compile 'net.argius:minestra:1.2.1'
```



Examples
--------

See also test classes.



### ImmArray

The classes of `ImmArray` are mere array wrappers and provides functional operations without functional DSL.
Neither pipeline nor any performance improvement strategies.

```
// import java.util.*;
// import minestra.collection.*;

ImmArray<String> a = ImmArray.of(Arrays.asList("1"), Optional.of("2"), ImmArray.of("3", "A"),
    Optional.empty(), Arrays.asList("5"), "B", Stream.of("6", "7"))
    .flatten();
for (String s : a) {
    System.out.printf("%s, ", s);
}
// => 1, 2, 3, A, 5, B, 6, 7,
```

```
// import java.util.*;
// import java.util.stream.*;
// import minestra.collection.*;

List<String> list1 = IntStream.of(1, 2, 3).mapToObj(x -> "#" + x).collect(Collectors.toList());
List<String> list2 = IntImmArray.of(1, 2, 3).mapToObj(x -> "#" + x).toList();
```


### PathIterator and PathFilter

`PathIterator` is an iterator which walks through a file tree like `Files.find()`.
`PathFilter` is a filter-generator for filtering objects of `java.nio.file.Path`.

```
// import java.time.*;
// import java.nio.file.*;
// import minestra.file.*;

Predicate<Path> filter1 = PathFilter.sizeGT(100 * 1024L); // file size is larger than 100KB
Predicate<Path> filter2 = PathFilter.mtimeBetween(
    Instant.parse("2017-04-01T00:00:00Z"), Instant.parse("2018-03-31T23:59:59Z")
); // mtime is between 2017-04-01 and 2018-03-31T23:59:59 (UTC)
Predicate<Path> filter3 = filter1.and(filter2);
PathIterator.streamOf(Paths.get("/tmp"))
    .filter(filter3)
    .forEach(System.out::println);
for (Path x : PathIterator.of(Paths.get("/tmp"))) {
    if (filter3.test(x)) {
        System.out.println(x);
    }
}
```


### PathString

`PathString` is an extension of `java.nio.file.Path` .

```
// import minestra.file.PathString;

PathString path1 = PathString.of("/tmp/file.dat");
System.out.println(path1.extension()); // => Optional[dat]
PathString path2 = PathString.of("/tmp/file");
System.out.println(path2.extension()); // => Optional.empty
```


### LetterCaseConverter

`LetterCaseConverter` is a letter-case converter.

```
// import java.util.Arrays;
// import minestra.text.*;

String[] a = {
    LetterCaseConverter.capitalize("hello"),
    LetterCaseConverter.uncapitalize("World"),
    LetterCaseConverter.toCamelCase("read_all_files"),
    LetterCaseConverter.toPascalCase("forget-me-not"),
    LetterCaseConverter.toSnakeCase("CaseConverter"),
    LetterCaseConverter.toChainCase("toString")};
System.out.println(Arrays.toString(a));

// => [Hello, world, readAllFiles, ForgetMeNot, case_converter, to-string]
```


### PropsRef, ResourceSheaf

`PropsRef` provides access to Properties.
`ResourceSheaf` is similar to `ResourceBundle`, and provides access to message resources.

```
// import java.util.*;
// import minestra.text.*;

// resource file:
// minestra/messages_en
//   key1=en
// minestra/messages
//   key1=no-suffix
//   key2=MSG
ResourceSheaf res = ResourceSheaf.create(getClass()).withLocation("minestra/").withMessages().withLocales(Locale.ENGLISH);
Properties props = new Properties();
props.setProperty("key1", "A");
props.setProperty("key2", "B");
props.setProperty("key3", "C");
PropsRef pr = PropsRef.aggregate(res, PropsRef.wrap(props));
pr.getProperty("key1"); // => en
pr.string("key1"); // => en
pr.string("key2"); // => MSG
pr.string("key3"); // => C
```



License
-------

Apache 2.0 License.
