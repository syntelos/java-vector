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

import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.StringTokenizer;

/**
 * 
 */
public class Font
    extends Paint
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


    public final static String DefaultFontFamily = DefaultFamily.Monospaced.name();

    public final static String DefaultFontName = DefaultFontFamily+'-'+(int)SZ;

    public final static Font Default = new Font(DefaultFontFamily,(int)SZ);


    public final float ascent, descent, height, em;
    public final float prop, spacing, leading;

    /* package */ final String name;


    public Font(String code){
        this(Ctor(code));
    }
    private Font(String[] code){
        this(code[0],code[1],code[2]);
    }
    private Font(String name, String style, String size){
        this(name,(null != style)?(vector.font.Font.Style.For(style)):(null),(null != size)?(Integer.parseInt(size)):(11));
    }
    public Font(int size){
        this(DefaultFontFamily,vector.font.Font.Style.PLAIN,size);
    }
    public Font(String name, int psize){
        this(name,vector.font.Font.Style.PLAIN,psize);
    }
    public Font(String name, vector.font.Font.Style style, int psize){
        super();
        this.name = name;
        this.setAntiAlias(true);

        if (null != name){

            if (null != style)
                this.setTypeface(Typeface.create(name,style.ordinal()));
            else 
                this.setTypeface(Typeface.create(name,0));
        }

        this.setTextSize(Frame.Points2Pixels(psize));

        final android.graphics.Paint.FontMetrics metrics = this.getFontMetrics();

        this.ascent = Math.abs(metrics.ascent);
        this.descent = metrics.descent;
        this.height = (this.ascent + this.descent);
        this.em = (int)this.measureText(" ",0,1);
        this.prop = (this.height/SZ);
        this.spacing = (this.prop*PW);
        this.leading = (this.prop*PH);
    }
    public Font(Font font, float dy){
        super(font);
        this.name = font.name;
        this.setTextSize(this.getTextSize()+dy);

        final android.graphics.Paint.FontMetrics metrics = this.getFontMetrics();

        this.ascent = Math.abs(metrics.ascent);
        this.descent = metrics.descent;
        this.height = (this.ascent + this.descent);
        this.em = (int)this.measureText(" ",0,1);
        this.prop = (this.height/SZ);
        this.spacing = (this.prop*PW);
        this.leading = (this.prop*PH);
    }
    public Font(android.graphics.Paint paint){
        super(paint);
        if (paint instanceof Font)
            this.name = ((Font)paint).name;
        else
            this.name = null;

        this.setAntiAlias(true);

        final android.graphics.Paint.FontMetrics metrics = this.getFontMetrics();

        this.ascent = Math.abs(metrics.ascent);
        this.descent = metrics.descent;
        this.height = (this.ascent + this.descent);
        this.em = this.measureText(" ",0,1);
        this.prop = (this.height/SZ);
        this.spacing = (this.prop*PW);
        this.leading = (this.prop*PH);
    }
    public Font(android.graphics.Paint paint, Font font){
        super(paint);
        if (paint instanceof Font)
            this.name = ((Font)paint).name;
        else
            this.name = null;
        this.setAntiAlias(true);

        if (null != font){
            this.setTypeface(font.getTypeface());
            this.setTextSize(font.getTextSize());

            this.ascent = font.ascent;
            this.descent = font.descent;
            this.height = font.height;
            this.em = font.em;
            this.prop = font.prop;
            this.spacing = font.spacing;
            this.leading = font.leading;
        }
        else {
            final android.graphics.Paint.FontMetrics metrics = this.getFontMetrics();

            this.ascent = Math.abs(metrics.ascent);
            this.descent = metrics.descent;
            this.height = (this.ascent + this.descent);
            this.em = this.measureText(" ",0,1);
            this.prop = (this.height/SZ);
            this.spacing = (this.prop*PW);
            this.leading = (this.prop*PH);
        }
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
    public boolean isPlain(){
        return (0 == super.getTypeface().getStyle());
    }
    public boolean isBold(){
        return super.getTypeface().isBold();
    }
    public boolean isItalic(){
        return super.getTypeface().isItalic();
    }
    public String getFamily(){
        return null;
    }
    public String getName(){
        return this.name;
    }
    public int getSize(){

        return Frame.Pixels2Points(this.getTextSize());
    }
    public final Font decrementSize(){
        return this.decrementSize(2);
    }
    public final Font incrementSize(){
        return this.incrementSize(2);
    }
    public final Font decrementSize(float dy){

        return new Font(this,-Math.abs(dy));
    }
    public final Font incrementSize(float dy){

        return new Font(this,+Math.abs(dy));
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


    private final static String[] Ctor(String string){
        String[] re = new String[3];
        if (null != string){
            final StringTokenizer strtok = new StringTokenizer(string,"-");
            final int count = strtok.countTokens();
            switch(count){
            case 0:
                break;
            case 1:
                re[0] = strtok.nextToken();
                break;
            case 2:
                re[0] = strtok.nextToken();
                re[2] = strtok.nextToken();
                break;
            case 3:
                re[0] = strtok.nextToken();
                re[1] = strtok.nextToken();
                re[2] = strtok.nextToken();
                break;
            default:
                re[0] = strtok.nextToken();
                final int cat = (count-3);
                for (int cc = 0; cc < cat; cc++){
                    re[0] += ('-'+strtok.nextToken());
                }
                re[1] = strtok.nextToken();
                re[2] = strtok.nextToken();
                break;
            }
        }
        return re;
    }
}
