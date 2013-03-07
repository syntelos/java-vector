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
package platform;

import vector.Bounds;

import json.Json;
import json.Reader;

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
    extends java.lang.Object
{

    protected static Object screen = null;

    protected static int width;
    protected static int height;

    protected static int x;
    protected static int y;


    public final static Bounds Center(Bounds b){

        if (null != b){

            final float sx = Frame.width;
            final float sy = Frame.height;

            final float bx = (b.width/2);
            final float by = (b.height/2);

            b.x = (sx-bx);
            b.y = (sy-by);
        }
        return b;
    }

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Display display;


    public Frame(){
        this(null);
    }
    public Frame(String title){
        super();

        this.display = this.createDisplay();
    }



    public Display getDisplay(){

        return this.display;
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
    }
    /**
     * For subclasses not using Frame eval or Display open
     */
    public void modified(){

        Display display = this.display;
        if (null != display){

            display.modified();
        }
    }
    /**
     * For subclasses not using Frame eval or Display open
     */
    public void outputScene(){

        Display display = this.display;
        if (null != display){

            display.outputScene();
        }
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
