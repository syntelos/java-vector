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
import org.jivesoftware.smack.packet.Message;
import static org.jivesoftware.smack.packet.Message.Type.*;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
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
     * 
     */
    public interface Consumer {

        public Consumer receive(Message xm);

        public Consumer headline(Message xm);

        public Consumer error(Message xm);
    }
    /**
     * Persistent channel
     */
    public final static XThread Instance = new XThread();

    public final static void Save(){

        Instance.save();
    }
    public final static void Send(String m){

        Instance.send(m);
    }


    protected String id, host, password, resource;

    protected String logon;

    protected int port;

    protected String to;

    private volatile XMPPConnection connection;

    private volatile State state = State.Run;

    private volatile Message.Type messageType = Message.Type.chat;


    /**
     * 
     */
    private XThread(){
        super();
    }


    public void save(){

        this.disconnect();

        this.id = Preferences.GetThread();
        this.host = Preferences.GetHost();
        this.port = Preferences.GetPort();

        this.logon = Preferences.GetLogon();
        this.password = Preferences.GetPassword();
        this.to = Preferences.GetTo();
        this.resource = Preferences.GetResource();

        this.connect();
    }
    public boolean send(String m){

        if (null != this.connection){

            Message message = new Message(this.to.toString(), this.messageType);
            message.setFrom(this.connection.getUser());
            message.setThread(this.id);
            message.setBody(m);


            this.connection.sendPacket(message);

            return true;
        }
        return false;
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
    public void connect(){

        this.disconnect();

        if (this.isReady()){

            this.connection = this.createConnection();
        }
    }
    public void disconnect(){
        if (null != this.connection){
            try {
                this.connection.disconnect();
            }
            finally {
                this.connection = null;
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

        return (pk instanceof Message);
    }
    public void processPacket(Packet pk){
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
            throw new IllegalStateException(xm.getType().name());
        }
    }
    protected void receive(Message m){

        System.err.printf("XThread receive(%s)%n",m.getBody());

        Output.Instance.receive(m);
    }
    protected void headline(Message m){

        System.err.printf("XThread headline(%s)%n",m.getBody());

        Output.Instance.headline(m);
    }
    protected void error(Message m){

        final XMPPError.Type et = m.getError().getType();
        switch(et){
        case WAIT:

            System.err.printf("XThread error(WAIT(DELAY))%n");

            this.state = State.Delay;

            break;
        case CANCEL:

            System.err.printf("XThread error(CANCEL(CANCEL))%n");

            this.state = State.Cancel;
            break;
        case MODIFY:

            System.err.printf("XThread error(MODIFY(%s))%n",this.state.name());

            break;
        case AUTH:

            System.err.printf("XThread error(AUTH(Cancel))%n");

            this.state = State.Cancel;
            break;
        case CONTINUE:

            System.err.printf("XThread error(CONTINUE(%s))%n",this.state.name());

            break;
        default:

            System.err.printf("XThread error(UNKNOWN('%s',%s))%n",et.name(),this.state.name());
            break;
        }

        Output.Instance.error(m);
    }
    public boolean isReady(){
        return (null != this.logon && null != this.password);
    }
    protected XMPPConnection createConnection(){
        try {
            System.err.printf("XThread Connect(host: '%s', port: '%d')%n",this.host,this.port);

            final ConnectionConfiguration config = new ConnectionConfiguration(this.host,this.port);

            config.setRosterLoadedAtLogin(false);

            config.setDebuggerEnabled(false);

            SASLAuthentication.supportSASLMechanism("PLAIN", 0);

            XMPPConnection connection = new XMPPConnection(config);

            connection.connect();

            System.err.printf("XThread Login(logon: '%s', resource: '%s')%n",this.logon, this.resource);

            connection.login(this.logon, this.password, this.resource);
            connection.sendPacket(new Presence(Presence.Type.available));

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
