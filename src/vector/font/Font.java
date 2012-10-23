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
package vector.font;

import vector.Bounds;
import vector.Padding;

import platform.geom.Point;

/**
 * 
 */
public interface Font {

    public enum Style {
        PLAIN, BOLD, ITALIC;
    }
    /**
     * @return Group name
     */
    public String getFamily();
    /**
     * @return Item name
     */
    public String getName();
    /**
     * @return Named size
     */
    public int getSize();
    /**
     * @return X-dimension of space (0x20) character
     */
    public float getEm();
    /**
     * @return Y-dimension above baseline 
     */
    public float getAscent();
    /**
     * @return Y-dimension below baseline 
     */
    public float getDescent();
    /**
     * @return Default padding horizontal
     */
    public float getSpacing();
    /**
     * @return Default padding vertical
     */
    public float getLeading();
    /**
     * @return Sum of ascent and descent
     */
    public float getHeight();
    /**
     * @param padding Text padding
     * 
     * @return Glyph text baseline origin
     */
    public Point queryBaseline(Padding padding);
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
    public float[] queryBaselinePositions(Padding padding, GlyphVector vector);

    public Padding defaultPadding();

    public Bounds boundingBox(Padding padding, int rows, int cols);

    public Bounds boundingBox(Padding padding, int rows, float width);

    public GlyphVector createGlyphVector(String string);

    public float em(float n);
    /**
     * @return Font code string <code>'family-style-size'</code>
     */
    public String toString();
}
