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
package platform.font;

import platform.Font;
import platform.Path;
import platform.Shape;
import platform.Transform;
import platform.geom.Point;

/**
 * 
 */
public class GlyphVector
    extends Object
    implements vector.font.GlyphVector
{

    private final Path path;

    private final float[] xary;

    private final int count;


    public GlyphVector(Font font, String string){
        super();
        if (null != font && null != string){
            final int count = string.length();
            if (0 < count){
                this.count = count;
                this.path = new Path();
                font.getTextPath(string,0,count,0f,0f,this.path);
                this.xary = new float[count];
                font.getTextWidths(string,0,count,this.xary);
            }
            else
                throw new IllegalArgumentException();
        }
        else
            throw new IllegalArgumentException();
    }


    public Shape createOutline(Point baseline){

        Path copy = new Path();

        copy.addPath(this.path,baseline.x,baseline.y);

        return copy;
    }
    public float[] queryPositions(Point baseline){

        final int count = this.count<<1;
        final float[] positions = new float[count];
        for (int cc = 0; cc < count; ){
            positions[cc++] = this.xary[cc>>1];
            positions[cc++] = baseline.y;
        }
        return positions;
    }
}
