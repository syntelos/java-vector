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
import java.awt.Shape;

/**
 * Border for parent.
 */
public class Border
    extends AbstractComponent
    implements Component.Layout, 
               java.lang.Cloneable
{
    public enum Style {
        SQUARE, ROUND, NONE;


        public String toString(){
            return this.name().toLowerCase();
        }

        public final static Style For(String string){
            if (null == string)
                return null;
            else {
                try {
                    return Style.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                    return null;
                }
            }
        }
    }


    protected Color color, colorOver;
    protected Color background, backgroundOver;
    protected Style style;
    protected boolean fill, fixed;
    protected float arc;
    protected Stroke stroke, strokeOver;
    protected Shape shape;


    public Border(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.color = Color.black;
        this.style = Style.SQUARE;
        this.fill = false;
        this.fixed = false;
        this.arc = 0f;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.colorOver = null;
        this.background = null;
        this.backgroundOver = null;
        this.stroke = null;
        this.strokeOver = null;
        this.shape = null;
    }
    @Override
    public void resized(){
        super.resized();

        this.layout();
    }
    @Override
    public void modified(){
        super.modified();

        this.layout();
    }
    public Border clone(){
        try {
            return (Border)super.clone();
        }
        catch (CloneNotSupportedException exc){
            throw new InternalError();
        }
    }
    public Component.Layout.Order queryLayout(){

        if (this.fixed)
            return Component.Layout.Order.Content;
        else
            return Component.Layout.Order.Parent;
    }
    public Bounds queryBoundsContent(){

        return this.getBoundsVector();
    }
    public void layout(Component.Layout.Order order){
        switch(order){
        case Content:
            break;
        case Parent:
            break;
        default:
            throw new IllegalStateException(order.name());
        }
        this.modified();
    }
    public final boolean isFill(){

        return this.fill;
    }
    public final Boolean getFill(){

        if (this.fill)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Border setFill(boolean fill){

        this.fill = fill;
        return this;
    }
    public final Border setFill(Boolean fill){

        if (null != fill)
            return this.setFill(fill.booleanValue());
        else
            return this;
    }
    public final boolean isFixed(){

        return this.fixed;
    }
    public final Boolean getFixed(){

        if (this.fixed)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Border setFixed(boolean fixed){

        this.fixed = fixed;
        return this;
    }
    public final Border setFixed(Boolean fixed){

        if (null != fixed)
            return this.setFixed(fixed.booleanValue());
        else
            return this;
    }
    public final float getArc(){

        return this.arc;
    }
    public final Border setArc(float arc){
        if (0.0f <= arc){
            this.arc = arc;
        }
        return this;
    }
    public final Border setArc(Number arc){

        if (null != arc)
            return this.setArc(arc.floatValue());
        else
            return this;
    }
    public final Color getColor(){

        return this.color;
    }
    public final Border setColor(Color color){
        if (null != color){
            this.color = color;
        }
        return this;
    }
    public final Border setColor(String code){
        if (null != code)
            return this.setColor(new Color(code));
        else
            return this;
    }
    public final Color getColorOver(){

        return this.colorOver;
    }
    public final Border setColorOver(Color colorOver){
        if (null != colorOver){
            this.colorOver = colorOver;
        }
        return this;
    }
    public final Border setColorOver(String code){
        if (null != code)
            return this.setColorOver(new Color(code));
        else
            return this;
    }
    public final Color getBackground(){

        return this.background;
    }
    public final Border setBackground(Color background){
        if (null != background){
            this.background = background;
        }
        return this;
    }
    public final Border setBackground(String code){
        if (null != code)
            return this.setBackground(new Color(code));
        else
            return this;
    }
    public final Color getBackgroundOver(){

        return this.backgroundOver;
    }
    public final Border setBackgroundOver(Color backgroundOver){
        if (null != backgroundOver){
            this.backgroundOver = backgroundOver;
        }
        return this;
    }
    public final Border setBackgroundOver(String code){
        if (null != code)
            return this.setBackgroundOver(new Color(code));
        else
            return this;
    }
    public final Style getStyle(){

        return this.style;
    }
    public final String getStyleString(){
        Style style = this.style;
        if (null != style)
            return style.toString();
        else
            return null;
    }
    public final Border setStyle(Style style){
        if (null != style){
            this.style = style;
        }
        return this;
    }
    public final Border setStyle(String code){
        if (null != code)
            return this.setStyle(Style.For(code));
        else
            return this;
    }
    public final Stroke getStroke(){

        return this.stroke;
    }
    public final Border setStroke(Stroke stroke){

        this.stroke = stroke;

        return this;
    }
    public final Stroke getStrokeOver(){

        return this.strokeOver;
    }
    public final Border setStrokeOver(Stroke strokeOver){

        this.strokeOver = strokeOver;

        return this;
    }
    public final Shape getShape(){

        return this.shape;
    }
    public final Border outputScene(Graphics2D g){

        final Shape shape = this.shape;
        if (this.visible){
            final boolean mouseIn = this.mouseIn;
            /*
             *
             */
            final Color background = this.background;
            if (null != background){
                final Color backgroundOver = this.backgroundOver;
                if (null != backgroundOver && mouseIn) 
                    g.setColor(backgroundOver);
                else 
                    g.setColor(this.background);

                if (null != shape)
                    g.fill(shape);
                else
                    g.fill(this.getBoundsVector());
            }
            /*
             *
             */
            if (null != shape){

                final Color colorOver = this.colorOver;
                if (null != colorOver && mouseIn) 
                    g.setColor(colorOver);
                else
                    g.setColor(this.color);

                if (this.fill){

                    g.fill(shape);
                }


                final Stroke strokeOver = this.strokeOver;
                if (null != strokeOver && mouseIn){

                    if (null != strokeOver.color)
                        g.setColor(strokeOver.color);

                    g.setStroke(strokeOver);

                    g.draw(shape);
                }
                else if (null != this.stroke){

                    if (null != stroke.color)
                        g.setColor(stroke.color);

                    g.setStroke(this.stroke);

                    g.draw(shape);
                }
                else {

                    g.draw(shape);
                }
            }
        }
        return this;
    }
    public Border outputOverlay(Graphics2D g){

        return this;
    }
    protected void layout(){

        if (!this.fixed){

            this.setBoundsVectorInit(this.getParentVector());
        }

        Bounds bounds = this.getBoundsVector();
        switch(this.style){
        case SQUARE:
            this.shape = bounds;
            break;
        case ROUND:
            this.shape = bounds.round(this.arc);
            break;
        case NONE:
            this.shape = null;
            break;
        default:
            throw new IllegalStateException(this.style.name());
        }
    }
    public boolean input(Event e){
        if (super.input(e)){

            this.outputScene();
            return true;
        }
        else
            return false;
    }

    public ObjectJson toJson(){

        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("color",this.getColor());
        thisModel.setValue("color-over",this.getColorOver());
        thisModel.setValue("background",this.getBackground());
        thisModel.setValue("background-over",this.getBackgroundOver());
        thisModel.setValue("style",this.getStyleString());
        thisModel.setValue("fill",this.getFill());
        thisModel.setValue("fixed",this.getFixed());
        thisModel.setValue("arc",this.getArc());
        thisModel.setValue("stroke",this.getStroke());
        thisModel.setValue("stroke-over",this.getStrokeOver());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setColor( thisModel.getValue("color",Color.class));
        this.setColorOver( thisModel.getValue("color-over",Color.class));
        this.setBackground( thisModel.getValue("background",Color.class));
        this.setBackgroundOver( thisModel.getValue("background-over",Color.class));
        this.setStyle( (String)thisModel.getValue("style"));
        this.setFill( (Boolean)thisModel.getValue("fill"));
        this.setFixed( (Boolean)thisModel.getValue("fixed"));
        this.setArc( (Number)thisModel.getValue("arc"));
        this.setStroke( (Stroke)thisModel.getValue("stroke",Stroke.class));
        this.setStrokeOver( (Stroke)thisModel.getValue("stroke-over",Stroke.class));

        return true;
    }
}
