Priority levels include the following

  * **Continuous**: currently partial, further work in the coming weeks
  * **High**: scheduled for coming weeks
  * **Medium**: scheduled for coming months
  * **Low**: may occur in the indefinite future

## Unconventional scene graphs ##

Content oriented classes may be employed without the conventional scene graph: some classes work with no Component parent.  The Component API requires elements of its scene graph infrastructure including the graphics Context and input Event.  Unconventional scene graphs are discontinuous: they have non - Components that are used by Components and use Components.
```
  Component -> NonComponent -> Component
```

For example,
```
  DisplaySubclass -> NonComponent -> Text
```

This objective is **continuous**, and has been applied to the Text class for its Content layout modes.

## Portable applications ##

An underlying platform layer adapts the **vector** package to multiple platforms including AWT and Android.  A platform is defined or selected via the run - time class path.   Applications are class file portable across platforms, subject to platform packaging requirements.
```
            Application
          ---------------
              vector
          ---------------
           AWT | Android
```

Future platform ideas beyond [AWT](http://docs.oracle.com/javase/6/docs/api/index.html?java/awt/package-summary.html) and [Android](http://developer.android.com/) include [JOGL](http://www.jogamp.org/).

This objective is considered **continuous** as partial and high priority, while only the AWT platform works at the moment and current applications will not be portable until all AWT classes are replaced by platform package classes.

See also http://code.google.com/p/java-vector/issues/detail?id=7

## Text layout editor ##

`TextLayout` editing will apply mouse motion events to cursor position, and super - editor functionality will create and destroy `TextEdit` objects.

This objective has **medium** priority.

## Partial scene descriptions ##

Partial scene graph specification and loading permits a scene description to include a component scene description by reference.

This objective has **medium** priority.

## Scene desktop ##

Drag and drop, and cut and paste complete and partial scene graphs.

This objective has **medium** priority.

## Text desktop ##

Text editing cut, copy and paste including mouse selection.

This objective has **medium** priority.

## Visual composer ##

Multiple display subclasses in a frame subclass for scene view, source edit, and other tools.

This objective has **low** priority.
