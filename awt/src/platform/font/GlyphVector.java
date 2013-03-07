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
package platform.font;

import platform.Font;
import platform.Path;
import platform.Shape;
import platform.geom.Point;

/**
 * 
 */
public class GlyphVector
    extends Object
    implements vector.font.GlyphVector
{

    private final java.awt.font.GlyphVector vector;

    private final int count;


    public GlyphVector(Font font, String string){
        super();
        if (null != font && null != string){
            final int count = string.length();
            if (-1 < count){
                this.vector = font.createGlyphVector(font.frc,string);
                this.count = count;
            }
            else
                throw new IllegalArgumentException();
        }
        else
            throw new IllegalArgumentException();
    }


    public Shape createOutline(Point baseline){

        return new Path(this.vector.getOutline(baseline.x,baseline.y));
    }
    public float[] queryPositions(Point baseline){

        final int c = (this.count+1);

        float[] positions = this.vector.getGlyphPositions(0,c,new float[c<<1]);

        for (int cc = 0; cc < c; cc++){
            int ix = (cc<<1);
            int iy = (ix+1);
            positions[ix] += baseline.x;
            positions[iy] += baseline.y;
        }
        return positions;
    }
}
