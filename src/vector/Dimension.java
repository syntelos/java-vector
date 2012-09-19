/*
 * Java Vector
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

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * 
 */
public abstract class Dimension
    extends Dimension2D
{
    /**
     * 
     */
    public static class Float
        extends Dimension
    {
        public float width, height;


        public Float(){
            super();
        }
        public Float(float w, float h){
            super();
            this.width = w;
            this.height = h;
        }
        public Float(double w, double h){
            this( (float)w, (float)h);
        }
        public Float(Rectangle2D.Float r){
            this(r.width,r.height);
        }
        public Float(Rectangle2D.Double r){
            this(r.width,r.height);
        }
        public Float(Rectangle2D r){
            this(r.getWidth(),r.getHeight());
        }
        public Float(Dimension2D r){
            this(r.getWidth(),r.getHeight());
        }


        public double getWidth(){
            return this.width;
        }
        public double getHeight(){
            return this.height;
        }
        public void setSize(double w, double h){
            this.width = (float)w;
            this.height = (float)h;
        }
    }
    /**
     * 
     */
    public static class Double
        extends Dimension
    {
        public double width, height;


        public Double(){
            super();
        }
        public Double(double w, double h){
            super();
            this.width = w;
            this.height = h;
        }
        public Double(Rectangle2D.Float r){
            this(r.width,r.height);
        }
        public Double(Rectangle2D.Double r){
            this(r.width,r.height);
        }
        public Double(Rectangle2D r){
            this(r.getWidth(),r.getHeight());
        }
        public Double(Dimension2D r){
            this(r.getWidth(),r.getHeight());
        }


        public double getWidth(){
            return this.width;
        }
        public double getHeight(){
            return this.height;
        }
        public void setSize(double w, double h){
            this.width = w;
            this.height = h;
        }
    }


    protected Dimension() {
        super();
    }


    public boolean isEmpty(){
        return (0.0 == this.getWidth() && 0.0 == this.getHeight());
    }
    public boolean isNotEmpty(){
        return (0.0 < this.getWidth() && 0.0 < this.getHeight());
    }
    public void floor(Dimension2D that){
        if (null != that)
            this.setSize(Math.min(this.getWidth(),that.getWidth()),Math.min(this.getHeight(),that.getHeight()));
    }
    public void ceil(Dimension2D that){
        if (null != that)
            this.setSize(Math.max(this.getWidth(),that.getWidth()),Math.max(this.getHeight(),that.getHeight()));
    }
    public java.awt.Dimension toDimensionAWT(){
        return new java.awt.Dimension((int)this.getWidth(),(int)this.getHeight());
    }
    public java.awt.Dimension toHalfDimensionAWT(){
        return new java.awt.Dimension((int)(this.getWidth()/2.0),(int)(this.getHeight()/2.0));
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (that instanceof Dimension2D)
            return this.equals( (Dimension2D)that);
        else
            return false;
    }
    public boolean equals(Dimension2D that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return (this.getWidth() == that.getWidth() &&
                    this.getHeight() == that.getHeight());
    }
    public boolean notEquals(Dimension2D that){
        if (this == that)
            return false;
        else if (null == that)
            return true;
        else
            return (this.getWidth() != that.getWidth() ||
                    this.getHeight() != that.getHeight());
    }
}
