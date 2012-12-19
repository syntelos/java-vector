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

import json.Json;

/**
 * 
 */
public abstract class Stroke
    extends java.awt.BasicStroke
{
    /**
     * If not null, override any other color definition for the
     * application of this stroke.
     */
    protected Color color;


    public Stroke(float lineWidth, int endCap, int lineJoin, float miterLimit, float[] dashArray, float dashPhase){
        super(lineWidth,endCap,lineJoin,miterLimit,dashArray,dashPhase);
    }


    public boolean hasColor(){
        return (null != this.color);
    }
    public Color getColorVector(){
        return this.color;
    }

}
