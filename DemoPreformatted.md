The preformatted demo ([launch](http://java-vector.googlecode.com/git/demo/preformatted.jnlp) / [json](http://code.google.com/p/java-vector/source/browse/preformatted.json)) demonstrates a simple variation on the [general approach to attributed text](ProgrammingText.md) in the **vector** package.

```js

{
"background": "#FFFFFF",
"components": [
{
"class": "vector.TextLayout",
"color": "#000000",
"font": "monospaced-18",
"bounds": "(50,50,50,50)",
"fit": true,
"debug": true,
"components": [
{
"class": "vector.Border",
"color": "#0000FF"
}
],
"text": "    protected float shapeAreaWidth(){\n..."
}
]
}
```

A built-in simple text parser is employed for the special, read-only property named "text".  The default wrapping (none) lays-out this paragraph as preformatted text.

The [TextLayout](http://code.google.com/p/java-vector/source/browse/src/vector/TextLayout.java) class performs layout for members of the class [Component Layout Text](http://code.google.com/p/java-vector/source/browse/src/vector/Component.java).

The `Component Layout` interface permits programming in terms of layout strategy.  The `Component Layout Text` interface permits the `TextLayout` class to perform paragraph level layout for any implementor of this interface.

This demonstration illustrates how the `Text` objects have been created to implement the requirements of `Component Layout Text` class membership.  White - space and non- white - space substrings are defined as individual `Text` instance objects (bordered in red).
