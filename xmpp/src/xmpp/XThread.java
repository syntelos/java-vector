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

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.Session;
import org.jivesoftware.smack.packet.XMPPError;

/**
 * Persistent single channel XMPP conversation
 */
public class XThread
    extends Object
    implements CharSequence, Comparable<XThread>,
               org.jivesoftware.smack.filter.PacketFilter,
               org.jivesoftware.smack.PacketListener
{
    public final static boolean Debug;
    static {
        final String conf = System.getProperty("xmpp.XThread.Debug");
        Debug = (null != conf && "true".equalsIgnoreCase(conf));
    }


    protected static XThread Instance;
    /**
     * Persistent channel
     */
    public static XThread Instance(){
        if (null == Instance){
            Instance = new XThread();
        }
        return Instance;
    }

    public final static void Connect(){

        Instance().connect();
    }
    public final static void Disconnect(){

        Instance().disconnect();
    }
    public final static void Send(String m){

        Instance().send(m);
    }
    public final static void Status(XAddress.Full to){

        Instance().status(to);
    }
    public final static void Select(XAddress to){

        Instance().select(to);
    }
    public final static void Contact(XAddress to){

        Instance().contact(to);
    }


    /*
     * These fields only change value on (connect)
     */
    protected String id, host, password, resource;

    protected XAddress logon;

    protected int port;

    protected volatile XAddress to;

    protected volatile XMPPConnection connection;

    protected volatile Message.Type messageType = Message.Type.chat;


    /**
     * 
     */
    protected XThread(){
        super();
    }


    public boolean isConnected(){
        return (null != this.connection && this.connection.isConnected());
    }
    public boolean isAuthenticated(){
        return (null != this.connection && this.connection.isAuthenticated());
    }
    public boolean isEncrypted(){
        return (null != this.connection && this.connection.isSecureConnection());
    }
    public void status(XAddress.Full to){

        if (null != this.to && this.to.equals(to)){

            Output.Headline("Status(%s)",to.identifier);

            this.to = to;
        }
    }
    public void select(XAddress to){
        if (null != to){

            Output.Headline("Select(%s)",to.identifier);

            this.to = to;

            Preferences.SetTo(to);
        }
    }
    public void contact(XAddress to){
        if (null != this.logon && null != to){

            if (null != this.to){
                if (to.equals(this.to))
                    return;
                else
                    this.to = null;
            }

            Presence q = new Presence(Presence.Type.subscribe);

            q.setMode(Presence.Mode.available);

            q.setFrom(this.connection.getUser());

            q.setTo(to.logon);

            this.connection.sendPacket(q);

            Output.Send(q);
        }
    }
    public boolean send(String m){

        if (null != m){
            m = m.trim();
            if (0 < m.length()){

                if (null != this.connection && null != this.to){

                    final String to = this.to.full;

                    Message message = new Message(to, this.messageType);
                    message.setFrom(this.connection.getUser());
                    message.setBody(m);


                    this.connection.sendPacket(message);

                    Output.Send(message);

                    return true;

                }
                else if (null == this.connection)
                    Output.Error("Send dropped for missing connection");
                else
                    Output.Error("Send dropped for missing contact");
            }
        }
        return false;
    }
    public void connect(){

        this.disconnect();

        this.id = Preferences.GetSession();
        this.host = Preferences.GetHost();
        this.port = Preferences.GetPort();

        this.logon = Preferences.ComposeLogon();
        this.password = Preferences.GetPassword();
        try {
            this.select(Preferences.ComposeTo());
        }
        catch (RuntimeException optimistic){
        }
        this.resource = Preferences.ComposeResource();

        if (this.isReady()){
            Output.Headline("Connect");

            this.connection = this.createConnection();

            Status.Up();
        }
    }
    public void disconnect(){
        if (null != this.connection){
            Output.Error("Disconnect");
            Status.Clear();
            try {
                this.connection.disconnect();
            }
            finally {
                this.connection = null;

                Status.Down();
            }
        }
    }
    public boolean accept(Packet pk){

        return true;
    }
    public void processPacket(Packet pk){

        if (pk instanceof Message){
            Message xm = (Message)pk;

            switch(xm.getType()){
            case normal:
            case chat:
            case groupchat:
                this.receive(xm);
                break;
            case headline:
                this.headline(xm);
                break;
            case error:
                this.error(xm);
                break;
            default:
                Output.Error("process(type: %s)",xm.getType().name());
                break;
            }
        }
        else if (pk instanceof Presence){
            Presence xp = (Presence)pk;

            this.update(xp);
        }
        else if (pk instanceof Bind){
            Bind xb = (Bind)pk;

            final String resource = xb.getResource();

            final String jid = xb.getJid();

            if (null != jid){

                if (null != resource){
                    Output.Error("bind(resource: %s, jid: %s)",resource,jid);

                }
                else {
                    Output.Error("bind(jid: %s)",jid);

                }
            }
            else if (null != resource){
                Output.Error("bind(resource: %s)",resource);

            }
        }
        else if (pk instanceof Registration){
            Registration xr = (Registration)pk;

            final StringBuilder registration = new StringBuilder();
            final java.util.Map<String,String> attributes = xr.getAttributes();
            if (null != attributes){
                boolean once = true;
                for (String key : attributes.keySet()){
                    String value = attributes.get(key);
                    if (once)
                        once = false;
                    else
                        registration.append(", ");

                    registration.append(key);
                    registration.append(": ");
                    registration.append(value);
                }
            }

            Output.Error("registration(%s)",registration);
        }
        else if (pk instanceof RosterPacket){
            RosterPacket xr = (RosterPacket)pk;

            final StringBuilder roster = new StringBuilder();
            final java.util.Collection<RosterPacket.Item> items = xr.getRosterItems();
            if (null != items){
                boolean once = true;
                for (RosterPacket.Item item : items){

                    String user = item.getUser();
                    String name = item.getName();
                    RosterPacket.ItemType type = item.getItemType();
                    RosterPacket.ItemStatus status = item.getItemStatus();
                    java.util.Set<String> groups = item.getGroupNames();

                    if (once)
                        once = false;
                    else
                        roster.append("; ");

                    roster.append("name: ");
                    roster.append(name);
                    roster.append(", user: ");
                    roster.append(user);
                    roster.append(", type: ");
                    roster.append(type);
                    roster.append(", status: ");
                    roster.append(status);
                    if (null != groups && (!groups.isEmpty())){
                        roster.append(", groups { ");
                        boolean grouping = true;
                        for (String group : groups){
                            if (grouping)
                                grouping = false;
                            else
                                roster.append(", ");

                            roster.append(group);
                        }
                        roster.append("}");
                    }
                }
            }

            Output.Error("roster(%s)",roster);
        }
        else if (pk instanceof Session){

            Output.Error("session()");
        }
        else
            Output.Error("process(class: %s)",pk.getClass().getName());
    }
    protected void update(Presence p){

        switch(p.getType()){

        case available:
        case unavailable:

            Status.Update(p);
            break;

        case subscribe:

            Status.Update(p);
            Output.Receive(p);

            if (null == this.to){
                this.to = new XAddress.From(p);
            }
            break;

        case unsubscribe:
        case unsubscribed:

            Status.Update(p);
            break;

        case error:

            Output.Error("error(%s)",(new XAddress.From(p)));
            break;

        default:
            break;
        }

    }
    protected void receive(Message m){

        if (null != this.connection){
            final String sender = this.connection.getUser();
            final String from = m.getFrom();
            if (!from.equals(sender)){
                Status.Receive(m);
            }
        }
        Output.Receive(m);
    }
    protected void headline(Message m){

        Output.Headline(m);
    }
    protected void error(Message m){

        final XMPPError.Type et = m.getError().getType();
        switch(et){
        case WAIT:

            Output.Error("error(WAIT, from: %s)",(new XAddress.From(m)));

            break;
        case CANCEL:

            Output.Error("error(CANCEL, from: %s)",(new XAddress.From(m)));

            break;
        case MODIFY:

            Output.Error("error(MODIFY, from: %s)",(new XAddress.From(m)));

            break;
        case AUTH:

            Output.Error("error(AUTH, from: %s)",(new XAddress.From(m)));

            break;
        case CONTINUE:

            Output.Error("error(CONTINUE, from: %s)",(new XAddress.From(m)));

            break;
        default:

            Output.Error("error(name: %s, from: %s)",et.name(),(new XAddress.From(m)));
            break;
        }
    }
    public boolean isReady(){
        return (null != this.logon && null != this.password);
    }
    protected XMPPConnection createConnection(){
        try {

            final ConnectionConfiguration config = new ConnectionConfiguration(this.host,this.port);

            config.setRosterLoadedAtLogin(false);

            config.setDebuggerEnabled(XThread.Debug);

            SASLAuthentication.supportSASLMechanism("PLAIN", 0);

            XMPPConnection connection = new XMPPConnection(config);

            connection.connect();

            final String logon = this.logon.logon;

            connection.login(logon, this.password, this.resource);

            connection.addPacketListener(this,this);

            return connection;
        }
        catch (XMPPException exc){

            return null;
        }
    }
    public int length(){
        return this.id.length();
    }
    public char charAt(int idx){
        return this.id.charAt(idx);
    }
    public CharSequence subSequence(int start, int end){
        return this.id.subSequence(start,end);
    }
    public int compareTo(XThread that){
        if (this == that)
            return 0;
        else 
            return this.id.compareTo(that.id);
    }
    public int hashCode(){
        return this.id.hashCode();
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.id.equals(that.toString());
    }
    public String toString(){
        return this.id;
    }
}
