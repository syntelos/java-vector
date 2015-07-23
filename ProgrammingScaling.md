The [Display](http://code.google.com/p/java-vector/source/browse/src/vector/Display.java) root component scales the scene to the [platform Frame](http://code.google.com/p/java-vector/source/browse/awt/src/platform/Frame.java).

Each [Component](http://code.google.com/p/java-vector/source/browse/src/vector/Component.java) may define its own scaling with its local transform, named simply "transform" in the convenience implementation [AbstractComponent](http://code.google.com/p/java-vector/source/browse/src/vector/AbstractComponent.java).  This is useful for exploiting the application of the vector transform that occurs within the host system graphics pipeline.

Component scaling is applied to mouse input event coordinates ([by the component parent](ProgrammingCoordinateSpaces.md)) so that input coordinates correctly match to shape coordinates, as seen in the [tabgrid demo](http://java-vector.googlecode.com/git/demo/tabgrid.jnlp).

## Transform ##
The [platform Transform (e.g. AWT)](http://code.google.com/p/java-vector/source/browse/awt/src/platform/Transform.java) class is a transformation matrix.   The cross platform programming interface is presented in the [Transform proof case](http://code.google.com/p/java-vector/source/browse/prove/src/platform/Transform.java).

The host system graphics pipeline applies the transformation matrix, so applications need only to determine the scaling required for their display without applying scaling directly to their shapes in a redundant (and expensive) operation.

## Operators ##
The component operators "relocated", "resized" and "modified" may be employed in the dynamic layout of a scene, for the definition of component state that is implied by user properties (explicit state exposed via JSON).  One of these operators is called following the definition of a set of one or more properties that produces a [coherent state](ProgrammingCoherentState.md).

The design of a component (class) must choose a strategy that is one of top-down or bottom-up for the definition of its dimensions and scaling.  The top-down strategy is related to the parent component, and the bottom-up strategy is related to the content of the component.  In some cases, for example in [Text](http://code.google.com/p/java-vector/source/browse/src/vector/Text.java), this strategy is defined explicitly by a boolean property (Text "fixed").  Attempting both in one class is not possible as the product will be a cyclic dependency (with no solution for indempotent operators).
