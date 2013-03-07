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
package platform.geom;

/**
 * 
 */
public class Dimension
    extends java.awt.geom.Dimension2D
    implements vector.geom.Dimension
{
    public float width, height;


    public Dimension(){
        super();
    }
    public Dimension(float width, float height){
        super();
        this.width = width;
        this.height = height;
    }


    public boolean isEmpty(){
        return (0.0f == this.width && 0.0f == this.height);
    }
    public boolean isNotEmpty(){
        return (0.0f < this.width && 0.0 < this.height);
    }
    public void floor(Dimension that){
        if (null != that){
            this.width = Math.min(this.width,that.width);
            this.height = Math.min(this.height,that.height);
        }
    }
    public void ceil(Dimension that){
        if (null != that){
            this.width = Math.max(this.width,that.width);
            this.height = Math.max(this.height,that.height);
        }
    }
    public double getWidth(){
        return this.width;
    }
    public double getHeight(){
        return this.height;
    }
    public void setSize(double width, double height){
        this.width = (float)width;
        this.height = (float)height;
    }
    public Dimension clone(){
        return (Dimension)super.clone();
    }
    protected StringBuilder toStringBuilder(){
        StringBuilder string = new StringBuilder();
        string.append(this.width);
        string.append(',');
        string.append(this.height);
        return string;
    }
    public String toString(){
        return this.toStringBuilder().toString();
    }
}
