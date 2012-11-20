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
package xs;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.Subscription;

/**
 * An XMPP address is a messaging network reference
 * <pre>
 * logon = identifier@host
 * full = logon/resource
 * </pre>
 */
public class XAddress
    extends xma.XAddress
{

    /**
     * Require input equal to or greater than identifier
     */
    public static class Identifier
        extends XAddress
    {
        public Identifier(String input){
            super(null,input,Require.Identifier);
        }
        public Identifier(JID jid){
            super(jid,jid.getId(),Require.Identifier);
        }
    }
    /**
     * Require input equal to or greater than logon
     */
    public static class Logon
        extends XAddress
    {
        public Logon(String input){
            super(null,input,Require.Logon);
        }
        public Logon(JID jid){
            super(jid,jid.getId(),Require.Logon);
        }
    }
    /**
     * Require full XMPP address input string
     */
    public static class Full
        extends XAddress
    {
        public Full(String input){
            super(null,input,Require.Full);
        }
        public Full(JID jid){
            super(jid,jid.getId(),Require.Full);
        }
    }
    /**
     * 
     */
    public static class From
        extends XAddress.Logon
    {

        public From(Message m){
            this(m.getFromJid());
        }
        public From(Subscription s){
            this(s.getFromJid());
        }
        public From(Presence p){
            this(p.getFromJid());
        }
        public From(JID jid){
            super(jid);
        }
    }
    /**
     * 
     */
    public static class To
        extends XAddress.Logon
    {

        public To(Message m){
            super(m.getRecipientJids()[0]);
        }
    }



    private JID jid;


    /**
     * Accept substrings
     * 
     * @param string A part or whole XMPP address
     */
    public XAddress(JID jid){
        this(jid,jid.getId(),Require.Identifier);
    }
    /**
     * Accept substrings
     * 
     * @param string A part or whole XMPP address
     */
    public XAddress(String string){
        this(null,string,Require.Identifier);
    }
    /**
     * Disallow substrings 
     * 
     * @param string A part or whole XMPP address
     * @param require Input requirement
     */
    public XAddress(JID jid, String string, XAddress.Require require){
        super(string,require);

        this.jid = jid;
    }



    public JID toJID(){
        if (null == this.jid){
            if (null != this.full)
                this.jid = new JID(this.full);
            else if (null != this.logon)
                this.jid = new JID(this.logon);
            else
                return null;
        }
        return this.jid;
    }
    public JID[] toRecipients(){
        JID jid = this.toJID();
        if (null != jid)
            return new JID[]{jid};
        else
            throw new IllegalStateException(this.toString());
    }
}
