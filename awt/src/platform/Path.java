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
package platform;

import vector.Bounds;

/**
 * 
 */
public class Path
    extends java.awt.geom.Path2D.Float
    implements Shape
{

    public Path(){
        super();
    }
    public Path(vector.Path.Winding winding){
        super(winding.ordinal());
    }
    public Path(Shape shape){
        super(shape);
    }
    public Path(Shape shape, Transform xform){
        super(shape,xform);
    }
    public Path(java.awt.Shape shape){
        super(shape);
    }


    public Bounds getBoundsVector(){
        java.awt.geom.Rectangle2D b = super.getBounds2D();
        return new Bounds(b.getX(),b.getY(),b.getWidth(),b.getHeight());
    }
}
