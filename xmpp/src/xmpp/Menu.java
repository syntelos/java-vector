/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, John Pritchard, Syntelos
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
package xmpp;

import vector.Event;

import vector.dialog.Viewport;

/**
 * 
 */
public class Menu
    extends vector.dialog.Menu
{
    public enum MM {
        Logon, Resize;
    }


    public Menu(){
        super(MM.class);
    }

    
    @Override
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {
            switch(e.getType()){
            case Action:
                final Event.NamedAction a = (Event.NamedAction)e;
                if (a.isValueClass(MM.class)){
                    final Event.NamedAction<MM> m = (Event.NamedAction<MM>)a;

                    final Terminal terminal = this.getParentVector();
                    switch(m.getValue()){

                    case Logon:
                        terminal.displayLogon();
                        this.drop(this);
                        return true;
                    case Resize:
                        terminal.displayViewport();
                        this.drop(this);
                        return true;

                    default:
                        return false;
                    }
                }
                break;
            default:
                break;
            }
            return false;
        }
    }

}
