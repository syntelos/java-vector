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

import platform.Color;

/**
 * 
 */
public class Display
    extends platform.Display
{

    public Display(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.setBackground(Color.white);

        final Output output = Output.Instance;
        {
            this.add(output);
        }
        final Input input = new Input();
        {
            this.add(input);
        }
        final Status status = Status.Instance;
        {
            this.add(status);
        }
        final Logon logon = new Logon();
        {
            this.add(logon);
        }
    }
    @Override
    public void modified(){
        super.modified();

        this.outputScene();
    }
    @Override
    public void resized(){
        super.resized();

        this.outputScene();
    }
    public Output getOutput(){
        if (this.has(0))
            return (Output)this.get(0);
        else
            return null;
    }
    public Input getInput(){
        if (this.has(1))
            return (Input)this.get(1);
        else
            return null;
    }
    public Status getStatus(){
        if (this.has(2))
            return (Status)this.get(2);
        else
            return null;
    }
    public Logon getLogon(){
        if (this.has(3))
            return (Logon)this.get(3);
        else
            return null;
    }
    public Logon logon(){
        Logon logon = this.getLogon();
        if (null == logon){
            logon = new Logon();
            this.add(logon);

        }
        else {
            this.drop(logon);
        }
        this.modified();

        this.outputScene();

        return logon;
    }
}
