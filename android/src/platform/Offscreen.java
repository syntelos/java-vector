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
package platform;

/**
 * {@link Output} buffer
 * 
 * @see Display
 */
public final class Offscreen
    extends Object
    implements vector.Image.Offscreen
{


    /**
     * @param component First argument to platform context (Display)
     */
    public Offscreen(Object component){
        this(component,null);
    }
    private Offscreen(Object observer, Object bounds){
        this(observer,0,0);
    }
    /**
     * @param observer First argument to platform context (Display)
     * @param width Pixel buffer X dimension
     * @param height Pixel buffer Y dimension
     */
    public Offscreen(Object observer, int width, int height){
        super();
    }


    public int getWidth(){
        return 0;
    }
    public int getHeight(){
        return 0;
    }
    @Override
    public void flush(){
    }
    public Context blit(Context g){

        g.draw(this);

        return g;
    }
    public Context create(){


        return null;
    }
}
