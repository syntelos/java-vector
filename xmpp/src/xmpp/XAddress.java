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
    extends xma.XAddress
{

    public final static class Default {

        public final static String Host = "talk.l.google.com";

        public final static String Resource = "Vector";

        public final static int Port = 5222; 
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
    public XAddress(String string, Require require){
        super(string,require);
    }


    public boolean isHostDefault(){

        return (null == this.host);
    }
    public boolean isNotHostDefault(){

        return (null != this.host);
    }
    public boolean isResourceDefault(){

        return (null == this.resource);
    }
    public boolean isNotResourceDefault(){

        return (null != this.resource);
    }
}
