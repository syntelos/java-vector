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

import vector.Bounds;

import json.Json;
import json.Reader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
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
    public final static String Title;
    static {
        String config = System.getProperty("platform.Frame.Title");
        if (null == config)
            Title = "Vector";
        else
            Title = config;
    }

    protected final static int DefaultWidth = 640;
    protected final static int DefaultX = (Frame.DefaultWidth>>1);
    protected final static int DefaultHeight = 480;
    protected final static int DefaultY = (Frame.DefaultHeight>>1);

    protected final static Dimension2D Screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    protected final static int Width = Math.max(Frame.DefaultWidth,(int)(((float)Frame.Screen.getWidth())/2.0f));
    protected final static int Height = Math.max(Frame.DefaultHeight,(int)(((float)Frame.Screen.getHeight())/2.0f));

    protected final static int X = Math.max(Frame.DefaultX,(Frame.Width/2));
    protected final static int Y = Math.max(Frame.DefaultY,(Frame.Height/2));


    public final static Bounds Center(Bounds b){

        if (null != b){

            final float sx = Frame.Width;
            final float sy = Frame.Height;

            final float bx = (b.width/2);
            final float by = (b.height/2);

            b.x = Math.max(0f,(sx-bx));
            b.y = Math.max(0f,(sy-by));
        }
        return b;
    }

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Display display;


    public Frame(){
        this(Frame.Title);
    }
    /**
     * This constructor is not portable
     */
    public Frame(String title){
        super((null != title)?(title):(Frame.Title));

        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        this.setSize(Frame.Width,Frame.Height);

        this.setLocation(Frame.X,Frame.Y);

        this.reinit( this.createDisplay());

        this.pack();
        this.show();
    }



    public Display getDisplay(){

        return this.display;
    }
    public Display createDisplay(){

        return new Display();
    }
    public void reinit(Display display){

        final Dimension frame = this.getSize();
        final Insets insets = this.getInsets();

        final int width = frame.width-(insets.left+insets.right);
        final int height = frame.height-(insets.top+insets.bottom);
        final Dimension awt = new Dimension(width,height);


        if (display != this.display){

            this.destroy(this.display);

            this.display = display;
            if (null != display){

                display.init();

                this.add(display, BorderLayout.CENTER);

                display.setSize(awt);
            }
        }
        else {
            display.init();

            display.setSize(awt);
        }
    }
    public void init(){

        this.reinit(this.display);
    }
    public void destroy(Display display){

        if (null != display && display == this.display){
            this.display = null;

            this.remove(display);

            display.destroy();
        }
    }
    public void destroy(){

        Display display = this.display;
        if (null != display){
            this.display = null;

            this.remove(display);

            display.destroy();
        }
        this.dispose();
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
