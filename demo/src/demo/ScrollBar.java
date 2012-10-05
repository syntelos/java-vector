package demo;

import vector.Color;
import vector.Context;
import vector.Event;

import json.Json;
import json.ObjectJson;

import java.awt.geom.Point2D;

public class ScrollBar
    extends vector.ContainerScrollPosition
{

    protected Color color, colorOver;

    protected float radius;


    public ScrollBar(){
        super();
    }


    public void init(){
        super.init();

        this.color = Color.black;
        this.radius = 5.0f;
    }
    public boolean input(Event e){

        if (super.input(e)){

            this.outputOverlay();
            return true;
        }
        else {
            switch(e.getType()){
            case MouseMoved:
                this.outputOverlay();
                return true;
            case MouseDrag:
                this.outputOverlay();
                return true;
            default:
                return false;
            }
        }
    }
    public ScrollBar outputScene(Context g){
        return this;
    }
    public ScrollBar outputOverlay(Context g){

        if (null != this.axis){

            Point2D.Float p = this.queryPosition(this.radius);

            if (this.mouseIn && null != this.colorOver)
                g.setColor(this.colorOver);
            else
                g.setColor(this.color);

            // int x = (int)p.x;
            // int y = (int)p.y;
            // int d = (int)(2.0f*this.radius);

            // g.fillOval(x,y,d,d);
        }
        return this;
    }
    public final Color getColor(){
        return this.color;
    }
    public final ScrollBar setColor(Color color){
        if (null != color){
            this.color = color;
        }
        return this;
    }
    public final Color getColorOver(){
        return this.colorOver;
    }
    public final ScrollBar setColorOver(Color colorOver){
        if (null != colorOver){
            this.colorOver = colorOver;
        }
        return this;
    }
    public final float getRadius(){
        return this.radius;
    }
    public final ScrollBar setRadius(float radius){

        this.radius = radius;

        return this;
    }
    public final ScrollBar setRadius(Float radius){
        if (null != radius)
            return this.setRadius(radius.floatValue());
        else
            return this;
    }

    public ObjectJson toJson(){

        ObjectJson thisModel = super.toJson();

        thisModel.setValue("color",this.getColor());
        thisModel.setValue("color-over",this.getColorOver());
        thisModel.setValue("radius",this.getRadius());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setColor( thisModel.getValue("color",Color.class));
        this.setColorOver( thisModel.getValue("color-over",Color.class));
        this.setRadius( thisModel.getValue("radius",Float.class));

        return true;
    }

}
