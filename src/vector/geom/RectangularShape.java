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

/**
 * 
 */
public interface RectangularShape
    extends platform.Shape
{

    public boolean isEmpty();

    public double getX();

    public double getY();

    public double getWidth();

    public double getHeight();

    public double getMinX();

    public double getMinY();

    public double getMaxX();

    public double getMaxY();

    public double getCenterX();

    public double getCenterY();

    public void setFrame(double x, double y, double w, double h);

    public void setFrameVector(RectangularShape r);

    public boolean contains(int x, int y);

    public boolean contains(float x, float y);

    public boolean contains(RectangularShape r);

}
