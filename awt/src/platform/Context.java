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
import vector.DebugTrace;
import vector.Image;
import vector.Stroke;

import java.awt.image.ImageObserver;


/**
 * Graphics output context for AWT
 * 
 * <h3>System properties</h3>
 * 
 * The following properties are read at class initialization time.
 * 
 * <dl>
 * <dt><code>platform.Context.Trace</code></dt>
 * <dd> Boolean to enable context tracing to process standard error
 * </dd>
 * <dt><code>platform.Context.Deep</code></dt>
 * <dd> Boolean to enable context call stack tracing to process standard error
 * </dd>
 * <dl>
 */
public class Context
    extends Object
    implements vector.Context
{

    public final static boolean Trace;
    static {
        boolean trace = false;
        try {
            String conf = System.getProperty("platform.Context.Trace");
            trace = (null != conf && conf.equals("true"));
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        Trace = trace;
    }

    public final static boolean Deep;
    static {
        boolean deep = false;
        try {
            String conf = System.getProperty("platform.Context.Deep");
            deep = (null != conf && conf.equals("true"));
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        Deep = deep;
    }




    private final int depth;

    private ImageObserver observer;

    private java.awt.Graphics2D instance;

    private boolean trace = Context.Trace, deep = Context.Deep;


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
            this.deep = context.deep;
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
            this.deep = context.deep;
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
    public boolean isTracingDeep(){
        return this.deep;
    }
    public vector.Context setTracingDeep(boolean deep){
        this.deep = deep;
        return this;
    }
    public void transform(Transform at)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] transform(%s)", this.depth, at)).print(this.deep);
        this.instance.transform(at);
    }
    public void translate(float x, float y)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] transform(%f,%f)", this.depth, x, y)).print(this.deep);
        this.instance.translate(x,y);
    }
    public void translate(double dob0, double dob1)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] translate(%g, %g)", this.depth, dob0, dob1)).print(this.deep);
        this.instance.translate(dob0, dob1);
    }
    public void fill(Shape shape)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] fill(%s)", this.depth, shape)).print(this.deep);
        this.instance.fill(shape);
    }
    public void clip(Shape shape)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] clip(%s)", this.depth, shape)).print(this.deep);
        this.instance.clip(shape);
    }
    public void draw(Shape shape)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] draw(%s)", this.depth, shape)).print(this.deep);
        this.instance.draw(shape);
    }
    public void drawString(String string0, int a1, int a2)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] drawString(%s, %d, %d)", this.depth, string0, a1, a2)).print(this.deep);
        this.instance.drawString(string0, a1, a2);
    }
    public void drawString(String string0, float flo1, float flo2)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] drawString(%s, %f, %f)", this.depth, string0, flo1, flo2)).print(this.deep);
        this.instance.drawString(string0, flo1, flo2);
    }
    public Stroke getStroke()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] getStroke()", this.depth)).print(this.deep);
        java.awt.Stroke stroke = this.instance.getStroke();
        if (stroke instanceof Stroke)
            return (Stroke)stroke;
        else if (null == stroke)
            return null;
        else
            throw new IllegalStateException(stroke.getClass().getName());
    }
    public Transform getTransform()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] getTransform()", this.depth)).print(this.deep);
        return new Transform( this.instance.getTransform());
    }
    public void setStroke(Stroke stroke)
    {
        if (null != stroke.color)
            this.setColor(stroke.color);

        if (this.trace) (new DebugTrace(this.depth,"[%d] setStroke(%s)", this.depth, stroke)).print(this.deep);

        this.instance.setStroke(stroke);
    }
    public void setTransform(Transform at)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] setTransform(%s)", this.depth, at)).print(this.deep);
        this.instance.setTransform(at);
    }
    public void finalize()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] finalize()", this.depth)).print(this.deep);
        this.instance.finalize();
    }
    public String toString()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] toString()", this.depth)).print(this.deep);
        return this.instance.toString();
    }
    public vector.Context create()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] create()", this.depth)).print(this.deep);
        return new Context(this);
    }
    public vector.Context create(int x, int y, int w, int h)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] create(%d, %d, %d, %d)", this.depth, x, y, w, h)).print(this.deep);
        return new Context(this,x,y,w,h);
    }
    public boolean drawImage(Image image, Transform at)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] drawImage(%s, %s, %s)", this.depth, image, at, this.observer)).print(this.deep);
        return this.instance.drawImage((java.awt.Image)image, at, this.observer);
    }
    public boolean drawImage(Image image0, int x, int y)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] drawImage(%s, %d, %d, %s)", this.depth, image0, x, y, this.observer)).print(this.deep);
        return this.instance.drawImage((java.awt.Image)image0, x, y, this.observer);
    }
    public boolean drawImage(Image image0, int x, int y, int w, int h)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] drawImage(%s, %d, %d, %d, %d, %s)", this.depth, image0, x, y, w, h, this.observer)).print(this.deep);
        return this.instance.drawImage((java.awt.Image)image0, x, y, w, h, this.observer);
    }
    public Color getColor()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] getColor()", this.depth)).print(this.deep);
        return new Color( this.instance.getColor());
    }
    public void setColor(Color color0)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] setColor(%s)", this.depth, color0)).print(this.deep);
        this.instance.setColor(color0);
    }
    public void clipTo(int a0, int a1, int a2, int a3)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] clipRect(%d, %d, %d, %d)", this.depth, a0, a1, a2, a3)).print(this.deep);
        this.instance.clipRect(a0, a1, a2, a3);
    }
    public void dispose()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] dispose()", this.depth)).print(this.deep);
        this.instance.dispose();
    }
    public Shape getClip()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] getClip()", this.depth)).print(this.deep);
        java.awt.Shape clip = this.instance.getClip();
        if (clip instanceof Shape)
            return (Shape)clip;
        else if (null == clip)
            return null;
        else
            throw new IllegalStateException(clip.getClass().getName());//[TODO]
    }
    public Font getFont()
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] getFont()", this.depth)).print(this.deep);

        java.awt.Font font = this.instance.getFont();
        if (font instanceof Font)
            return (Font)font;
        else if (null == font)
            return null;
        else
            return new Font(font);
    }
    public void setClip(Shape shape)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] setClip(%s)", this.depth, shape)).print(this.deep);
        this.instance.setClip(shape);
    }
    public void setFont(Font font)
    {
        if (this.trace) (new DebugTrace(this.depth,"[%d] setFont(%s)", this.depth, font)).print(this.deep);
        this.instance.setFont(font);
    }

}
