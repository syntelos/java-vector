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

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.StringTokenizer;

/**
 * Storage class, not intermediate value, employs 32 bit floating point.
 */
public class Bounds
    extends java.awt.geom.Rectangle2D.Float
{
    public Bounds(){
        super(0,0,0,0);
    }
    public Bounds(String string){
        super(0,0,0,0);
        if (null != string){
            StringTokenizer strtok = new StringTokenizer(string,"][)(,");
            switch (strtok.countTokens()){
            case 0:
                break;
            case 4:
                try {
                    final float x = java.lang.Float.parseFloat(strtok.nextToken());

                    final float y = java.lang.Float.parseFloat(strtok.nextToken());

                    final float w = java.lang.Float.parseFloat(strtok.nextToken());

                    final float h = java.lang.Float.parseFloat(strtok.nextToken());

                    this.setFrame(x,y,w,h);
                }
                catch (RuntimeException exc){
                    throw new IllegalArgumentException(string,exc);
                }
                break;
            default:
                throw new IllegalArgumentException(string);
            }
        }
    }
    public Bounds(float x, float y, float w, float h){
        super(x,y,w,h);
    }
    public Bounds(float w, float h){
        super(0f,0f,w,h);
    }
    public Bounds(double x, double y, double w, double h){
        super((float)x,(float)y,(float)w,(float)h);
    }
    public Bounds(double w, double h){
        super(0f,0f,(float)w,(float)h);
    }
    public Bounds(java.awt.geom.RectangularShape rect){
        this(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
    }
    public Bounds(java.awt.Shape shape){
        this(shape.getBounds2D());
    }
    public Bounds(Component c){
        this(c.getBoundsVector());
    }


    public void init(){

        this.setFrame(0,0,0,0);
    }
    /**
     * @return Objective scale to this from that
     */
    public Transform scaleFrom(java.awt.geom.RectangularShape that){
        final double sx = (this.width/that.getWidth());
        final double sy = (this.height/that.getHeight());

        return Transform.getScaleInstance(sx,sy);
    }
    /**
     * @return Objective scale to that from this
     */
    public Transform scaleTo(java.awt.geom.RectangularShape that){
        final double sx = (that.getWidth()/this.width);
        final double sy = (that.getHeight()/this.height);

        return Transform.getScaleInstance(sx,sy);
    }
    public RoundRectangle2D.Float round(float arc){

        return new RoundRectangle2D.Float(this.x,this.y,this.width,this.height,arc,arc);
    }
    /**
     * @return Interior midpoint relative to box location: width
     * divided by two, height divided by two
     */
    public Point2D.Float midpoint(){
        float x = (this.width/2.0f);
        float y = (this.height/2.0f);
        return new Point2D.Float(x,y);
    }
    public Bounds apply(Align align, Bounds parent){

        if (null != align)
            return align.apply(this,parent);
        else
            return this;
    }
    /**
     * @param g Graphics context
     * @return The argument graphics context, not a clone or copy
     */
    public Graphics2D clip(Graphics2D g){

        g.translate(this.x,this.y);
        g.clipRect(0,0,(int)Math.ceil(this.width+1),(int)Math.ceil(this.height+1));
        return g;
    }
    public Bounds clone(){
        return (Bounds)super.clone();
    }
    public String toString(){
        StringBuilder string = new StringBuilder();
        string.append('(');
        string.append(this.x);
        string.append(',');
        string.append(this.y);
        string.append(',');
        string.append(this.width);
        string.append(',');
        string.append(this.height);
        string.append(')');
        return string.toString();
    }

}
