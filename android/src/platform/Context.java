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

import platform.geom.Point;

import android.graphics.Canvas;
import android.view.View;

/**
 * Graphics output context
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
    extends Canvas
    implements vector.Context,
               android.opengl.GLSurfaceView.GLWrapper
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



    public final int depth;

    public final View observer;

    public final Canvas instance;

    protected final int save;

    private boolean trace = Context.Trace, deep = Context.Deep;


    public Context(View observer, Canvas instance){
        super();
        if (null != observer && null != instance){
            this.observer = observer;
            this.instance = instance;
            this.depth = 1;
            this.save = 0;
        }
        else
            throw new IllegalArgumentException();
    }
    public Context(Context context){
        super();
        if (null != context){
            this.instance = context.instance;
            this.save = this.instance.save();

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
            this.instance = context.instance;
            this.save = this.instance.save();
            {
                this.instance.clipRect(x,y,(x+w),(y+h));
                this.instance.translate(x,y);
            }
            this.observer = context.observer;
            this.depth = (context.depth+1);
            this.trace = context.trace;
            this.deep = context.deep;
        }
        else
            throw new IllegalArgumentException();
    }


    public Canvas getNative(){
        return this.instance;
    }
    public int depth(){
        return this.depth;
    }
    public boolean hasGL(){
        return true;
    }
    public platform.gl.GL wrap(javax.microedition.khronos.opengles.GL gl){

        return new platform.gl.GL(this,gl);
    }
    public platform.gl.GL getGL(){

        return this.wrap(this.instance.getGL());
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
    }
    public void translate(float x, float y)
    {
    }
    public void translate(double dob0, double dob1)
    {
    }
    public void fill(Shape shape)
    {
    }
    public void clip(Shape shape)
    {
    }
    public void draw(Shape shape)
    {
    }
    public Stroke getStroke()
    {
        return null;
    }
    public void setStroke(Stroke stroke)
    {
    }
    public Transform getTransform()
    {
        return null;
    }
    public void setTransform(Transform at)
    {
    }
    public vector.Context create()
    {
        return null;
    }
    public vector.Context create(int x, int y, int w, int h)
    {
        return null;
    }
    public void draw(Image image)
    {
    }
    public Color getColor()
    {
        return null;
    }
    public void setColor(Color color0)
    {
    }
    public void clipTo(int a0, int a1, int a2, int a3)
    {
    }
    public void dispose()
    {
        this.instance.restoreToCount(this.save);
    }
    public Shape getClip()
    {
        return null;
    }
    public Font getFont()
    {
        return null;
    }
    public void setClip(Shape shape)
    {
    }
    public void setFont(Font font)
    {
    }

}
