Because **vector** components are light and flexible, a fully interactive and attributed text solution in **vector** is a component for each piece of attributed text.  The [Text](http://code.google.com/p/java-vector/source/browse/src/vector/Text.jav) class is a component and is attributed for color and font, and is also fully interactive.  The [TextLayout](http://code.google.com/p/java-vector/source/browse/src/vector/TextLayout.java) class is a container populated by instances of the [Text](http://code.google.com/p/java-vector/source/browse/src/vector/Text.java) class.  This solution is ideal: it permits an attributed text list to be a first class member of the scene graph.

_An illustration can be launched [here](http://java-vector.googlecode.com/git/demo/preformatted.jnlp)_.

Producing text in **vector** requires individual Text objects appropriate to the wrapping or preformatted layout model in `TextLayout`, and style changes in color and font.   Architectures for this purpose are related to source text formats and application requirements.

For example, an XML source format is a well known solution for applications with dynamic text loading requirements.   In this case, a subclass of `TextLayout` may be employed with a URL property to load text.  This approach could employ a temporary daemon thread to perform an asynchronous loading operation for a `TextLayout` that is found to be empty by the `modified` operator.

In contrast, applications with static text requirements may be constructed in one of two principal ways.  One way employs the built-in simple text parser employed by the [preformatted demo](DemoPreformatted.md).  Another way builds a dynamic loading tool and writes a constructed scene description to a JSON file for use in the application runtime.
