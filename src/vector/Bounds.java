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

import vector.geom.RectangularShape;

import platform.Shape;
import platform.Transform;

import platform.geom.Point;
import platform.geom.Rectangle;
import platform.geom.RoundRectangle;

import java.util.StringTokenizer;

/**
 * {@link Component} bounding box is a storage class, not intermediate
 * value, in 32 bit floating point.
 * 
 * <p> Internal {@link Padding} is contained within the boundaries of
 * a {@link Component}.  External margin is not contained within the
 * boundaries of a {@link Component}, it is external to the bounding
 * box of a {@link Component}. </p>
 * 
 * @see Padding
 * @see Component
 */
public class Bounds
    extends Rectangle
{

    public Bounds(){
        super(0,0,0,0);
    }
    public Bounds(String string){
        super(0,0,0,0);
        if (null != string){
            StringTokenizer strtok = new StringTokenizer(string,"][)(,");
            switch (strtok.countTokens()){
            case 0:
                break;
            case 2:
                try {

                    final float w = java.lang.Float.parseFloat(strtok.nextToken());

                    final float h = java.lang.Float.parseFloat(strtok.nextToken());

                    this.setFrame(0,0,w,h);
                }
                catch (RuntimeException exc){
                    throw new IllegalArgumentException(string,exc);
                }
                break;
            case 4:
                try {
                    final float x = java.lang.Float.parseFloat(strtok.nextToken());

                    final float y = java.lang.Float.parseFloat(strtok.nextToken());

                    final float w = java.lang.Float.parseFloat(strtok.nextToken());

                    final float h = java.lang.Float.parseFloat(strtok.nextToken());

                    this.setFrame(x,y,w,h);
                }
                catch (RuntimeException exc){
                    throw new IllegalArgumentException(string,exc);
                }
                break;
            default:
                throw new IllegalArgumentException(string);
            }
        }
    }
    public Bounds(float x, float y, float w, float h){
        super(x,y,w,h);
    }
    public Bounds(float w, float h){
        super(0f,0f,w,h);
    }
    public Bounds(double x, double y, double w, double h){
        super((float)x,(float)y,(float)w,(float)h);
    }
    public Bounds(double w, double h){
        super(0f,0f,(float)w,(float)h);
    }
    public Bounds(RectangularShape r){
        this(r.getX(),r.getY(),r.getWidth(),r.getHeight());
    }
    public Bounds(Rectangle r){
        this(r.x,r.y,r.width,r.height);
    }
    public Bounds(Shape s){
        this((Rectangle)s.getBoundsVector());
    }
    public Bounds(Component c){
        this(c.getBoundsVector());
    }


    public void init(){

        this.setFrame(0,0,0,0);
    }
    public final boolean isNotEmpty(){
        return (0.0f != this.width && 0.0f != this.height);
    }
    /**
     * Optimized relation of spatial peers (siblings)
     */
    public final boolean contains(Rectangle that){
        if (this.x <= that.x && this.y <= that.y){

            final float thisX1 = (this.x + this.width);
            final float thisY1 = (this.y + this.height);

            final float thatX1 = (that.x + that.width);
            final float thatY1 = (that.y + that.height);

            return (thisX1 >= thatX1 && thisY1 >= thatY1);
        }
        else
            return false;
    }
    /**
     * Relation of container (this parent) and contained (that child)
     */
    public final boolean pcontains(Rectangle that){

        if (0.0f <= that.x && 0.0f <= that.y){

            final float thisX1 = this.width;
            final float thisY1 = this.height;

            final float thatX1 = (that.x + that.width);
            final float thatY1 = (that.y + that.height);

            return (thisX1 >= thatX1 && thisY1 >= thatY1);
        }
        else
            return false;
    }
    /**
     * @return Objective scale to this from that
     */
    public Transform scaleFromAbsolute(RectangularShape that){
        final double sx = (this.width/(that.getX()+that.getWidth()));
        final double sy = (this.height/(that.getY()+that.getHeight()));

        return Transform.getScaleInstance(sx,sy);
    }
    /**
     * @return Objective scale to that from this
     */
    public Transform scaleToAbsolute(RectangularShape that){
        final double sx = ((that.getX()+that.getWidth())/this.width);
        final double sy = ((that.getY()+that.getHeight())/this.height);

        return Transform.getScaleInstance(sx,sy);
    }
    /**
     * @return Objective scale to this from that
     */
    public Transform scaleFromRelative(RectangularShape that){
        final double sx = (this.width/that.getWidth());
        final double sy = (this.height/that.getHeight());

        return Transform.getScaleInstance(sx,sy);
    }
    /**
     * @return Objective scale to that from this
     */
    public Transform scaleToRelative(RectangularShape that){
        final double sx = (that.getWidth()/this.width);
        final double sy = (that.getHeight()/this.height);

        return Transform.getScaleInstance(sx,sy);
    }
    public RoundRectangle round(float arc){

        return new RoundRectangle(this.x,this.y,this.width,this.height,arc,arc);
    }
    /**
     * @return Interior midpoint relative to box location: width
     * divided by two, height divided by two
     */
    public Point midpoint(){
        float x = (this.width/2.0f);
        float y = (this.height/2.0f);
        return new Point(x,y);
    }
    public Bounds apply(Align align, Bounds parent){

        if (null != align)
            return align.apply(this,parent);
        else
            return this;
    }
    public Bounds apply(Align.Horizontal align, Bounds parent){

        if (null != align)
            return align.apply(this,parent);
        else
            return this;
    }
    public Bounds apply(Align.Vertical align, Bounds parent){

        if (null != align)
            return align.apply(this,parent);
        else
            return this;
    }
    public Bounds apply(Align align, Padding margin, Bounds parent){

        if (null != align)
            return align.apply(this,margin,parent);
        else
            return this;
    }
    public Bounds apply(Align.Horizontal align, Padding margin, Bounds parent){

        if (null != align)
            return align.apply(this,margin,parent);
        else
            return this;
    }
    public Bounds apply(Align.Vertical align, Padding margin, Bounds parent){

        if (null != align)
            return align.apply(this,margin,parent);
        else
            return this;
    }
    /**
     * @param g Graphics context
     * @return The argument graphics context, not a clone or copy
     */
    public Context clip(Context g){

        g.translate(this.x,this.y);
        g.clipTo(0,0,(int)Math.ceil(this.width+1),(int)Math.ceil(this.height+1));
        return g;
    }
    public Bounds clone(){
        return (Bounds)super.clone();
    }
    public String toString(){
        StringBuilder string = new StringBuilder();
        string.append('(');
        string.append(this.x);
        string.append(',');
        string.append(this.y);
        string.append(',');
        string.append(this.width);
        string.append(',');
        string.append(this.height);
        string.append(')');
        return string.toString();
    }

}
