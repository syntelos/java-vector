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
    extends vector.Output
{

    private Offscreen scene, overlay;


    public Output(){
        super();
    }


    public Offscreen scene(Object component){

        if (null == this.scene){

            this.scene = new Offscreen(component);
        }
        return this.scene;
    }
    public Offscreen overlay(Object component){

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
        super.flush();
    }
}
