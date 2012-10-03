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

    protected boolean fixed;

    protected float[] domain, range;

    protected Path2D.Float shape;
    /**
     * Preserves the indempotence of a default grid layout
     */
    protected boolean dynamic;


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

        this.fixed = false;

        this.dynamic = false;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.domain = null;
        this.range = null;

        this.shape = null;
    }
    /**
     * Calls {@link #layout()}
     */
    @Override
    public void resized(){
        super.resized();

        this.layout();
    }
    /**
     * Clears the visual state derived from properties, and calls
     * {@link #layout()}
     */
    @Override
    public void modified(){
        super.modified();

        this.layout();
    }
    @Override
    public void relocated(){
        super.relocated();

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
            this.fixed = true;
            break;
        case Parent:
            this.fixed = false;
            break;
        default:
            throw new IllegalStateException(order.name());
        }
        this.modified();
    }
    public boolean isDynamic(){
        return this.dynamic;
    }
    public Boolean getDynamic(){
        if (this.dynamic)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public Grid setDynamic(boolean dynamic){
        this.dynamic = dynamic;
        return this;
    }
    public Grid setDynamic(Boolean dynamic){
        if (null != dynamic)
            return this.setDynamic(dynamic.booleanValue());
        else
            return this;
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
    public final float[] getDomain(){
        float[] domain = this.domain;
        if (null != domain)
            return domain.clone();
        else
            return new float[0];
    }
    public Grid setDomain(float[] domain){

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
    public Grid setRange(float[] range){

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

            g.setColor(this.color);
            g.draw(shape);
        }
        return this;
    }
    public Grid outputOverlay(Graphics2D g){

        return this;
    }
    protected void layout(){

        if (!this.fixed){

            this.setBoundsVectorInit(this.getParentVector());
        }

        final Bounds bounds = this.getBoundsVector();
        if (!bounds.isEmpty()){
            float[] range = this.range;
            if (null == range || this.dynamic){
                this.dynamic = true;

                range = Default(bounds.height);

                this.setRange(range);
            }
            float[] domain = this.domain;
            if (null == domain || this.dynamic){
                this.dynamic = true;

                domain = Default(bounds.width);

                this.setDomain(domain);
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
        ObjectJson thisModel = super.toJson();

        thisModel.setValue("color", this.getColor());
        thisModel.setValue("domain", this.getDomain());
        thisModel.setValue("range", this.getRange());
        thisModel.setValue("fixed",this.getFixed());
        thisModel.setValue("dynamic",this.getDynamic());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setColor( (Color)thisModel.getValue("color",Color.class));
        this.setDomain( (float[])thisModel.getValue("domain",float[].class));
        this.setRange( (float[])thisModel.getValue("range",float[].class));
        this.setFixed( (Boolean)thisModel.getValue("fixed"));
        this.setDynamic( (Boolean)thisModel.getValue("dynamic"));

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
}
