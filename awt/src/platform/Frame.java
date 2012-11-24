/*
 * Vector (http://code.google.com/p/java-awt/)
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
package platform;

import json.Json;
import json.Reader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Dimension2D;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */
public class Frame
    extends javax.swing.JFrame
{

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Display display;


    public Frame(){
        this(null);
    }
    public Frame(String title){
        super((null == title)?("Vector"):(title));

        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        final Dimension2D screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(((float)screen.getWidth())/2.0f);
        int height = (int)(((float)screen.getHeight())/2.0f);
        this.setSize(width,height);
        int x = (width/2);
        int y = (height/2);
        this.setLocation(x,y);
        

        this.add((this.display = this.createDisplay()), BorderLayout.CENTER);

        this.display.setSize(width,height);

        this.pack();
        this.show();
    }



    public Display createDisplay(){

        return new Display();
    }
    public void init(){

        Display display = this.display;
        if (null != display){

            display.init();
        }
    }
    public void destroy(){

        Display display = this.display;
        if (null != display){
            this.display = null;
            display.destroy();
        }
        this.dispose();
    }
    public final Frame warn(Throwable t, String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args),t);

        return this;
    }
    public final Frame warn(String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args));

        return this;
    }
    public final Frame error(Throwable t, String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args),t);

        return this;
    }
    public final Frame error(String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args));

        return this;
    }
    public final boolean open(File file){

        return this.display.open(file);
    }
    public final boolean open(URL url){

        return this.display.open(url);
    }
    public final boolean eval(String[] cli){
        if (0 < cli.length){

            String string = cli[0];
            try {
                try {
                    URL url = new URL(string);

                    if (!this.open(url)){

                        this.error("Reading failed '%s'",url);

                        return false;
                    }
                    else
                        return true;
                }
                catch (MalformedURLException exc){

                    File file = new File(string);
                    if (file.isFile()){

                        if (!this.open(file)){

                            this.error("Reading failed '%s'",file);

                            return false;
                        }
                        else
                            return true;
                    }
                    else {

                        this.error("File not found '%s'",file);

                        return false;
                    }
                }
            }
            catch (Throwable t){

                this.error(t,"Error reading '%s'",string);

                return false;
            }
        }
        else {

            this.error("Require file or url");

            return false;
        }
    }

    public static void main(String[] argv){

        final Frame frame = new Frame();

        frame.init();

        if (frame.eval(argv))
            return;
        else
            System.exit(1);
    }
}
