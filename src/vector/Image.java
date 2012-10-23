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

import platform.Context;
import platform.Transform;
import platform.geom.Point;

/**
 * A raster image source or buffer is not transformed from its basic
 * dimensions until it is output.
 */
public interface Image {

    public int getWidth();

    public int getHeight();

    /**
     * Discard internal image data
     */
    public void flush();
    /**
     * Draw image at context (0,0)
     */
    public Context blit(Context g);

    /**
     * The offscreen image application constructor requires the {@link
     * platform.Display} with integer width and height dimensions.
     */
    public interface Offscreen
        extends Image
    {
        public Context create();
    }
}
