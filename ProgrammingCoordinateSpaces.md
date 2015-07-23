A Container is defined as a Component with zero or more descendants by the [Component Container](http://code.google.com/p/java-vector/source/browse/src/vector/Component.java) interface.

More abstractly, every Component is contained in a parent coordinate space.  The root component, [Display](http://code.google.com/p/java-vector/source/browse/src/vector/Display.java), is contained within a [bounding box](http://code.google.com/p/java-vector/source/browse/src/vector/Bounds.java) that reflects the input coordinates of the Display.  A Display is an AWT component (Canvas) that (typically) has a non-zero location.  This location translates mouse input coordinates.

The **vector** package builds on the 2D capabilities of its [Java](http://www.java.com/) platform, and generalizes this translation, with a [matrix](http://en.wikipedia.org/wiki/Matrix_(math)) ([transform](http://code.google.com/p/java-vector/source/browse/src/vector/Transform.java)) in each Component.

As a result, each Component defines an interior coordinate space in terms of translation and scaling.

Due to the requirement for [coherent state transitions](ProgrammingCoherentState.md), the relationship between the parent and child coordinate space is represented in two independent pieces of information: scaling and translation.  The scaling information is maintained within the component transform named "local", and typically defined as a field with type Transform named simply "transform".  And the translation information is maintained within the component bounding box, and dynamically exposed by the matrix transform named "parent".  This (parent) transform is applied to input events and the output graphics context in order to transform their coordinates from the parent to the child.

A component receives the graphics context parameter to its output methods (scene and overlay) in its parent's coordinate space.  It is free to define bounding and visualization strategies that may overlap.

A component receives the event parameter to its input method with mouse event coordinates transformed into its own coordinate space.  The parent needs to know if mouse events are contained within its descendants in order that the package and its applications are able to produce hierarchically correct mouse enter and exit events.

A component may contain a shape, for example the string of glyphs displayed by a [Text](http://code.google.com/p/java-vector/source/browse/src/vector/Text.java) component.  It may be useful to consider the shape coordinate space as no more than the real values of the points and dimensions of the shape, and then from this perspective to think of the component and container coordinate spaces as the hierarchy of transforms that visualizes the shape.

The bounding box of a component is a shape in the coordinate space of the component's parent.
