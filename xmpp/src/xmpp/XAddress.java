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

import org.jivesoftware.smack.packet.Packet;

/**
 * An XMPP address is a messaging network reference
 * <pre>
 * logon = identifier@host
 * full = logon/resource
 * </pre>
 */
public class XAddress
    extends java.lang.Object
    implements java.lang.CharSequence,
               java.io.Serializable,
               java.lang.Comparable<XAddress>
{
    public final static long serialVersionUID = 1L;

    public final static class Default {

        public final static String Host = "talk.l.google.com";

        public final static String Resource = "Vector";

        public final static int Port = 5222; 
    }
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
            super(input,Require.Identifier);
        }
    }
    /**
     * Require input equal to or greater than logon
     */
    public static class Logon
        extends XAddress
    {
        public Logon(String input){
            super(input,Require.Logon);
        }
    }
    /**
     * Require full XMPP address input string
     */
    public static class Full
        extends XAddress
    {
        public Full(String input){
            super(input,Require.Full);
        }
    }
    /**
     * 
     */
    public static class From
        extends XAddress.Full
    {
        public From(){
            super(Preferences.GetLogon()+'/'+Preferences.ComposeResource());
        }
        public From(Packet m){
            super(m.getFrom());
        }
    }
    /**
     * 
     */
    public static class To
        extends XAddress.Logon
    {
        public To(){
            super(Preferences.GetTo());
        }
        public To(Packet m){
            super(m.getTo());
        }
    }


    public final String identifier, host, resource, logon, full;

    public final String resourceKind, resourceSession;


    /**
     * Accept substrings
     * 
     * @param string A part or whole XMPP address
     */
    public XAddress(String string){
        this(string,Require.Identifier);
    }
    /**
     * Disallow substrings 
     * 
     * @param string A part or whole XMPP address
     * @param require Input requirement
     */
    public XAddress(String string, XAddress.Require require){
        super();
        String[] parts = XAddress.Scan(string);
        if (null == parts)
            throw new IllegalArgumentException(string);
        else {
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
                    this.host = Preferences.GetHost();
                    this.resource = Preferences.ComposeResource();
                    this.resourceKind = Preferences.GetResource();
                    this.resourceSession = Preferences.GetSession();
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
                    this.resource = Preferences.ComposeResource();
                    this.resourceKind = Preferences.GetResource();
                    this.resourceSession = Preferences.GetSession();
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
            strbuf.append('@');
            strbuf.append(this.host);

            this.logon = strbuf.toString();

            strbuf.append('/');
            strbuf.append(this.resource);

            this.full = strbuf.toString();
        }
    }


    public boolean isHostDefault(){

        return Preferences.IsHostDefault(this.host);
    }
    public boolean isNotHostDefault(){

        return (!this.isHostDefault());
    }
    public boolean isResourceDefault(){

        return Preferences.IsResourceDefault(this.resource);
    }
    public boolean isNotResourceDefault(){

        return (!this.isResourceDefault());
    }
    public char charAt(int idx){
        return this.full.charAt(idx);
    }
    public int length(){
        return this.full.length();
    }
    public CharSequence subSequence(int start, int end){
        return this.full.subSequence(start,end);
    }
    public int hashCode(){
        return this.full.hashCode();
    }
    public String toString(){
        return this.full;
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (that instanceof XAddress)

            return this.equals( (XAddress)that);

        else if (this.isHostDefault())
            return this.identifier.equals(that);
        else
            return this.logon.equals(that);
    }
    public boolean equals(XAddress that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else if (this.isResourceDefault() || that.isResourceDefault()){

            if (this.isHostDefault() || that.isHostDefault())
                return this.identifier.equals(that.identifier);
            else
                return this.logon.equals(that.logon);
        }
        else if (this.logon.equals(that.logon)){

            return this.resourceKind.equals(that.resourceKind);
        }
        else
            return false;
    }
    public int compareTo(XAddress that){
        if (this == that)
            return 0;
        else if (this.isResourceDefault() || that.isResourceDefault()){

            if (this.isHostDefault() || that.isHostDefault())
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
