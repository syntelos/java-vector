/*
 * Java Vector
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

/**
 * Output fast overlay on a double buffered scene.  Requires flush on
 * {@link Component user} resize.
 * 
 * @see Display
 * @see Offscreen
 */
public final class Output
    extends Object
{
    /**
     * Overlay following Init exclusively
     */
    public enum Require {
        Init, Overlay, Scene;
    }


    private Offscreen offscreen;

    private Require require = Require.Init;


    public Output(){
        super();
    }


    public void completedScene(){

        this.require = Require.Init;
    }
    public void completedOverlay(){

        this.require = Require.Init;
    }
    public void requestScene(){

        this.require = Require.Scene;
    }
    public void requestOverlay(){

        if (Require.Init == this.require){

            this.require = Require.Overlay;
        }
    }
    public boolean requireOverlay(){

        return (Require.Overlay == this.require);
    }
    public Offscreen offscreen(java.awt.Component component){

        if (null == this.offscreen){

            this.offscreen = new Offscreen(component);
        }
        return this.offscreen;
    }
    public void flush(){

        Offscreen offscreen = this.offscreen;
        if (null != offscreen){
            this.offscreen = null;
            try {
                offscreen.flush();
            }
            catch (Exception ignore){
            }
        }
        this.require = Require.Init;
    }
}
