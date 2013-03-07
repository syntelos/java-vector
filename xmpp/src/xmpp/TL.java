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
 * {@link At} user {@link Display} ({@link Terminal}).
 */
public enum TL {
    /*
     * At head, but can't parse as an XAddress
     */
    Head,
    /*
     * No (at)
     */
    Tail,
    /*
     * Empty string
     */
    Empty,
    /*
     * Command "clear"
     */
    Clear,
    /*
     * An identifier: remote command or address
     */
    Identifier,
    /*
     * A qualified XAddress
     */
    Logon;

    /**
     * Classify the output of the {@link At.Command At parser}.
     */
    public static class Command
        extends java.lang.Object
    {

        public final At at;

        public final TL tl;

        public final XAddress address;

        public final String source, head, tail;

        public Command(String m){
            this(new At.Command(m));
        }
        public Command(At.Command at){
            super();
            this.at = at.at;
            this.source = at.source;
            this.head = at.head;
            this.tail = at.tail;

            XAddress addr = null;
            TL tl = TL.Empty;

            switch(at.at){

            case Head:
                tl = TL.Head;

                if ("clear".equalsIgnoreCase(at.head)){

                    addr = null;

                    tl = TL.Clear;
                }
                else {
                    try {
                        addr = new XAddress(head);
                        if (addr.isHostDefault())
                            tl = TL.Identifier;
                        else
                            tl = TL.Logon;
                    }
                    catch (RuntimeException exc){
                    }
                }
                break;

            case Tail:
                tl = TL.Tail;
                break;
            default:
                break;
            }
            this.address = addr;
            this.tl = tl;
        }

    }
    public static void main(String[] argv){

        for (String arg: argv){

            final TL.Command tl = new TL.Command(arg);

            System.out.printf("\"%s\" := At(%s)",tl.source,tl.at.name());

            switch (tl.tl){

            case Identifier:
                System.out.printf(" Select(%s)",tl.address.identifier);
                if (null != tl.tail){
                    System.out.printf(" Send(%s)",tl.tail);
                }
                break;
            case Logon:
                System.out.printf(" Select(%s)",tl.address.logon);
                if (null != tl.tail){
                    System.out.printf(" Send(%s)",tl.tail);
                }
                break;

            case Tail:
                System.out.printf(" Send(%s)",tl.tail);
                break;
            default:
                break;
            }
            System.out.println();
        }
    }
}
