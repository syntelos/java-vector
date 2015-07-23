[Component](http://code.google.com/p/java-vector/source/browse/src/vector/Component.java) output refers to the graphical visualization of the content of a scene graph.  Each component has two graphical output methods, for scene and overlay.
```java

public Component outputScene(Context cg);
public Component outputOverlay(Context cg);```

The outputScene method is called once per second, or more, to render the scene graph into a buffer.  This buffer is copied into the platform graphics pipeline for each call to outputScene or outputOverlay.

The outputOverlay method is called to render a fast overlay onto the scene output buffer.  Overlay is intended to occur many times for each rendering of the scene.

## Interactive ##
Any [Component](http://code.google.com/p/java-vector/source/browse/src/vector/Component.java) may request the platform system to redraw the application window by calling one of the output request methods for scene or overlay.
```java

public Component outputScene();
public Component outputOverlay();```

## Animated ##
The [platform Repainter (e.g. AWT)](http://code.google.com/p/java-vector/source/browse/awt/src/platform/event/Repainter.java) will periodically request scene or overlay rendering from the platform system.

## Example ##
A first example overlay is the blinking text caret in the [Text Edit](http://code.google.com/p/java-vector/source/browse/src/vector/TextEdit.java) class.  The caret may blink many times while the scene graph has no other changes, so the caret is redrawn over the buffered scene.
