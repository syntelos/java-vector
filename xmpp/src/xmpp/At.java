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

import java.util.StringTokenizer;

/**
 * {@link Input} in the patterns
 * 
 * <pre>
 * @contact
 * @contact: message
 * @contact message
 * </pre>
 * 
 * will cause contact to be selected or contacted. An optional message
 * will be sent when the present contact is known.
 * 
 * More typical input is a message without a contact prefix.
 */
public enum At {
    Identifier,
    Logon,
    Tail,
    Empty;

    /**
     * <pre>
     * head, tail := ( '@' head (_:)* )? ( tail )?
     * address := head
     * at := address
     * </pre>
     */
    public static class Command
        extends java.lang.Object
    {

        public final At at;

        public final XAddress address;

        public final String source, head, tail;

        public Command(String string){
            super();
            this.source = string;

            XAddress addr = null;
            At at = At.Empty;
            String head = null, tail = null;

            if (null != string){
                String[] tokens = Command.Parse(string);
                if (null != tokens){
                    final int count = tokens.length;
                    boolean inHead = false;
                    switch(count){
                    case 1:
                        tail = tokens[0];

                        at = At.Tail;

                        break;

                    default:
                        for (int cc = 0; cc < count; cc++){
                            String tok = tokens[cc];
                            if (1 == tok.length()){

                                if (null != tail)
                                    tail += tok;
                                else if (inHead){
                                    switch(tok.charAt(0)){
                                    case ' ':
                                    case ':':
                                        inHead = false;
                                        break;
                                    default:
                                        head += tok;
                                        break;
                                    }
                                }
                                else {
                                    switch(tok.charAt(0)){
                                    case '@':
                                        inHead = (0 == cc);
                                        break;
                                    case ' ':
                                    case ':':
                                        break;
                                    default:
                                        tail = tok;

                                        at = At.Tail;

                                        break;
                                    }
                                }
                            }
                            else if (inHead){

                                if (null == head)
                                    head = tok;
                                else
                                    head += tok;
                            }
                            else if (null == tail){

                                tail = tok;

                                at = At.Tail;
                            }
                            else
                                tail += tok;
                        }
                        break;
                    }

                    if (null != head){
                        try {
                            addr = new XAddress(head);
                            if (addr.isHostDefault())
                                at = At.Identifier;
                            else
                                at = At.Logon;
                        }
                        catch (RuntimeException exc){
                        }
                    }
                }
            }
            this.at = at;
            this.address = addr;
            this.head = head;
            this.tail = tail;
        }


        public final static String[] Parse(String s){
            final StringTokenizer strtok = new StringTokenizer(s.trim(),"@: ",true);
            final int count = strtok.countTokens();
            if (0 < count){
                final String[] tokens = new String[count];
                for (int cc = 0; cc < count; cc++){
                    tokens[cc] = strtok.nextToken();
                }
                return tokens;
            }
            else
                return null;
        }
    }
    public static void main(String[] argv){

        for (String arg: argv){
            final At.Command ac = new At.Command(arg);

            System.out.printf("\"%s\" := At(%s)",ac.source,ac.at.name());

            switch (ac.at){

            case Identifier:
                System.out.printf(" Select(%s)",ac.address.identifier);
                if (null != ac.tail){
                    System.out.printf(" Send(%s)",ac.tail);
                }
                break;
            case Logon:
                System.out.printf(" Select(%s)",ac.address.logon);
                if (null != ac.tail){
                    System.out.printf(" Send(%s)",ac.tail);
                }
                break;

            case Tail:
                System.out.printf(" Send(%s)",ac.tail);
                break;
            default:
                break;
            }
            System.out.println();
        }
    }
}
