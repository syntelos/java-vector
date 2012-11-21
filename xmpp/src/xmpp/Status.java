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


    protected static Status Instance;
    /**
     * Persistent channel
     */
    public static Status Instance(){
        if (null == Instance){
            Instance = new Status();
        }
        return Instance;
    }

    public static Status Select(XAddress to){
        if (null != to)
            return Instance().select(to);
        else
            return Instance();
    }
    public static Status Select(String to){
        if (null != to && 0 < to.length())
            return Instance().select(new XAddress.Identifier(to));
        else
            return Instance();
    }
    public static Status Up(){
        return Instance().up();
    }
    public static Status Down(){
        return Instance().down();
    }
    public static Status Clear(){
        return Instance().clear();
    }
    public static Status Receive(Message m){
        return Instance().receive(m);
    }
    public static Status Update(Presence p){
        return Instance().update(p);
    }

    
    /**
     * 
     */
    public static class Label
        extends vector.Button
    {

        protected XAddress address;

        protected Presence.Mode mode;


        public Label(XAddress address){
            super();
            if (null != address)
                this.address = address;
            else
                throw new IllegalArgumentException();
        }


        @Override
        public void init(){
            super.init();

            final Color bgd = Style.BGD(0.3f);
            final Color fg1 = Style.FG();

            this.setEnumClass(Actor.class);
            this.setEnumValue(Actor.Select);
            this.setFont(Style.FontSmall());
            this.setFixed(true);
            this.setCols(40);
            this.setColor(fg1);
            this.setColorOver(fg1);
            this.setText(this.address.logon+'/'+this.address.resourceKind);

            Border border = new Border();
            this.setBorder(border);

            final Color fg2 = Style.FG(0.5f);

            border.setBackground(bgd);
            border.setColor(fg2);
            border.setColorOver(fg2);
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

            final Color ok = Style.OK(0.5f);

            this.setColor(ok);
            this.setColorOver(ok);
            border.setColor(ok);
            border.setColorOver(ok);
        }
        public void update(Presence p){

            this.address = new XAddress.From(p);

            Border border = this.getBorder();

            this.mode = p.getMode();

            final Color color;

            switch(this.mode){
            case chat:
                color = Style.ST(0.5f);
                break;
            case available:
                color = Style.OK(0.5f);
                break;
            case away:
            case xa:
                color = Style.AY(0.5f);
                break;
            case dnd:
            default:
                color = Style.NG(0.5f);
                break;
            }
            this.setColor(color);
            this.setColorOver(color);
            border.setColor(color);
            border.setColorOver(color);
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


    protected Status(){
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

            final Color bgd = Style.BGD(0.4f);
            final Color ng = Style.NG(0.5f);

            border.setBackground(bgd);
            border.setColor(ng);
            border.setColorOver(ng);
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

            final Color ok = Style.OK(0.5f);

            border.setColor(ok);
            border.setColorOver(ok);
        }

        this.outputScene();
        return this;
    }
    public Status down(){

        Border border = this.getBorder();
        if (null != border){

            final Color ng = Style.NG(0.5f);

            border.setColor(ng);
            border.setColorOver(ng);
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

        if (null != addr){

            Label known = this.search(addr);
            if (null != known){
                try {
                    XThread.Select(known.address);
                }
                catch (RuntimeException exc){

                    //exc.printStackTrace()
                }
            }
            else if (null != addr.logon){
                try {
                    XThread.Contact(addr);
                }
                catch (RuntimeException exc){

                    //exc.printStackTrace()
                }
            }
            else
                throw new IllegalArgumentException(addr.identifier);
        }
        return this;
    }
}
