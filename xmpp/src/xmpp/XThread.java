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
import static org.jivesoftware.smack.packet.Message.Type.*;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.Session;
import org.jivesoftware.smack.packet.XMPPError;
import static org.jivesoftware.smack.packet.XMPPError.Type.*;

/**
 * Persistent single channel XMPP conversation
 */
public final class XThread
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
    /**
     *
     */
    public enum State {
        /**
         * 
         */
        Cancel,
        /**
         * 
         */
        Run,
        /**
         * 
         */
        Delay;
    }

    /**
     * Persistent channel
     */
    public final static XThread Instance = new XThread();

    public final static void Connect(){

        Instance.connect();
    }
    public final static void Disconnect(){

        Instance.disconnect();
    }
    public final static void Send(String m){

        Instance.send(m);
    }
    public final static void Status(XAddress.Full to){

        Instance.status(to);
    }
    public final static void Select(XAddress to){

        Instance.select(to);
    }
    public final static void Contact(XAddress to){

        Instance.contact(to);
    }


    /*
     * These fields only change value on (connect)
     */
    protected String id, host, password, resource;

    protected XAddress logon;

    protected int port;
    /*
     * This field is protected from entering state (NULL) after (NOT NULL)
     */
    private volatile XAddress to;

    private volatile XMPPConnection connection;

    private volatile State state = State.Run;

    private volatile Message.Type messageType = Message.Type.chat;

    private volatile XAddress contact;

    /**
     * 
     */
    private XThread(){
        super();
    }


    public boolean isStateCancel(){
        return (State.Cancel == this.state);
    }
    public boolean isStateRun(){
        return (State.Run == this.state);
    }
    public boolean isStateDelay(){
        return (State.Delay == this.state);
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

            Output.Instance.headline("XThread Status(%s)",to.identifier);

            this.to = to;
        }
        else if (null != this.contact && this.contact.equals(to)){

            Output.Instance.headline("XThread Contacted(%s)",to.identifier);

            this.contact = null;

            this.to = to;
        }
    }
    public void select(XAddress to){
        if (null != to){

            Output.Instance.headline("XThread Select(%s)",to.identifier);

            this.to = to;

            this.contact = null;

            Preferences.SetTo(to);
        }
    }
    public void contact(XAddress to){
        if (null != to){

            Output.Instance.headline("XThread Contact(%s)",to.identifier);

            this.contact = to;

            //Preferences.SetTo(to);
        }
    }
    public boolean send(String m){

        if (null != this.connection && null != this.to){

            if (null == this.contact){

                final String to = this.to.full;

                Message message = new Message(to, this.messageType);
                message.setFrom(this.connection.getUser());
                message.setThread(this.id);
                message.setBody(m);


                this.connection.sendPacket(message);

                Output.Instance.send(message);

                return true;
            }
            else
                Output.Instance.error("Send dropped for pending contact");
        }
        else if (null == this.connection)
            Output.Instance.error("Send dropped for missing connection");
        else
            Output.Instance.error("Send dropped for missing contact");

        return false;
    }
    public void connect(){

        this.disconnect();

        this.id = Preferences.GetSession();
        this.host = Preferences.GetHost();
        this.port = Preferences.GetPort();

        this.logon = Preferences.ComposeLogon();
        this.password = Preferences.GetPassword();
        this.to = Preferences.ComposeTo();
        this.resource = Preferences.ComposeResource();

        if (this.isReady()){
            Output.Instance.headline("XThread Connect");

            this.connection = this.createConnection();

            Status.Instance.up();
        }
    }
    public void disconnect(){
        if (null != this.connection){
            Output.Instance.headline("XThread Disconnect");
            Status.Instance.clear();
            try {
                this.connection.disconnect();
            }
            finally {
                this.connection = null;
                Status.Instance.down();
            }
        }
    }
    public boolean ready() throws XMPPException {

        switch(this.state){
        case Cancel:

            this.connect();
            this.state = State.Run;
            return true;

        case Run:
            return true;

        case Delay:
            this.state = State.Run;
            return true;

        default:
            throw new IllegalStateException(this.state.name());
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
                Output.Instance.headline("XThread process(message: %s)",xm.getType().name());
                break;
            }
        }
        else if (pk instanceof Presence){
            Presence xp = (Presence)pk;

            this.update(xp);
        }
        else if (pk instanceof Bind){

        }
        else
            Output.Instance.headline("XThread process(class: %s)",pk.getClass().getName());
    }
    protected void update(Presence p){

        Status.Instance.update(p);
    }
    protected void receive(Message m){

        Output.Instance.receive(m);
    }
    protected void headline(Message m){

        Output.Instance.headline(m);
    }
    protected void error(Message m){

        final XMPPError.Type et = m.getError().getType();
        switch(et){
        case WAIT:

            Output.Instance.error("XThread error(WAIT:Delay)");

            this.state = State.Delay;

            break;
        case CANCEL:

            Output.Instance.error("XThread error(CANCEL:Cancel)");

            this.state = State.Cancel;
            break;
        case MODIFY:

            Output.Instance.error("XThread error(MODIFY:%s)",this.state.name());

            break;
        case AUTH:

            Output.Instance.error("XThread error(AUTH:Cancel)");

            this.state = State.Cancel;
            break;
        case CONTINUE:

            Output.Instance.error("XThread error(CONTINUE:%s)",this.state.name());

            break;
        default:

            Output.Instance.error("XThread error(UNKNOWN(%s:%s))",et.name(),this.state.name());
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

            this.state = State.Run;

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
