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
    extends android.graphics.Matrix
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

    /* package */ final static double DegFromRad = (180.0 / Math.PI);


    private Transform inverse;

    private float[] android;


    public Transform(){
        super();
    }
    public Transform(String string){
        super();
        if (null != string){
            StringTokenizer strtok = new StringTokenizer(string,"][)(,");
            switch(strtok.countTokens()){
            case 0:
                break;
            case 6:
                try {
                    final double m00 = Double.parseDouble(strtok.nextToken());
                    final double m01 = Double.parseDouble(strtok.nextToken());
                    final double m02 = Double.parseDouble(strtok.nextToken());
                    final double m10 = Double.parseDouble(strtok.nextToken());
                    final double m11 = Double.parseDouble(strtok.nextToken());
                    final double m12 = Double.parseDouble(strtok.nextToken());

                    this.setTransform(m00,m10,m01,m11,m02,m12);
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
    public Transform(Transform t){
        super(t);
    }


    public void init(){

        this.inverse = null;
        this.android = null;

        this.setToIdentity();
    }
    /* package */ final float[] valuesAndroid(){
        float[] android = this.android;
        if (null == android){
            android = new float[9];
            super.getValues(android);
            this.android = android;
        }
        return android;
    }
    /* package */ final void valuesAndroid(float[] android){

        if (null != android && 9 == android.length){
            this.inverse = null;
            this.android = null;
            super.setValues(android);
        }
        else if (null != android)
            throw new IllegalArgumentException(String.valueOf(android.length));
        else
            throw new IllegalArgumentException();
    }

    public void getMatrix(double[] awt){
        final float[] android = this.valuesAndroid();

        final int awtlen = awt.length;
        if (4 <= awtlen){

            awt[0] = android[MSCALE_X];
            awt[1] = android[MSKEW_Y];
            awt[2] = android[MSKEW_X];
            awt[3] = android[MSCALE_Y];

            if (6 <= awtlen){
                awt[4] = android[MTRANS_X];
                awt[5] = android[MTRANS_Y];

                if (9 <= awtlen){
                    awt[6] = android[MPERSP_0];
                    awt[7] = android[MPERSP_1];
                    awt[8] = android[MPERSP_2];
                }
            }
        }
        else
            throw new IllegalArgumentException(String.valueOf(awtlen));
    }
    public double getDeterminant(){
        final float[] android = this.valuesAndroid();

        final double m00 = android[MSCALE_X];
        final double m10 = android[MSKEW_Y];
        final double m01 = android[MSKEW_X];
        final double m11 = android[MSCALE_Y];

        return (m00 * m11 - m01 * m10);
    }
    public double getScaleX(){
        final float[] android = this.valuesAndroid();
        return android[MSCALE_X];
    }
    public double getScaleY(){
        final float[] android = this.valuesAndroid();
        return android[MSCALE_Y];
    }
    public double getShearX(){
        final float[] android = this.valuesAndroid();
        return android[MSKEW_X];
    }
    public double getShearY(){
        final float[] android = this.valuesAndroid();
        return android[MSKEW_Y];
    }
    public double getTranslateX(){
        final float[] android = this.valuesAndroid();
        return android[MTRANS_X];
    }
    public double getTranslateY(){
        final float[] android = this.valuesAndroid();
        return android[MTRANS_Y];
    }
    public void translate(double tx, double ty){

        super.postTranslate( (float)tx, (float)ty);
    }
    public void rotate(double rad){

        final float deg = (float)(DegFromRad * rad);

        super.setRotate(deg);
    }
    public void rotate(double rad, double cx, double cy){

        final float deg = (float)(DegFromRad * rad);

        super.setRotate(deg,(float)cx,(float)cy);
    }
    public void rotate(double vx, double vy){

        final double h = Math.sqrt( (vx*vx)+(vy*vy));
        final float cos = (float)(vx/h);
        final float sin = (float)(vy/h);

        super.setSinCos(sin,cos);
    }
    public void scale(double sx, double sy){

        super.postScale( (float)sx, (float)sy);
    }
    public void shear(double shx, double shy){

        super.postSkew( (float)shx, (float)shy);
    }
    public void setToIdentity(){
        this.reset();
    }
    public void setToTranslation(double tx, double ty){
        this.reset();

        super.setTranslate((float)tx,(float)ty);
    }
    public void setToRotation(double rad){
        this.reset();

        final float deg = (float)(DegFromRad * rad);

        super.setRotate(deg);
    }
    public void setToRotation(double rad, double cx, double cy){
        this.reset();

        final float deg = (float)(DegFromRad * rad);

        super.setRotate(deg,(float)cx,(float)cy);
    }
    public void setToRotation(double vx, double vy, double cx, double cy){
        this.reset();

        final double h = Math.sqrt( (vx*vx)+(vy*vy));
        final float cos = (float)(vx/h);
        final float sin = (float)(vy/h);

        super.setSinCos(sin,cos,(float)cx,(float)cy);
    }
    public void setToRotation(double vx, double vy){
        this.reset();

        final double h = Math.sqrt( (vx*vx)+(vy*vy));
        final float cos = (float)(vx/h);
        final float sin = (float)(vy/h);

        super.setSinCos(sin,cos);
    }
    public void setToScale(double sx, double sy){

        super.setScale((float)sx,(float)sy);
    }
    public void setToShear(double shx, double shy){

        super.setSkew((float)shx,(float)shy);
    }
    public void setTransform(Transform tx){

        if (null != tx){
            
            super.set(tx);
        }
    }
    public void setTransform(double... awt){
        final int awtlen = awt.length;
        if (4 <= awtlen){
            final float[] android = this.valuesAndroid().clone();

            android[MSCALE_X] = (float)awt[0];
            android[MSKEW_Y] = (float)awt[1];
            android[MSKEW_X] = (float)awt[2];
            android[MSCALE_Y] = (float)awt[3];

            if (6 <= awtlen){
                android[MTRANS_X] = (float)awt[4];
                android[MTRANS_Y] = (float)awt[5];

                if (9 <= awtlen){
                    android[MPERSP_0] = (float)awt[6];
                    android[MPERSP_1] = (float)awt[7];
                    android[MPERSP_2] = (float)awt[8];
                }
            }
            this.valuesAndroid(android);
        }
    }
    public void concatenate(Transform tx){
        if (null != tx){
            if (!super.postConcat(tx))
                throw new IllegalStateException(this+" * "+tx);
        }
    }
    public void preConcatenate(Transform tx){
        if (null != tx){
            if (!super.preConcat(tx))
                throw new IllegalStateException(tx+" * "+this);
        }
    }
    public Point inverseTransform(Point src, Point dst){

        float[] srcary = new float[]{src.x,src.y};
        float[] dstary = new float[]{dst.x,dst.y};

        this.createInverse().mapPoints(srcary,0,dstary,0,1);

        dst.x = dstary[0];
        dst.y = dstary[1];

        return dst;
    }
    public void inverseTransform(float[] src, int sofs, float[] dst, int dofs, int count){

        this.createInverse().mapPoints(src,sofs,dst,dofs,count);
    }

    public Bounds transform(Bounds in){
        float[] src = new float[]{
            in.x, in.y,
            (in.x+in.width), (in.y+in.height)
        };
        float[] tgt = new float[4];

        super.mapPoints(src,0,tgt,0,2);

        in.x = tgt[0];
        in.y = tgt[1];
        in.width = (tgt[2]-tgt[0]);
        in.height = (tgt[3]-tgt[1]);

        return in;
    }
    public Bounds inverseTransform(Bounds in){

        float[] src = new float[]{
            in.x, in.y,
            (in.x+in.width), (in.y+in.height)
        };
        float[] tgt = new float[4];

        this.createInverse().mapPoints(src,0,tgt,0,2);

        in.x = tgt[0];
        in.y = tgt[1];
        in.width = (tgt[2]-tgt[0]);
        in.height = (tgt[3]-tgt[1]);

        return in;
    }
    public Bounds scaleFrom(Bounds in){
        final float[] android = this.valuesAndroid();
        final double sx = android[MSCALE_X];
        final double sy = android[MSCALE_Y];

        in.x = (float)(in.x * sx);
        in.y = (float)(in.y * sy);
        in.width = (float)(in.width * sx);
        in.height = (float)(in.height * sy);

        return in;
    }
    public Bounds scaleTo(Bounds in){
        final float[] android = this.valuesAndroid();
        final double sx = android[MSCALE_X];
        final double sy = android[MSCALE_Y];

        in.x = (float)(in.x / sx);
        in.y = (float)(in.y / sy);
        in.width = (float)(in.width / sx);
        in.height = (float)(in.height / sy);

        return in;
    }
    public Transform scaleFromAbsolute(Bounds internal, RectangularShape external){

        this.inverse = null;
        this.android = null;

        this.setTransform(internal.scaleFromAbsolute(external));

        return this;
    }
    public Transform scaleFromRelative(Bounds internal, RectangularShape external){

        this.inverse = null;
        this.android = null;

        this.setTransform(internal.scaleFromRelative(external));

        return this;
    }
    public Transform scaleToAbsolute(Bounds internal, RectangularShape external){

        this.inverse = null;
        this.android = null;

        this.setTransform(internal.scaleToAbsolute(external));

        return this;
    }
    public Transform scaleToRelative(Bounds internal, RectangularShape external){

        this.inverse = null;
        this.android = null;

        this.setTransform(internal.scaleToRelative(external));

        return this;
    }
    public Transform translateLocation(Point location){

        this.inverse = null;
        this.android = null;

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
        Transform inverse = this.inverse;
        if (null == inverse){
            inverse = new Transform();

            if (super.invert(inverse))

                return (this.inverse = inverse);
            else
                throw new IllegalStateException(this.toString());
        }
        else
            return inverse;
    }
    /**
     * Android native super class is not cloneable, so this method
     * simulates clone for subclasses.  A new instance is constructed
     * using the simple constructor, and the android API "set" method
     * is called with "this".  This process does not employ a subclass
     * copy constructor (like clone), but it does employ the simple
     * constructor (unlike clone).
     */
    public Transform clone(){
        try {
            Transform copy = this.getClass().newInstance();
            copy.set(this);
            return copy;
        }
        catch (java.lang.InstantiationException exc){

            return new Transform(this);
        }
        catch (java.lang.IllegalAccessException exc){

            return new Transform(this);
        }
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
