Component input is unified into a single method and argument in the **vector** package.  This choice is intended to maximize both convenience of implementation and runtime efficiency.  The [button demo](http://code.google.com/p/java-vector/source/browse/demo/src/demo/Button.java) illustrates a typical case, and the [Container](http://code.google.com/p/java-vector/source/browse/src/vector/Container.java) class illustrates a complex case.

In the button demo,
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
the simple, universal [event](http://code.google.com/p/java-vector/source/browse/src/vector/Event.java) type selection defines the required path of execution (for this simple demo).

The component input method returns a boolean true for "event consumed" and false for "event not consumed", although not all event types are implemented as "narrow cast" consumables.  In many cases shown by the Container input process, event processing has been implemented in a broadcast algorithm for the benefit of applications of the event type.  For example in the case of the Action event type, a Dialog window may need to receive the Action event as well as (possibly) another member of the scene graph.

The greatest benefit of the boolean return value from the input method is for the programming of subclasses, as in the button demo case.  The superclasses of the demo button class return true for the mouse motion and click events that they implement.  The demo Button subclass, therefore, only needs to listen for the Action produced by the [vector Button class](http://code.google.com/p/java-vector/source/browse/src/vector/Button.java) in order to complete the semantics required for its application.  This simplicity is made possible by the tight fit of the design of each class to a compact feature set.
