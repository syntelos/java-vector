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
package vector;

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

        this.display.init();
    }



    protected Display createDisplay(){

        return new Display();
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
    public final boolean open(File file){

        return this.display.open(file);
    }
    public final boolean open(URL url){

        return this.display.open(url);
    }


    public static void main(String[] argv){

        Frame frame = new Frame();
        if (0 < argv.length){

            String string = argv[0];
            try {
                try {
                    URL url = new URL(string);

                    if (!frame.open(url))
                        System.exit(1);

                }
                catch (MalformedURLException exc){

                    File file = new File(string);
                    if (file.isFile()){

                        if (!frame.open(file))
                            System.exit(1);
                    }
                    else {

                        frame.warn("Error, unrecognized file '%s'",file);
                        System.exit(1);
                    }
                }
            }
            catch (Throwable t){

                frame.warn(t,"Error loading '%s'",string);
                System.exit(1);
            }
        }
        else {

            frame.warn("Error, require argument JSON file");
            System.exit(1);
        }
    }
}
