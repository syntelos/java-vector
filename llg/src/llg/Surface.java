/*
 * Copyright (c) 2009 John Pritchard, all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package llg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * A feature of {@link Luna}.
 * 
 * The surface is a list having both east and west directions.  On the
 * screen, east is in the right hand direction, and west is in the
 * left hand direction.  As the relation between world and device
 * coordinates is a simple scale, the westerly direction quickly
 * enters into negative coordinates.
 * 
 * @author jdp
 */
public final class Surface 
    extends Line
{

    private final static Stroke StrokeS = new BasicStroke(3.9f,1,1);
    private final static Stroke StrokeP = new BasicStroke(0.5f,1,1);

    private final static java.util.Random R = new java.util.Random();
    private final static double XPmin = 48;
    private final static double XPmax = 68;
    private final static double Xmin = 12;
    private final static double Xmax = 52;

    private final static double Ymin = 2;
    public final static double Ymax = 16;
    public final static double Yavg = ((Ymin + Ymax) / 2.0);

    final static int PointsY = (int)(Ymax+3);

    private final static int PP = 8;
    private final static int PP2 = (PP*2);

    private final static double R(double min, double max){
        double value = R.nextFloat();
        return ((value * (max - min)) + min);
    }
    private final static double RX(){
        return R(Xmin,Xmax);
    }
    private final static double RXP(){
        return R(XPmin,XPmax);
    }
    private final static double RY(){
        return R(Ymin,Ymax);
    }
    public final static int PointsMax = 125;
    private final static int Rpoints(){
        int a = (int)(R(1,5));
        int b = (int)(R(1,5));
        return (a*b*5);
    }
    private final static boolean Rpad(){
        return (1 == R.nextInt(PP));
    }


    public final boolean pad;
    private final Shape basement;
    private final Line2D.Double lineHi, lineLo, linePad;

    private volatile int points, indent;

    public volatile double distance;

    private volatile String pointsString;
    private volatile Rectangle pointsStringBounds;
    private volatile boolean over;
    private volatile Surface west, east;

    /**
     * The first surface element constructed is a pad.
     */
    public Surface(){
        super();
        this.pad = true;
        this.x1 = RX();
        this.y1 = RY();
        this.points();
        this.x2 = (this.x1 + RXP());
        this.y2 = this.y1;
        this.midX = Vector.Mid(this.x1,this.x2);
        this.midY = Vector.Mid(this.y1,this.y2);
        this.basement = Basement(this);
        this.lineHi = LineHi(this);
        this.lineLo = LineLo(this);
        this.linePad = LinePad(this);
    }
    private Surface(Surface other, boolean east){
        super();
        if (east){
            this.west = other;
            /*
             * -(W(1(other)2)E)-(W(1(this)2)E)-
             */
            this.x1 = other.x2;
            this.y1 = other.y2;
            if ((!other.pad)&& Rpad()){
                this.pad = true;
                this.points();
                this.x2 = (this.x1 + RXP());
                this.y2 = this.y1;
            }
            else {
                this.pad = false;
                this.x2 = (this.x1 + RX());
                this.y2 = RY();
            }
        }
        else {
            this.east = other;
            /*
             * -(W(1(this)2)E)-(W(1(other)2)E)-
             */
            this.x2 = other.x1;
            this.y2 = other.y1;
            if ((!other.pad)&& Rpad()){
                this.pad = true;
                this.points();
                this.x1 = (this.x2 - RXP());
                this.y1 = this.y2;
            }
            else {
                this.pad = false;
                this.x1 = (this.x2 - RX());
                this.y1 = RY();
            }
        }
        this.midX = Vector.Mid(this.x1,this.x2);
        this.midY = Vector.Mid(this.y1,this.y2);
        this.basement = Basement(this);
        this.lineHi = LineHi(this);
        this.lineLo = LineLo(this);
        if (this.pad)
            this.linePad = LinePad(this);
        else
            this.linePad = null;
    }


    public void tick(){
    }
    public void crash(){
        this.points(0);
    }
    public Surface west(){
        Surface west = this.west;
        if (null == west){
            west = new Surface(this,false);
            this.west = west;
        }
        return west;
    }
    public Surface east(){
        Surface east = this.east;
        if (null == east){
            east = new Surface(this,true);
            this.east = east;
        }
        return east;
    }
    public Surface over(Point2D.Double p){
        if (this.x1 <= p.x){
            if (p.x <= this.x2){
                this.over = true;

                java.lang.Double distance = Vector.Distance(this,p);
                if (null != distance){
                    if (p.y > this.y1)
                        this.distance = -distance;
                    else
                        this.distance = distance;
                }
                else if (p.y > this.y1)
                    this.distance = java.lang.Double.MIN_VALUE;
                else
                    this.distance = java.lang.Double.MAX_VALUE;

                return this;
            }
            else {
                this.over = false;
                return this.east().over(p);
            }
        }
        else {
            this.over = false;
            return this.west().over(p);
        }
    }
    public Surface landing(){
        if (this.pad)
            return this;
        else {
            Surface w = this.landing(-1,PP);
            Surface e = this.landing(+1,PP);
            if (null == w){
                if (null == e)
                    return this;//(Crash!)
                else
                    return e;
            }
            else if (null == e)
                return w;

            else if (w.distance < e.distance)
                return w;
            else
                return e;
        }
    }
    public void destroy(){

        Surface east = this.east;
        this.east = null;

        Surface west = this.west;
        this.west = null;

        if (null != east)
            east.destroy();

        if (null != west)
            west.destroy();
    }
    public int winPoints(){
        int p = this.points;
        this.points(0);
        return p;
    }
    public float winFuel(float fuel){
        int p = this.points;
        if (0 != p){
            fuel += (p * Lander.RocketFuelWinPointsRatio);
            if (fuel > 1f)
                return 1f;
        }
        return fuel;
    }
    public void draw(Graphics2D g){

        g.setColor(ColorSurfC);
        if (null != this.basement){
            g.setClip(this.basement);
            g.fill(this.basement);
        }
        g.setStroke(StrokeS);

        if (this.over)
            g.setColor(ColorOverB);
        else
            g.setColor(ColorSurfB);

        g.draw(this.lineLo);

        if (this.over)
            g.setColor(ColorOverA);
        else
            g.setColor(ColorSurfA);

        if (this.pad){

            g.setClip(null);

            g.draw(this.lineHi);

            if (this.over)
                g.setColor(ColorPadA);
            else
                g.setColor(ColorPadB);

            g.draw(this.linePad);

            if (null != this.pointsString && Camera.Current.is('A')){

                g.setStroke(StrokeP);

                int x1 = (int)this.x1;
                int x2 = (int)this.x2;
                int y1 = (int)this.y1;

                g.setColor(ColorTechP);

                Luna.Instance.fontP.drawString(this.pointsString,
                                               ((int)this.midX - this.indent), PointsY, g);
            }
        }
        else {

            g.draw(this.lineHi);

            g.setClip(null);
        }
    }
    private void points(){
        this.points(Rpoints());
    }
    private void points(int points){
        this.points = points;
        if (0 == points){
            this.pointsString = null;
            this.pointsStringBounds = null;
            this.indent = 0;
        }
        else {
            this.pointsString = Integer.toString(this.points);
            this.pointsStringBounds = Luna.Instance.fontP.stringBounds(this.pointsString);
            this.indent = (this.pointsStringBounds.width/2);
        }
    }
    private Surface landing(int dir, int limit){
        int d = Math.abs(dir);
        if (this.pad){
            this.distance = d;
            return this;
        }
        else if (limit < d)
            return null;
        else if (0 < dir)
            return this.east().landing(dir++,limit);
        else 
            return this.west().landing(dir--,limit);
    }

    private final static Color ColorSurfA = new Color(0x70,0x70,0x70,0x80);
    private final static Color ColorSurfB = new Color(0x50,0x50,0x50,0x80);
    private final static Color ColorSurfC = new Color(0x40,0x40,0x40);

    private final static Color ColorOverA = new Color(0x80,0x80,0x80,0x80);
    private final static Color ColorOverB = new Color(0x60,0x60,0x60,0x80);

    private final static Color ColorPadA = new Color(0x10,0x10,0xff,0x50);
    private final static Color ColorPadB = new Color(0x10,0x10,0xd0,0x50);

    private final static Color ColorTechP = new Color(0xa0,0x10,0x10);

    private final static Shape Basement(Line2D.Double surface){
        try {
            Path2D.Double basement = new Path2D.Double();

            double x1 = (surface.x1-0.4);
            double y1 = (surface.y1-0.4);
            double x2 = (surface.x2+0.4);
            double y2 = (surface.y2-0.4);
 
            basement.moveTo(x1,y1);
            basement.lineTo(x2,y2);
            basement.lineTo(x2,Ymax);
            basement.lineTo(x1,Ymax);
            basement.lineTo(x1,y1);
 
            return basement;
        }
        catch (Throwable t){
            Polygon basement = new Polygon();
            int x1 = (int)(surface.x1-0.4);
            int y1 = (int)(surface.y1-0.4);
            int x2 = (int)(surface.x2+0.4);
            int y2 = (int)(surface.y2-0.4);
            int ym = (int)Ymax;

            basement.addPoint(x1,y1);
            basement.addPoint(x2,y2);
            basement.addPoint(x2,ym);
            basement.addPoint(x1,ym);
            basement.addPoint(x1,y1);

            return basement;
        }
    }
    private final static Line2D.Double LineLo(Line2D.Double surface){

        double x1 = (surface.x1-0.1);
        double y1 = (surface.y1+0.5);
        double x2 = (surface.x2+0.1);
        double y2 = (surface.y2+0.5);

        return new Line2D.Double(x1,y1,x2,y2);
    }
    private final static Line2D.Double LineHi(Line2D.Double surface){

        double x1 = (surface.x1-0.1);
        double y1 = (surface.y1-0.3);
        double x2 = (surface.x2+0.1);
        double y2 = (surface.y2-0.3);

        return new Line2D.Double(x1,y1,x2,y2);
    }
    private final static Line2D.Double LinePad(Line2D.Double surface){

        double x1 = (surface.x1+0.1);
        double y1 = (surface.y1-0.5);
        double x2 = (surface.x2-0.1);
        double y2 = (surface.y2-0.5);

        return new Line2D.Double(x1,y1,x2,y2);
    }
}
