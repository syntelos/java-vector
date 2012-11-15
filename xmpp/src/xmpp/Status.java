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
public class Status
    extends vector.TableSmall
{
    public final static platform.Font Font = platform.Font.decode("monospaced-12");

    public final static Color BG = Color.white.opacity(0.5f);
    public final static Color FG = Color.black.opacity(0.5f);
    public final static Color OK = Color.green.opacity(0.5f);
    public final static Color NG = Color.red.opacity(0.5f);


    public final static Status Instance = new Status();
    
    public final static Border Configure(Border border){

        border.setBackground(BG);
        border.setColor(FG);
        border.setColorOver(OK);
        border.setStyle(Border.Style.ROUND);
        border.setArc(10.0);
        border.setStroke(new Stroke(1f));
        border.setStrokeOver(new Stroke(2f));

        return border;
    }
    public final static vector.Text Configure(vector.Text text){

        text.setFont(Status.Font);
        text.setFixed(true);
        text.setCols(40);
        text.setColor(BG);
        text.setColorOver(OK);

        return text;
    }


    private Status(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.setCols(1);
        this.setCellSpacing(2f);

        Border border = new Border();
        this.add(border);
        border.setColor(OK);
        border.setColorOver(NG);
        border.setStyle(Border.Style.ROUND);
        border.setStroke(new Stroke(2f));
    }
    public void layout(){

        super.layout();

        Bounds b = this.getBoundsVector();
        Bounds p = this.getParentVector().getBoundsVector();

        b.x = (p.width*0.75f);
        b.y = 16f;
        this.setBoundsVector(b);

        Border border = this.getBorder();
        border.layout();
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
    public Label search(String text){

        for (Label label: this.listContent(Label.class)){

            if (text.equals(label.getText()))
                return label;
        }
        return null;
    }
    public Status clear(){

        while (this.has(1))
            this.remove(1);

        this.modified();

        this.outputScene();

        return this;
    }
    public Status update(Presence p){
        final String from = p.getFrom();

        Label label = this.search(from);

        if (null == label){
            label = new Label();
            {
                this.add(label);
                Status.Configure(label);
                label.setText(from);

                Border border = new Border();
                label.setBorder(border);
                Status.Configure(border);
            }
        }

        Border border = label.getBorder();

        switch(p.getMode()){
        case chat:
        case available:
            label.setColor(OK);
            label.setColorOver(OK);
            border.setColor(OK);
            border.setColorOver(OK);
            break;
        case away:
        case xa:
        case dnd:
            label.setColor(NG);
            label.setColorOver(NG);
            border.setColor(NG);
            border.setColorOver(NG);
            break;
        }

        this.modified();

        this.outputScene();

        return this;
    }
}
