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

import path.Op;
import path.Operand;
import path.Winding;

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
    public Path(Winding winding){
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


    public Winding getWinding(){

        return Winding.For(super.getWindingRule());
    }
    /**
     * @param winding Ignore null property value
     */
    public Path setWinding(Winding winding){

        if (null != winding)
            super.setWindingRule(winding.ordinal());

        return this;
    }
    public boolean isWindingNonZero(){

        return (Winding.NonZero == Winding.For(super.getWindingRule()));
    }
    public boolean isWindingEvenOdd(){

        return (Winding.EvenOdd == Winding.For(super.getWindingRule()));
    }
    public Path setWindingNonZero(){
        super.setWindingRule(Winding.NonZero.ordinal());
        return this;
    }
    public Path setWindingEvenOdd(){
        super.setWindingRule(Winding.EvenOdd.ordinal());
        return this;
    }
    public void add(Op op, float[] operands){
        switch(op){
        case MoveTo:
            this.moveTo(operands);
            break;
        case LineTo:
            this.lineTo(operands);
            break;
        case QuadTo:
            this.quadTo(operands);
            break;
        case CubicTo:
            this.cubicTo(operands);
            break;
        case Close:
            this.close();
            break;
        }
    }
    /**
     * Not implemented for an alternative use of the {@link
     * path.Operand path Operand} ctor in this package.
     */
    public float[] getVerticesPath(int opx, Op op, float[] vertices){

        /*
         * [TODO] This function is called by the second Operand ctor when used by Fv3.
         * It should be defined sometime in future.
         */
        final int vertices_len;

        if (null != vertices)
            vertices_len = vertices.length;
        else
            vertices_len = 0;

        throw new java.lang.UnsupportedOperationException(String.format("Op: %s, Op-Index: %d, Vertices-Len: %d",op,opx,vertices_len));
    }
    public void moveTo(float[] operands) {

        super.moveTo(operands[0],operands[1]);
    }
    public void moveTo(Point p){
        super.moveTo(p.x,p.y);
    }
    public void lineTo(float[] operands) {

        super.lineTo(operands[0],operands[1]);
    }
    public void lineTo(Point p){
        super.lineTo(p.x,p.y);
    }
    public void quadTo(float[] operands)
    {
        switch(operands.length){
        case 4:
            super.quadTo(operands[0],operands[1],operands[2],operands[3]);
            break;
        case 6:
            super.quadTo(operands[0],operands[1],operands[3],operands[4]);
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(operands.length));
        }
    }
    public void quadTo(Point p0, Point p1){
        super.quadTo(p0.x,p0.y,p1.x,p1.y);
    }
    public void cubicTo(float[] operands)
    {
        switch(operands.length){
        case 6:
            super.curveTo(operands[0],operands[1],operands[2],operands[3],operands[4],operands[5]);
            break;
        case 9:
            super.curveTo(operands[0],operands[1],operands[3],operands[4],operands[6],operands[7]);
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(operands.length));
        }
    }
    public void cubicTo(Point p0, Point p1, Point p2){
        super.curveTo(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
    }
    public void cubicTo(float x0, float y0, float x1, float y1, float x2, float y2){

        super.curveTo(x0,y0,x1,y1,x2,y2);
    }
    public void close(){
        super.closePath();
    }
    public void set(path.Path p){
        super.reset();
        this.add(p);
    }
    public void add(path.Path p){

        for (Operand operand: p.toPathIterable()){
            switch(operand.op){
            case MoveTo:
                this.moveTo(operand.vertices);
                break;
            case LineTo:
                this.lineTo(operand.vertices);
                break;
            case QuadTo:
                this.quadTo(operand.vertices);
                break;
            case CubicTo:
                this.cubicTo(operand.vertices);
                break;
            case Close:
                this.close();
                break;
            }
        }
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
    public final Path apply(String pexpr){
        return this.apply(new path.Parser(pexpr));
    }
    public final Path apply(path.Parser p){
        return p.apply(this);
    }
    public java.lang.Iterable<Operand> toPathIterable(){

        return Path.CopyTo(this).toPathIterable();
    }
    public java.util.Iterator<Operand> toPathIterator(){

        return Path.CopyTo(this).toPathIterator();
    }



    /**
     * Convert Shape PathIterator to Attribute "d" (of SVG Element
     * Path) format.
     * 
     * @see #CopyTo(Shape)
     */
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
    /**
     * Consume Shape for PathIterator, produce new Path for data
     * export.  
     * 
     * @see #ToString(Shape)
     */
    public final static fv3.math.Path CopyTo(Shape shape){

        fv3.math.Path path = new fv3.math.Path();

        PathIterator p = shape.getPathIterator(null);
        double[] s = new double[6];
        while (!p.isDone()){

            switch(p.currentSegment(s)){
            case PathIterator.SEG_MOVETO:

                path.moveTo((float)s[0],(float)s[1]);
                break;
            case PathIterator.SEG_LINETO:

                path.lineTo((float)s[0],(float)s[1]);
                break;
            case PathIterator.SEG_QUADTO:

                path.quadTo((float)s[0],(float)s[1],(float)s[2],(float)s[3]);
                break;
            case PathIterator.SEG_CUBICTO:

                path.cubicTo((float)s[0],(float)s[1],(float)s[2],(float)s[3],(float)s[4],(float)s[5]);
                break;
            case PathIterator.SEG_CLOSE:

                path.close();
                break;
            }
            p.next();
        }
        return path;
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

    public final static boolean EqPoint(Point p, double x, double y){

        return EqPoint(p.x,p.y,x,y,3);
    }
    public final static boolean EqPoint(Point p, double x, double y, double r){

        return EqPoint(p.x,p.y,x,y,r);
    }
    public final static boolean EqPoint(double xp, double yp, double xc, double yc, double r){

        return (xc > (xp-r) && xc < (xp+r) && yc > (yp-r) && yc < (yp+r));
    }

}
