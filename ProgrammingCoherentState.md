The five Component [operators](ProgrammingOperators.md) are defined as indempotent for coherent state transitions: given a coherent state, the operator must produce the same resulting state for any number of calls to the operator.  These states include all of the instance fields of the class related to graphics output, including component and content coordinates and dimensions.

These requirements permit the Component operators to be employed in a reasonable way, as indempotent operations.  And these principles provide a framework for reasoning about the correct design of vector Components.

An initial state, before a call to an operator, is coherent when the operators of the class and its super classes are indempotent.  The class must be designed to not produce incoherent states by calls to its operators.

The Component operators are called after a complete set of user properties -- for example any subset of those properties exposed via JSON -- have been defined.  This is an important aspect to the design of the **vector** package: that a coherent state may be composed (by any number of calls to property setters) before any one of the Component operators is called.

## Debugging indempotence ##

A typical symptom of a lack of indempotence is a loss of layout under window resizing (or other modification events).  The most direct way to debug these problems is by stepping through the path of execution from the `resized` or `modified` operator.  _One good debugger is [JSwat](http://code.google.com/p/jswat/)_.

Watch for the loss or clobbering of explicit state (JSON / user properties), or the reuse of implicit state.

For example, such a problem can be produced using the [preformatted demo](DemoPreformatted.md) by changing the definition of the [TextLayout](http://code.google.com/p/java-vector/source/browse/src/vector/TextLayout.java) class layout function to use `getBoundsVector` in the place of `queryBoundsContent`.

In this case, the `TextLayout` container layout is producing the information in the bounding boxes of its children, as well as consuming that information as input to the layout algorithm.  This is an example of the reuse of implicit state: the products of the layout function are implicit state in this case, while in other cases the bounding box may be explicit state.
