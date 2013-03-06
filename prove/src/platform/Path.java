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

/**
 * 
 */
public class Path
    extends Object
    implements vector.geom.Path
{

    public Path(){
        super();
    }
    public Path(Winding winding){
        super();
    }
    public Path(Shape shape){
        super();
    }
    public Path(Shape shape, Transform xform){
        super();
    }


    public void reset(){
    }
    public Winding getWinding(){

        return Winding.For(0);
    }
    /**
     * @param winding Ignore null property value
     */
    public Path setWinding(Winding winding){

        return this;
    }
    public boolean isWindingNonZero(){

        return false;
    }
    public boolean isWindingEvenOdd(){

        return false;
    }
    public Path setWindingNonZero(){

        return this;
    }
    public Path setWindingEvenOdd(){

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
    }
    public void moveTo(Point p){
    }
    public void moveTo(float x, float y){
    }
    public void lineTo(float[] operands) {
    }
    public void lineTo(Point p){
    }
    public void lineTo(float x, float y){
    }
    public void quadTo(float[] operands)
    {
    }
    public void quadTo(Point p0, Point p1){
    }
    public void quadTo(float x0, float y0, float x1, float y1)
    {
    }
    public void cubicTo(float[] operands)
    {
    }
    public void cubicTo(Point p0, Point p1, Point p2){
    }
    public void cubicTo(float x0, float y0, float x1, float y1, float x2, float y2){
    }
    public void close(){
    }
    public void set(path.Path p){
    }
    public void add(path.Path p){
    }
    public void set(Shape p){
    }
    public void add(Shape p){
    }
    public void transform(Transform t){
    }
    public Bounds getBoundsVector(){

        return new Bounds();
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
