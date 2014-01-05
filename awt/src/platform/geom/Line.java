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
public class Line
    extends java.awt.geom.Line2D.Float
    implements vector.geom.Line
{

    public Line(){
        super();
    }
    public Line(float x0, float y0, float x1, float y1){
        super(x0,y0,x1,y1);
    }
    public Line(Point a, Point b){
        super(a,b);
    }


    public Bounds getBoundsVector(){
        java.awt.geom.Rectangle2D bounds = super.getBounds2D();
        return new Bounds(bounds.getX(),bounds.getY(),bounds.getWidth(),bounds.getHeight());
    }
    public Path toPath(){
        return new Path(this);
    }
    public String toString(){
        return Path.ToString(this);
    }

}
