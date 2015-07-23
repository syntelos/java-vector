The button demo ([launch](http://java-vector.googlecode.com/git/demo/button.jnlp) / [json](http://code.google.com/p/java-vector/source/browse/button.json)) is an example application with a class found in [demo/Button.java](http://code.google.com/p/java-vector/source/browse/demo/src/demo/Button.java).

This class is compiled by the ant target 'demo-compile' and packaged into the demo jar with the ant target 'demo'.  The [button.jnlp](http://code.google.com/p/java-vector/source/browse/demo/button.jnlp) shows this classpath and demonstrates this class in action.

Following the [source code](http://code.google.com/p/java-vector/source/browse/demo/src/demo/Button.java), this class simply relocates itself to a new random location on each button action event.  It doesn't concern itself with the details of an action event because it has been written from one button only.

```java

@Override
public boolean input(Event e){
if (super.input(e))
return true;
else {
switch (e.getType()){
case Action:
this.setLocationVector(this.randomLocation());
this.relocated();
this.outputScene();
return true;
default:
return false;
}
}
}```

After redefining its location it calls the Component operator **relocated**.  One of the five Component operators, **relocated** must be called following `setLocationVector` (and any related property changes) in order to permit super and sub classes to handle this kind of change.

Finally it calls `outputScene` to cause the root container to redraw the scene graph, which changed when the button component changed its location.  Alternatively, some components may define overlays which can be quickly redrawn over the existing scene -- an example being the blinking text editor caret (cursor).
