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

import vector.Bounds;
import vector.Padding;

import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

/**
 * Font primitive.
 */
public class Font
    extends java.awt.Font
{


    /**
     * Default size and padding proportions
     */
    public final static float SZ = 11f;
    public final static float PW = 4f;
    public final static float PH = 1f;

    public final static String DefaultFontName = "monospaced-11";

    protected final static Toolkit TK = Toolkit.getDefaultToolkit();



    public final FontRenderContext frc;
    public final FontMetrics metrics;
    public final int ascent, descent, height, em;


    public Font(String code){
        this(java.awt.Font.decode(code));
    }
    public Font(java.awt.Font font){
        super(font);

        this.metrics = TK.getFontMetrics(this);
        this.ascent = this.metrics.getAscent();
        this.descent = this.metrics.getDescent();
        this.height = (this.ascent + this.descent);
        this.em = this.metrics.charWidth(0x20);
        FontRenderContext frc = this.metrics.getFontRenderContext();
        if (frc.isAntiAliased())
            this.frc = frc;
        else
            this.frc = new FontRenderContext(frc.getTransform(),true,frc.usesFractionalMetrics());
    }


    public final Padding defaultPadding(){
        final float prop = this.getSize2D()/SZ;
        return new Padding( prop*PW, prop*PH);
    }
    public final Bounds boundingBox(Padding padding, int rows, int cols){
        final float x1 = padding.left;
        final float y1 = padding.top;
        final float x2 = (padding.getWidth()+(this.em * cols));
        final float y2 = (y1+(this.height*rows));
            
        return new Bounds(x1,y1,(x2-x1),(y2-y1));
    }
    public final Bounds boundingBox(Padding padding, int rows, float width){
        final float x1 = padding.left;
        final float y1 = padding.top;
        final float x2 = (padding.getWidth()+width);
        final float y2 = (y1+(this.height*rows));
            
        return new Bounds(x1,y1,(x2-x1),(y2-y1));
    }
    public final Font decrementSize(){
        return this.decrementSize(2);
    }
    public final Font incrementSize(){
        return this.incrementSize(2);
    }
    public final Font decrementSize(float dy){

        float size = this.getSize2D()-dy;

        if (6 <= size)
            return new Font(this.deriveFont(size));
        else
            return this;
    }
    public final Font incrementSize(float dy){

        float size = this.getSize2D()+dy;

        return new Font(this.deriveFont(size));
    }
    public final GlyphVector createGlyphVector(Object string){

        return this.createGlyphVector(this.frc,string.toString());
    }
    public final float em(float n){

        return (n*this.em);
    }
    public final String getStyleString(){

        if (this.isPlain())
            return "plain";

        else if (this.isBold()){

            if (this.isItalic())
                return "bolditalic";
            else
                return "bold";
        }
        else 
            return "italic";
    }
    public final String toString(){
        StringBuilder string = new StringBuilder();
        string.append(this.getName());
        string.append('-');
        if (!this.isPlain()){
            string.append(this.getStyleString());
            string.append('-');
        }
        string.append(this.getSize());
        return string.toString();
    }
}
