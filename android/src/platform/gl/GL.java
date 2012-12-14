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
package platform.gl;

import platform.Context;

/**
 * Android GL context
 * 
 * @see platform.Context
 */
public class GL
    extends Context
    implements vector.gl.GL,
               javax.microedition.khronos.opengles.GL
{

    public final javax.microedition.khronos.opengles.GL gl;


    public GL(Context context, javax.microedition.khronos.opengles.GL gl){
        super(context);
        if (null != gl)
            this.gl = gl;
        else
            throw new IllegalArgumentException();
    }


    public boolean hasGL(){
        return true;
    }
    public platform.gl.GL getGL(){
        return this;
    }
}
