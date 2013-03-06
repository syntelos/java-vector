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
package geda;

import platform.Color;

/**
 * Indexed color map
 */
public enum ColorMap {
    BACKGROUND_COLOR(Color.black),
    PIN_COLOR(Color.green),
    NET_ENDPOINT_COLOR(Color.red),
    GRAPHIC_COLOR(Color.green),
    NET_COLOR(Color.green),
    ATTRIBUTE_COLOR(Color.red),
    LOGIC_BUBBLE_COLOR(Color.yellow),
    DOTS_GRID_COLOR(Color.gray),
    DETACHED_ATTRIBUTE_COLOR(Color.gray),
    TEXT_COLOR(Color.green),
    BUS_COLOR(Color.green),
    SELECT_COLOR(Color.orange),
    BOUNDINGBOX_COLOR(Color.green),
    ZOOM_BOX_COLOR(Color.white),
    STROKE_COLOR(Color.green),
    LOCK_COLOR(Color.red),
    OUTPUT_BACKGROUND_COLOR(Color.white),
    FREESTYLE1_COLOR(Color.cyan),
    FREESTYLE2_COLOR(Color.blue),
    FREESTYLE3_COLOR(Color.magenta),
    FREESTYLE4_COLOR(Color.orange),
    JUNCTION_COLOR(Color.orange),
    MESH_GRID_MAJOR_COLOR(Color.lightGray),
    MESH_GRID_MINOR_COLOR(Color.lightGray);


    private Color color;


    private ColorMap(Color color){
        this.color = color;
    }


    public void setColor(Color color){
        if (null != color)
            this.color = color;
    }
    public Color getColor(){
        return this.color;
    }

    private final static ColorMap[] Values = ColorMap.values();

    public final static ColorMap For(int index){
        if (-1 < index && index < Values.length)
            return Values[index];
        else
            return null;
    }
    public final static Color Color(int index){
        if (-1 < index && index < Values.length)

            return Values[index].color;
        else
            return null;
    }
}
