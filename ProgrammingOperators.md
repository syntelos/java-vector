The five [Component](http://code.google.com/p/java-vector/source/browse/src/vector/Component.java) operators are defined with the `void` return type, take no arguments, and include the following methods.
```java

public void init();
public void destroy();
public void relocated();
public void resized();
public void modified();```

These methods are indempotent: given a [coherent state](ProgrammingCoherentState.md), one or more calls to one of these methods produces one unique state.  This is one of the principal framework design solutions offered by the **vector** package.

The `init` and `destroy` operators maintain a special relationship for the initialization, destruction, and reinitialization of component instance objects.

A Component class constructor is public, takes no arguments, and performs no work.  Instance field default values must be assigned in the initialization operator, `init`, rather than the constructor (including field declaration assignments).  The initialization operator is responsible for the state of the class after construction, adding into the scene graph, and having its parent component defined (by a call to `setParentVector`).  This state must be returned to on each invocation of the initialization operator, subject to some marginal variations according to class design.  The initialization operator may be called from JSON input with the JSON native boolean `true` value mapped to the JSON object property name "init".

The component destruction operator, `destroy`, is called at the release of a component instance from the scene graph in order to drop cyclic references.  It is also called by the initialization operator for the reinitialization case.  Therefore its responsibilities are slightly more than a conventional destruction operator.  The vector component destruction operator returns the state of an instance to that following construction and before adding into the scene graph, in support of both garbage collection and reinitialization.

The `relocated` operator is called following `setLocationVector`, and setting any other related properties required by the class instance relocation operation.  The [Small Table](http://code.google.com/p/java-vector/source/browse/src/vector/TableSmall.java), for example, performs a grid layout on its immediate descendants (children) using the `setLocationVector` method, followed by a call to `relocated`.

The `resized` operator is called following `setBoundsVector`, and any related property setting required by the resizing operation.  The [Big Table](http://code.google.com/p/java-vector/source/browse/src/vector/TableBig.java), for example, performs a grid layout on its immediate descendants (children) using the `setBoundsVector` method, followed by a call to `resized`.  Also the [Display](http://code.google.com/p/java-vector/source/browse/src/vector/Display.java) calls the resizing operation on the scene graph when after it has been resized (by the [Frame](http://code.google.com/p/java-vector/source/browse/src/vector/Frame.java)).

The `modified` operator is called for any other visually significant changes to the scene graph.  Primarily, it is called after loading a scene graph from a JSON scene description.

_(The operators defined in the **vector** package have been enumerated in the [Operators](Operators.md) section)._