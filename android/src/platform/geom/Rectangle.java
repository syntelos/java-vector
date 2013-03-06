/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, John Pritchard, Syntelos
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
package platform.geom;

import platform.Path;

import vector.Bounds;

/**
 * 
 */
public class Rectangle
    extends Object
    implements vector.geom.Rectangle
{

    public float x, y, width, height;


    public Rectangle(){
        super();
    }
    public Rectangle(float x, float y, float w, float h){
        super();
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }
    public Rectangle(android.graphics.Rect r){
        super();
        this.x = r.left;
        this.y = r.top;
        this.width = (r.right-r.left);
        this.height = (r.bottom-r.top);
    }
    public Rectangle(android.graphics.RectF r){
        super();
        this.x = r.left;
        this.y = r.top;
        this.width = (r.right-r.left);
        this.height = (r.bottom-r.top);
    }
    public Rectangle(vector.geom.RectangularShape r){
        this((float)r.getX(),(float)r.getY(),(float)r.getWidth(),(float)r.getHeight());
    }


    public boolean isEmpty(){
        return (0f >= this.width)&&(0f >= this.height);
    }
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public double getWidth(){
        return this.width;
    }
    public double getHeight(){
        return this.height;
    }
    public double getMinX(){
        return this.x;
    }
    public double getMinY(){
        return this.y;
    }
    public double getMaxX(){
        return (this.x+this.width);
    }
    public double getMaxY(){
        return (this.y+this.height);
    }
    public double getCenterX(){
        return (this.x+(this.width/2));
    }
    public double getCenterY(){
        return (this.y+(this.height/2));
    }
    public void setFrame(double x, double y, double w, double h){
        this.x = (float)x;
        this.y = (float)y;
        this.width = (float)w;
        this.height = (float)h;
    }
    public void setFrameVector(vector.geom.RectangularShape r){
        this.x = (float)r.getX();
        this.y = (float)r.getY();
        this.width = (float)r.getWidth();
        this.height = (float)r.getHeight();
    }
    public boolean contains(int x, int y){
        return ((this.x <= x && (this.x+this.width) >= x)&&
                (this.y <= y && (this.y+this.height) >= y));
    }
    public boolean contains(float x, float y){
        return ((this.x <= x && (this.x+this.width) >= x)&&
                (this.y <= y && (this.y+this.height) >= y));
    }
    public boolean contains(vector.geom.RectangularShape r){
        final float x0 = (float)r.getX();
        final float y0 = (float)r.getY();
        final float w = (float)r.getWidth();
        final float h = (float)r.getHeight();
        final float x1 = (x0+w);
        final float y1 = (y0+h);

        return ((this.x <= x0 && (this.x+this.width) >= x0)&&
                (this.y <= y0 && (this.y+this.height) >= y0)&&
                (this.x <= x1 && (this.x+this.width) >= x1)&&
                (this.y <= y1 && (this.y+this.height) >= y1));
    }
    public Bounds getBoundsVector(){

        return new Bounds(this.x,this.y,this.width,this.height);
    }
    public Path toPath(){
        return new Path(this);
    }
    public String toString(){
        return Path.ToString(this);
    }
    public Rectangle clone(){
        try {
            return (Rectangle)super.clone();
        }
        catch (java.lang.CloneNotSupportedException exc){
            throw new java.lang.InternalError();
        }
    }
}
