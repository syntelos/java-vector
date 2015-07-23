**vector** develops the strengths of the [Java](http://www.oracle.com/technetwork/java/index.html) [2D](http://en.wikipedia.org/wiki/Java_2D) platform for scalable graphical user interfaces with crisp solutions to 2D UI programming framework problems.

The **vector** package defines itself in the [Component](http://code.google.com/p/java-vector/source/browse/src/vector/Component.java) interface.  With the semantics first outlined in [Notes](http://java-vector.googlecode.com/git/src/vector/Notes.txt), and then elaborated in [Display](http://code.google.com/p/java-vector/source/browse/src/vector/Display.java), [Bounds](http://code.google.com/p/java-vector/source/browse/src/vector/Bounds.java), and [Transform](http://code.google.com/p/java-vector/source/browse/src/vector/Transform.java), the Component interface defines a first class member of the interactive scene graph as having a bounding box, transform, parent, five operators, and GUI and JSON I/O.

The **vector** package includes a JSON scene description language for configuring and sharing a scene display.  The scene display algorithm permits both replacements and updates for internetworking (sharing).  A replacement is differentiated from an update by the inclusion of the special "init" property.

```js

{
"class": some.Class,
"init" : true,
.
.
.
}```

The JSON "init" property has a boolean value, and (when true) directs a component to reinitialize itself to the state of a new object, just added into the scene graph -- destroying the state of the scene graph subtree from this component.  An existing scene graph can be updated by omitting (or defining false) the "init" property.  In this case, many property values and components may be omitted as well.

The JSON scene description provides no features for managing the classpath, and the **vector** package currently has no class loader related features.  The current design and implementation is defined for communicating processes that share a common classpath, as illustrated by the webstart case.  This covers the majority of application cases excepting the dynamic loading case.  Future work may develop dynamic loading, just because it looks like a fun problem to solve given the strengths of the platform.
