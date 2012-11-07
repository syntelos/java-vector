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

import platform.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * One child {@link Xmpp}
 */
public class Display
    extends platform.Display
{
    protected final static Charset UTF8 = Charset.forName("UTF-8");


    public Display(){
        super();
    }


    @Override
    public void init(){

        this.setBackground(Color.white);

        super.init();
    }
    @Override
    public void modified(){

        super.modified();
    }
    public Xmpp get(){
        if (this.has(0))
            return (Xmpp)this.get(0);
        else
            return null;
    }
    @Override
    public boolean open(File file){
        try {
            LineNumberReader fin = new LineNumberReader(new InputStreamReader(new FileInputStream(file),UTF8));
            try {
                fin.setLineNumber(1);

                this.init();

                Xmpp xmpp = new Xmpp();

                this.add(xmpp);

                if (xmpp.read(fin)){

                    this.modified();

                    this.outputScene();

                    return true;
                }
                else
                    return false;
            }
            finally {
                fin.close();
            }
        }
        catch (IOException exc){

            this.error(exc,"Reading file '%s'",file);

            return false;
        }
    }
    @Override
    public boolean open(URL url){
        try {
            LineNumberReader uin = new LineNumberReader(new InputStreamReader(url.openStream(),UTF8));
            try {
                uin.setLineNumber(1);

                this.init();

                Xmpp xmpp = new Xmpp();

                this.add(xmpp);

                if (xmpp.read(uin)){

                    this.modified();

                    this.outputScene();

                    return true;
                }
                else
                    return false;
            }
            finally {
                uin.close();
            }
        }
        catch (IOException exc){

            this.error(exc,"Reading URL '%s'",url);

            return false;
        }
    }
}
