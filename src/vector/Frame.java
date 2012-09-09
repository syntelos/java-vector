package vector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Dimension2D;

/**
 * 
 */
public class Frame
    extends javax.swing.JFrame
{

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

    public static void main(String[] argv){

        new Frame();
    }
}
