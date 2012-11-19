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
import vector.Padding;
import vector.Stroke;
import vector.TableSmall;
import vector.TextEdit;

import platform.Color;

/**
 * Status of connection and contacts
 */
public class Status
    extends vector.TableSmall
{
    public final static platform.Font Font = platform.Font.decode("monospaced-16");

    public final static Color BG = Color.gray.opacity(0.5f);
    public final static Color FG = Color.black.opacity(0.5f);
    public final static Color OK = Color.green.opacity(0.5f);
    public final static Color AY = Color.yellow.opacity(0.5f);
    public final static Color ST = Color.blue.opacity(0.5f);
    public final static Color NG = Color.red.opacity(0.5f);

    /**
     * 
     */
    public final static Status Instance = new Status();

    public final static void Select(XAddress to){
        if (null != to)
            Instance.select(to);
    }
    public final static void Select(String to){
        if (null != to && 0 < to.length())
            Instance.select(new XAddress.Identifier(to));
    }
    
    /**
     * 
     */
    public static class Label
        extends vector.Button
    {

        private XAddress address;

        private Presence.Mode mode;


        public Label(XAddress address){
            super();
            if (null != address)
                this.address = address;
            else
                throw new IllegalArgumentException();
        }


        public void init(){
            super.init();

            this.setEnumClass(Actor.class);
            this.setEnumValue(Actor.Select);
            this.setFont(Status.Font);
            this.setFixed(true);
            this.setCols(40);
            this.setColor(FG);
            this.setColorOver(FG);
            this.setText(this.address.logon+'/'+this.address.resourceKind);

            Border border = new Border();
            this.setBorder(border);

            border.setColor(FG);
            border.setColorOver(FG);
            border.setStyle(Border.Style.ROUND);
            border.setArc(6.0);
            border.setStroke(new Stroke(1f));
            border.setStrokeOver(new Stroke(2f));
        }
        public boolean equals(XAddress addr){
            return this.address.equals(addr);
        }
        public boolean hasMode(){
            return (null != this.mode);
        }
        public Presence.Mode getMode(){
            return this.mode;
        }
        public void receive(XAddress from){

            this.address = from;

            this.setColor(OK);
            this.setColorOver(OK);
            border.setColor(OK);
            border.setColorOver(OK);
        }
        public void update(Presence p){

            this.address = new XAddress.From(p);

            Border border = this.getBorder();

            this.mode = p.getMode();

            switch(this.mode){
            case chat:
                this.setColor(ST);
                this.setColorOver(ST);
                border.setColor(ST);
                border.setColorOver(ST);
                break;
            case available:
                this.setColor(OK);
                this.setColorOver(OK);
                border.setColor(OK);
                border.setColorOver(OK);
                break;
            case away:
            case xa:
                this.setColor(AY);
                this.setColorOver(AY);
                border.setColor(AY);
                border.setColorOver(AY);
                break;
            case dnd:
            default:
                this.setColor(NG);
                this.setColorOver(NG);
                border.setColor(NG);
                border.setColorOver(NG);
                break;
            }
        }
        protected Label buttonInputAction(){
            return this;
        }
        public boolean input(Event e){
            if (super.input(e)){
                switch(e.getType()){
                case MouseUp:
                    if (this.mouseIn){
                        try {
                            Status.Select(this.address);

                            Event.NamedAction<Actor> dup = new platform.event.NamedAction(Actor.Select);

                            this.getRootContainer().input(dup);

                            return true;
                        }
                        catch (IllegalArgumentException exc){
                            exc.printStackTrace();
                        }
                    }
                default:
                    break;
                }
            }
            return false;
        }
    }


    private Status(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.setCols(1);
        this.setCellSpacing(2f);

        final Border border = new Border();
        {
            this.add(border);
            border.setBackground(BG);
            border.setColor(NG);
            border.setColorOver(NG);
            border.setStyle(Border.Style.ROUND);
            border.setStroke(new Stroke(2f));
            border.setStrokeOver(new Stroke(4f));
        }
    }
    public void layout(){

        super.layout();

        Bounds b = this.getBoundsVector();
        Bounds p = this.getParentVector().getBoundsVector();

        b.y = 16f;
        b.x = (p.width-b.width-20f);

        this.setBoundsVector(b);

        Border border = this.getBorder();
        border.layout();
    }
    public Label search(XAddress addr){

        for (Label label: this.listContent(Label.class)){

            if (label.equals(addr))
                return label;
        }
        return null;
    }
    public Status up(){

        Border border = this.getBorder();
        if (null != border){
            border.setColor(OK);
            border.setColorOver(OK);
        }

        this.outputScene();
        return this;
    }
    public Status down(){

        Border border = this.getBorder();
        if (null != border){
            border.setColor(NG);
            border.setColorOver(NG);
        }

        this.outputScene();
        return this;
    }
    public Status clear(){

        while (this.has(1))
            this.remove(1);

        this.modified();

        this.outputScene();

        return this;
    }
    public Status receive(Message m){

        final XAddress.Full from = new XAddress.From(m);
        Label label = this.search(from);

        switch(m.getType()){
        case normal:
        case groupchat:
        case chat:
            if (null == label){
                label = new Label(from);

                this.add(label);
            }

            label.receive(from);

            break;
        default:
            break;
        }

        this.modified();

        this.outputScene();

        return this;
    }
    public Status update(Presence p){

        final XAddress.Full from = new XAddress.From(p);

        Label label = this.search(from);

        switch(p.getType()){
        case available:
        case unavailable:
        case subscribe:
            if (null == label){
                label = new Label(from);

                this.add(label);
            }

            label.update(p);
            break;
        case unsubscribe:
        case unsubscribed:
            if (null != label){
                label = this.remove(label);
                if (null != label){
                    label.destroy();
                }
            }
            break;
        default:
            break;
        }

        this.modified();

        this.outputScene();

        return this;
    }
    public Status select(XAddress addr){

        Label known = this.search(addr);
        if (null != known){

            XThread.Select(known.address);
            return this;
        }
        else if (addr.isNotHostDefault()){

            XThread.Contact(addr);
            return this;
        }
        else
            throw new IllegalArgumentException(addr.identifier);
    }
}
