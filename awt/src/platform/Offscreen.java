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
    implements vector.Image.Offscreen
{

    private final int width, height;

    private ImageObserver observer;


    /**
     * @param component First argument to platform context (Display)
     */
    public Offscreen(java.awt.Component component){
        this(component,component.getBounds());
    }
    private Offscreen(ImageObserver observer, Rectangle bounds){
        this(observer,bounds.width,bounds.height);
    }
    /**
     * @param observer First argument to platform context (Display)
     * @param width Pixel buffer X dimension
     * @param height Pixel buffer Y dimension
     */
    public Offscreen(ImageObserver observer, int width, int height){
        super(width,height,TYPE_INT_ARGB);
        if (null != observer && 0 < width && 0 < height){
            this.observer = observer;
            this.width = width;
            this.height = height;
        }
        else
            throw new IllegalArgumentException();
    }


    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    @Override
    public void flush(){

        this.observer = null;

        super.flush();
    }
    public Context blit(Context g){

        g.draw(this);

        return g;
    }
    public Context create(){

        final Graphics2D g = (Graphics2D)this.getGraphics();

        g.setRenderingHint(KEY_RENDERING,VALUE_RENDER_QUALITY);
        g.setRenderingHint(KEY_ANTIALIASING,VALUE_ANTIALIAS_ON);
        g.setRenderingHint(KEY_TEXT_ANTIALIASING,VALUE_TEXT_ANTIALIAS_ON);

        return new Context(this.observer,g);
    }
}
