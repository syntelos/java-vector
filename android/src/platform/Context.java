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
    extends Object
    implements vector.Context
{

    public final static boolean Trace = false;
    public final static boolean Deep = false;


    public Context(Object observer, Object instance){
        super();
    }
    public Context(Context context){
        super();
    }
    public Context(Context context, int x, int y, int w, int h){
        super();
    }


    public Object getNative(){
        return this;
    }
    public int depth(){
        return 0;
    }
    public boolean hasGL(){
        return false;
    }
    public vector.gl.GL getGL(){
        throw new UnsupportedOperationException();
    }
    public boolean isTracing(){
        return false;
    }
    public vector.Context setTracing(boolean trace){

        return this;
    }
    public boolean isTracingDeep(){
        return false;
    }
    public vector.Context setTracingDeep(boolean deep){

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
