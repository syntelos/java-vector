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

import vector.Bounds;

import java.awt.geom.PathIterator;

/**
 * 
 */
public class Path
    extends java.awt.geom.Path2D.Float
    implements vector.geom.Path
{

    public Path(){
        super();
    }
    public Path(vector.Path.Winding winding){
        super(winding.ordinal());
    }
    public Path(Shape shape){
        super(shape);
    }
    public Path(Shape shape, Transform xform){
        super(shape,xform);
    }
    public Path(java.awt.Shape shape){
        super(shape);
    }


    public void moveTo(Point p){
        super.moveTo(p.x,p.y);
    }
    public void lineTo(Point p){
        super.lineTo(p.x,p.y);
    }
    public void quadTo(Point p0, Point p1){
        super.quadTo(p0.x,p0.y,p1.x,p1.y);
    }
    public void curveTo(Point p0, Point p1, Point p2){
        super.curveTo(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
    }
    public void close(){
        super.closePath();
    }
    public void set(Path p){
        super.reset();
        super.append(p,false);
    }
    public void add(Path p){
        super.append(p,false);
    }
    public void set(Shape p){
        super.reset();
        super.append(p.toPath(),false);
    }
    public void add(Shape p){
        super.append(p.toPath(),false);
    }
    public void transform(Transform t){
        super.transform(t);
    }
    public Bounds getBoundsVector(){
        java.awt.geom.Rectangle2D b = super.getBounds2D();
        return new Bounds(b.getX(),b.getY(),b.getWidth(),b.getHeight());
    }
    public Path toPath(){
        return this;
    }
    public String toString(){
        return Path.ToString(this);
    }

    public final static String ToString(Shape shape){
        StringBuilder string = new StringBuilder();
        PathIterator p = shape.getPathIterator(null);
        double[] s = new double[6];
        while (!p.isDone()){

            switch(p.currentSegment(s)){
            case PathIterator.SEG_MOVETO:
                string.append('M');
                string.append(String.format("%f",s[0]));
                string.append(',');
                string.append(String.format("%f",s[1]));
                break;
            case PathIterator.SEG_LINETO:
                string.append('L');
                string.append(String.format("%f",s[0]));
                string.append(',');
                string.append(String.format("%f",s[1]));
                break;
            case PathIterator.SEG_QUADTO:
                string.append('Q');
                string.append(String.format("%f",s[0]));
                string.append(',');
                string.append(String.format("%f",s[1]));
                string.append(',');
                string.append(String.format("%f",s[2]));
                string.append(',');
                string.append(String.format("%f",s[3]));
                break;
            case PathIterator.SEG_CUBICTO:
                string.append('C');
                string.append(String.format("%f",s[0]));
                string.append(',');
                string.append(String.format("%f",s[1]));
                string.append(',');
                string.append(String.format("%f",s[2]));
                string.append(',');
                string.append(String.format("%f",s[3]));
                string.append(',');
                string.append(String.format("%f",s[4]));
                string.append(',');
                string.append(String.format("%f",s[5]));
                break;
            case PathIterator.SEG_CLOSE:
                string.append('Z');
                break;
            }
            p.next();
        }
        return string.toString();
    }
    public final static boolean IsOpen(Shape shape){
        boolean closed = false;

        PathIterator p = shape.getPathIterator(null);
        double[] s = new double[6];
        if (!p.isDone()){
            switch(p.currentSegment(s)){
            case PathIterator.SEG_MOVETO:
                return true;
            }
        }
        return false;
    }
    public final static boolean IsClosed(Shape shape){
        boolean closed = false;

        PathIterator p = shape.getPathIterator(null);
        double[] s = new double[6];
        while (!p.isDone()){
            switch(p.currentSegment(s)){
            case PathIterator.SEG_MOVETO:
                closed = false;
                break;
            case PathIterator.SEG_LINETO:
                closed = false;
                break;
            case PathIterator.SEG_QUADTO:
                closed = false;
                break;
            case PathIterator.SEG_CUBICTO:
                closed = false;
                break;
            case PathIterator.SEG_CLOSE:
                closed = true;
                break;
            }
            p.next();
        }
        return closed;
    }
    public final static boolean IsCloser(Shape shape, Point pt){
        boolean closer = false;

        PathIterator p = shape.getPathIterator(null);
        double[] s = new double[6];
        while (!p.isDone()){
            switch(p.currentSegment(s)){
            case PathIterator.SEG_MOVETO:
                closer = Path.EqPoint(pt,s[0],s[1],8);
                break;
            case PathIterator.SEG_LINETO:
                break;
            case PathIterator.SEG_QUADTO:
                break;
            case PathIterator.SEG_CUBICTO:
                break;
            case PathIterator.SEG_CLOSE:
                break;
            }
            p.next();
        }
        return closer;
    }
    public final static boolean IsNotClosed(Shape shape){
        return (!IsClosed(shape));
    }
    public final static int IndexOf(Shape shape, Point pt){
        int index = 0;
        PathIterator p = shape.getPathIterator(null);
        double[] s = new double[6];
        while (!p.isDone()){

            switch(p.currentSegment(s)){
            case PathIterator.SEG_MOVETO:
                if (Path.EqPoint(pt,s[0],s[1]))
                    return index;
                else
                    index++;
                break;
            case PathIterator.SEG_LINETO:
                if (Path.EqPoint(pt,s[0],s[1]))
                    return index;
                else
                    index++;
                break;
            case PathIterator.SEG_QUADTO:
                if (Path.EqPoint(pt,s[0],s[1]))
                    return index;
                else {
                    index++;
                    if (Path.EqPoint(pt,s[2],s[3]))
                        return index;
                    else
                        index++;
                }
                break;
            case PathIterator.SEG_CUBICTO:
                if (Path.EqPoint(pt,s[0],s[1]))
                    return index;
                else {
                    index++;
                    if (Path.EqPoint(pt,s[2],s[3]))
                        return index;
                    else {
                        index++;
                        if (Path.EqPoint(pt,s[4],s[5]))
                            return index;
                        else 
                            index++;
                    }
                }
                break;
            case PathIterator.SEG_CLOSE:
                break;
            }
            p.next();
        }
        return -1;
    }
    public final static platform.Path LineTo(platform.Path shape, Point a){
        shape.lineTo(a.getX(),a.getY());
        return shape;
    }
    public final static platform.Path MoveTo(platform.Path shape, Point a){
        shape.moveTo(a.getX(),a.getY());
        return shape;
    }
    public final static platform.Path QuadTo(platform.Path shape, Point a, Point b){
        shape.quadTo(a.getX(),a.getY(),b.getX(),b.getY());
        return shape;
    }
    public final static platform.Path CurveTo(platform.Path shape, Point a, Point b, Point c){
        shape.curveTo(a.getX(),a.getY(),b.getX(),b.getY(),c.getX(),c.getY());
        return shape;
    }
    public final static boolean EqPoint(Point p, double x, double y){

        return EqPoint(p.getX(),p.getY(),x,y,3);
    }
    public final static boolean EqPoint(Point p, double x, double y, double r){

        return EqPoint(p.getX(),p.getY(),x,y,r);
    }
    public final static boolean EqPoint(double xp, double yp, double xc, double yc, double r){

        return (xc > (xp-r) && xc < (xp+r) && yc > (yp-r) && yc < (yp+r));
    }

}
