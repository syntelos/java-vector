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

import platform.Shape;

/**
 * Text cursor logic
 */
public final class Cursor {


    public final Home home;

    private final IBeam ibeam = new IBeam();

    private volatile boolean left = true;

    private volatile int index;


    public Cursor(Home home){
        super();
        if (null != home)
            this.home = home;
        else
            this.home = Home.Nil;
    }


    public boolean isLeft(){
        return this.left;
    }
    public boolean isRight(){
        return (!this.left);
    }
    public int getIndex(){
        return this.index;
    }
    public boolean home(){

        if (this.home.length != this.index || true != this.left){
            this.index = this.home.length;
            this.left = true;
            return true;
        }
        else
            return false;
    }
    public boolean left(int index){

        if (index != this.index || true != this.left){
            this.index = index;
            this.left = true;
            return true;
        }
        else
            return false;
    }
    public boolean end(int count){

        int index = (count-1);
        if (index != this.index || false != this.left){
            this.index = index;
            this.left = false;
            return true;
        }
        else
            return false;
    }
    public boolean move(int dx, int count){
        if (this.left){
            int index = this.index + dx;
            if (this.home.lengthM1 < index){

                if (index < count)
                    return this.left(index);
                else 
                    return this.end(count);
            }
            else 
                return this.home();
        }
        else {
            if (this.home.length > dx){
                if (true != this.left){
                    this.left = true;
                    return true;
                }
            }
            return false;
        }
    }
    public char[] backspace(char[] string){
        if (null == string)
            return string;
        else {
            int strlen = string.length;

            if (this.left){
                if (this.home.length < this.index){

                    char[] copier = new char[strlen-1];

                    if (1 < this.index)
                        System.arraycopy(string,0,copier,0,(this.index-1));

                    System.arraycopy(string,this.index,copier,(this.index-1),(strlen-this.index));

                    this.index--;

                    return copier;
                }
                else
                    return string;
            }
            else if (this.home.lengthP1 == strlen){
                this.index = this.home.length;
                this.left = true;
                return this.home.cat();
            }
            else if (this.home.length < this.index){

                char[] copier = new char[strlen-1];

                System.arraycopy(string,0,copier,0,(strlen-1));

                this.index--;

                return copier;
            }
            else 
                throw new IllegalStateException();
        }
    }
    public char[] delete(char[] string){
        if (null == string)
            return this.home.cat();
        else {
            int strlen = string.length;

            if (this.left){

                char[] copier = new char[strlen-1];

                if (0 < this.index)
                    System.arraycopy(string,0,copier,0,this.index);

                System.arraycopy(string,(this.index+1),copier,this.index,(strlen-this.index-1));

                return copier;
            }
            else
                return string;
        }
    }
    public char[] add(char[] string, char ch, int bound){
        if (0 < bound){
            if (null == string){
                this.left = false;
                this.index = this.home.length;
                return new char[]{ch};
            }
            else {
                int strlen = string.length;
                int newlen = (strlen+1);
                if (newlen <= bound){

                    char[] copier = new char[newlen];

                    if (this.left){

                        if (0 == this.index)
                            System.arraycopy(string,0,copier,1,strlen);
                        else {
                            System.arraycopy(string,0,copier,0,this.index);
                            System.arraycopy(string,this.index,copier,(this.index+1),(strlen-this.index));
                        }
                        copier[this.index] = ch;

                        this.index++;
                    }
                    else {

                        System.arraycopy(string,0,copier,0,strlen);

                        copier[strlen] = ch;

                        this.index = strlen;
                    }
                    return copier;
                }
            }
        }
        return string;
    }
    public Shape getIBeam(Text text){

        if (text.isEmpty())
            return this.ibeam.getShape(text);
        else
            return this.ibeam.getShape(this.index,this.left,text);
    }
}
