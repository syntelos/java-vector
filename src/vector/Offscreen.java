package vector;

import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.RenderingHints.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

/**
 * {@link Output} buffer
 */
public final class Offscreen
    extends java.awt.image.BufferedImage
{

    public Offscreen(Component component){
        this(component.getBoundsVector());
    }
    public Offscreen(Rectangle2D.Float bounds){
        this(bounds.width,bounds.height);
    }
    public Offscreen(float width, float height){
        super((int)width,(int)height,TYPE_INT_ARGB);
    }


    public Graphics2D blit(ImageObserver p, Graphics g){

        g.drawImage(this,0,0,p);

        return this.hint(g);
    }
    public Graphics2D create(){

        return this.hint(this.getGraphics());
    }
    public Graphics2D hint(Graphics eg){

        final Graphics2D g = (Graphics2D)eg;

        g.setRenderingHint(KEY_RENDERING,VALUE_RENDER_QUALITY);
        g.setRenderingHint(KEY_ANTIALIASING,VALUE_ANTIALIAS_ON);
        g.setRenderingHint(KEY_TEXT_ANTIALIASING,VALUE_TEXT_ANTIALIAS_ON);

        return g;
    }

}
