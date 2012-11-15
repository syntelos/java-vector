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

import org.jivesoftware.smack.packet.Message;

import vector.Border;
import vector.Bounds;
import vector.Button;
import vector.Component;
import vector.Event;
import vector.Label;
import vector.Padding;
import vector.Stroke;
import vector.TableSmall;
import vector.TextEdit;

import platform.Color;

/**
 * 
 */
public class Output
    extends vector.Container
    implements XThread.Consumer
{
    public final static Output Instance = new Output();
    

    private Output(){
        super();
    }


    @Override
    public void modified(){
        super.modified();

        this.layout();
    }
    @Override
    public void relocated(){
        super.relocated();

        //this.layout()!
    }
    @Override
    public void resized(){
        super.resized();

        this.layout();
    }
    public void logon(){

        final Display parent = this.getParentVector();
        parent.logon();
    }
    @Override
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
    public Output receive(Message m){

        final Label label = new Label();
        {
            this.add(label);
            Logon.Configure(label);
            label.setText(m.getBody());
            label.setColor(Color.blue);

            Border border = new Border();
            label.setBorder(border);
            Logon.Configure(border);
        }

        return this.scrollToLast();
    }
    public Output headline(Message m){

        return this.headline(m.getBody());
    }
    public Output headline(String m){

        final Label label = new Label();
        {
            this.add(label);
            Logon.Configure(label);
            label.setText(m);
            label.setColor(Color.green);

            Border border = new Border();
            label.setBorder(border);
            Logon.Configure(border);
        }

        return this.scrollToLast();
    }
    public Output error(Message m){

        return this.error(m.getBody());
    }
    public Output error(String m){

        final Label label = new Label();
        {
            this.add(label);
            Logon.Configure(label);
            label.setText(m);
            label.setColor(Color.red);

            Border border = new Border();
            label.setBorder(border);
            Logon.Configure(border);
        }

        return this.scrollToLast();
    }
    protected Output scrollToLast(){

        return this;
    }
    public void layout(){
        final Display parent = this.getParentVector();
        final Bounds parentBounds = parent.getBoundsVector();
        final Input input = parent.getInput();
        final Bounds inputBounds = input.getBoundsVector();
        final Bounds thisBounds = this.getBoundsVector();
        {
            thisBounds.x = 0f;
            thisBounds.y = 0f;
            thisBounds.height = (parentBounds.height-inputBounds.height-16f);
            thisBounds.width = parentBounds.width;

            this.setBoundsVector(thisBounds);

            float xx = 0f;
            float yy = thisBounds.height;


            final Component.Iterator<Component> cit = this.listContent(Component.class).reverse();

            for (Component c: cit){

                Bounds cb = c.getBoundsVector();
                yy -= cb.height;

                cb.x = xx;
                cb.y = yy;
                cb.width = thisBounds.width;

                c.setBoundsVector(cb);
            }
        }
        {
            inputBounds.x = 0f;
            inputBounds.y = (thisBounds.x+thisBounds.height+16f);
            inputBounds.width = parentBounds.width;

            input.setBoundsVector(inputBounds);
        }
        this.relocated();
    }
}
