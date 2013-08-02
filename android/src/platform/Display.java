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
import vector.Component;
import vector.Event;
import vector.Viewport;

import platform.event.Repainter;
import platform.geom.Point;

import json.ArrayJson;
import json.Json;
import json.ObjectJson;
import json.Reader;

import android.graphics.Canvas;
import android.view.View;
import android.view.SurfaceHolder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Display
    extends android.opengl.GLSurfaceView
    implements vector.Display,
               android.view.SurfaceHolder.Callback,
               android.opengl.GLSurfaceView.GLWrapper,
               android.opengl.GLSurfaceView.Renderer
{

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Color background;

    protected boolean bufferOverlay;

    protected Component[] components;

    protected final Transform transform = new Transform();

    protected final Output output = new Output();

    protected Repainter outputOverlayAnimate;

    protected boolean mouseIn;

    protected Bounds boundsNative, boundsUser;

    protected Document document;

    protected volatile SurfaceHolder surface;

    protected volatile boolean useGL;
    /**
     * Change this value once and only once for each instance object.
     * @see android.opengl.GLSurfaceView#setRenderer(android.opengl.Renderer)
     */
    protected volatile boolean inGL;


    public Display(){
        super(Frame.Instance);

        this.setWillNotDraw(false);
        this.setGLWrapper(this);
    }


    /**
     * The Frame and super class have been initialized.
     */
    public void init(){
        this.destroy();

        vector.DisplayService.Register(this);

        this.mouseIn = false;

        this.bufferOverlay = true;

        this.transform.init();

        this.layout();

        if (this.useGL){

            if (!this.inGL){

                this.inGL = true;

                //this.setRenderer(this);//[TODO] manage Repainter vs GLThread+GLSurface.Mode
            }
        }
    }
    public void init(Boolean init){
        if (null != init && init.booleanValue()){

            this.init();
        }
    }
    public void destroy(){

        vector.DisplayService.Deregister(this);

        this.outputOverlayAnimateCancel();
        try {
            for (Component c: this){
                c.destroy();
            }
        }
        finally {
            this.components = null;
            this.boundsNative = null;
            this.boundsUser = null;

            this.flush();
        }
    }
    public void resized(){

        this.layout();

        for (Component c: this){

            c.resized();
        }
    }
    public void modified(){

        this.layout();

        for (Component c: this){

            c.modified();
        }
    }
    public void relocated(){
    }
    public platform.gl.GL wrap(javax.microedition.khronos.opengles.GL gl){

        if (gl instanceof platform.gl.GL)
            return (platform.gl.GL)gl;
        else
            return new platform.gl.GL(new Context(this,this.surface),gl);
    }
    public void flush(){

        this.output.flush();
    }
    public boolean isVisible(){
        return false;
    }
    public final boolean hasBackground(){

        return (null != this.background);
    }
    public final Color getBackgroundVector(){

        return this.background;
    }
    public final Display setBackground(Color background){
        if (null != background){
            this.background = background;
        }
        return this;
    }
    public final Display setBackground(String code){
        if (null != code)
            return this.setBackground(new Color(code));
        else
            return this;
    }
    public boolean isBufferOverlay(){
        return this.bufferOverlay;
    }
    public Boolean getBufferOverlay(){
        return this.bufferOverlay;
    }
    public Display setBufferOverlay(boolean value){

        this.bufferOverlay = value;

        return this;
    }
    public Display setBufferOverlay(Boolean value){
        if (null != value)
            return this.setBufferOverlay(value.booleanValue());
        else
            return this;
    }
    protected final <F extends Frame> F getFrame(){

        return null;
    }
    public final <T extends Component> T getParentVector(){

        return null;
    }
    public final <T extends Component> T getRootContainer(){

        return (T)this;
    }
    public Component setParentVector(Component parent){

        throw new UnsupportedOperationException();
    }
    public final Component setVisibleVector(boolean visible){

        return this;
    }
    public final Viewport getViewport(){

        return new Viewport(this.boundsUser);
    }
    public final Display setViewport(Viewport.Size size){

        return this;
    }
    public boolean contains(int x, int y){

        return false;
    }
    public final Bounds getBoundsVector(){

        return this.boundsUser.clone();
    }
    public final Component setBoundsVector(Bounds bounds){
        if (null != bounds){
            this.boundsUser = bounds;
        }
        return this;
    }
    public final Display setBounds(Bounds bounds){

        return this;
    }
    public final boolean contains(float x, float y){

        return false;
    }
    public final boolean contains(Point p){
        return false;
    }
    public final Point getLocationVector(){

        return new Point(0,0);
    }
    public final Component setLocationVector(Point p){

        return this;
    }
    public final Transform getTransformLocal(){

        return this.transform.clone();
    }
    public Display setTransformLocal(Transform transform){
        if (null != transform)
            this.transform.setTransform(transform);
        return this;
    }
    public Display setTransformLocal(float sx, float sy){

        return this.setTransformLocal(Transform.getScaleInstance(sx,sy));
    }
    public final Transform getTransformParent(){

        return this.getTransformLocal().translateLocation(this.getLocationVector());
    }
    /**
     * Broadcast event to all consumers
     * 
     * @return Consumed (at least once)
     */
    public boolean input(Event e){

        boolean action = false;

        /*
         * Interpret Viewport event
         */
        if (Event.Type.Action == e.getType()){

            action = true;

            final Event.NamedAction actor = (Event.NamedAction)e;

            if (actor.isValueClass(Viewport.Size.class)){

                final Event.NamedAction<Viewport.Size> viewport = 
                    (Event.NamedAction<Viewport.Size>)actor;

                this.setViewport( viewport.getValue());
            }
        }

        /*
         * Dispatch event
         */
        boolean re = false;

        for (Component c: this){
            /*
             * Broadcast
             */
            re = (c.input(e) || re);
        }

        /*
         * Output scene
         */
        if (action || re){

            this.outputScene();
        }
        return re;
    }
    public final boolean isMouseIn(){
        return this.mouseIn;
    }
    protected final void output1(Context g){

        if (this.output.requireOverlay()){

            this.outputOverlay(this.output.scene(this).blit(g));
        }
        else {
            Offscreen bScene = this.output.scene(this);

            Context gScene = bScene.create();
            try {
                this.outputScene(gScene);
            }
            finally {
                gScene.dispose();
            }
            bScene.blit(g);

            this.outputOverlay(g);
        }
    }
    protected final void output2(Context g){

        if (this.output.requireOverlay()){

            Offscreen bScene = this.output.scene(this);
            Offscreen bOverlay = this.output.overlay(this);
            Context gOverlay = bOverlay.create();
            try {

                bScene.blit(gOverlay);

                this.outputOverlay(gOverlay);

                bOverlay.blit(g);
            }
            finally {
                gOverlay.dispose();
            }
        }
        else {
            Offscreen bScene = this.output.scene(this);
            Offscreen bOverlay = this.output.overlay(this);
            Context gOverlay = bOverlay.create();
            try {
                Context gScene = bScene.create();
                try {
                    this.outputScene(gScene);
                }
                finally {
                    gScene.dispose();
                }
                bScene.blit(gOverlay);

                this.outputOverlay(gOverlay);

                bOverlay.blit(g);
            }
            finally {
                gOverlay.dispose();
            }
        }
    }
    public final Display outputScene(vector.Context g){

        this.output.completedScene();

        this.getTransformLocal().transformFrom(g);

        if (null != this.background){
            g.setColor(this.background);
            Bounds bounds = this.getBoundsVector();
            bounds.x = 0;
            bounds.y = 0;
            g.fill(bounds);
        }

        for (Component c: this){

            if (c.isVisible()){

                vector.Context cg = g.create();
                try {
                    c.outputScene(cg);
                }
                finally {
                    cg.dispose();
                }
            }
        }
        return this;
    }
    public final Display outputOverlay(vector.Context g){

        this.output.completedOverlay();

        this.getTransformLocal().transformFrom(g);

        for (Component c: this){

            if (c.isVisible()){

                vector.Context cg = g.create();
                try {
                    c.outputOverlay(cg);
                }
                finally {
                    cg.dispose();
                }
            }
        }
        return this;
    }
    public final Display outputScene(){
        this.output.requestScene();
        this.invalidate();
        return this;
    }
    public final Display outputOverlay(){
        this.output.requestOverlay();
        this.invalidate();
        return this;
    }
    public final Display outputOverlayAnimateSuspend(){
        Repainter outputOverlayAnimate = this.outputOverlayAnimate;
        if (null != outputOverlayAnimate){

            outputOverlayAnimate.suspend();
        }
        return this;
    }
    public final Display outputOverlayAnimateResume(){
        Repainter outputOverlayAnimate = this.outputOverlayAnimate;
        if (null != outputOverlayAnimate){

            outputOverlayAnimate.resume();
        }
        return this;
    }
    public final Display outputOverlayAnimateCancel(){
        Repainter outputOverlayAnimate = this.outputOverlayAnimate;
        if (null != outputOverlayAnimate){
            this.outputOverlayAnimate = null;
            outputOverlayAnimate.cancel();
        }
        return this;
    }
    public final Display outputOverlayAnimate(long period){
        Repainter outputOverlayAnimate = this.outputOverlayAnimate;
        if (null == outputOverlayAnimate){
            outputOverlayAnimate = new Repainter.Overlay(this);
            this.outputOverlayAnimate = outputOverlayAnimate;
            outputOverlayAnimate.start();
        }
        outputOverlayAnimate.period(period);
        return this;
    }

    public final Component.Iterator iterator(){
        return new Component.Iterator(this.components);
    }
    public final int count(){

        return Component.Tools.Count(this.components);
    }
    public final boolean has(int idx){

        return Component.Tools.Has(this.components,idx);
    }
    public final <C extends Component> C get(int idx){

        return (C)Component.Tools.Get(this.components,idx);
    }
    public final <C extends Component> C set(C comp, int idx){

        final Component old = Component.Tools.Set(this.components,comp,idx);

        old.destroy();

        comp.setParentVector(this);
        comp.init();

        return comp;
    }
    public final <C extends Component> C replace(C a, C b){

        return (C)Component.Tools.Replace(this.components,a,b);
    }
    public final int indexOf(Component comp){

        return Component.Tools.IndexOf(this.components,comp);
    }
    public final int indexOf(Class<? extends Component> compClass){

        return Component.Tools.IndexOf(this.components,compClass);
    }
    public final <C extends Component> C add(C comp){
        if (null != comp){
            this.components = Component.Tools.Add(this.components,comp);

            comp.setParentVector(this);
            comp.init();
        }
        return comp;
    }
    public <C extends Component> C insert(C comp, int idx){
        if (null != comp){
            this.components = Component.Tools.Insert(this.components,comp,idx);

            comp.setParentVector(this);
            comp.init();
        }
        return comp;
    }
    public final <C extends Component> C addUnique(C comp){
        int idx = Component.Tools.IndexOf(this.components,comp.getClass());
        if (-1 < idx)
            return (C)Component.Tools.Get(this.components,idx);
        else 
            return this.add(comp);
    }
    public final <C extends Component> C remove(C comp){
        return this.remove(Component.Tools.IndexOf(this.components,comp));
    }
    public final <C extends Component> C remove(int idx){
        C comp = null;
        if (-1 < idx){
            comp = (C)this.components[idx];

            this.components = Component.Tools.Remove(this.components,idx);
        }
        return comp;
    }
    public <C extends Component> Component.Iterator<C> listMouseIn(Class<C> clas){

        return Component.Tools.ListMouseIn(this.components,clas);
    }
    public <C extends Component> Component.Iterator<C> list(Class<C> clas){

        return Component.Tools.List(this.components,clas);
    }
    public <C extends Component> Component.Iterator<C> listContent(Class<C> clas){

        return Component.Tools.ListLayoutContent(this.components,clas);
    }
    public <C extends Component> Component.Iterator<C> listParent(Class<C> clas){

        return Component.Tools.ListLayoutContent(this.components,clas);
    }
    public final Display warn(Throwable t, String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args),t);

        return this;
    }
    public final Display error(Throwable t, String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args),t);

        return this;
    }
    public final Display error(String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args));

        return this;
    }
    public boolean drop(Component c){

        int idx = this.indexOf(c);
        if (-1 < idx){

            this.remove(idx).destroy();

            this.outputScene();

            return true;
        }
        else
            return false;
    }
    public boolean drop(Class<? extends Component> c){

        int idx = this.indexOf(c);
        if (-1 < idx){

            this.remove(idx).destroy();

            this.outputScene();

            return true;
        }
        else
            return false;
    }
    public Display show(Component c){
        if (null != c){
            int idx = this.indexOf(c.getClass());
            if (0 > idx){

                c = this.add(c);

                if (c instanceof Component.Build.InShow){

                    ((Component.Build.InShow)c).build();
                }

                c.setVisibleVector(true);

                this.modified();

                this.center(c);
            }
            else if (c.isVisible()){

                this.remove(idx).destroy();
            }
            else {
                c.setVisibleVector(true);

                this.modified();

                this.center(c);
            }
            this.outputScene();
        }
        return this;
    }
    public Display show(Class<? extends Component> c){
        if (null != c){
            int idx = this.indexOf(c);
            if (0 > idx){
                try {
                    Component cc = c.newInstance();

                    cc = this.add(cc);

                    if (cc instanceof Component.Build.InShow){

                        ((Component.Build.InShow)cc).build();
                    }

                    cc.setVisibleVector(true);

                    this.modified();

                    this.center(cc);
                }
                catch (InstantiationException exc){
                    throw new IllegalArgumentException(c.getName(),exc);
                }
                catch (IllegalAccessException exc){
                    throw new IllegalArgumentException(c.getName(),exc);
                }
            }
            else {

                this.remove(idx).destroy();
            }
            this.outputScene();
        }
        return this;
    }
    public Display center(Component d){

        if (null != d){

            final Bounds c = this.boundsUser;

            if (null != boundsUser){

                final Bounds b = d.getBoundsVector();

                b.x = (c.width/2)-(b.width/2);
                b.y = (c.height/2)-(b.height/2);

                d.setBoundsVector(b);
            }
        }
        return this;
    }
    public void shown(){

        this.outputOverlayAnimateResume();
    }
    public void hidden(){

        this.outputOverlayAnimateSuspend();
    }
    public void layout(){
    }

    @Override
    public void onResume() {
        super.onResume();

        this.shown();
    }
    @Override
    public void onPause() {
        super.onPause();

        this.hidden();
    }
    @Override
    protected void onMeasure(int psw, int psh){
        super.onMeasure(psw,psh);

        final int x = this.getLeft();
        final int y = this.getTop();
        final int w = this.getWidth();
        final int h = this.getHeight();

        this.boundsNative = new Bounds(x,y,w,h);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){

        this.boundsNative = new Bounds(left,top,(right-left),(bottom-top));

        this.modified();

        this.outputScene();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        Bounds b = this.boundsNative;

        if (null != b){

            final float x = b.x;
            final float y = b.y;

            this.boundsNative = new Bounds(x,y,w,h);
        }

        this.modified();

        this.outputScene();
    }
    @Override
    protected void onDraw(Canvas canvas) {

        final Context context = new Context(this,canvas);

        this.output1(context);
    }
    public void surfaceCreated(SurfaceHolder holder){
        super.surfaceCreated(holder);

        this.surface = holder;

        this.shown();
    }
    public void surfaceChanged(SurfaceHolder holder, int fmt, int w, int h){
        super.surfaceChanged(holder, fmt, w, h);

        this.surface = holder;
    }
    public void surfaceDestroyed(SurfaceHolder holder){
        super.surfaceDestroyed(holder);

        this.surface = null;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config){

        final Context context = this.wrap(gl);

        try {
            this.output1(context);
        }
        finally {
            context.disposeAndroid();
        }
    }
    public void onSurfaceChanged(GL10 gl, int width, int height){

        final Context context = this.wrap(gl);

        try {
            this.output1(context);
        }
        finally {
            context.disposeAndroid();
        }
    }
    public void onDrawFrame(GL10 gl){

        final Context context = this.wrap(gl);
        try {
            this.output1(context);
        }
        finally {
            context.disposeAndroid();
        }
    }

    public Json toJson(){
        Json thisModel = new ObjectJson();
        thisModel.setValue("class",this.getClass().getName());
        thisModel.setValue("init",Boolean.TRUE);
        thisModel.setValue("transform",this.transform);
        thisModel.setValue("bounds",this.getBoundsVector());
        thisModel.setValue("background",this.getBackgroundVector());
        thisModel.setValue("buffer-overlay",this.getBufferOverlay());
        thisModel.setValue("components",new ArrayJson(this));
        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.init( (Boolean)thisModel.getValue("init"));

        this.setTransformLocal( thisModel.getValue("transform",Transform.class));
        this.setBoundsVector( thisModel.getValue("bounds",Bounds.class));
        this.setBackground( thisModel.getValue("background",Color.class));
        this.setBufferOverlay( (Boolean)thisModel.getValue("buffer-overlay"));

        Component.Tools.DecodeComponents(this,thisModel);

        this.modified();

        this.outputScene();

        return true;
    }
    public boolean hasDocumentVector(){

        return (null != this.document);
    }
    public Document getDocumentVector(){

        return this.document;
    }
    public boolean open(File file){
        try {
            this.document = new Document(file);

            FileInputStream fin = new FileInputStream(file);
            try {
                Reader reader = new Reader();
                Json json = reader.read(fin);
                if (null != json)

                    return this.fromJson(json);
                else 
                    return false;
            }
            finally {
                fin.close();
            }
        }
        catch (IOException exc){

            this.error(exc,"Reading file '%s'",file);

            return false;
        }
    }
    public boolean open(URL url){
        try {
            this.document = new Document(url);

            InputStream uin = url.openStream();
            try {
                Reader reader = new Reader();
                Json json = reader.read(uin);
                if (null != json)

                    return this.fromJson(json);
                else 
                    return false;
            }
            finally {
                uin.close();
            }
        }
        catch (IOException exc){

            this.error(exc,"Reading URL '%s'",url);

            return false;
        }
    }
    public boolean copy(URL url){
        return false;
    }
    public boolean copy(File file){
        return false;
    }
    public boolean save(){
        return false;
    }

}
