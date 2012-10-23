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
public interface Dimension 
    extends Cloneable
{

    public boolean isEmpty();
    public boolean isNotEmpty();
    public void floor(platform.geom.Dimension d);
    public void ceil(platform.geom.Dimension d);
    public double getWidth();
    public double getHeight();
    public void setSize(double w, double h);
    public java.lang.String toString();
}