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
package vector.geom;

import platform.geom.Point;

/**
 * 
 */
public interface Path
    extends platform.Shape
{
    public void moveTo(float x, float y);
    public void moveTo(double x, double y);
    public void moveTo(Point p);

    public void lineTo(float x, float y);
    public void lineTo(double x, double y);
    public void lineTo(Point p);

    public void quadTo(float x0, float y0, float x1, float y1);
    public void quadTo(double x0, double y0, double x1, double y1);
    public void quadTo(Point p0, Point p1);

    public void curveTo(float x0, float y0, float x1, float y1, float x2, float y2);
    public void curveTo(double x0, double y0, double x1, double y1, double x2, double y2);
    public void curveTo(Point p0, Point p1, Point p2);
    /**
     * Connect tail (last pen down point, lineTo) to head (previous
     * pen up point, moveTo)
     */
    public void close();
    /**
     * Clear existing path data - state
     */
    public void reset();
    public void set(platform.Path p);
    public void add(platform.Path p);
    public void set(platform.Shape p);
    public void add(platform.Shape p);
    public void transform(platform.Transform t);
}
