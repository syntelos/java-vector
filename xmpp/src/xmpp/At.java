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
 * {@link Input} parser for at code classifiers, see {@link TL
 * terminal language}.
 * 
 * <p> At code includes the empty string, <code>""</code>, and the
 * following patterns for the special characers <code>'@'</code> and
 * <code>':'</code>.
 * 
 * <pre>
 * @head: tail
 * @head tail
 * @head
 * tail
 * </pre>
 * </p>
 * 
 * <p> At code permits local and remote semantics via transparency.
 * An at code string with no particular semantics is repeated to the
 * selected current remote recipient. </p>
 * 
 * <p> An at code head that matches to a known remote recipient has
 * the function to select the identified recipient as the current
 * remote recipient.  In this case the head clause is consumed, and
 * any tail is sent to the remote recipient. </p>
 */
public enum At {
    /**
     * Has head and tail
     */
    Head,
    /**
     * Has tail
     */
    Tail,
    /**
     * Is empty
     */
    Empty;

    /**
     * <pre>
     * head, tail := ( '@' head (_:)* )? ( tail )?
     * </pre>
     */
    public static class Command
        extends java.lang.Object
    {

        public final At at;

        public final String source, head, tail;

        public Command(String string){
            super();
            this.source = string;

            String head = null, tail = null;

            if (null != string){
                String[] tokens = Command.Parse(string);
                if (null != tokens){
                    final int count = tokens.length;
                    boolean inHead = false;
                    switch(count){
                    case 1:
                        tail = tokens[0];

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
                            }
                            else
                                tail += tok;
                        }
                        break;
                    }
                }
            }
            if (null == head){
                if (null == tail){
                    this.at = At.Empty;
                    this.head = null;
                    this.tail = null;
                }
                else {
                    this.at = At.Tail;
                    this.head = null;
                    this.tail = tail;
                }
            }
            else {
                this.at = At.Head;
                this.head = head;
                this.tail = tail;
            }
        }


        public final boolean isEmpty(){
            return (At.Empty == this.at);
        }
        public final boolean isNotEmpty(){
            return (At.Empty != this.at);
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

            case Head:
                System.out.printf(" Head(%s)",ac.head);

            case Tail:
                System.out.printf(" Tail(%s)",ac.tail);

            default:
                break;
            }
            System.out.println();
        }
    }
}
