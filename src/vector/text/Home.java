/*
 * Java Vector
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
package vector.text;

import vector.Text;

/**
 * An uneditable line left hand side element of text is empty, or a
 * prompt, etc.
 */
public final class Home 
    extends Object
{
    public final static Home Nil = new Home();


    public final int length, lengthM1, lengthP1;

    public final CharSequence string;

    private final char[] cary;


    public Home(){
        this(0,null);
    }
    public Home(int length){
        this(length,null);
    }
    public Home(CharSequence string){
        this(string.length(),string);
    }
    public Home(int length, CharSequence string){
        super();

        if (null != string){
            if (length == string.length()){
                this.length = length;
                this.string = string;
                this.cary = Text.ToCharArray(string);
            }
            else {
                throw new IllegalArgumentException(String.valueOf(length));
            }
        }
        else if (-1 < length){
            this.length = length;
            this.string = Home.Fill(length);
            this.cary = Text.ToCharArray(string);
        }
        else {
            throw new IllegalArgumentException(String.valueOf(length));
        }

        this.lengthM1 = this.length-1;
        this.lengthP1 = this.length+1;
    }


    public char[] cat(){
        if (null != this.cary)
            return this.cary.clone();
        else
            return null;
    }
    public char[] cat(CharSequence text){

        return this.cat(Text.ToCharArray(text));
    }
    public char[] cat(char[] text){
        if (0 < this.length){
            if (null != text){
                char[] cary = new char[this.length+text.length];
                System.arraycopy(this.cary,0,cary,0,this.length);
                System.arraycopy(text,0,cary,this.length,text.length);
                return cary;
            }
            else
                return this.cary.clone();
        }
        else if (null == text)
            return null;
        else
            return text;
    }
    public String trim(String text){
        if (this.length < text.length())
            return text.substring(this.length).trim();
        else
            return "";
    }
    public String trim(char[] text){
        if (null != text && this.length < text.length){

            final int count = (text.length-this.length);
            if (0 < count)
                return new String(text,this.length,count).trim();
        }
        return "";
    }
    public int hashCode(){
        return this.string.hashCode();
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (that instanceof Home)
            return this.equals( (Home)that);
        else
            return false;
    }
    public boolean equals(Home that){
        if (null == that)
            return false;
        else
            return this.string.equals(that.string);
    }
    public String toString(){
        return String.format("[%d]%s",length,string);
    }


    public final static String Fill(int len){
        StringBuilder string = new StringBuilder();
        for (int cc = 0; cc < len; cc++){
            string.append(' ');
        }
        return string.toString();
    }
}
