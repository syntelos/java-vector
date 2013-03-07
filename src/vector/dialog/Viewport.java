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
package vector.dialog;

import vector.Border;
import vector.Button;
import vector.Event;
import vector.Stroke;

/**
 * Dialog list of viewport buttons that produce {@link
 * vector.Viewport$Size Viewport Size} events.
 * 
 * <p> These events are interpreted by the platform Display. </p>
 * 
 * @see Style
 */
public class Viewport
    extends Menu<vector.Viewport.Size>
{


    public Viewport(){
        super(vector.Viewport.Size.class);
    }


    /**
     * 
     */
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {

            switch(e.getType()){
            case Action:{
                Event.NamedAction action = (Event.NamedAction)e;

                if (this.mouseIn && action.isValueClass(this.enumClass)){

                    this.drop(this);

                    /*
                     * Can't do 
                     *     this.drop(this)
                     * and then
                     *     this.outputScene()
                     * because this has just been orphaned.
                     * 
                     * Display will perform an outputScene for each
                     * Action event.
                     */
                    return true;
                }
                else
                    return false;
            }
            default:
                return false;
            }
        }
    }


    public static void main(String[] argv){

        final platform.Frame frame = new platform.Frame();

        frame.init();

        final vector.Display display = frame.getDisplay();

        display.setBackground(Style.BG());

        final Viewport dialog = new Viewport();

        display.show(dialog);

        try {
            json.Json scene = display.toJson();

            System.out.println(scene);
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
