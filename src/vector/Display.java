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

import vector.event.Repainter;

import json.ArrayJson;
import json.Json;
import json.ObjectJson;
import json.Reader;

import lxl.List;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Display<T extends Component>
    extends java.awt.Canvas
    implements Component.Container<T>,
               java.awt.event.KeyListener,
               java.awt.event.MouseListener,
               java.awt.event.MouseMotionListener,
               java.awt.event.MouseWheelListener,
               java.awt.event.ComponentListener
{

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Color background;

    protected Component[] components;

    protected final Transform transform = new Transform();

    protected final Output output = new Output();

    protected Repainter outputOverlayAnimate;

    protected boolean mouseIn;

    protected Rectangle2D boundsNative;

    protected Bounds boundsUser;


    public Display(){
        super();
        super.addKeyListener(this);
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
        super.addMouseWheelListener(this);
        super.addComponentListener(this);
    }


    public void init(){

        this.destroy();

        this.mouseIn = false;

        this.transform.init();

        this.layout();
    }
    protected void init(Boolean init){
        if (null != init && init.booleanValue()){

            this.init();
        }
    }
    public void destroy(){
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
    protected void flush(){

        this.output.flush();
    }
    public final Color getBackground(){

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
        super.setVisible(visible);
        return this;
    }
    public final Bounds getBoundsVector(){

        if (null == this.boundsUser)

            return new Bounds(super.getBounds());
        else
            return this.boundsUser.clone();
    }
    public final Component setBoundsVector(Bounds bounds){
        if (null != bounds){
            this.boundsUser = bounds;
        }
        return this;
    }
    public final Display setBounds(Bounds bounds){
        if (null != bounds){
            super.setBounds(new Rectangle((int)Math.floor(bounds.x),(int)Math.floor(bounds.y),(int)Math.ceil(bounds.width),(int)Math.ceil(bounds.height)));
        }
        return this;
    }
    public Component setBoundsVectorForScale(Bounds bounds){

        this.setBoundsVector(bounds);

        return this;
    }
    public final boolean contains(float x, float y){
        return super.contains( (int)x, (int)y);
    }
    public final boolean contains(Point2D.Float p){
        return super.contains( (int)p.x, (int)p.y);
    }
    public final Point2D.Float getLocationVector(){
        Point p = super.getLocation();
        return new Point2D.Float(p.x,p.y);
    }
    public final Component setLocationVector(Point2D p){
        super.setLocation( (int)p.getX(), (int)p.getY());
        return this;
    }
    public final Transform getTransformLocal(){

        return this.transform.clone();
    }
    protected Display setTransformLocal(Transform transform){
        if (null != transform)
            this.transform.setTransform(transform);
        return this;
    }
    protected Display setTransformLocal(float sx, float sy){

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
        boolean re = false;

        for (Component c: this){
            /*
             * Broadcast
             */
            re = (c.input(e) || re);
        }
        return re;
    }
    public final boolean isMouseIn(){
        return this.mouseIn;
    }
    public final void update(Graphics g){

        this.output1((Graphics2D)g);
    }
    public final void paint(Graphics g){

        this.output1((Graphics2D)g);
    }
    protected final void output1(Graphics2D g){

        if (this.output.requireOverlay()){

            this.outputOverlay(this.output.scene(this).blit(this,g));
        }
        else {
            Offscreen scene = this.output.scene(this);

            Graphics2D gscene = scene.create();
            try {
                this.outputScene(gscene);
            }
            finally {
                gscene.dispose();
            }
            scene.blit(this,g);

            this.outputOverlay(g);
        }
    }
    protected final void output2(Graphics2D g){

        if (this.output.requireOverlay()){

            this.outputOverlay(this.output.overlay(this).blit(this,g));
        }
        else {
            Offscreen scene = this.output.scene(this);
            Offscreen overlay = this.output.overlay(this);
            Graphics2D goverlay = overlay.create();
            try {
                Graphics2D gscene = scene.create();
                try {
                    this.outputScene(gscene);
                }
                finally {
                    gscene.dispose();
                }
                scene.blit(this,goverlay);

                this.outputOverlay(goverlay);

                overlay.blit(this,g);
            }
            finally {
                goverlay.dispose();
            }
        }
    }
    public final Display outputScene(Graphics2D g){

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

                Graphics2D cg = (Graphics2D)g.create();
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
    public final Display outputOverlay(Graphics2D g){

        this.output.completedOverlay();

        this.getTransformLocal().transformFrom(g);

        for (Component c: this){

            if (c.isVisible()){

                Graphics2D cg = (Graphics2D)g.create();
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
        this.repaint();
        return this;
    }
    public final Display outputOverlay(){
        this.output.requestOverlay();
        this.repaint();
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

    public final java.util.Iterator<T> iterator(){
        return new Component.Iterator(this.components);
    }
    public final int count(){

        return Component.Tools.Count(this.components);
    }
    public final boolean has(int idx){

        return Component.Tools.Has(this.components,idx);
    }
    public final T get(int idx){

        return (T)Component.Tools.Get(this.components,idx);
    }
    public final int indexOf(Component comp){

        return Component.Tools.IndexOf(this.components,comp);
    }
    public final int indexOf(Class<? extends Component> compClass){

        return Component.Tools.IndexOf(this.components,compClass);
    }
    public final T add(T comp){
        if (null != comp){
            this.components = Component.Tools.Add(this.components,comp);

            comp.setParentVector(this);
            comp.init();
        }
        return comp;
    }
    public T insert(T comp, int idx){
        if (null != comp){
            this.components = Component.Tools.Insert(this.components,comp,idx);

            comp.setParentVector(this);
            comp.init();
        }
        return comp;
    }
    public final T addUnique(T comp){
        int idx = Component.Tools.IndexOf(this.components,comp.getClass());
        if (-1 < idx)
            return (T)Component.Tools.Get(this.components,idx);
        else 
            return this.add(comp);
    }
    public final T remove(T comp){
        return this.remove(Component.Tools.IndexOf(this.components,comp));
    }
    public final T remove(int idx){
        T comp = null;
        if (-1 < idx){
            comp = (T)this.components[idx];

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
    protected final Point2D.Float transformFromParent(Point2D point){

        return this.getTransformParent().transformFrom(point);
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

    public void mouseClicked(MouseEvent evt){
    }
    public void mousePressed(MouseEvent evt){

        final Event.Mouse.Action action = Event.Mouse.Action.PointButton(evt);

        if (null != action){
            final Point2D.Float point = this.transformFromParent(evt.getPoint());

            final Event down = new vector.event.MouseDown(action,point);

            for (Component c: this){

                if (c.input(down))
                    break;
            }
        }
    }
    public void mouseReleased(MouseEvent evt){

        final Event.Mouse.Action action = Event.Mouse.Action.PointButton(evt);

        if (null != action){
            final Point2D.Float point = this.transformFromParent(evt.getPoint());

            final Event up = new vector.event.MouseUp(action,point);

            for (Component c: this){

                if (c.input(up))
                    break;
            }
        }
    }
    public void mouseEntered(MouseEvent evt){
        this.requestFocus();

        this.mouseIn = true;

        final Point2D.Float point = this.transformFromParent(evt.getPoint());

        final Event entered = new vector.event.MouseEntered(point);

        final Event exited = new vector.event.MouseExited(point);

        for (Component c: this){

            if (c.contains(point)){

                c.input(entered);
            }
            else if (c.isMouseIn()){

                c.input(exited);
            }
        }
    }
    public void mouseExited(MouseEvent evt){
        this.mouseIn = false;

        final Point2D.Float point = this.transformFromParent(evt.getPoint());

        final Event exited = new vector.event.MouseExited(point);

        for (Component c: this.listMouseIn(Component.class)){

            c.input(exited);
        }
    }
    public void mouseDragged(MouseEvent evt){

        final Event.Mouse.Action action = Event.Mouse.Action.PointButton(evt);

        if (null != action){
            final Point2D.Float point = this.transformFromParent(evt.getPoint());

            final Event dragged = new vector.event.MouseDrag(action,point);

            final Event entered = new vector.event.MouseEntered(point);

            final Event exited = new vector.event.MouseExited(point);

            for (Component c: this){

                if (c.isMouseIn()){

                    if (c.contains(point)){

                        c.input(dragged);
                    }
                    else {
                        c.input(exited);
                    }
                }
                else if (c.contains(point)){
                    c.input(entered);
                }
            }
        }
    }
    public void mouseMoved(MouseEvent evt){

        final Point2D.Float point = this.transformFromParent(evt.getPoint());

        final Event moved = new vector.event.MouseMoved(point);

        final Event entered = new vector.event.MouseEntered(point);

        final Event exited = new vector.event.MouseExited(point);

        for (Component c: this){

            if (c.isMouseIn()){

                if (c.contains(point)){

                    c.input(moved);
                }
                else {
                    c.input(exited);
                }
            }
            else if (c.contains(point)){
                c.input(entered);
            }
        }
    }
    public void mouseWheelMoved(MouseWheelEvent evt){

        final Event e = new vector.event.MouseWheel(evt.getWheelRotation());

        for (Component c: this){

            if (c.input(e))
                break;
        }
    }
    public void keyTyped(KeyEvent e){
    }
    public void keyPressed(KeyEvent evt){

        final Event e = new vector.event.KeyDown(evt);

        for (Component c: this){

            if (c.input(e))
                break;
        }
    }
    public void keyReleased(KeyEvent evt){

        final Event e = new vector.event.KeyUp(evt);

        for (Component c: this){

            if (c.input(e))
                break;
        }
    }
    public final void componentResized(ComponentEvent evt){
        this.flush();
        this.boundsNative = this.getBounds();
        this.resized();
        this.repaint();
    }
    public final void componentMoved(ComponentEvent evt){
    }
    public final void componentShown(ComponentEvent evt){
        this.requestFocus();
        this.shown();
    }
    public final void componentHidden(ComponentEvent evt){
        this.hidden();
    }
    protected void shown(){

        this.outputOverlayAnimateResume();
    }
    protected void hidden(){

        this.outputOverlayAnimateSuspend();
    }
    @Override
    public void layout(){
        if (null == this.boundsNative){
            this.boundsNative = this.getBounds();
        }
        if (null == this.boundsUser){
            this.boundsUser = new Bounds(this.boundsNative);
        }
        this.transform.scaleToAbsolute(this.boundsUser,this.boundsNative);
    }

    public Json toJson(){
        Json thisModel = new ObjectJson();
        thisModel.setValue("class",this.getClass().getName());
        thisModel.setValue("init",Boolean.TRUE);
        thisModel.setValue("transform",this.transform);
        thisModel.setValue("bounds",this.getBoundsVector());
        thisModel.setValue("background",this.getBackground());
        thisModel.setValue("components",new ArrayJson(this));
        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.init( (Boolean)thisModel.getValue("init"));

        this.setTransformLocal( thisModel.getValue("transform",Transform.class));

        this.setBoundsVectorForScale( thisModel.getValue("bounds",Bounds.class));

        this.setBackground( thisModel.getValue("background",Color.class));

        Component.Tools.DecodeComponents(this,thisModel);

        this.modified();

        this.outputScene();

        return true;
    }
    public boolean open(File file){
        try {
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

            this.warn(exc,"Error reading file '%s'",file);

            return false;
        }
    }
    public boolean open(URL url){
        try {
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

            this.warn(exc,"Error reading URL '%s'",url);

            return false;
        }
    }
}
