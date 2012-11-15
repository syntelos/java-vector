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
import vector.Stroke;
import vector.TableSmall;
import vector.TextEdit;

import platform.Color;

/**
 * Hard coded XMPP information dialog.
 */
public class Logon
    extends vector.Dialog
{
    public final static platform.Font Font = platform.Font.decode("monospaced-24");

    public final static Border Configure(Border border){

        border.setBackground(Color.white);
        border.setColor(Color.black);
        border.setColorOver(Color.red);
        border.setStyle(Border.Style.ROUND);
        border.setArc(16.0);
        border.setStroke(new Stroke(2f));
        border.setStrokeOver(new Stroke(4f));

        return border;
    }
    public final static vector.Text Configure(vector.Text text){

        text.setFont(Logon.Font);
        text.setFixed(true);
        text.setCols(40);
        text.setColor(Color.black);
        text.setColorOver(Color.red);

        return text;
    }


    private TextEdit id, host, password, resource, logon, to, port;


    public Logon(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.setEnumClass(AcceptCancel.class);
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
             * Conversational thread identifier
             */
            final Label idLabel = new Label();
            {
                input.add(idLabel);
                Configure(idLabel);
                idLabel.setText("Thread");
            }
            this.id = new TextEdit();
            {
                input.add(this.id);
                Configure(this.id);
                this.id.setText(Preferences.GetThread());

                border = new Border();
                this.id.setBorder(border);
                Configure(border);
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
             * To (email)
             */
            final Label toLabel = new Label();
            {
                input.add(toLabel);
                Configure(toLabel);
                toLabel.setText("To");
            }
            this.to = new TextEdit();
            {
                input.add(this.to);
                Configure(this.to);
                if (null != this.to)
                    this.to.setText(Preferences.GetTo());

                border = new Border();
                this.to.setBorder(border);
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
            this.password = new TextEdit();
            {
                input.add(this.password);
                Configure(this.password);
                this.password.setText(Preferences.GetPassword());

                border = new Border();
                this.password.setBorder(border);
                Configure(border);
            }
            /*
             * Resource
             */
            final Label resourceLabel = new Label();
            {
                input.add(resourceLabel);
                Configure(resourceLabel);
                resourceLabel.setText("Resource");
            }
            this.resource = new TextEdit();
            {
                input.add(this.resource);
                Configure(this.resource);
                this.resource.setText(Preferences.GetResource());

                border = new Border();
                this.resource.setBorder(border);
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
                    accept.setText("Accept");
                    accept.setEnumClass(AcceptCancel.class);
                    accept.setEnumValue(AcceptCancel.Accept);

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
                    cancel.setText("Cancel");
                    cancel.setEnumClass(AcceptCancel.class);
                    cancel.setEnumValue(AcceptCancel.Cancel);

                    border = new Border();
                    cancel.setBorder(border);
                    Configure(border);
                }
            }
        }
    }
    public void save(){
        Preferences.SetThread(this.id.getText());
        Preferences.SetHost(this.host.getText());
        Preferences.SetPort(this.port.getText());

        Preferences.SetLogon(this.logon.getText());
        Preferences.SetPassword(this.password.getText());
        Preferences.SetResource(this.resource.getText());

        XThread.Save();
    }
    public boolean input(Event e){
        if (super.input(e)){

            switch(e.getType()){

            case Action:

                this.save();

                break;
            default:
                break;
            }
            return true;
        }
        else
            return false;
    }
}
