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
package xmpp;

import vector.Border;
import vector.Event;

/**
 * 
 */
public class Input
    extends vector.TextEdit
{

    public Input(){
        super();
    }


    public void init(){
        super.init();

        Logon.Configure(this);
        Border border = new Border();
        {
            this.setBorder(border);
            Logon.Configure(border);
        }
    }
    public void send(){

        String m = this.getText();

        if (null != m){
            m = m.trim();
            if (0 < m.length()){

                this.setText(null);

                XThread.Send(m);

                this.modified();

                this.outputScene();
            }
        }
    }
    public void logon(){

        final Display parent = this.getParentVector();
        parent.logon();
    }
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {
            switch(e.getType()){

            case Action:{
                final Event.NamedAction<Actor> action = (Event.NamedAction<Actor>)e;
                switch(action.getValue()){
                case Connect:
                case Disconnect:
                    /*
                     * Logon 
                     */
                    this.outputScene();
                    return true;
                case Select:
                    /*
                     * Status [see Status/Label(input)]
                     */
                    return true;
                default:
                    break;
                }
                return false;
            }
            case KeyUp:
                if (this.mouseIn){
                    final Event.Key k = (Event.Key)e;

                    switch (k.getCode()){

                    case ENTER:

                        this.send();

                        return true;

                    case ESCAPE:

                        this.logon();

                        return true;
                    }
                }
                return false;
            default:
                return false;
            }
        }
    }

}
