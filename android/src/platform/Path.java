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

import platform.geom.Point;

import vector.Bounds;

import path.Op;
import path.Operand;
import path.Winding;

import android.graphics.Path.FillType;

/**
 * 
 */
public class Path
    extends android.graphics.Path
    implements vector.geom.Path
{
    /* package */ final static FillType Winding2FillType(Winding w){
        if (null == w)
            return FillType.WINDING;
        else {
            switch(w){
            case EvenOdd:
                return FillType.EVEN_ODD;
            default:
                return FillType.WINDING;
            }
        }
    }
    /* package */ final static Winding FillType2Winding(FillType ft){
        if (null == ft)
            return Winding.Default;
        else {
            switch(ft){
            case EVEN_ODD:
                return Winding.EvenOdd;
            default:
                return Winding.Default;
            }
        }
    }


    public Path(){
        super();
    }
    public Path(Winding winding){
        super();
        this.setWinding(winding);
    }
    public Path(Shape shape){
        super();
        if (null != shape){

            super.set(shape.toPath());
        }
    }
    public Path(Shape shape, Transform xform){
        super();
        if (null != shape){

            super.set(shape.toPath());

            if (null != xform){

                this.transform(xform);
            }
        }
    }



    public Winding getWinding(){

        final FillType fillType = this.getFillType();
        if (null == fillType)

            return Winding.Default;
        else 
            return Path.FillType2Winding(fillType);
    }
    /**
     * @param winding Ignore null property value
     */
    public Path setWinding(Winding winding){
        if (null != winding){

            super.setFillType(Path.Winding2FillType(winding));
        }
        return this;
    }
    public boolean isWindingNonZero(){

        return (Winding.NonZero == this.getWinding());
    }
    public boolean isWindingEvenOdd(){

        return (Winding.EvenOdd == this.getWinding());
    }
    public Path setWindingNonZero(){

        return this.setWinding(Winding.NonZero);
    }
    public Path setWindingEvenOdd(){

        return this.setWinding(Winding.EvenOdd);
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
            super.cubicTo(operands[0],operands[1],operands[2],operands[3],operands[4],operands[5]);
            break;
        case 9:
            super.cubicTo(operands[0],operands[1],operands[3],operands[4],operands[6],operands[7]);
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(operands.length));
        }
    }
    public void cubicTo(Point p0, Point p1, Point p2){
        super.cubicTo(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
    }
    public void set(path.Path p){
        if (p instanceof android.graphics.Path)
            super.set( (android.graphics.Path)p);
        else if (null != p)
            throw new IllegalArgumentException(p.getClass().getName());
    }
    public void add(path.Path p){
        if (p instanceof android.graphics.Path)
            super.addPath( (android.graphics.Path)p);
        else if (null != p)
            throw new IllegalArgumentException(p.getClass().getName());
    }
    public void set(Shape p){
        if (p instanceof android.graphics.Path)
            super.set( (android.graphics.Path)p);
        else if (null != p){
            this.set( (path.Path)p.toPath());
        }
    }
    public void add(Shape p){
        if (p instanceof android.graphics.Path)
            super.addPath( (android.graphics.Path)p);
        else if (null != p){
            this.addPath(p.toPath());
        }
    }
    public void transform(Transform t){

        super.transform(t,null);
    }
    public Bounds getBoundsVector(){

        final android.graphics.RectF b = new android.graphics.RectF();

        super.computeBounds(b,true);

        return new Bounds(new platform.geom.Rectangle(b));
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



    public final static String ToString(Shape shape){
        StringBuilder string = new StringBuilder();
        return string.toString();
    }
    public final static fv3.math.Path CopyTo(Shape shape){

        fv3.math.Path path = new fv3.math.Path();

        return path;
    }
    public final static boolean IsOpen(Shape shape){
        return false;
    }
    public final static boolean IsClosed(Shape shape){
        boolean closed = false;
        return closed;
    }
    public final static boolean IsCloser(Shape shape, Point pt){
        boolean closer = false;
        return closer;
    }
    public final static boolean IsNotClosed(Shape shape){
        return (!IsClosed(shape));
    }
    public final static int IndexOf(Shape shape, Point pt){

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
