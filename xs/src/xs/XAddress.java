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
    extends java.lang.Object
    implements java.io.Serializable,
               java.lang.Comparable<XAddress>
{
    public final static long serialVersionUID = 1L;


    /**
     * Require input equal to or greater than
     */
    public enum Require {
        Identifier, Logon, Full;
    }
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


    public final Require type;

    public final String identifier, host, resource, logon, full;

    public final String resourceKind, resourceSession;

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
        super();

        this.jid = jid;

        final String[] parts = XAddress.Scan(string);
        if (null == parts || null == require)
            throw new IllegalArgumentException(string);
        else {
            this.type = require;
            /*
             */
            switch(parts.length){
            case 1:
                switch (require){
                case Logon:
                case Full:
                    throw new IllegalArgumentException(string);
                default:
                    /*
                     * identifier
                     */
                    this.identifier = parts[0];
                    this.host = null;
                    this.resource = null;
                    this.resourceKind = null;
                    this.resourceSession = null;
                    break;
                }
                break;
            case 2:
                switch (require){
                case Full:
                    throw new IllegalArgumentException(string);
                default:
                    /*
                     * identifier, host
                     */
                    this.identifier = parts[0];
                    this.host = parts[1];
                    this.resource = null;
                    this.resourceKind = null;
                    this.resourceSession = null;
                    break;
                }
                break;
            case 3:
                /*
                 * identifier, host, resource
                 */
                this.identifier = parts[0];
                this.host = parts[1];
                this.resource = parts[2];
                String[] components = Resource(this.resource);
                if (null != components){
                    this.resourceKind = components[0];
                    this.resourceSession = components[1];
                }
                else {
                    this.resourceKind = this.resource;
                    this.resourceSession = this.resource;
                }
                break;
            default:
                throw new IllegalStateException();
            }
            /*
             */
            StringBuilder strbuf = new StringBuilder();
            strbuf.append(this.identifier);

            if (null != this.host){
                strbuf.append('@');
                strbuf.append(this.host);

                this.logon = strbuf.toString();

                if (null != this.resource){

                    strbuf.append('/');
                    strbuf.append(this.resource);

                    this.full = strbuf.toString();
                }
                else
                    this.full = null;
            }
            else {
                this.logon = null;
                this.full = null;
            }
        }
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
    public String toString(){
        if (null != this.full)
            return this.full;
        else if (null != this.logon)
            return this.logon;
        else
            return this.identifier;
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (that instanceof XAddress)

            return this.equals( (XAddress)that);

        else if (null == this.host)
            return this.identifier.equals(that);
        else
            return this.logon.equals(that);
    }
    public boolean equals(XAddress that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else if (null == this.resource || null == that.resource){

            if (null == this.host || null == that.host)
                return this.identifier.equals(that.identifier);
            else
                return this.logon.equals(that.logon);
        }
        else if (null != this.logon && null != that.logon){

            if (this.logon.equals(that.logon)){

                if (null != this.resource && null != that.resource)
                    return this.resourceKind.equals(that.resourceKind);
                else
                    return true;
            }
        }
        return false;
    }
    public int compareTo(XAddress that){
        if (this == that)
            return 0;
        else if (null == this.resource || null == that.resource){

            if (null == this.host || null == that.host)
                return this.identifier.compareTo(that.identifier);
            else
                return this.logon.compareTo(that.logon);
        }
        else {
            return this.full.compareTo(that.full);
        }
    }



    public final static String[] Scan(String string){
        if (null == string)
            return null;
        else {
            final char[] cary = string.toCharArray();
            final int carlen = cary.length;
            if (0 < carlen){
                /*
                 * measure
                 */
                int indexOfSlash = -1, indexOfHost = -1;
                scan:
                for (int cc = 0; cc < carlen; cc++){

                    switch(cary[cc]){

                    case '/':
                        indexOfSlash = cc;
                        break scan;
                    case '@':
                        indexOfHost = cc;
                        break;
                    default:
                        break;
                    }
                }
                /*
                 * cut
                 */
                if (-1 < indexOfHost){
                    if (-1 < indexOfSlash){
                        return new String[]{
                            string.substring(0,indexOfHost),
                            string.substring(indexOfHost+1,indexOfSlash),
                            string.substring(indexOfSlash+1)
                        };
                    }
                    else {
                        return new String[]{
                            string.substring(0,indexOfHost),
                            string.substring(indexOfHost+1)
                        };
                    }
                }
                else if (-1 < indexOfSlash)
                    throw new IllegalArgumentException(string);
                else {
                    return new String[]{
                        string
                    };
                }
            }
            else
                return null;
        }
    }
    public final static String[] Resource(String string){
        if (null != string){
            final int dot = string.lastIndexOf('.');
            if (0 < dot){
                return new String[]{
                    string.substring(0,dot),
                    string.substring(dot+1)
                };
            }
            else {
                final char[] cary = string.toCharArray();
                final int carlen = cary.length;
                if (0 < carlen){

                    for (int cc = (carlen-1); -1 < cc; cc--){

                        char ch = cary[cc];

                        if (IsNotHex(ch)){

                            final int end;

                            if (5 == cc && 'i' == ch && 'a' == cary[0] && 'n' == cary[1])
                                end = (cc + 2);
                            else if (4 == cc && 'n' == ch && 'i' == cary[0] && 'P' == cary[1])
                                end = (cc + 2);
                            else
                                end = (cc + 1);

                            return new String[]{
                                string.substring(0,end),
                                string.substring(end)
                            };
                        }
                    }
                }
            }
        }
        return null;
    }
    public final static boolean IsNotHex(char ch){
        if ('a' <= ch && 'f' >= ch)
            return false;
        else if ('A' <= ch && 'F' >= ch)
            return false;
        else if ('0' <= ch && '9' >= ch)
            return false;
        else
            return true;
    }
}
