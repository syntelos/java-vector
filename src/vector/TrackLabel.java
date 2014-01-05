/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, John Pritchard, Syntelos
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package vector;

import platform.geom.Point;

import json.Json;
import json.ObjectJson;

/**
 * Track mouse motion with ordinate value.  One of the horizontal or
 * vertical {@link Align} properties set to "center" is employed as
 * the tracking axis.
 */
public class TrackLabel
    extends Text
    implements Component.AlignHorizontal, Component.AlignVertical
{

    protected Align.Horizontal horizontal;

    protected Align.Vertical vertical;


    public TrackLabel(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.fixed = true;
    }
    @Override
    public void modified(){

        this.fixed = true;

        super.modified();

        this.align();
    }
    @Override
    public void resized(){

        this.fixed = true;

        super.resized();

        this.align();
    }
    @Override
    public void relocated(){

        this.fixed = true;

        super.relocated();

        this.align();
    }
    public void layout(Order order){
        switch(order){
        case Parent:
            throw new UnsupportedOperationException(order.name());
        case Content:
            this.fixed = true;
            break;
        default:
            throw new IllegalStateException(order.name());
        }
        this.modified();
    }
    /**
     * @return True to receive the motion events of the parent, as for
     * {@link TrackPointer}
     */
    @Override
    public boolean contains(Point p){
        return true;
    }

    public boolean isHorizontal(Align.Horizontal align){
        return (align == this.horizontal);
    }
    public Align.Horizontal getHorizontal(){
        return this.horizontal;
    }
    public String getHorizontalString(){
        Align.Horizontal horizontal = this.horizontal;
        if (null != horizontal)
            return horizontal.toString();
        else
            return null;
    }
    public TrackLabel setHorizontal(Align.Horizontal horizontal){
        this.horizontal = horizontal;
        return this;
    }
    public TrackLabel setHorizontal(String horizontal){
        return this.setHorizontal(Align.Horizontal.For(horizontal));
    }
    public boolean isVertical(Align.Vertical align){
        return (align == this.vertical);
    }
    public Align.Vertical getVertical(){
        return this.vertical;
    }
    public String getVerticalString(){
        Align.Vertical vertical = this.vertical;
        if (null != vertical)
            return vertical.toString();
        else
            return null;
    }
    public TrackLabel setVertical(Align.Vertical vertical){
        this.vertical = vertical;
        return this;
    }
    public TrackLabel setVertical(String vertical){
        return this.setVertical(Align.Vertical.For(vertical));
    }
    public boolean input(Event e){

        if (super.input(e)){

            this.setVisibleVector(this.getParentVector().isMouseIn());

            this.moveto(((Event.Mouse.Motion)e).getPoint());

            this.outputOverlay();

            return true;
        }
        else {
            switch(e.getType()){

            case MouseMoved:{

                this.moveto(((Event.Mouse.Motion)e).getPoint());

                this.outputOverlay();

                return false;
            }
            default:
                return false;
            }
        }
    }
    public TrackLabel outputScene(Context g){

        return this;
    }
    public TrackLabel outputOverlay(Context g){

        if (this.isNotEmpty()){

            super.outputScene(g);
        }
        return this;
    }

    public String propertyNameOfValue(Class vac){
        if (null == vac)
            return null;
        else {
            String name = super.propertyNameOfValue(vac);
            if (null != name)
                return name;
            else {

                if (Align.Horizontal.class.isAssignableFrom(vac))
                    return "horizontal";
                else if (Align.Vertical.class.isAssignableFrom(vac))
                    return "vertical";
                else
                    return null;
            }
        }
    }
    public ObjectJson toJson(){
        ObjectJson thisModel = (ObjectJson)super.toJson();

        thisModel.setValue("horizontal",this.getHorizontalString());
        thisModel.setValue("vertical",this.getVerticalString());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setHorizontal( (String)thisModel.getValue("horizontal"));
        this.setVertical( (String)thisModel.getValue("vertical"));

        return true;
    }
    public void align(){

        if (null != this.horizontal || null != this.vertical){

            Bounds bounds = this.getBoundsVector();
            Bounds parent = this.getParentBounds();
            bounds.apply(this.horizontal,this.getMargin(),parent);
            bounds.apply(this.vertical,this.getMargin(),parent);
            this.setBoundsVector(bounds);
        }
    }
    /**
     * Called by {@link #moveto(platform.Point) moveto}
     * to update label string with tracking ordinate
     */
    public TrackLabel setText(float input){

        super.setText(String.valueOf(input));

        return this;
    }
    public TrackLabel moveto(Point input){

        if (null != this.horizontal || null != this.vertical){

            if (Align.Horizontal.CENTER == this.horizontal){

                this.setText(input.x);

                super.layout();
            }
            else if (Align.Vertical.CENTER == this.vertical){

                this.setText(input.y);

                super.layout();
            }
            /*
             * Set text before getting bounds
             */
            Bounds bounds = this.getBoundsVector();
            bounds.x = input.x;
            bounds.y = input.y;

            Bounds parent = this.getParentBounds();

            if (Align.Horizontal.CENTER == this.horizontal){

                bounds.x -= (bounds.width/2.0f);
            }
            else {
                bounds.apply(this.horizontal,this.getMargin(),parent);
            }


            if (Align.Vertical.CENTER == this.vertical){

                bounds.y -= (bounds.height/2.0f);
            }
            else {
                bounds.apply(this.vertical,this.getMargin(),parent);
            }

            this.setBoundsVector(bounds);

            super.modified();
        }
        return this;
    }

}
