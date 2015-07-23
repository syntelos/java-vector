The dialog demo ([launch](http://java-vector.googlecode.com/git/demo/dialog.jnlp) / [json](http://code.google.com/p/java-vector/source/browse/dialog.json)) demonstrates a number of features of the **vector** package.  The following listing of the JSON scene description has been reduced to clarify this illustration.

```js

{
"components": [
{
"class": "vector.Dialog",
"enum-class": "vector.Dialog$AcceptCancel",
"bounds": "100,100,100,100",
"fit": true,
"components": [
{   "class": "vector.Border" },
{   "class": "vector.TableSmall",
"bounds": "4,4,4,4",
"components": [
{
"class": "vector.Text", "fixed": true,
"text": "Server"
},
{
"class": "vector.TextEdit", "fixed": true,
"border": {...}
},
{...},
{
"class": "vector.TableSmall",
"components": [
{
"class": "vector.Button", "text": "Accept",
"enum-class": "vector.Dialog$AcceptCancel",
"enum-value": "Accept",
"border": {...}
},
{
"class": "vector.Button", "text": "Cancel",
"enum-class": "vector.Dialog$AcceptCancel",
"enum-value": "Cancel",
"border": {...}
}    ]
}    ]    }    ]    }    ]   }```

## Layout ##

From the top, the [vector Dialog class](http://code.google.com/p/java-vector/source/browse/src/vector/Dialog.java) is given bounds and directed to employ the "fit" to children layout strategy that it inherits from [Container](http://code.google.com/p/java-vector/source/browse/src/vector/Container.java).  The X,Y location of this bounding box is preserved, while the W,H dimension is replaced with the dynamic layout of its children.

The component children of the dialog is first the dialog [Border](http://code.google.com/p/java-vector/source/browse/src/vector/Border.java), and second a small table.  The [Small Table](http://code.google.com/p/java-vector/source/browse/src/vector/TableSmall.java) preserves the dimensions of its children as well as its own location in fitting its children into a predefined number of columns.  The small table default number of columns is two, which is employed here (no use of "cols" in the scene description).

The small table has a number of children in an order which it lays out into two columns.  The second to last child, not present in the reduced code (above), is a [Container](http://code.google.com/p/java-vector/source/browse/src/vector/Container.java) with no properties which is used as a table cell placeholder.  The Container class has been used for this purpose because it is a simple Component, while any Component that has an _empty_ [coherent state](ProgrammingCoherentState.md) would be applicable.

## Operation ##

The [Dialog](http://code.google.com/p/java-vector/source/browse/src/vector/Dialog.java), [Button](http://code.google.com/p/java-vector/source/browse/src/vector/Button.java), and action [Event](http://code.google.com/p/java-vector/source/browse/src/vector/Event.java) classes employ an [Enum](http://docs.oracle.com/javase/6/docs/api/java/lang/Enum.html) architecture.

In a dialog they share an Enum class, creating an action event group.  This class name is formatted for the [Class forName](http://docs.oracle.com/javase/6/docs/api/java/lang/Class.html#forName(java.lang.String)) function: a nested inner class name is separated from its containing class name by the `'$'` character.

In this simple example, the only listener for the button click action is the dialog which closes when an action in this (enum) class is dispatched by a button.

The [vector Button class](http://code.google.com/p/java-vector/source/browse/src/vector/Button.java) dispatches its action in the following code.

```java

public boolean input(Event e){
if (super.input(e))
return true;
else {

switch(e.getType()){
case MouseUp:
if (this.mouseIn){

if (null != this.enumValue){

final Component root = this.getRootContainer();
if (null != root){

NamedAction action = new NamedAction(this.enumValue);

root.input(action);
}
else
throw new IllegalStateException();
}

return true;
}
else
return false;

default:
return false;
}
}
}```

The Button creates and dispatches an action Event with its Enum value.

The [vector Dialog class](http://code.google.com/p/java-vector/source/browse/src/vector/Dialog.java) receives the action in the following code.

```java

public boolean input(Event e){
if (super.input(e))
return true;
else {

switch(e.getType()){
case Action:{
Event.NamedAction action = (Event.NamedAction)e;

if (this.mouseIn && null != this.enumClass && this.enumClass.equals(action.getValueClass())){

this.drop(this);

return true;
}
else
return false;
}
default:
return false;
}
}
}```

It simply disposes itself.  The Component drop method removes and destroys a component from the scene graph.
