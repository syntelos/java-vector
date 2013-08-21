/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, The DigiVac Company
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
package vector.data;

import java.net.URLDecoder;
import java.util.StringTokenizer;

/**
 * 
 * @since java-1.5
 */
public class DataMessage
    extends java.lang.Object
    implements java.lang.Appendable,
               java.lang.CharSequence,
               java.lang.Iterable<DataMessageTerm>
{

    public final DataKind kind;

    public final DataMessageType type;

    private DataMessageTerm[] content;

    private StringBuilder string;


    public DataMessage(DataKind kind, DataMessageType type){
        super();
        if (null != kind && null != type){
            this.kind = kind;
            this.type = type;
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    public boolean isTMTC(){ return this.type.isTMTC();}
    public boolean isTEXT(){ return this.type.isTEXT();}
    public boolean isCODE(){ return this.type.isCODE();}
    public boolean isERROR(){ return this.type.isERROR();}
    public boolean isINFO(){ return this.type.isINFO();}
    public boolean isLAN(){ return this.type.isLAN();}
    public boolean isWAN(){ return this.type.isWAN();}

    public boolean isNotTMTC(){ return this.type.isNotTMTC();}
    public boolean isNotTEXT(){ return this.type.isNotTEXT();}
    public boolean isNotCODE(){ return this.type.isNotCODE();}
    public boolean isNotERROR(){ return this.type.isNotERROR();}
    public boolean isNotINFO(){ return this.type.isNotINFO();}
    public boolean isNotLAN(){ return this.type.isNotLAN();}
    public boolean isNotWAN(){ return this.type.isNotWAN();}

    @Override
    public DataMessage append(char ch){
        throw new UnsupportedOperationException();
    }
    @Override
    public DataMessage append(CharSequence string, int start, int end){
        throw new UnsupportedOperationException();
    }
    @Override
    public DataMessage append(CharSequence string){

        if (this.isNotTMTC()){

            if (null != string){

                if (string instanceof String){
                    string = ((String)string).trim();
                }

                if (0 < string.length()){

                    if (null == this.string){
                        this.string = new StringBuilder();
                    }

                    if (0 != this.length()){
                        this.string.append('\n');
                    }

                    this.string.append(string);
                }
            }
        }
        else {

            final StringTokenizer strtok = new StringTokenizer(string.toString()," =?\t\r\n",true);

            char sep = 0;
            Object name = null, value = null;

            while (strtok.hasMoreTokens()){
                String tok = strtok.nextToken();
                switch(tok.charAt(0)){
                case ' ':
                    sep = ' ';
                    break;
                case '=':
                    sep = '=';
                    break;
                case '?':
                    sep = '?';
                    break;
                case '\t':
                    sep = '\t';
                    break;
                case '\r':
                    sep = '\n';
                    break;
                case '\n':
                    sep = '\n';
                    break;
                default:
                    if (null == name){
                        try {
                            name = kind.identifier(tok);
                        }
                        catch (RuntimeException exc){

                            throw new IllegalArgumentException(String.format("Error unrecognized name (%s) in (%s)",tok,string),exc);
                        }
                    }
                    else if (null == value && (' ' == sep || '=' == sep)){
                        value = tok;
                        this.add(new DataMessageTerm(this.kind,name,value));
                        name = null;
                        value = null;
                    }
                    else {
                        this.add(new DataMessageTerm(this.kind,name,null));
                        name = tok;
                        value = null;
                        try {
                            name = kind.identifier(tok);
                        }
                        catch (RuntimeException exc){

                            throw new IllegalArgumentException(String.format("Error unrecognized name (%s) in (%s)",tok,string),exc);
                        }
                    }
                    break;
                }
            }
        }
        return this;
    }
    public DataMessageTerm add(DataMessageTerm term){
        if (null == term)
            return null;
        else {
            this.content = DataMessageTerm.Add(this.content,term);

            if (null == this.string){
                this.string = new StringBuilder();
            }

            if (0 != this.string.length()){
                this.string.append('\n');
            }

            this.string.append(term.string);

            return term;
        }
    }
    public DataMessageTerm.Iterator iterator(){

        return new DataMessageTerm.Iterator(this.content);
    }
    public int length(){
        StringBuilder string = this.string;
        if (null == string)
            return 0;
        else
            return string.length();
    }
    public char charAt(int idx){
        StringBuilder string = this.string;
        if (null == string)
            throw new IndexOutOfBoundsException(String.valueOf(idx));
        else
            return string.charAt(idx);
    }
    public CharSequence subSequence(int start, int end){
        StringBuilder string = this.string;
        if (null == string)
            throw new IndexOutOfBoundsException(String.valueOf(start));
        else
            return string.subSequence(start,end);
    }
    public String toString(){
        StringBuilder string = this.string;
        if (null == string)
            return "";
        else
            return string.toString();
    }
}
