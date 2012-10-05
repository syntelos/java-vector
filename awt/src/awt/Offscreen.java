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
package awt;

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

    private ImageObserver observer;


    public Offscreen(java.awt.Component component){
        this(component.getBounds());
        this.observer = component;
    }
    private Offscreen(Rectangle bounds){
        this(bounds.width,bounds.height);
    }
    private Offscreen(int width, int height){
        super(width,height,TYPE_INT_ARGB);
    }


    @Override
    public void flush(){

        this.observer = null;

        super.flush();
    }
    public Context blit(Context g){

        g.drawImage(this,0,0);

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
