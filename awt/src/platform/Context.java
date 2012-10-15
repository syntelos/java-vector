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
import vector.Color;
import vector.DebugTrace;
import vector.Font;
import vector.Stroke;
import vector.Transform;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;


/**
 * Graphics output context for AWT
 */
public class Context
    extends Object
    implements vector.Context
{

    private final int depth;

    private ImageObserver observer;

    private java.awt.Graphics2D instance;

    private boolean trace;


    public Context(ImageObserver observer, java.awt.Graphics2D instance){
        super();
        if (null != observer && null != instance){
            this.observer = observer;
            this.instance = instance;
            this.depth = 1;
        }
        else
            throw new IllegalArgumentException();
    }
    public Context(Context context){
        super();
        if (null != context){

            this.instance = (java.awt.Graphics2D)context.instance.create();
            this.observer = context.observer;
            this.depth = (context.depth+1);
            this.trace = context.trace;
        }
        else
            throw new IllegalArgumentException();
    }
    public Context(Context context, int x, int y, int w, int h){
        super();
        if (null != context){

            this.instance = (java.awt.Graphics2D)context.instance.create(x,y,w,h);
            this.observer = context.observer;
            this.depth = (context.depth+1);
            this.trace = context.trace;
        }
        else
            throw new IllegalArgumentException();
    }


    public Object getNative(){
        return this.instance;
    }
    public int depth(){
        return this.depth;
    }
    public boolean hasGL(){
        return false;
    }
    public Object getGL(){
        throw new UnsupportedOperationException();
    }
    public boolean isTracing(){
        return this.trace;
    }
    public vector.Context setTracing(boolean trace){
        this.trace = trace;
        return this;
    }
    public void transform(Transform at)
    {
        if (this.trace) DebugTrace.out.printf("[%d] transform(%s)%n", this.depth, at);
        this.instance.transform(at);
    }
    public void translate(float x, float y)
    {
        if (this.trace) DebugTrace.out.printf("[%d] transform(%f,%f)%n", this.depth, x, y);
        this.instance.translate(x,y);
    }
    public void translate(double dob0, double dob1)
    {
        if (this.trace) DebugTrace.out.printf("[%d] translate(%g, %g)%n", this.depth, dob0, dob1);
        this.instance.translate(dob0, dob1);
    }
    public void fill(Shape shape)
    {
        if (this.trace) DebugTrace.out.printf("[%d] fill(%s)%n", this.depth, shape);
        this.instance.fill(shape);
    }
    public void clip(Shape shape)
    {
        if (this.trace) DebugTrace.out.printf("[%d] clip(%s)%n", this.depth, shape);
        this.instance.clip(shape);
    }
    public void draw(Shape shape)
    {
        if (this.trace) DebugTrace.out.printf("[%d] draw(%s)%n", this.depth, shape);
        this.instance.draw(shape);
    }
    public void drawGlyphVector(GlyphVector glyphVector0, float flo1, float flo2)
    {
        if (this.trace) DebugTrace.out.printf("[%d] drawGlyphVector(%s, %f, %f)%n", this.depth, glyphVector0, flo1, flo2);
        this.instance.drawGlyphVector(glyphVector0, flo1, flo2);
    }
    public void drawString(String string0, int a1, int a2)
    {
        if (this.trace) DebugTrace.out.printf("[%d] drawString(%s, %d, %d)%n", this.depth, string0, a1, a2);
        this.instance.drawString(string0, a1, a2);
    }
    public void drawString(String string0, float flo1, float flo2)
    {
        if (this.trace) DebugTrace.out.printf("[%d] drawString(%s, %f, %f)%n", this.depth, string0, flo1, flo2);
        this.instance.drawString(string0, flo1, flo2);
    }
    public Stroke getStroke()
    {
        if (this.trace) DebugTrace.out.printf("[%d] getStroke()%n", this.depth);
        java.awt.Stroke stroke = this.instance.getStroke();
        if (stroke instanceof Stroke)
            return (Stroke)stroke;
        else if (stroke instanceof java.awt.BasicStroke)
            return new Stroke( (java.awt.BasicStroke)stroke);
        else if (null != stroke)
            throw new IllegalStateException(stroke.getClass().getName());
        else
            return null;
    }
    public Transform getTransform()
    {
        if (this.trace) DebugTrace.out.printf("[%d] getTransform()%n", this.depth);
        return new Transform( this.instance.getTransform());
    }
    public void setStroke(Stroke stroke)
    {
        if (this.trace) DebugTrace.out.printf("[%d] setStroke(%s)%n", this.depth, stroke);

        this.instance.setStroke(stroke);

        if (null != stroke.color)
            this.instance.setColor(stroke.color);
    }
    public void setTransform(Transform at)
    {
        if (this.trace) DebugTrace.out.printf("[%d] setTransform(%s)%n", this.depth, at);
        this.instance.setTransform(at);
    }
    public void finalize()
    {
        if (this.trace) DebugTrace.out.printf("[%d] finalize()%n", this.depth);
        this.instance.finalize();
    }
    public String toString()
    {
        if (this.trace) DebugTrace.out.printf("[%d] toString()%n", this.depth);
        return this.instance.toString();
    }
    public vector.Context create()
    {
        if (this.trace) DebugTrace.out.printf("[%d] create()%n", this.depth);
        return new Context(this);
    }
    public vector.Context create(int x, int y, int w, int h)
    {
        if (this.trace) DebugTrace.out.printf("[%d] create(%d, %d, %d, %d)%n", this.depth, x, y, w, h);
        return new Context(this,x,y,w,h);
    }
    public boolean drawImage(Image image, Transform at)
    {
        if (this.trace) DebugTrace.out.printf("[%d] drawImage(%s, %s, %s)%n", this.depth, image, at, this.observer);
        return this.instance.drawImage(image, at, this.observer);
    }
    public boolean drawImage(Image image0, int x, int y)
    {
        if (this.trace) DebugTrace.out.printf("[%d] drawImage(%s, %d, %d, %s)%n", this.depth, image0, x, y, this.observer);
        return this.instance.drawImage(image0, x, y, this.observer);
    }
    public boolean drawImage(Image image0, int x, int y, int w, int h)
    {
        if (this.trace) DebugTrace.out.printf("[%d] drawImage(%s, %d, %d, %d, %d, %s)%n", this.depth, image0, x, y, w, h, this.observer);
        return this.instance.drawImage(image0, x, y, w, h, this.observer);
    }
    public Color getColor()
    {
        if (this.trace) DebugTrace.out.printf("[%d] getColor()%n", this.depth);
        return new Color( this.instance.getColor());
    }
    public void setColor(Color color0)
    {
        if (this.trace) DebugTrace.out.printf("[%d] setColor(%s)%n", this.depth, color0);
        this.instance.setColor(color0);
    }
    public void clipRect(int a0, int a1, int a2, int a3)
    {
        if (this.trace) DebugTrace.out.printf("[%d] clipRect(%d, %d, %d, %d)%n", this.depth, a0, a1, a2, a3);
        this.instance.clipRect(a0, a1, a2, a3);
    }
    public void dispose()
    {
        if (this.trace) DebugTrace.out.printf("[%d] dispose()%n", this.depth);
        this.instance.dispose();
    }
    public Shape getClip()
    {
        if (this.trace) DebugTrace.out.printf("[%d] getClip()%n", this.depth);
        return this.instance.getClip();
    }
    public Bounds getClipBounds()
    {
        if (this.trace) DebugTrace.out.printf("[%d] getClipBounds()%n", this.depth);
        return new Bounds( this.instance.getClipBounds());
    }
    public Bounds getClipBounds(Bounds bounds)
    {
        if (this.trace) DebugTrace.out.printf("[%d] getClipBounds(%s)%n", this.depth, bounds);
        bounds.setFrame(this.instance.getClipBounds());
        return bounds;        
    }
    public Bounds getClipRect()
    {
        if (this.trace) DebugTrace.out.printf("[%d] getClipRect()%n", this.depth);
        return new Bounds( this.instance.getClipRect());
    }
    public Font getFont()
    {
        if (this.trace) DebugTrace.out.printf("[%d] getFont()%n", this.depth);
        /*
         * TODO: Review for complexity
         */
        return new Font( new platform.Font(this.instance.getFont()));
    }
    public void setClip(int x, int y, int w, int h)
    {
        if (this.trace) DebugTrace.out.printf("[%d] setClip(%d, %d, %d, %d)%n", this.depth, x, y, w, h);
        this.instance.setClip(x, y, w, h);
    }
    public void setClip(Shape shape)
    {
        if (this.trace) DebugTrace.out.printf("[%d] setClip(%s)%n", this.depth, shape);
        this.instance.setClip(shape);
    }
    public void setFont(Font font)
    {
        if (this.trace) DebugTrace.out.printf("[%d] setFont(%s)%n", this.depth, font);
        this.instance.setFont(font);
    }

}
