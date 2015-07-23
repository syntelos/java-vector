The [JSON library](http://github.com/syntelos/json) conveniently parses and formats its text context via a object representation (tree).  The following example is from the [Text](http://code.google.com/p/java-vector/source/browse/src/vector/Text.java) class.
```java

public ObjectJson toJson(){

ObjectJson thisModel = super.toJson();

thisModel.setValue("text", this.toString());
thisModel.setValue("font", this.getFont().toString());
thisModel.setValue("color", this.getColor());
thisModel.setValue("color-over", this.getColorOver());
thisModel.setValue("fill",this.getFill());
thisModel.setValue("fixed",this.getFixed());
thisModel.setValue("cols",this.getCols());
thisModel.setValue("stroke",this.getStroke());
thisModel.setValue("stroke-over",this.getStrokeOver());

return thisModel;
}
public boolean fromJson(Json thisModel){

super.fromJson(thisModel);

this.setText( (String)thisModel.getValue("text"));
this.setFont( (String)thisModel.getValue("font"));
this.setColor( (Color)thisModel.getValue("color",Color.class));
this.setColorOver( (Color)thisModel.getValue("color-over",Color.class));
this.setFill( (Boolean)thisModel.getValue("fill"));
this.setFixed( (Boolean)thisModel.getValue("fixed"));
this.setCols( (Integer)thisModel.getValue("cols",Integer.class));
this.setStroke( (Stroke)thisModel.getValue("stroke",Stroke.class));
this.setStrokeOver( (Stroke)thisModel.getValue("stroke-over",Stroke.class));

this.modified();

return true;
}```

The [JSON format](http://www.json.org/) has seven terms with the following binding methods:

  * _string_
    * `java.lang.String s = json.getValue(stringname)`
    * `json.setValue(stringname,s)`
  * _number_
    * `java.lang.Number n = json.getValue(stringname)`
    * `json.setValue(stringname,n)`
  * _object_
    * `json.ObjectJson o = json.at(stringname)`
    * `json.setValue(stringname,o)`
  * _array_
    * `lxl.List li = json.getValue(stringname)`
    * `json.setValue(stringname,li)`
    * `json.setValue(stringname,iterable)`
    * `json.setValue(stringname,array)`
  * `true`
    * `java.lang.Boolean b = json.getValue(stringname)`
    * `json.setValue(stringname,b)`
  * `false`
    * `java.lang.Boolean b = json.getValue(stringname)`
    * `json.setValue(stringname,b)`
  * `null`
    * `java.lang.Object o = json.getValue(stringname)`
    * `json.setValue(stringname,o)`

In most cases, using the best primitive for a property is obvious.

One alternative example is the [Bounds](http://code.google.com/p/java-vector/source/browser/src/vector/Bounds.java) class, which employs a string rather than an array with the thought that future versions of Bounds may define relative coordinates or dimensions.  This choice also simplifies its usage, although that idea is not as interesting as making the best fit choice for data communication and storage.  For interoperating with other programs, the best JSON is more valuable than the most convenient **vector** coding pattern.  _The network is the computer_.

The [json](http://github.com/syntelos/json) library has a number of java instance object binding strategies available via its `getValue` / `setValue` methods.  The big object strategy is employed by the [vector component](http://code.google.com/p/java-vector/source/browser/src/vector/Component.java), and is represented by the [json.Builder](http://github.com/syntelos/json/blob/master/src/json/Builder.java) interface.

Lib `json` also knows how to handle the `java.lang` primitive types, and will attempt a string (`object.toString() - class construct(String)`) strategy for any other class employed as a parameter to the `json.getValue(name,class)` method.  The string strategy is employed by the vector classes [Bounds](http://code.google.com/p/java-vector/source/browser/src/vector/Bounds.java), [Transform](http://code.google.com/p/java-vector/source/browser/src/vector/Transform.java), and [Padding](http://code.google.com/p/java-vector/source/browser/src/vector/Padding.java).

These facilities are found in the [json.Json](https://github.com/syntelos/json/blob/master/src/json/Json.java) abstract base class, and its friends [Primitive](http://github.com/syntelos/json/blob/master/src/json/Primitive.java) and [Builder](http://github.com/syntelos/json/blob/master/src/json/Builder.java).

