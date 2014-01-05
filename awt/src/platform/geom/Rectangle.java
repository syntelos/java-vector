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
package platform.geom;

import platform.Path;

import vector.Bounds;

/**
 * 
 */
public class Rectangle
    extends java.awt.geom.Rectangle2D.Float
    implements vector.geom.Rectangle
{

    public Rectangle(){
        super();
    }
    public Rectangle(float x, float y, float w, float h){
        super(x,y,w,h);
    }
    public Rectangle(java.awt.geom.Rectangle2D.Float r){
        super(r.x,r.y,r.width,r.height);
    }
    public Rectangle(java.awt.geom.Rectangle2D.Double r){
        super((float)r.x,(float)r.y,(float)r.width,(float)r.height);
    }
    public Rectangle(java.awt.geom.RectangularShape r){
        this((float)r.getX(),(float)r.getY(),(float)r.getWidth(),(float)r.getHeight());
    }
    public Rectangle(vector.geom.RectangularShape r){
        this((float)r.getX(),(float)r.getY(),(float)r.getWidth(),(float)r.getHeight());
    }


    public void setFrameVector(vector.geom.RectangularShape r){

        super.setFrame(r.getX(),r.getY(),r.getWidth(),r.getHeight());
    }
    public boolean contains(vector.geom.RectangularShape r){

        return super.contains(r.getX(),r.getY(),r.getWidth(),r.getHeight());
    }
    public boolean contains(int x, int y){

        return super.contains(x,y);
    }
    public boolean contains(float x, float y){

        return super.contains(x,y);
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
}
