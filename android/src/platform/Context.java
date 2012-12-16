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
import android.view.SurfaceHolder;

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

    private final SurfaceHolder surface;

    protected final int save;

    private boolean trace = Context.Trace, deep = Context.Deep;


    public Context(View observer, Canvas instance){
        super();
        if (null != observer && null != instance){
            this.observer = observer;
            this.surface = null;
            this.instance = instance;
            this.depth = 1;
            this.save = 0;
        }
        else
            throw new IllegalArgumentException();
    }
    public Context(View observer, SurfaceHolder surface){
        super();
        if (null != observer && null != surface){
            this.observer = observer;
            this.surface = surface;
            this.instance = surface.lockCanvas();
            this.depth = 1;
            this.save = 0;
        }
        else
            throw new IllegalArgumentException();
    }
    public Context(Context context){
        super();
        if (null != context){
            this.observer = context.observer;
            this.surface = null;
            this.instance = context.instance;
            this.save = this.instance.save();

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
            this.observer = context.observer;
            this.surface = null;
            this.instance = context.instance;
            this.save = this.instance.save();
            {
                this.instance.clipRect(x,y,(x+w),(y+h));
                this.instance.translate(x,y);
            }
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

    public void setBitmap(android.graphics.Bitmap bitmap) {
        this.instance.setBitmap(bitmap);
    }
    public void setViewport(int width, int height) {
        this.instance.setViewport(width,height);
    }
    public boolean isOpaque(){
        return this.instance.isOpaque();
    }
    public int getWidth(){
        return this.instance.getWidth();
    }
    public int getHeight(){
        return this.instance.getHeight();
    }
    public int getDensity(){
        return this.instance.getDensity();
    }
    public void setDensity(int density){
        this.instance.setDensity(density);
    }
    public int save(){
        return this.instance.save();
    }
    public int save(int flags){
        return this.instance.save(flags);
    }
    public int saveLayer(android.graphics.RectF bounds, android.graphics.Paint paint, int flags){
        return this.instance.saveLayer(bounds, paint, flags);
    }
    public int saveLayer(float left, float top, float right, float bottom,
                         android.graphics.Paint paint, int flags)
    {
        return this.instance.saveLayer(left, top, right, bottom, paint, flags);
    }
    public int saveLayerAlpha(android.graphics.RectF bounds, int alpha, int flags){
        return this.instance.saveLayerAlpha(bounds, alpha, flags);
    }
    public int saveLayerAlpha(float left, float top, float right, float bottom, int alpha, int flags){
        return this.instance.saveLayerAlpha(left, top, right, bottom, alpha, flags);
    }
    public void restore(){
        this.instance.restore();
    }
    public int getSaveCount(){
        return this.instance.getSaveCount();
    }
    public void restoreToCount(int saveCount){
        this.instance.restoreToCount(saveCount);
    }
    public void translate(float dx, float dy){
        this.instance.translate(dx, dy);
    }
    public void scale(float sx, float sy){
        this.instance.scale(sx, sy);
    }
    public void rotate(float degrees){
        this.instance.rotate(degrees);
    }
    public void skew(float sx, float sy){
        this.instance.skew(sx, sy);
    }
    public void concat(android.graphics.Matrix matrix){
        this.instance.concat(matrix);
    }
    public void setMatrix(android.graphics.Matrix matrix){
        this.instance.setMatrix(matrix);
    }
    public void getMatrix(android.graphics.Matrix ctm){
        this.instance.getMatrix(ctm);
    }
    public boolean clipRect(android.graphics.RectF rect, android.graphics.Region.Op op){
        return this.instance.clipRect(rect, op);
    }
    public boolean clipRect(android.graphics.Rect rect, android.graphics.Region.Op op){
        return this.instance.clipRect(rect, op);
    }
    public boolean clipRect(android.graphics.RectF rect){
        return this.instance.clipRect(rect);
    }
    public boolean clipRect(android.graphics.Rect rect){
        return this.instance.clipRect(rect);
    }
    public boolean clipRect(float left, float top, float right, float bottom,
                            android.graphics.Region.Op op)
    {
        return this.instance.clipRect(left, top, right, bottom, op);
    }
    public boolean clipRect(float left, float top, float right, float bottom){
        return this.instance.clipRect(left, top, right, bottom);
    }
    public boolean clipRect(int left, int top, int right, int bottom){
        return this.instance.clipRect(left, top, right, bottom);
    }
    public boolean clipPath(android.graphics.Path path, android.graphics.Region.Op op){
        return this.instance.clipPath(path, op);
    }
    public boolean clipPath(android.graphics.Path path){
        return this.instance.clipPath(path);
    }
    public boolean clipRegion(android.graphics.Region region, android.graphics.Region.Op op){
        return this.instance.clipRegion(region, op);
    }
    public boolean clipRegion(android.graphics.Region region){
        return this.instance.clipRegion(region);
    }
    public android.graphics.DrawFilter getDrawFilter(){
        return this.instance.getDrawFilter();
    }
    public void setDrawFilter(android.graphics.DrawFilter filter){
        this.instance.setDrawFilter(filter);
    }
    public boolean quickReject(android.graphics.RectF rect, android.graphics.Canvas.EdgeType type){
        return this.instance.quickReject(rect, type);
    }
    public boolean quickReject(android.graphics.Path path, android.graphics.Canvas.EdgeType type){
        return this.instance.quickReject(path, type);
    }
    public boolean quickReject(float left, float top, float right, float bottom,
                               android.graphics.Canvas.EdgeType type)
    {
        return this.instance.quickReject(left, top, right, bottom, type);
    }
    public boolean getClipBounds(android.graphics.Rect bounds){
        return this.instance.getClipBounds(bounds);
    }
    public void drawRGB(int r, int g, int b){
        this.instance.drawRGB(r, g, b);
    }
    public void drawARGB(int a, int r, int g, int b){
        this.instance.drawARGB(a, r, g, b);
    }
    public void drawColor(int color){
        this.instance.drawColor(color);
    }
    public void drawColor(int color, android.graphics.PorterDuff.Mode mode){
        this.instance.drawColor(color, mode);
    }
    public void drawPaint(android.graphics.Paint paint){
        this.instance.drawPaint(paint);
    }
    public void drawPoints(float[] pts, int offset, int count, android.graphics.Paint paint){
        this.instance.drawPoints(pts, offset, count, paint);
    }
    public void drawPoints(float[] pts, android.graphics.Paint paint){
        this.instance.drawPoints(pts, paint);
    }
    public void drawPoint(float x, float y, android.graphics.Paint paint){
        this.instance.drawPoint(x, y, paint);
    }
    public void drawLine(float startX, float startY, float stopX, float stopY, 
                         android.graphics.Paint paint)
    {
        this.instance.drawLine(startX, startY, stopX, stopY, paint);
    }
    public void drawLines(float[] pts, int offset, int count, android.graphics.Paint paint){
        this.instance.drawLines(pts, offset, count, paint);
    }
    public void drawLines(float[] pts, android.graphics.Paint paint){
        this.instance.drawLines(pts, paint);
    }
    public void drawRect(android.graphics.RectF rect, android.graphics.Paint paint){
        this.instance.drawRect(rect, paint);
    }
    public void drawRect(android.graphics.Rect r, android.graphics.Paint paint){
        this.instance.drawRect(r, paint);
    }
    public void drawRect(float left, float top, float right, float bottom,
                         android.graphics.Paint paint)
    {
        this.instance.drawRect(left, top, right, bottom, paint);
    }
    public void drawOval(android.graphics.RectF oval, android.graphics.Paint paint){
        this.instance.drawOval(oval, paint);
    }
    public void drawCircle(float cx, float cy, float radius, android.graphics.Paint paint){
        this.instance.drawCircle(cx, cy, radius, paint);
    }
    public void drawArc(android.graphics.RectF oval, float startAngle, float sweepAngle,
                        boolean useCenter, android.graphics.Paint paint)
    {
        this.instance.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
    }
    public void drawRoundRect(android.graphics.RectF rect, float rx, float ry, android.graphics.Paint paint){
        this.instance.drawRoundRect(rect, rx, ry, paint);
    }
    public void drawPath(android.graphics.Path path, android.graphics.Paint paint){
        this.instance.drawPath(path, paint);
    }
    public void drawBitmap(android.graphics.Bitmap bitmap, float left, float top, android.graphics.Paint paint){
        this.instance.drawBitmap(bitmap, left, top, paint);
    }
    public void drawBitmap(android.graphics.Bitmap bitmap, android.graphics.Rect src, android.graphics.RectF dst, android.graphics.Paint paint){
        this.instance.drawBitmap(bitmap, src, dst, paint);
    }
    public void drawBitmap(android.graphics.Bitmap bitmap, android.graphics.Rect src, android.graphics.Rect dst, android.graphics.Paint paint){
        this.instance.drawBitmap(bitmap, src, dst, paint);
    }
    public void drawBitmap(int[] colors, int offset, int stride, float x,
                           float y, int width, int height, boolean hasAlpha,
                           android.graphics.Paint paint)
    {
        this.instance.drawBitmap(colors, offset, stride, x,
                                 y, width, height, hasAlpha,
                                 paint);
    }
    public void drawBitmap(int[] colors, int offset, int stride, int x, int y,
                           int width, int height, boolean hasAlpha,
                           android.graphics.Paint paint)
    {
        this.instance.drawBitmap(colors, offset, stride, x, y,
                                 width, height, hasAlpha,
                                 paint);
    }
    public void drawBitmap(android.graphics.Bitmap bitmap, android.graphics.Matrix matrix, android.graphics.Paint paint){
        this.instance.drawBitmap(bitmap, matrix, paint);
    }
    public void drawBitmapMesh(android.graphics.Bitmap bitmap, int meshWidth, int meshHeight,
                               float[] verts, int vertOffset,
                               int[] colors, int colorOffset, android.graphics.Paint paint)
    {
        this.instance.drawBitmapMesh(bitmap, meshWidth, meshHeight,
                                     verts, vertOffset,
                                     colors, colorOffset, paint);
    }
    public void drawVertices(android.graphics.Canvas.VertexMode mode, int vertexCount,
                             float[] verts, int vertOffset,
                             float[] texs, int texOffset,
                             int[] colors, int colorOffset,
                             short[] indices, int indexOffset,
                             int indexCount, android.graphics.Paint paint)
    {
        this.instance.drawVertices(mode, vertexCount,
                                   verts, vertOffset,
                                   texs, texOffset,
                                   colors, colorOffset,
                                   indices, indexOffset,
                                   indexCount, paint);
    }
    public void drawText(char[] text, int index, int count, float x, float y,
                         android.graphics.Paint paint)
    {
        this.instance.drawText(text, index, count, x, y, paint);
    }
    public void drawText(java.lang.String text, float x, float y, android.graphics.Paint paint){
        this.instance.drawText(text, x, y, paint);
    }
    public void drawText(java.lang.String text, int start, int end, float x, float y, 
                         android.graphics.Paint paint)
    {
        this.instance.drawText(text, start, end, x, y, paint);
    }
    public void drawText(java.lang.CharSequence text, int start, int end, float x,
                         float y, android.graphics.Paint paint)
    {
        this.instance.drawText(text, start, end, x, y, paint);
    }
    public void drawPosText(char[] text, int index, int count, float[] pos, 
                            android.graphics.Paint paint)
    {
        this.instance.drawPosText(text, index, count, pos, paint);
    }
    public void drawPosText(java.lang.String text, float[] pos, android.graphics.Paint paint){
        this.instance.drawPosText(text, pos, paint);
    }
    public void drawTextOnPath(char[] text, int index, int count, android.graphics.Path path, 
                               float hOffset, float vOffset, android.graphics.Paint paint)
    {
        this.instance.drawTextOnPath(text, index, count, path, 
                                     hOffset, vOffset, paint);
    }
    public void drawTextOnPath(String text, android.graphics.Path path, float hOffset,
                               float vOffset, android.graphics.Paint paint)
    {
        this.instance.drawTextOnPath(text, path, hOffset,
                                     vOffset, paint);
    }
    public void drawPicture(android.graphics.Picture picture){
        this.instance.drawPicture(picture);
    }
    public void drawPicture(android.graphics.Picture picture, android.graphics.RectF dst){
        this.instance.drawPicture(picture, dst);
    }
    public void drawPicture(android.graphics.Picture picture, android.graphics.Rect dst){
        this.instance.drawPicture(picture, dst);
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
    public void translate(double x, double y)
    {
        this.translate((float) x, (float) y);
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
        return new Context(this);
    }
    public vector.Context create(int x, int y, int w, int h)
    {
        return new Context(this,x,y,w,h);
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
    /* package */ void disposeAndroid()
    {
        if (null != this.surface){

            this.surface.unlockCanvasAndPost(this.instance);
        }
    }
    public void dispose()
    {
        if (null != this.surface){

            this.surface.unlockCanvasAndPost(this.instance);
        }
        else if (null != this.instance && 0 != this.save){

            this.instance.restoreToCount(this.save);
        }
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
