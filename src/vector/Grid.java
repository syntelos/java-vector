/*
 * Java Vector
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
import java.awt.font.GlyphVector;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * <p> This class will draw a grid as defined by the domain and range
 * shape coordinates.  It can optionally track mouse motion. </p>
 * 
 * <p> If not "fixed" ("bounds" defined explicitly), this class will
 * assume the bounds and transform of its parent. </p>
 * 
 * <h3>Domain &amp; Range</h3>
 * 
 * <p> The domain and range lists are coordinates in the shape
 * coordinate space, under the local transform. </p>
 * 
 */
public class Grid
    extends AbstractComponent
    implements Component.Layout
{

    protected Color color;

    protected boolean mouse, fixed;

    protected float[] domain, range;

    protected Path2D.Float shape, pointer;


    public Grid(){
        super();
    }

    /**
     * Grid Future: called after defining user properties in Grid
     */
    @Override
    public void init(){
        super.init();

        this.color = Color.black;
        this.mouse = false;
        this.fixed = false;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.domain = null;
        this.range = null;

        this.shape = null;
        this.pointer = null;
    }
    /**
     * Calls {@link #layout()}
     */
    @Override
    public void resized(){
        super.resized();

        this.pointer = null;

        this.layout();
    }
    /**
     * Clears the visual state derived from properties, and calls
     * {@link #layout()}
     */
    @Override
    public void modified(){
        super.modified();

        this.pointer = null;

        this.layout();
    }
    @Override
    public void relocated(){
        super.relocated();

        this.pointer = null;

        this.layout();
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
    public final Color getColor(){

        return this.color;
    }
    public final Grid setColor(Color color){
        if (null != color){
            this.color = color;
        }
        return this;
    }
    public final Grid setColor(String code){
        if (null != code)
            return this.setColor(new Color(code));
        else
            return this;
    }
    public final boolean isMouse(){
        return this.mouse;
    }
    public final Boolean getMouse(){
        return this.mouse;
    }
    public final Grid setMouse(boolean mouse){
        this.mouse = mouse;
        return this;
    }
    public final Grid setMouse(Boolean mouse){
        if (null != mouse)
            this.mouse = mouse;
        return this;
    }
    public final float[] getDomain(){
        float[] domain = this.domain;
        if (null != domain)
            return domain.clone();
        else
            return new float[0];
    }
    public final Grid setDomain(float[] domain){

        if (null != domain && 0 < domain.length)

            this.domain = domain.clone();
        else
            this.domain = null;

        return this;
    }
    public final float[] getRange(){
        float[] range = this.range;
        if (null != range)
            return range.clone();
        else
            return new float[0];
    }
    public final Grid setRange(float[] range){

        if (null != range && 0 < range.length)

            this.range = range.clone();
        else
            this.range = null;

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
    public final Grid setFixed(boolean fixed){

        this.fixed = fixed;
        return this;
    }
    public final Grid setFixed(Boolean fixed){

        if (null != fixed)
            return this.setFixed(fixed.booleanValue());
        else
            return this;
    }
    public Grid outputScene(Graphics2D g){
        Shape shape = this.shape;
        if (null != shape){
            this.getTransformParent().transformFrom(g);

            g.setColor(this.getColor());
            g.draw(shape);
        }
        return this;
    }
    public Grid outputOverlay(Graphics2D g){
        Shape shape = this.pointer;
        if (null != shape){
            g.setColor(this.getColor());
            g.draw(shape);
        }
        return this;
    }
    @Override
    public boolean input(Event e){

        switch(e.getType()){

        case MouseEntered:
            this.mouseIn = true;
            return true;
        case MouseExited:
            this.mouseIn = false;
            if (this.mouse){
                this.pointer = null;
                this.outputOverlay();
            }
            return true;
        case MouseMoved:
            if (this.mouse){

                final float radius = Radius(this.getBoundsVector());

                final Point2D mouse = ((Event.Mouse.Motion)e).getPoint();
                {
                    final float x = (float)mouse.getX();
                    final float y = (float)mouse.getY();
                    final float x0 = (x-radius);
                    final float x1 = (x+radius);
                    final float y0 = (y-radius);
                    final float y1 = (y+radius);

                    final Path2D.Float pointer = new Path2D.Float();
                    {
                        pointer.moveTo(x0,y);
                        pointer.lineTo(x1,y);
                        pointer.moveTo(x,y0);
                        pointer.lineTo(x,y1);
                    }
                    this.pointer = pointer;
                }
                this.outputOverlay();
            }
            return false;
        default:
            return false;
        }
    }
    protected void layout(){

        if (!this.fixed){

            Component parent = this.getParentVector();

            this.setBoundsVectorInit(parent);
            this.setTransformLocal(parent.getTransformLocal());
        }

        final Bounds bounds = this.getBoundsVector();
        if (!bounds.isEmpty()){
            float[] range = this.range;
            if (null == range){
                range = Default(bounds.height);
            }
            float[] domain = this.domain;
            if (null == domain){
                domain = Default(bounds.width);
            }

            final float x0 = 0f;
            final float x1 = bounds.width;
            final float y0 = 0f;
            final float y1 = bounds.height;

            final Path2D.Float shape = new Path2D.Float();

            for (float x: domain){
                shape.moveTo(x,y0);
                shape.lineTo(x,y1);
            }
            for (float y: range){
                shape.moveTo(x0,y);
                shape.lineTo(x1,y);
            }
            this.shape = shape;
        }
    }

    public ObjectJson toJson(){
        ObjectJson thisModel = (ObjectJson)super.toJson();

        thisModel.setValue("color", this.getColor());

        thisModel.setValue("mouse",this.getMouse());

        thisModel.setValue("domain", this.getDomain());

        thisModel.setValue("range", this.getRange());

        thisModel.setValue("fixed",this.getFixed());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setColor( (Color)thisModel.getValue("color",Color.class));

        this.setMouse( (Boolean)thisModel.getValue("mouse"));

        this.setDomain( (float[])thisModel.getValue("domain",float[].class));

        this.setRange( (float[])thisModel.getValue("range",float[].class));

        this.setFixed( (Boolean)thisModel.getValue("fixed"));

        return true;
    }


    public final static float[] Default(double end){
        float[] list = new float[10];
        double start = 0.0;
        double inc = (end / 9.0);
        for (int cc = 0; cc < 10; cc++, start += inc){
            list[cc] = (float)start;
        }
        return list;
    }
    public final static float Radius(Bounds dim){
        float gross = Math.max(dim.width,dim.height);
        if (100f < gross)
            return (0.01f * gross);
        else
            return (0.05f * gross);
    }
}
