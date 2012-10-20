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
package vector.text;

import vector.Padding;
import vector.Text;

import platform.Font;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;


/**
 * Cursor glyph
 */
public class IBeam {

    private volatile int index;

    private volatile boolean left;


    public IBeam(){
        super();
    }


    public Shape getShape(int index, boolean left, Text text){

        this.index = index;
        this.left = left;

        final Font font = text.getFont();

        final Path2D.Float cursor = new Path2D.Float();

        final Point2D.Float top;
        if (left){

            top = text.getTopLeft(index);
        }
        else {

            top = text.getTopRight(index);
        }

        final float x = top.x, y0 = top.y, y1 = (y0+font.height);

        cursor.moveTo(x,y0);
        cursor.lineTo(x,y1);

        return cursor;
    }
    public Shape getShape(Text text){

        this.index = 0;
        this.left = true;

        final Font font = text.getFont();
        final Padding padding = text.getPadding();

        final Path2D.Float cursor = new Path2D.Float();

        final float x = padding.left, y0 = padding.top, y1 = (y0+font.height);

        cursor.moveTo(x,y0);
        cursor.lineTo(x,y1);

        return cursor;
    }
}
