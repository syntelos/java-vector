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
import java.awt.geom.RectangularShape;
import java.awt.geom.NoninvertibleTransformException;
import java.util.StringTokenizer;

/**
 * Modifications to an existing matrix need to employ methods defined
 * here, or need to add (overriding) method definitions here.
 */
public class Transform
    extends java.awt.geom.AffineTransform
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
    public Transform(java.awt.geom.AffineTransform t){
        super(t);
    }


    public void init(){

        this.inverse = null;

        this.setToIdentity();
    }
    public Bounds transform(Bounds in){
        double[] src = new double[]{
            in.x, in.y,
            (in.x+in.width), (in.y+in.height)
        };
        double[] tgt = new double[4];

        super.transform(src,0,tgt,0,2);

        in.x = (float)tgt[0];
        in.y = (float)tgt[1];
        in.width = (float)(tgt[2]-tgt[0]);
        in.height = (float)(tgt[3]-tgt[1]);

        return in;
    }
    /**
     * Define scale as internal scale from external
     */
    public Transform scaleFromAbsolute(Bounds internal, RectangularShape external){

        this.inverse = null;

        this.setTransform(internal.scaleFrom(external));

        return this;
    }
    /**
     * Concatenate scale with internal scale from external
     */
    public Transform scaleFromRelative(Bounds internal, RectangularShape external){

        this.inverse = null;

        this.concatenate(internal.scaleFrom(external));

        return this;
    }
    /**
     * Define scale as internal scale to external
     */
    public Transform scaleToAbsolute(Bounds internal, RectangularShape external){

        this.inverse = null;

        this.setTransform(internal.scaleTo(external));

        return this;
    }
    /**
     * Concatenate scale with internal scale to external
     */
    public Transform scaleToRelative(Bounds internal, RectangularShape external){

        this.inverse = null;

        this.concatenate(internal.scaleTo(external));

        return this;
    }
    public Transform translateLocation(Point2D.Float location){

        this.inverse = null;

        this.translate(location.x,location.y);

        return this;
    }
    /**
     * Common input transformation from parent to child -- this is the
     * parent transform.
     */
    public Point2D.Float transformFrom(Point2D source){
        try {
            Point2D.Float target = new Point2D.Float(0,0);
            /*
             * The transform arithmetic is double, and the point class
             * will store to float
             */
            this.inverseTransform(source,target);

            return target;
        }
        catch (NoninvertibleTransformException exc){
            throw new IllegalStateException(this.toString(),exc);
        }
    }
    /**
     * Common graphics transformation from parent to child -- this is
     * the parent transform.
     * 
     * @param g Graphics context
     * 
     * @return The argument graphics context (not a clone or copy)
     */
    public Graphics2D transformFrom(Graphics2D g){

        g.transform(this);

        return g;
    }
    @Override
    public Transform createInverse(){
        Transform inverse = this.inverse;
        if (null == inverse){
            try {

                return (this.inverse = new Transform( super.createInverse()));
            }
            catch (NoninvertibleTransformException exc){
                throw new IllegalStateException(this.toString(),exc);
            }
        }
        else
            return inverse;
    }
    public Transform clone(){
        return (Transform)super.clone();
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
