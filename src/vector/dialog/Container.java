/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, The DigiVac Company
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

import vector.Component;
import vector.Event;

/**
 * Class defined menu
 */
public class Container
    extends vector.Container
{

    protected Class<? extends Component> menuClass;


    /**
     * Undefined menu 
     */
    public Container(){
        super();
    }
    /**
     * Define menu
     */
    public Container(Class<? extends Component> menuClass){
        super();
        this.menuClass = menuClass;
    }


    /**
     * 
     */
    public boolean input(Event e){

        if (super.input(e))

            return true;

        else {
            switch(e.getType()){

            case KeyUp:
                if (this.mouseIn){
                    final Event.Key k = (Event.Key)e;

                    switch (k.getCode()){

                    case ESCAPE:

                        this.showMenu();

                        return true;
                    }
                }
                return false;

            case MouseUp:

                if (this.mouseIn){

                    final Event.Mouse m = (Event.Mouse)e;

                    if (m.isPoint()){

                        this.showMenu();

                        return true;
                    }
                }
                return false;

            default:
                return false;
            }
        }
    }
    /**
     * 
     */
    public Container showMenu(){

        if (null != this.menuClass){

            this.show(this.menuClass);
        }
        return this;
    }
}
