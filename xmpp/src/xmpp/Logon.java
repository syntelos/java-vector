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
import vector.Bounds;
import vector.Button;
import vector.Component;
import vector.Event;
import vector.Label;
import vector.Password;
import vector.TableSmall;
import vector.TextEdit;
import vector.dialog.Style;

import platform.Stroke;

/**
 * Hard coded XMPP information dialog.
 */
public class Logon
    extends vector.Dialog
{

    protected static Logon Instance;
    /**
     * Persistent channel
     */
    public static Logon Instance(){
        if (null == Instance){
            Instance = new Logon();
        }
        return Instance;
    }


    public final static Border Configure(Border border){

        border.setBackground(Style.BGD(0.5f));
        border.setColor(Style.FG());
        border.setColorOver(Style.NG());
        border.setStyle(Border.Style.ROUND);
        border.setArc(16.0);
        border.setStroke(new Stroke(2f));
        border.setStrokeOver(new Stroke(4f));

        return border;
    }
    public final static vector.Text Configure(vector.Text text){

        text.setFont(Style.FontLarge());
        text.setFixed(true);
        text.setCols(40);
        text.setColor(Style.FG());
        text.setColorOver(Style.NG());

        return text;
    }


    protected TextEdit host, port, logon, password;


    protected Logon(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.setEnumClass(Actor.class);
        this.setContent(true);
        this.setBoundsVector(new Bounds(100,100,100,100));

        Border border = new Border();
        this.add(border);
        Configure(border);

        final TableSmall input = new TableSmall();
        {
            this.add(input);
            input.setCellSpacing(4f);
            input.setBoundsVector(new Bounds(4,4,4,4));
            /*
             * Headline
             */
            final Label titleLabel = new Label();
            {
                input.add(titleLabel);
                Configure(titleLabel);
                titleLabel.setText("XMPP");
                titleLabel.setTableColSpan(2);
                titleLabel.setFont(Style.FontTitle());
            }
            /*
             * Service host
             */
            final Label hostLabel = new Label();
            {
                input.add(hostLabel);
                Configure(hostLabel);
                hostLabel.setText("Host");
            }
            this.host = new TextEdit();
            {
                input.add(this.host);
                Configure(this.host);
                this.host.setText(Preferences.GetHost());

                border = new Border();
                this.host.setBorder(border);
                Configure(border);
            }
            /*
             * Service port number
             */
            final Label portLabel = new Label();
            {
                input.add(portLabel);
                Configure(portLabel);
                portLabel.setText("Port");
            }
            this.port = new TextEdit();
            {
                input.add(this.port);
                Configure(this.port);
                this.port.setText(String.valueOf(Preferences.GetPort()));

                border = new Border();
                this.port.setBorder(border);
                Configure(border);
            }
            /*
             * Logon (email)
             */
            final Label logonLabel = new Label();
            {
                input.add(logonLabel);
                Configure(logonLabel);
                logonLabel.setText("Logon");
            }
            this.logon = new TextEdit();
            {
                input.add(this.logon);
                Configure(this.logon);
                if (null != this.logon)
                    this.logon.setText(Preferences.GetLogon());

                border = new Border();
                this.logon.setBorder(border);
                Configure(border);
            }
            /*
             * Password
             */
            final Label passwordLabel = new Label();
            {
                input.add(passwordLabel);
                Configure(passwordLabel);
                passwordLabel.setText("Password");
            }
            this.password = new Password();
            {
                input.add(this.password);
                Configure(this.password);
                this.password.setText(Preferences.GetPassword());

                border = new Border();
                this.password.setBorder(border);
                Configure(border);
            }
            /*
             * Inset buttons under text edit inputs
             */
            final Component buttonsInset = new vector.Container();
            {
                input.add(buttonsInset);
            }
            /*
             * Buttons layout
             */
            final TableSmall buttons = new TableSmall();
            {
                input.add(buttons);
                buttons.setCellSpacing(6f);
                /*
                 * Accept button
                 */
                final Button accept = new Button();
                {
                    buttons.add(accept);
                    Configure(accept);
                    accept.setText("Connect");
                    accept.setEnumClass(Actor.class);
                    accept.setEnumValue(Actor.Connect);

                    border = new Border();
                    accept.setBorder(border);
                    Configure(border);
                }
                /*
                 * Cancel button
                 */
                final Button cancel = new Button();
                {
                    buttons.add(cancel);
                    Configure(cancel);
                    cancel.setText("Disconnect");
                    cancel.setEnumClass(Actor.class);
                    cancel.setEnumValue(Actor.Disconnect);

                    border = new Border();
                    cancel.setBorder(border);
                    Configure(border);
                }
            }
        }
    }
    @Override
    public void destroy(){
        super.destroy();

        // this.host = null
        // this.port = null
        // this.logon = null
        // this.password = null
    }
    public void connect(){

        Preferences.SetHost(this.host.getText());
        Preferences.SetPort(this.port.getText());

        Preferences.SetLogon(this.logon.getText());
        Preferences.SetPassword(this.password.getText());

        XThread.Connect();
    }
    public void disconnect(){

        Preferences.SetHost(this.host.getText());
        Preferences.SetPort(this.port.getText());

        Preferences.SetLogon(this.logon.getText());
        Preferences.SetPassword(this.password.getText());

        XThread.Disconnect();
    }
    public boolean input(Event e){

        if (super.input(e)){

            switch(e.getType()){

            case Action:{
                /*
                 * In this case the Dialog has been orphaned after
                 * super.input(e)
                 */
                final Event.NamedAction<Actor> action = (Event.NamedAction<Actor>)e;

                switch(action.getValue()){

                case Connect:

                    this.connect();

                    return true;

                case Disconnect:

                    this.disconnect();

                    return true;

                default:
                    break;
                }
                break;
            }
            default:
                break;
            }
            return true;
        }
        else {
            return false;
        }
    }
}
