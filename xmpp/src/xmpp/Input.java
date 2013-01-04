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
 * Input text line
 */
public class Input
    extends vector.TextEdit
{

    protected static Input Instance;
    /**
     * Persistent channel
     */
    public static Input Instance(){
        if (null == Instance){
            Instance = new Input();
        }
        return Instance;
    }


    protected Input(){
        super();
    }


    public void init(){
        super.init();

        Logon.Configure(this);

        this.setCols(60);

        Border border = new Border();
        {
            this.setBorder(border);
            Logon.Configure(border);
        }
    }
    public boolean input(Event e){
        if (super.input(e)){

            return true;
        }
        else {
            switch(e.getType()){

            case KeyUp:
                if (this.mouseIn){
                    final Event.Key k = (Event.Key)e;

                    switch (k.getCode()){

                    case ENTER:

                        this.send();

                        return true;

                    case ESCAPE:

                        this.menu();

                        return true;
                    }
                }
                return false;

            case MouseUp:{

                /*
                 * Right click -> menu
                 */
                final Event.Mouse m = (Event.Mouse)e;

                if (m.isPoint() && Event.Mouse.Action.Point1 != m.getAction()){

                    this.menu();

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
    public void send(){

        String m = this.getText();

        if (null != m){

            final Terminal terminal = this.getParentVector();
            if (null != terminal){

                terminal.send(m);
            }

            this.setText(null);

            this.modified();

            this.outputScene();
        }
    }
    public void menu(){

        final Terminal parent = this.getParentVector();
        if (null != parent){

            parent.menu();
        }
    }
}
