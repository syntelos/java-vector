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
package vector.font;

import platform.Shape;
import platform.geom.Point;

/**
 * @see platform.Font
 */
public interface GlyphVector {
    /**
     * @param baseline Baseline origin coordinate
     * 
     * @return Outline from baseline origin
     */
    public Shape createOutline(Point baseline);
    /**
     * Glyph baseline position coordinates in shape coordinate space.
     * 
     * <pre>
     * ix = (idx*2);
     * iy = (ix+1);
     * </pre>
     * 
     * @param padding Text padding
     * @param vector Text vector
     * @param count Number of characters in text vector
     * 
     * @return Glyph baseline coordinates for characters in text
     * vector as an array of coordinates in order
     * <i>{Xo,Yo,...,Xn,Yn}</i>, or an array of length zero
     */
    public float[] queryPositions(Point baseline);
}
