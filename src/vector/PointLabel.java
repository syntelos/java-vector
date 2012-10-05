/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, The DigiVac Company
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

import json.Json;
import json.ObjectJson;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Label a horizontal or vertical point with the ordinate value.  One
 * of the horizontal or vertical {@link Align} properties set to
 * "center" is employed as the label axis.  The {@link #layout()
 * layout} operator employs the value returned by {@link
 * #getTextPoint() getTextPoint} as the ordinate value for the
 * "center" axis.
 * 
 * A point {@link Reference} may be employed (JSON "point-reference")
 * to pull an ordinate from a predecessor in the component hierarchy.
 */
public class PointLabel
    extends Text
    implements Component.AlignHorizontal, Component.AlignVertical
{

    protected Align.Horizontal horizontal;

    protected Align.Vertical vertical;

    protected Reference pointReference;


    public PointLabel(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.fixed = true;
        this.pointReference = null;
    }
    @Override
    public void modified(){

        this.fixed = true;

        super.modified();
    }
    @Override
    public void resized(){

        this.fixed = true;

        super.resized();
    }
    @Override
    public void relocated(){

        this.fixed = true;

        super.relocated();
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

    public Float getTextPoint(){

        String text = this.getText();

        if (null != text && 0 < text.length())
            return new Float(text);
        else
            return null;
    }
    public PointLabel setTextPoint(float input){

        this.setText(String.valueOf(input));

        return this;
    }
    public PointLabel setTextPoint(Number input){

        if (null != input)
            this.setText(input.toString());

        return this;
    }
    public Reference getTextPointReference(){


        return this.pointReference;
    }
    public PointLabel setTextPointReference(Reference reference){

        this.pointReference = reference;

        return this;
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
    public PointLabel setHorizontal(Align.Horizontal horizontal){
        this.horizontal = horizontal;
        return this;
    }
    public PointLabel setHorizontal(String horizontal){
        return this.setHorizontal(Align.Horizontal.For(horizontal));
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
    public PointLabel setVertical(Align.Vertical vertical){
        this.vertical = vertical;
        return this;
    }
    public PointLabel setVertical(String vertical){
        return this.setVertical(Align.Vertical.For(vertical));
    }

    public ObjectJson toJson(){
        ObjectJson thisModel = (ObjectJson)super.toJson();

        thisModel.setValue("horizontal",this.getHorizontalString());
        thisModel.setValue("vertical",this.getVerticalString());
        thisModel.setValue("point-reference",this.getTextPointReference());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setHorizontal( (String)thisModel.getValue("horizontal"));
        this.setVertical( (String)thisModel.getValue("vertical"));
        this.setTextPointReference( (Reference)thisModel.getValue("point-reference",Reference.class));

        return true;
    }
    public void layout(){

        if (null != this.pointReference){

            this.setTextPoint(this.pointReference.dereference(this,Number.class));
        }

        super.layout();

        if (null != this.horizontal || null != this.vertical){


            Float ordinate = this.getTextPoint();
            if (null != ordinate){

                Bounds bounds = this.getBoundsVector();
                Bounds parent = this.getParentBounds();

                if (Align.Horizontal.CENTER == this.horizontal){

                    bounds.x = (ordinate - (bounds.width/2.0f));

                    bounds.apply(this.vertical,this.getMargin(),parent);
                }
                else if (Align.Vertical.CENTER == this.vertical){

                    bounds.y = (ordinate - (bounds.height/2.0f));

                    bounds.apply(this.horizontal,this.getMargin(),parent);
                }

                this.setBoundsVector(bounds);

                this.setVisibleVector(parent.pcontains(bounds));
            }
        }
    }

}
