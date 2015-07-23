The tabgrid demo ([launch](http://java-vector.googlecode.com/git/demo/tabgrid.jnlp) / [json](http://code.google.com/p/java-vector/source/browse/tabgrid.json)) is a test of mouse event point transforms across the scene graph.

```js

{
"background": "#FFFFFF",
"components": [
{
"class": "vector.TableBig",
"cellspacing": 2.0,
"components": [
{
"class": "vector.Container",
"components": [
{
"class": "vector.Grid",
"color": "#808F8FAF",
"mouse": true
}
]
},
{...}
]
}
]
}```

## Layout ##

The [big table](http://code.google.com/p/java-vector/source/browse/src/vector/TableBig.java) changes the location and dimension of the bounding boxes of its children, fitting them to a tabular layout that fills its parent.

In this example the table is not "fixed": in this hand crafted code there no use of "fixed" in the scene description.  In this case the table layout adopts a top-down layout strategy, adopting the dimensions of its component parent.  If the table were fixed, it would require a "bounds" property.

## Operation ##

The following clip from the [Container (super) class](http://code.google.com/p/java-vector/source/browse/src/vector/Container.java) shows where the mouse point transformation is done.

```java

public boolean input(Event e){

switch(e.getType()){

case MouseMoved:{
final Point2D.Float point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());

final Event moved = new vector.event.MouseMoved(point);
final Event entered = new vector.event.MouseEntered(point);
final Event exited = new vector.event.MouseExited(point);

for (Component c: this){

if (c.isMouseIn()){

if (c.contains(point)){

c.input(moved);
}
else {
c.input(exited);
}
}
else if (c.contains(point)){
c.input(entered);
}
}
return false;
}
}```

The `transformFromParent` method is inherited by the table from the container from the [AbstractComponent class](http://code.google.com/p/java-vector/source/browse/src/vector/AbstractComponent.java).

```java

protected final Point2D.Float transformFromParent(Point2D point){

return this.getTransformParent().transformFrom(point);
}```

This method employs the `transformFrom(Point2D)` method of the [Transform class](http://code.google.com/p/java-vector/source/browse/src/vector/Transform.java) on its [parent transform](ProgrammingCoordinateSpaces.md).

```java

public Point2D.Float transformFrom(Point2D source){
try {
Point2D.Float target = new Point2D.Float(0,0);
/*
* The transform arithmetic is double, and the point class
* will store to float
*/
this.inverseTransform(source,target);

return target;
}
catch (NoninvertibleTransformException exc){
throw new IllegalStateException(this.toString(),exc);
}
}```

These layers of methods illustrate the transform conventions that have been established in the **vector** package, and are presented in [Programming Coordinate Spaces](ProgrammingCoordinateSpaces.md).
