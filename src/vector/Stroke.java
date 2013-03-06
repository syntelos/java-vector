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
package vector;

import platform.Color;

import json.Json;
import json.ObjectJson;

import lxl.List;

/**
 * Stroke interface
 */
public interface Stroke
    extends json.Builder
{
    public boolean hasColor();

    public Color getColorVector();

    public float getLineWidth();

    public int getEndCap();

    public int getLineJoin();

    public float getMiterLimit();

    public float[] getDashArray();

    public float getDashPhase();

}
