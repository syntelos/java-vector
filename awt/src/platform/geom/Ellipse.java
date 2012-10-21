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

import vector.Bounds;

/**
 * 
 */
public class Ellipse
    extends java.awt.geom.Ellipse2D.Float
    implements platform.Shape
{

    public Ellipse(){
        super();
    }
    public Ellipse(float x, float y, float w, float h){
        super(x,y,w,h);
    }


    public Bounds getBoundsVector(){

        return new Bounds(this.x,this.y,this.width,this.height);
    }
}
