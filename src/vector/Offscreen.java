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
