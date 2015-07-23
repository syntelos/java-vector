# Application #

The most liberal or judicious use of the package is highly recommended, and is intended to be fun following a basic understanding of the package.

## Class path ##

An application would simply employ the classpath that includes `vector-X.Y.Z.jar` and the jar files found in the `lib` directory.

## Structure ##

An application may use, subclass, or replace the
```java

vector.Frame```
main class.  Likewise, an application may implement an applet following the use of [Display](http://code.google.com/p/java-vector/source/browse/src/vector/Display.java) found in [Frame](http://code.google.com/p/java-vector/source/browse/src/vector/Frame.java).

An application may use, subclass, or replace almost any class in the package.  The interface classes [Component](http://code.google.com/p/java-vector/source/browse/src/vector/Component.java) and [Event](http://code.google.com/p/java-vector/source/browse/src/vector/Event.java) cannot be replaced, and likewise the common utilities [Bounds](http://code.google.com/p/java-vector/source/browse/src/vector/Bounds.java) and [Transform](http://code.google.com/p/java-vector/source/browse/src/vector/Transform.java) cannot be replaced.   Replacing [Display](http://code.google.com/p/java-vector/source/browse/src/vector/Display.java) is not recommended because it is the central hub of package semantics.

## A first example ##

A first example application is found in the [Button](http://code.google.com/p/java-vector/source/browse/demo/src/demo/Button.java) demo.  For more info see [Button Demo](DemoButton.md).

# Development #

As an [ant](http://ant.apache.org/) user, the project should readily import into virtually any development environment.

## Building ##

The vector jar is recompiled and repackaged (from [src](http://code.google.com/p/java-vector/source/browse/src/)) using [ant](http://ant.apache.org/) in the source directory by entering the command
```sh

ant```

Build the demo jar (from [demo/src](http://code.google.com/p/java-vector/source/browse/demo/src/)) with
```sh

ant demo```