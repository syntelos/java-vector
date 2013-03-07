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

import vector.gl.GL;

import platform.Color;
import platform.Font;
import platform.Shape;
import platform.Stroke;
import platform.Transform;
import platform.geom.Point;

/**
 * Platform independent graphics context
 * 
 * <h3>Status</h3>
 * 
 * <p> This interface will be evolving into the AWT and Android
 * versions of <b>vector</b>.  For the moment, this package is
 * primarily AWT oriented. </p>
 * 
 * <h3>GL</h3>
 * 
 * <p> Vector integration with GL is foreseen as an equivalent API
 * with no advantage over this API.  That is, when GL is present the
 * Context implementation should employ it.
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
    public GL getGL();
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
    /**
     * @return Debug call stack tracing enabled
     * @see DebugTrace
     */
    public boolean isTracingDeep();
    /**
     * @param trace Enable debug call stack tracing 
     * @see DebugTrace
     */
    public Context setTracingDeep(boolean trace);

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

    public void setClip(Shape shape);

    public void clipTo(int x, int y, int w, int h);

    public Shape getClip();

    public void fill(Shape shape);

    public void draw(Shape shape);

    public void draw(Image image);
}
