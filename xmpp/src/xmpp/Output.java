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
import org.jivesoftware.smack.packet.Presence;

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
{

    protected static Output Instance;
    /**
     * Persistent channel
     */
    public static Output Instance(){
        if (null == Instance){
            Instance = new Output();
        }
        return Instance;
    }
    public static Output Send(Message m){

        return Instance().send(m);
    }
    public static Output Send(Presence p){

        return Instance().send(p);
    }
    public static Output Receive(Message m){

        return Instance().receive(m);
    }
    public static Output Receive(Presence p){

        return Instance().receive(p);
    }
    public static Output Headline(Message m){

        return Instance().headline(m);
    }
    public static Output Headline(String fmt, Object... args){

        return Instance().headline(fmt,args);
    }
    public static Output Headline(String m){

        return Instance().headline(m);
    }
    public static Output Error(Message m){

        return Instance().error(m);
    }
    public static Output Error(String fmt, Object... args){

        return Instance().error(fmt,args);
    }
    public static Output Error(String m){

        return Instance().error(m);
    }
    

    protected Output(){
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

        this.layout();
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
    public Output send(Message m){

        final String from = (new XAddress.From(m)).identifier;

        final String body = m.getBody();

        final Label label = new Label();
        {
            final String string = String.format("%s: %s",from,body);

            this.add(label);

            final Color fg = Style.FG();

            Logon.Configure(label);
            label.setText(string);
            label.setColor(fg);

            Border border = new Border();
            label.setBorder(border);
            Logon.Configure(border);
            border.setColor(fg);
        }

        this.modified();

        this.outputScene();

        return this;
    }
    public Output send(Presence p){

        final String from = (new XAddress.From(p)).identifier;

        final String to = (new XAddress.To(p)).logon;

        final Label label = new Label();
        {
            final String string = String.format("%s> %s",from,to);

            this.add(label);

            final Color fg = Style.FG();

            Logon.Configure(label);
            label.setText(string);
            label.setColor(fg);

            Border border = new Border();
            label.setBorder(border);
            Logon.Configure(border);
            border.setColor(fg);
        }

        this.modified();

        this.outputScene();

        return this;
    }
    public Output receive(Message m){

        final String from = (new XAddress.From(m)).identifier;

        final String body = m.getBody();

        final Label label = new Label();
        {
            final String string = String.format("%s: %s",from,body);

            this.add(label);

            final Color st = Style.ST();

            Logon.Configure(label);
            label.setText(string);
            label.setColor(st);

            Border border = new Border();
            label.setBorder(border);
            Logon.Configure(border);
            border.setColor(st);
        }

        this.modified();

        this.outputScene();

        return this;
    }
    public Output receive(Presence p){

        final String from = (new XAddress.From(p)).logon;

        final String to = (new XAddress.To(p)).identifier;


        final Label label = new Label();
        {
            final String string = String.format("%s> %s",from,to);

            this.add(label);

            final Color bgd = Style.BGD();

            Logon.Configure(label);
            label.setText(string);
            label.setColor(bgd);

            Border border = new Border();
            label.setBorder(border);
            Logon.Configure(border);
            border.setColor(bgd);
        }

        this.modified();

        this.outputScene();

        return this;
    }
    public Output headline(Message m){

        final String from = (new XAddress.From(m)).identifier;

        final String body = m.getBody();

        return this.headline("%s: %s",from,body);
    }
    public Output headline(String fmt, Object... args){

        return this.headline(String.format(fmt,args));
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
            border.setColor(Color.green);
        }

        this.modified();

        this.outputScene();

        return this;
    }
    public Output error(Message m){

        final String from = (new XAddress.From(m)).identifier;

        final String body = m.getBody();

        return this.error("%s: %s",from,body);
    }
    public Output error(String fmt, Object... args){

        return this.error(String.format(fmt,args));
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
            border.setColor(Color.red);
        }

        this.modified();

        this.outputScene();

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

            float xx = 4f;
            float yy = thisBounds.height;


            final Component.Iterator<Component> cit = this.listContent(Component.class).reverse();

            for (Component c: cit){

                Bounds cb = c.getBoundsVector();
                yy -= (cb.height+4f);

                if (0f < yy){
                    cb.x = xx;
                    cb.y = yy;
                    cb.width = thisBounds.width;

                    c.setBoundsVector(cb);
                }
                else
                    this.remove(c);
            }
        }
        {
            inputBounds.x = 4f;
            inputBounds.y = (thisBounds.y+thisBounds.height+4f);
            inputBounds.width = parentBounds.width;

            input.setBoundsVector(inputBounds);
        }
    }
}
