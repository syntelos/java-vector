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


    private Offscreen scene, overlay;

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
    public Offscreen scene(java.awt.Component component){

        if (null == this.scene){

            this.scene = new Offscreen(component);
        }
        return this.scene;
    }
    public Offscreen overlay(java.awt.Component component){

        if (null == this.overlay){

            this.overlay = new Offscreen(component);
        }
        return this.overlay;
    }
    public void flush(){

        Offscreen scene = this.scene;
        if (null != scene){
            this.scene = null;
            try {
                scene.flush();
            }
            catch (Exception ignore){
            }
        }
        Offscreen overlay = this.overlay;
        if (null != overlay){
            this.overlay = null;
            try {
                overlay.flush();
            }
            catch (Exception ignore){
            }
        }
        this.require = Require.Init;
    }
}
