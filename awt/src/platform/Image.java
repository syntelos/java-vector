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

import platform.geom.Point;

import java.net.URL;

/**
 * GIF, JPEG or PNG
 */
public class Image
    extends Object
    implements vector.Image
{
    private final static java.awt.Toolkit TK = java.awt.Toolkit.getDefaultToolkit();


    public final URL source;

    protected java.awt.Image nativeImage;


    public Image(Class context, String resource){
        this(context.getResource(resource));
    }
    public Image(URL url){
        super();
        if (null != url){
            this.source = url;
            this.nativeImage = TK.createImage(url);
            TK.prepareImage(this.nativeImage,-1,-1,null);
        }
        else
            throw new IllegalArgumentException();
    }


    public int getWidth(){
        java.awt.Image nativeImage = this.nativeImage;
        if (null != nativeImage)
            return nativeImage.getWidth(null);
        else
            return 0;
    }
    public int getHeight(){
        java.awt.Image nativeImage = this.nativeImage;
        if (null != nativeImage)
            return nativeImage.getHeight(null);
        else
            return 0;
    }
    public void flush(){

        java.awt.Image nativeImage = this.nativeImage;
        if (null != nativeImage){
            this.nativeImage = null;

            nativeImage.flush();
        }
    }
    public Context blit(Context g){
        java.awt.Image nativeImage = this.nativeImage;
        if (null != nativeImage){

            g.draw(this);
            return g;
        }
        else
            throw new IllegalStateException();
    }
}