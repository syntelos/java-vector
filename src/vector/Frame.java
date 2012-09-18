package vector;

import json.Json;
import json.Reader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Dimension2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    public final Frame open(File file){

        this.display.open(file);

        return this;
    }


    public static void main(String[] argv){

        Frame frame = new Frame();
        if (0 < argv.length){

            boolean error = false;

            File file = new File(argv[0]);
            if (file.isFile()){

                frame.open(file);
            }
            else {

                frame.warn("Error, unrecognized file '%s'",file);
                System.exit(1);
            }
        }
    }
}
