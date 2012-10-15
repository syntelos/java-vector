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
package vector;


import java.awt.Image;
import java.awt.Shape;

import java.awt.font.GlyphVector;

/**
 * Platform independent graphics context
 * 
 * <h3>Status</h3>
 * 
 * <p> This interface will be evolving into the AWT and Android
 * versions of <b>vector</b>.  For the moment, this package is
 * primarily AWT oriented. </p>
 */
public interface Context {
    /**
     * @return The underlying platform native output context, for
     * example AWT Graphics2D or Android Canvas.
     */
    public Object getNative();
    /**
     * @return Whether {@link #getGL() getGL} will return a platform
     * native GL interface.
     */
    public boolean hasGL();
    /**
     * @return Platform native GL interface when {@link #hasGL()
     * hasGL} returns true, otherwise throw {@link
     * java.lang.UnsupportedOperationException} or {@link
     * java.lang.RuntimeException}.
     */
    public Object getGL();
    /**
     * @return Graphics context nesting depth
     */
    public int depth();
    /**
     * @return Debug tracing enabled
     * @see DebugTrace
     */
    public boolean isTracing();
    /**
     * @param trace Enable debug tracing 
     * @see DebugTrace
     */
    public Context setTracing(boolean trace);

    public vector.Context create();

    public vector.Context create(int x, int y, int w, int h);

    public void dispose();

    public Color getColor();

    public void setColor(Color color);

    public Transform getTransform();

    public void setTransform(Transform at);

    public void transform(Transform at);

    public void translate(float x, float y);

    public void translate(double x, double y);

    public Font getFont();

    public void setFont(Font font);

    public Stroke getStroke();
    /**
     * Define stroke with its optional color
     */
    public void setStroke(Stroke stroke);

    public void setClip(int x, int y, int w, int h);

    public void setClip(Shape shape);

    public void clipRect(int x, int y, int w, int h);

    public Shape getClip();

    public Bounds getClipBounds();

    public Bounds getClipBounds(Bounds rectangle);

    public Bounds getClipRect();

    public void fill(Shape shape);

    public void draw(Shape shape);

    public void drawGlyphVector(GlyphVector vector, float x, float y);

    public void drawString(String string, int x, int y);

    public void drawString(String string, float x, float y);

    public boolean drawImage(Image image, Transform at);

    public boolean drawImage(Image image, int x, int y);

    public boolean drawImage(Image image, int x, int y, int w, int h);

}
