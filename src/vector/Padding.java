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

import vector.geom.RectangularShape;

import platform.Font;

import java.util.StringTokenizer;

/**
 * Internal padding is contained within the boundaries of a {@link
 * Component}.  External margin is not contained within the boundaries
 * of a {@link Component}, it is external to the bounding box of a
 * {@link Component}.
 * 
 * @see Component
 * @see Text
 */
public class Padding
    extends Object
    implements Cloneable
{

    public final static Padding Default(){
        return new Padding(4,4);
    }
    public final static Padding Nil(){
        return new Padding(0,0);
    }


    public float left, right, top, bottom;


    public Padding(){
        super();
    }
    public Padding(float p){
        this(p,p);
    }
    public Padding(float w, float h){
        super();
        this.set(w,h);
    }
    public Padding(float left, float right, float top, float bottom){
        super();
        this.set(left, right, top, bottom);
    }
    public Padding(double left, double right, double top, double bottom){
        super();
        this.set(left, right, top, bottom);
    }
    public Padding(String code){
        super();
        StringTokenizer strtok = new StringTokenizer(code,")(,");
        switch(strtok.countTokens()){
        case 2:{
            final float w = Float.parseFloat(strtok.nextToken());
            final float h = Float.parseFloat(strtok.nextToken());
            this.set(w,h);
            break;
        }
        case 4:{
            final float l = Float.parseFloat(strtok.nextToken());
            final float r = Float.parseFloat(strtok.nextToken());
            final float t = Float.parseFloat(strtok.nextToken());
            final float b = Float.parseFloat(strtok.nextToken());
            this.set(l,r,t,b);
            break;
        }
        default:
            throw new IllegalArgumentException(code);
        }
    }


    public Padding init(){

        this.left = 0f;
        this.right = 0f;
        this.top = 0f;
        this.bottom = 0f;
        return this;
    }
    public float getWidth(){
        return (this.left + this.right);
    }
    public float getHeight(){
        return (this.top + this.bottom);
    }
    public boolean isEmpty(){
        return (0.0f == this.getWidth() && 0.0f == this.getHeight());
    }
    public boolean isNotEmpty(){
        return (0.0f != this.getWidth() && 0.0f != this.getHeight());
    }
    public Padding set(RectangularShape rect){

        return this.set(rect.getX(), 0.0, rect.getY(), 0.0);
    }
    public Padding set(Font font){

        return this.set(font.defaultPadding());
    }
    public Padding set(Padding p){

        return this.set(p.left, p.right, p.top, p.bottom);
    }
    public Padding set(float left, float right, float top, float bottom){
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        return this;
    }
    public Padding set(double left, double right, double top, double bottom){
        this.left = (float)left;
        this.right = (float)right;
        this.top = (float)top;
        this.bottom = (float)bottom;
        return this;
    }
    public Padding set(float w, float h){
        float w2 = (w/2.0f);
        float h2 = (h/2.0f);
        this.left = w2;
        this.right = w2;
        this.top = h2;
        this.bottom = h2;
        return this;
    }
    public final Bounds boundingBox(Font font, int rows, int cols){
        final float x1 = this.left;
        final float y1 = this.top;
        final float x2 = (this.getWidth()+(font.em * cols));
        final float y2 = (y1+(font.height*rows));
            
        return new Bounds(x1,y1,(x2-x1),(y2-y1));
    }
    public final Bounds boundingBox(Font font, int rows, float width){
        final float x1 = this.left;
        final float y1 = this.top;
        final float x2 = (this.getWidth()+width);
        final float y2 = (y1+(font.height*rows));
            
        return new Bounds(x1,y1,(x2-x1),(y2-y1));
    }
    public Padding clone(){
        try {
            return (Padding)super.clone();
        }
        catch (CloneNotSupportedException exc){

            throw new InternalError();
        }
    }
    public StringBuilder toStringBuilder(){
        StringBuilder string = new StringBuilder();
        string.append('(');
        string.append(this.left);
        string.append(',');
        string.append(this.right);
        string.append(',');
        string.append(this.top);
        string.append(',');
        string.append(this.bottom);
        string.append(')');
        return string;
    }
    public final String toString(){
        return this.toStringBuilder().toString();
    }
}
