package vector;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.awt.RenderingHints.*;
import java.awt.image.ImageObserver;

/**
 * {@link Output} buffer
 * 
 * @see Display
 */
public final class Offscreen
    extends java.awt.image.BufferedImage
{

    public Offscreen(Component component){
        this(component.getBounds());
    }
    public Offscreen(Rectangle bounds){
        this(bounds.width,bounds.height);
    }
    public Offscreen(int width, int height){
        super(width,height,TYPE_INT_ARGB);
    }


    public Graphics2D blit(ImageObserver p, Graphics2D g){

        g.drawImage(this,0,0,p);

        return this.hint(g);
    }
    public Graphics2D create(){

        return this.hint((Graphics2D)this.getGraphics());
    }
    public Graphics2D hint(Graphics2D g){

        g.setRenderingHint(KEY_RENDERING,VALUE_RENDER_QUALITY);
        g.setRenderingHint(KEY_ANTIALIASING,VALUE_ANTIALIAS_ON);
        g.setRenderingHint(KEY_TEXT_ANTIALIASING,VALUE_TEXT_ANTIALIAS_ON);

        return g;
    }
}
