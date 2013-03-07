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

import vector.Bounds;
import vector.Context;
import vector.geom.RectangularShape;

import platform.geom.Point;

import java.util.StringTokenizer;

/**
 * Modifications to an existing matrix need to employ methods defined
 * here, or need to add (overriding) method definitions here.
 */
public class Transform
    extends Object
{
    public static Transform getTranslateInstance(double tx, double ty){
        Transform t = new Transform();
        t.setToTranslation(tx,ty);
        return t;
    }
    public static Transform getRotateInstance(double theta){
        Transform t = new Transform();
        t.setToRotation(theta);
        return t;
    }
    public static Transform getRotateInstance(double theta, double cx, double cy){
        Transform t = new Transform();
        t.setToRotation(theta,cx,cy);
        return t;
    }
    public static Transform getRotateInstance(double rx, double ry){
        Transform t = new Transform();
        t.setToRotation(rx,ry);
        return t;
    }
    public static Transform getRotateInstance(double rx, double ry, double cx, double cy){
        Transform t = new Transform();
        t.setToRotation(rx,ry,cx,cy);
        return t;
    }
    public static Transform getScaleInstance(double sx, double sy){
        Transform t = new Transform();
        t.setToScale(sx,sy);
        return t;
    }
    public static Transform getShearInstance(double shx, double shy){
        Transform t = new Transform();
        t.setToShear(shx,shy);
        return t;
    }


    private Transform inverse;


    public Transform(){
        super();
    }
    public Transform(String string){
        super();
    }
    public Transform(Transform t){
        super();
    }


    public void init(){

        this.inverse = null;

        this.setToIdentity();
    }

    public void getMatrix(double[] m){
    }
    public double getDeterminant(){
        return 0;
    }
    public double getScaleX(){
        return 0;
    }
    public double getScaleY(){
        return 0;
    }
    public double getShearX(){
        return 0;
    }
    public double getShearY(){
        return 0;
    }
    public double getTranslateX(){
        return 0;
    }
    public double getTranslateY(){
        return 0;
    }
    public void translate(double tx, double ty){
    }
    public void rotate(double theta){
    }
    public void rotate(double theta, double anchorx, double anchory){
    }
    public void rotate(double vecx, double vecy){
    }
    public void scale(double sx, double sy){
    }
    public void shear(double shx, double shy){
    }
    public void setToIdentity(){
    }
    public void setToTranslation(double tx, double ty){
    }
    public void setToRotation(double theta){
    }
    public void setToRotation(double theta, double anchorx, double anchory){
    }
    public void setToRotation(double rx, double ry, double cx, double cy){
    }
    public void setToRotation(double rx, double ry){
    }
    public void setToScale(double sx, double sy){
    }
    public void setToShear(double shx, double shy){
    }
    public void setTransform(Transform tx){
    }
    public void setTransform(double... m){
    }
    public void concatenate(Transform tx){
    }
    public void preConcatenate(Transform tx){
    }
    public Point inverseTransform(Point src, Point dst){

        return dst;
    }
    public void inverseTransform(float[] src, int sofs, float[] dst, int dofs, int count){
    }
    public void invert(){
    }
    public boolean isIdentity(){
        return false;
    }

    public Bounds transform(Bounds in){

        return in;
    }
    public Bounds inverseTransform(Bounds in){

        return in;
    }
    public Bounds scaleFrom(Bounds in){

        final double sx = this.getScaleX();
        final double sy = this.getScaleY();

        in.x = (float)(in.x * sx);
        in.y = (float)(in.y * sy);
        in.width = (float)(in.width * sx);
        in.height = (float)(in.height * sy);

        return in;
    }
    public Bounds scaleTo(Bounds in){

        final double sx = this.getScaleX();
        final double sy = this.getScaleY();

        in.x = (float)(in.x / sx);
        in.y = (float)(in.y / sy);
        in.width = (float)(in.width / sx);
        in.height = (float)(in.height / sy);

        return in;
    }
    public Transform scaleFromAbsolute(Bounds internal, RectangularShape external){

        this.inverse = null;

        this.setTransform(internal.scaleFromAbsolute(external));

        return this;
    }
    public Transform scaleFromRelative(Bounds internal, RectangularShape external){

        this.inverse = null;

        this.setTransform(internal.scaleFromRelative(external));

        return this;
    }
    public Transform scaleToAbsolute(Bounds internal, RectangularShape external){

        this.inverse = null;

        this.setTransform(internal.scaleToAbsolute(external));

        return this;
    }
    public Transform scaleToRelative(Bounds internal, RectangularShape external){

        this.inverse = null;

        this.setTransform(internal.scaleToRelative(external));

        return this;
    }
    public Transform translateLocation(Point location){

        this.inverse = null;

        this.translate(location.x,location.y);

        return this;
    }
    public Point transformFrom(Point source){

        Point target = new Point(0,0);

        this.inverseTransform(source,target);

        return target;
    }
    public Context transformFrom(Context g){

        g.transform(this);

        return g;
    }
    public Transform createInverse(){

        return inverse;
    }
    public Transform clone(){
        return null;
    }
    public String toString(){
        final double[] matrix = new double[6];
        this.getMatrix(matrix);
        
        StringBuilder string = new StringBuilder();
        string.append("((");
        string.append(matrix[0]);
        string.append(',');
        string.append(matrix[2]);
        string.append(',');
        string.append(matrix[4]);
        string.append(")(");
        string.append(matrix[1]);
        string.append(',');
        string.append(matrix[3]);
        string.append(',');
        string.append(matrix[5]);
        string.append("))");
        return string.toString();
    }
}
