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

import vector.Bounds;
import vector.Padding;
import vector.font.GlyphVector;

import platform.geom.Point;

import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;


/**
 * Font primitive.
 */
public class Font
    extends java.awt.Font
    implements vector.font.Font
{

    public final static Font decode(String string){

        return new Font(string);
    }


    /**
     * Default size and padding proportions
     */
    public final static float SZ = 11f;
    public final static float PW = 4f;
    public final static float PH = 1f;

    protected final static Toolkit TK = Toolkit.getDefaultToolkit();


    public final static String DefaultFontFamily = DefaultFamily.Monospaced.name();

    public final static String DefaultFontName = DefaultFontFamily+'-'+(int)SZ;

    public final static Font Default = new Font(DefaultFontFamily,(int)SZ);


    /* package */ public final FontRenderContext frc;
    /* package */ final FontMetrics metrics;
    public final float ascent, descent, height, em;
    public final float prop, spacing, leading;


    public Font(String code){
        this(java.awt.Font.decode(code));
    }
    public Font(int size){
        this(DefaultFontFamily,Style.PLAIN,size);
    }
    public Font(String name, int size){
        this(name,Style.PLAIN,size);
    }
    public Font(String name, Style style, int size){
        super(name,style.ordinal(),size);

        this.metrics = TK.getFontMetrics(this);
        this.ascent = this.metrics.getAscent();
        this.descent = this.metrics.getDescent();
        this.height = (this.ascent + this.descent);
        this.em = this.metrics.charWidth(0x20);
        this.prop = this.getSize2D()/SZ;
        this.spacing = (prop*PW);
        this.leading = (prop*PH);

        FontRenderContext frc = this.metrics.getFontRenderContext();
        if (frc.isAntiAliased())
            this.frc = frc;
        else
            this.frc = new FontRenderContext(frc.getTransform(),true,frc.usesFractionalMetrics());
    }
    public Font(java.awt.Font font){
        super(font);

        this.metrics = TK.getFontMetrics(this);
        this.ascent = this.metrics.getAscent();
        this.descent = this.metrics.getDescent();
        this.height = (this.ascent + this.descent);
        this.em = this.metrics.charWidth(0x20);
        this.prop = this.getSize2D()/SZ;
        this.spacing = (prop*PW);
        this.leading = (prop*PH);

        FontRenderContext frc = this.metrics.getFontRenderContext();
        if (frc.isAntiAliased())
            this.frc = frc;
        else
            this.frc = new FontRenderContext(frc.getTransform(),true,frc.usesFractionalMetrics());
    }


    public float getEm(){
        return this.em;
    }
    public float getAscent(){
        return this.ascent;
    }
    public float getDescent(){
        return this.descent;
    }
    public float getLeading(){
        return this.leading;
    }
    public float getSpacing(){
        return this.spacing;
    }
    public float getHeight(){
        return this.height;
    }
    /**
     * @param padding Text padding
     * 
     * @return Glyph text baseline origin
     */
    public Point queryBaseline(Padding padding){
        if (null != padding){
            final float x = padding.left;
            final float y = (this.ascent + padding.top);

            return new Point(x,y);
        }
        else
            return new Point(0f,this.ascent);
    }
    /**
     * Glyph baseline position coordinates in shape coordinate space.
     * 
     * <pre>
     * ix = (idx*2);
     * iy = (ix+1);
     * </pre>
     * 
     * @param padding Text padding
     * @param vector Text vector
     * @param count Number of characters in text vector
     * 
     * @return Glyph baseline coordinates for characters in text
     * vector as an array of coordinates in order
     * <i>{Xo,Yo,...,Xn,Yn}</i>, or an array of length zero
     */
    public float[] queryBaselinePositions(Padding padding, GlyphVector vector){
        if (null != vector){

            final Point baseline = this.queryBaseline(padding);

            return vector.queryPositions(baseline);
        }
        else
            return new float[0];
    }
    public final Padding defaultPadding(){

        return new Padding( this.spacing, this.leading);
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
    public final GlyphVector createGlyphVector(String string){

        return new platform.font.GlyphVector( this, string);
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
