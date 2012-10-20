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
package vector.text;

import vector.Text;

import platform.Font;

import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Line editor
 */
public class Editor
    extends Visual
{

    public final Home home;

    public final Cursor cursor;


    public Editor(){
        this(Home.Nil);
    }
    public Editor(CharSequence text){
        this(Home.Nil,text);
    }
    public Editor(Home home){
        super();
        this.cursor = new Cursor(home);
        this.home = this.cursor.home;
    }
    public Editor(Home home, CharSequence text){
        super();
        this.cursor = new Cursor(home);
        this.home = this.cursor.home;
        this.set(text);
    }


    public String trim(){
        return this.home.trim(this.visual);
    }
    public Shape cursor(Text text){

        return this.cursor.getIBeam(text);
    }
    /**
     * @return Cursor modified
     */
    public boolean home(){

        return this.cursor.home();
    }
    /**
     * @return Cursor modified
     */
    public boolean end(){

        return this.cursor.end(this.visualLength());
    }
    /**
     * @return Cursor modified
     */
    public boolean left(){

        return this.cursor.move(-1,this.visualLength());
    }
    /**
     * @return Cursor modified
     */
    public boolean right(){

        return this.cursor.move(+1,this.visualLength());
    }
    /**
     * @return Text modified
     */
    public boolean backspace(){

        char[] update = this.cursor.backspace(this.visual);
        if (update != this.visual)

            return this.set(update);
        else
            return false;
    }
    /**
     * @return Text modified
     */
    public boolean delete(){

        char[] update = this.cursor.delete(this.visual);
        if (update != this.visual)

            return this.set(update);
        else
            return false;
    }
    /**
     * @return Text modified
     */
    public boolean add(char key, int bound){

        char[] update = this.cursor.add(this.visual, key, bound);
        if (update != this.visual)

            return this.set(update);
        else
            return false;
    }
    /**
     * @return Text modified
     */
    public boolean set(CharSequence string){

        this.set(this.home.cat(string));
        this.cursor.end(this.visualLength());
        return true;
    }
    public Editor add(CharSequence string){
        if (null == this.visual){
            this.set(string);
            return this;
        }
        else {
            char[] source = ToCharArray(string);
            if (null != source){
                int thisLength = this.visual.length;
                int sourceLength = source.length;
                char[] copier = new char[thisLength+sourceLength];
                System.arraycopy(this.visual,0,copier,0,thisLength);
                System.arraycopy(source,0,copier,thisLength,sourceLength);

                this.set(copier);
                this.cursor.end(thisLength);
            }
            return this;
        }
    }
    public Editor add(int index, CharSequence string){
        if (null == this.visual){
            if (0 == index){
                this.set(string);
                return this;
            }
            else
                throw new IllegalArgumentException(String.valueOf(index));
        }
        else {
            char[] source = ToCharArray(string);
            if (null != source){
                int thisLength = this.visual.length;
                int sourceLength = source.length;
                if (0 == index){
                    char[] copier = new char[thisLength+sourceLength];
                    System.arraycopy(source,0,copier,0,sourceLength);
                    System.arraycopy(this.visual,0,copier,sourceLength,thisLength);

                    this.set(copier);
                    this.cursor.left(index);
                }
                else if (index == (thisLength-1)){
                    char[] copier = new char[thisLength+sourceLength];
                    System.arraycopy(this.visual,0,copier,0,thisLength);
                    System.arraycopy(source,0,copier,thisLength,sourceLength);

                    this.set(copier);
                    this.cursor.left(index);
                }
                else if (index < thisLength){
                    char[] copier = new char[thisLength+sourceLength];
                    System.arraycopy(this.visual,0,copier,0,index);
                    System.arraycopy(source,0,copier,index,sourceLength);
                    System.arraycopy(this.visual,index,copier,(index+sourceLength),(thisLength-index-1));

                    this.set(copier);
                    this.cursor.left(index);
                }
                else 
                    throw new IllegalArgumentException(String.valueOf(index));
            }
        }
        return this;
    }
    public Editor clear(){

        super.clear();

        this.cursor.home();

        return this;
    }
}
