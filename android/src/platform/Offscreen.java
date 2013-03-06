/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, John Pritchard, Syntelos
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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * {@link Output} buffer
 * 
 * @see Display
 */
public final class Offscreen
    extends Object
    implements vector.Image.Offscreen
{

    private final View observer;

    private final int width, height;

    protected Bitmap nativeImage;


    /**
     * @param component First argument to platform context (Display)
     */
    public Offscreen(View observer){
        super();
        if (null != observer){
            android.graphics.Rect ar = new android.graphics.Rect();
            observer.getDrawingRect(ar);
            final int width = (ar.right-ar.left);
            final int height = (ar.bottom-ar.top);
            if (0 < width && 0 < height){
                this.observer = observer;
                this.width = width;
                this.height = height;
                this.nativeImage = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
            }
            else
                throw new IllegalArgumentException();
        }
        else
            throw new IllegalArgumentException();
    }
    /**
     * @param observer First argument to platform context (Display)
     * @param width Pixel buffer X dimension
     * @param height Pixel buffer Y dimension
     */
    public Offscreen(View observer, int width, int height){
        super();
        if (null != observer && 0 < width && 0 < height){
            this.observer = observer;
            this.width = width;
            this.height = height;
            this.nativeImage = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
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

        this.nativeImage = null;
    }
    public Context blit(Context g){

        if (null != this.nativeImage){

            g.draw(this);
        }
        return g;
    }
    public Context create(){

        if (null == this.nativeImage){
            this.nativeImage = Bitmap.createBitmap(this.width,this.height,Bitmap.Config.ARGB_8888);
        }

        return new Context(this.observer,new Canvas(this.nativeImage));
    }
}
