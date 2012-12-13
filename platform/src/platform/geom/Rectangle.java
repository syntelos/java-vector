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
    }


    public boolean isEmpty(){
        return false;
    }
    public double getX(){
        return 0;
    }
    public double getY(){
        return 0;
    }
    public double getWidth(){
        return 0;
    }
    public double getHeight(){
        return 0;
    }
    public double getMinX(){
        return 0;
    }
    public double getMinY(){
        return 0;
    }
    public double getMaxX(){
        return 0;
    }
    public double getMaxY(){
        return 0;
    }
    public double getCenterX(){
        return 0;
    }
    public double getCenterY(){
        return 0;
    }
    public void setFrame(double x, double y, double w, double h){
    }
    public void setFrameVector(vector.geom.RectangularShape r){
    }
    public boolean contains(int x, int y){
        return false;
    }
    public boolean contains(float x, float y){
        return false;
    }
    public boolean contains(vector.geom.RectangularShape r){
        return false;
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
        return null;
    }
}
