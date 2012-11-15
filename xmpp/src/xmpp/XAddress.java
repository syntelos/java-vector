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


    public final String identifier, host, resource, logon, full;


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
     * @param full Input requirement
     */
    public XAddress(String string, XAddress.Require full){
        super();
        String[] parts = XAddress.Scan(string);
        if (null == parts)
            throw new IllegalArgumentException(string);
        else {
            /*
             */
            switch(parts.length){
            case 1:
                switch (full){
                case Logon:
                case Full:
                    throw new IllegalArgumentException(string);
                default:
                    /*
                     * identifier
                     */
                    this.identifier = parts[0];
                    this.host = XAddress.Default.Host;
                    this.resource = XAddress.Default.Resource;
                    break;
                }
                break;
            case 2:
                switch (full){
                case Full:
                    throw new IllegalArgumentException(string);
                default:
                    /*
                     * identifier, host
                     */
                    this.identifier = parts[0];
                    this.host = parts[1];
                    this.resource = XAddress.Default.Resource;
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

        return ((Default.Host == this.host)||
                this.host.equalsIgnoreCase(Default.Host));
    }
    public boolean isNotHostDefault(){

        return ((Default.Host != this.host)&&
                (!this.host.equalsIgnoreCase(Default.Host)));
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
        else if (null == that)
            return false;
        else
            return this.full.equals(that.toString());
    }
    public int compareTo(XAddress that){
        if (this == that)
            return 0;
        else
            return this.full.compareTo(that.full);
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
}
