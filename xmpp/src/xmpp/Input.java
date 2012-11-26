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

import vector.dialog.Viewport;

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
    public void send(){

        String m = this.getText();

        if (null != m){

            At.Command input = new At.Command(m);

            switch (input.at){

            case Clear:
                {
                    this.setText(null);

                    Output.Clear();

                    this.modified();

                    this.outputScene();

                    return;
                }

            case Identifier:
            case Logon:
                try {
                    Status.Select(input.address);
                }
                catch (IllegalArgumentException nlogon){

                    this.setText(null);

                    XThread.Send(input.source);

                    this.modified();

                    this.outputScene();

                    return;
                }

            case Tail:

                this.setText(null);

                XThread.Send(input.tail);

                this.modified();

                this.outputScene();

            default:
                break;
            }
        }
    }
    public void logon(){

        final Display parent = this.getParentVector();
        parent.logon();
    }
    public boolean input(Event e){
        if (super.input(e)){

            return true;
        }
        else {
            switch(e.getType()){

            case Action:{
                Event.NamedAction action = (Event.NamedAction)e;

                if (action.isValueClass(Actor.class)){
                    final Event.NamedAction<Actor> actor = (Event.NamedAction<Actor>)e;
                    switch(actor.getValue()){
                    case Connect:
                    case Disconnect:
                        /*
                         * Logon 
                         */
                        this.outputScene();
                        return true;

                    default:
                        break;
                    }
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

            case MouseUp:{

                /*
                 * Right click -> menu
                 */
                final Event.Mouse m = (Event.Mouse)e;

                if (m.isPoint() && Event.Mouse.Action.Point1 != m.getAction()){

                    Display display = this.getRootContainer();

                    Viewport viewport = new Viewport();

                    display.show(viewport);

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

}
