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
public class RoundRectangle
    extends java.awt.geom.RoundRectangle2D.Float
    implements vector.geom.RoundRectangle
{

    public RoundRectangle(){
        super();
    }
    public RoundRectangle(float x, float y, float w, float h, float aw, float ah){
        super(x,y,w,h,aw,ah);
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
