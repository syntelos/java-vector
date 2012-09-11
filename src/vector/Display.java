package vector;

import json.ArrayJson;
import json.Json;
import json.ObjectJson;

import lxl.List;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Display
    extends java.awt.Canvas
    implements Component.Container,
               java.awt.event.KeyListener,
               java.awt.event.MouseListener,
               java.awt.event.MouseMotionListener,
               java.awt.event.MouseWheelListener,
               java.awt.event.ComponentListener
{

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Component[] components;

    private final AffineTransform transform = new AffineTransform();

    private final Output output = new Output();

    private boolean mouseIn;


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

        this.transform.setTransform(new AffineTransform());
    }
    protected void init(Boolean init){
        if (null != init && init.booleanValue()){

            this.init();
        }
    }
    public void destroy(){
        try {
            for (Component c: this)
                c.destroy();

            this.components = null;
        }
        finally {
            this.flush();
        }
    }
    protected void flush(){

        this.output.flush();
    }
    public Component.Container getParentVector(){

        return null;
    }
    public Component setParentVector(Component.Container parent){

        throw new UnsupportedOperationException();
    }
    public final Component setVisibleVector(boolean visible){
        super.setVisible(visible);
        return this;
    }
    public final Rectangle2D.Float getBoundsVector(){
        Rectangle bounds = super.getBounds();
        return new Rectangle2D.Float(bounds.x,bounds.y,bounds.width,bounds.height);
    }
    public final Component setBoundsVector(Rectangle2D.Float bounds){
        super.setBounds(new Rectangle((int)Math.floor(bounds.x),(int)Math.floor(bounds.y),(int)Math.ceil(bounds.width),(int)Math.ceil(bounds.height)));
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
    public final AffineTransform getTransformLocal(){

        return (AffineTransform)this.transform.clone();
    }
    protected Display setTransformLocal(AffineTransform transform){
        if (null != transform)
            this.transform.setTransform(transform);
        return this;
    }
    protected Display setTransformLocal(float sx, float sy){

        return this.setTransformLocal(AffineTransform.getScaleInstance(sx,sy));
    }
    public Display scaleTransformLocalRelative(Rectangle2D bounds){
        if (null != bounds){
            Rectangle2D thisBounds = this.getBoundsVector();
            float sw = (float)(thisBounds.getWidth()/(bounds.getX()+bounds.getWidth()));
            float sh = (float)(thisBounds.getHeight()/(bounds.getY()+bounds.getHeight()));

            this.transform.scale(sw,sh);
        }
        return this;
    }
    public Display scaleTransformLocalAbsolute(Rectangle2D bounds){
        if (null != bounds){
            Rectangle2D thisBounds = this.getBoundsVector();
            float sw = (float)(thisBounds.getWidth()/(bounds.getX()+bounds.getWidth()));
            float sh = (float)(thisBounds.getHeight()/(bounds.getY()+bounds.getHeight()));

            this.transform.setToScale(sw,sh);
        }
        return this;
    }
    public final AffineTransform getTransformParent(){

        final AffineTransform transform = this.getTransformLocal();

        final Point2D.Float location = this.getLocationVector();

        transform.translate(location.x,location.y);

        return transform;
    }
    public void resized(){

        for (Component c: this){

            c.resized();
        }
    }
    public void modified(){
    }
    public void relocated(){
    }
    public boolean input(Event e){
        return true;
    }
    public final boolean isMouseIn(){
        return this.mouseIn;
    }
    public final void update(Graphics g){

        this.output((Graphics2D)g);
    }
    public final void paint(Graphics g){

        this.output((Graphics2D)g);
    }
    protected final void output(Graphics2D g){

        if (this.output.requireOverlay()){

            this.outputOverlay(this.output.offscreen(this).blit(this,g));
        }
        else {
            Offscreen offscreen = this.output.offscreen(this);

            this.outputScene(offscreen.create());

            offscreen.blit(this,g);

            this.outputOverlay(g);
        }
    }
    public final void outputScene(Graphics2D g){

        this.output.completedScene();

        for (Component c: this){

            Graphics2D cg = (Graphics2D)g.create();
            try {
                c.outputScene(cg);
            }
            finally {
                cg.dispose();
            }
        }
    }
    public final void outputOverlay(Graphics2D g){

        this.output.completedOverlay();

        for (Component c: this){

            Graphics2D cg = (Graphics2D)g.create();
            try {
                c.outputOverlay(cg);
            }
            finally {
                cg.dispose();
            }
        }
    }
    public final Component.Container outputScene(){
        this.output.requestScene();
        this.repaint();
        return this;
    }
    public final Component.Container outputOverlay(){
        this.output.requestOverlay();
        this.repaint();
        return this;
    }

    public final java.util.Iterator<Component> iterator(){
        return new Component.Iterator(this.components);
    }
    public final int count(){

        return Component.Tools.Count(this.components);
    }
    public final boolean has(int idx){

        return Component.Tools.Has(this.components,idx);
    }
    public final Component get(int idx){

        return Component.Tools.Get(this.components,idx);
    }
    public final int indexOf(Component comp){

        return Component.Tools.IndexOf(this.components,comp);
    }
    public final int indexOf(Class<? extends Component> compClass){

        return Component.Tools.IndexOf(this.components,compClass);
    }
    public final Component add(Component comp){
        if (null != comp){
            this.components = Component.Tools.Add(this.components,comp);

            comp.setParentVector(this);
            comp.init();
        }
        return comp;
    }
    public final Component addUnique(Component comp){
        int idx = Component.Tools.IndexOf(this.components,comp.getClass());
        if (-1 < idx)
            return Component.Tools.Get(this.components,idx);
        else 
            return this.add(comp);
    }
    public final Component remove(Component comp){
        return this.remove(Component.Tools.IndexOf(this.components,comp));
    }
    public final Component remove(int idx){
        Component comp = null;
        if (-1 < idx){
            comp = this.components[idx];

            this.components = Component.Tools.Remove(this.components,idx);
        }
        return comp;
    }
    public Component.Iterator listMouseIn(){

        return Component.Tools.ListMouseIn(this.components);
    }
    public final Display warn(Throwable t, String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args),t);

        return this;
    }
    public final Display error(Throwable t, String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args),t);

        return this;
    }
    protected Point2D.Float transformFromParent(Point2D point){
        /*
         * The transform arithmetic is double, and the point class
         * will store to float
         */
        return (Point2D.Float)this.getTransformParent().transform(point,new Point2D.Float(0,0));
    }

    public void mouseClicked(MouseEvent evt){
    }
    public void mousePressed(MouseEvent evt){

        final Event.Mouse.Action action = Event.Mouse.Action.PointButton(evt);

        if (null != action){
            final Point2D.Float point = this.transformFromParent(evt.getPoint());

            final Event e = new AbstractEvent.AbstractMouse.AbstractMousePoint.Down(action,point);

            for (Component c: this){

                if (c.input(e))
                    break;
            }
        }
    }
    public void mouseReleased(MouseEvent evt){

        final Event.Mouse.Action action = Event.Mouse.Action.PointButton(evt);

        if (null != action){
            final Point2D.Float point = this.transformFromParent(evt.getPoint());

            final Event e = new AbstractEvent.AbstractMouse.AbstractMousePoint.Up(action,point);

            for (Component c: this){

                if (c.input(e))
                    break;
            }
        }
    }
    public void mouseEntered(MouseEvent evt){
        this.mouseIn = true;

        final Point2D.Float point = this.transformFromParent(evt.getPoint());

        final Event entered = new AbstractEvent.AbstractMouse.AbstractMouseMotion.Entered(point);

        final Event exited = new AbstractEvent.AbstractMouse.AbstractMouseMotion.Exited(point);

        for (Component c: this){

            if (c.contains(point)){

                c.input(entered);
            }
        }
    }
    public void mouseExited(MouseEvent evt){
        this.mouseIn = false;

        final Point2D.Float point = this.transformFromParent(evt.getPoint());

        final Event exited = new AbstractEvent.AbstractMouse.AbstractMouseMotion.Exited(point);

        for (Component c: this.listMouseIn()){

            c.input(exited);
        }
    }
    public void mouseDragged(MouseEvent evt){

        final Event.Mouse.Action action = Event.Mouse.Action.PointButton(evt);

        if (null != action){
            final Point2D.Float point = this.transformFromParent(evt.getPoint());

            final Event dragged = new AbstractEvent.AbstractMouse.AbstractMousePoint.Drag(action,point);

            for (Component c: this){

                if (c.input(dragged))
                    break;
            }
        }
    }
    public void mouseMoved(MouseEvent evt){

        final Point2D.Float point = this.transformFromParent(evt.getPoint());

        final Event moved = new AbstractEvent.AbstractMouse.AbstractMouseMotion.Moved(point);

        final Event entered = new AbstractEvent.AbstractMouse.AbstractMouseMotion.Entered(point);

        final Event exited = new AbstractEvent.AbstractMouse.AbstractMouseMotion.Exited(point);

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

        Event e = new AbstractEvent.AbstractMouse.AbstractMouseWheel(evt.getWheelRotation());

        for (Component c: this){

            if (c.input(e))
                break;
        }
    }
    public void keyTyped(KeyEvent e){
    }
    public void keyPressed(KeyEvent e){
    }
    public void keyReleased(KeyEvent e){
    }
    public final void componentResized(ComponentEvent evt){
        this.flush();
        this.resized();
        this.repaint();
    }
    public final void componentMoved(ComponentEvent evt){
    }
    public final void componentShown(ComponentEvent evt){
    }
    public final void componentHidden(ComponentEvent evt){
    }

    public Json toJson(){
        Json thisModel = new ObjectJson();
        thisModel.setValue("class",this.getClass().getName());
        thisModel.setValue("init",Boolean.TRUE);
        thisModel.setValue("transform",this.transform);
        thisModel.setValue("bounds",this.getBounds());
        thisModel.setValue("components",new ArrayJson(this));
        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.init( (Boolean)thisModel.getValue("init"));

        this.setTransformLocal( Component.Tools.DecodeTransform(thisModel.getValue("transform")));

        this.scaleTransformLocalRelative( Component.Tools.DecodeBounds(thisModel.getValue("bounds")));

        Component.Tools.DecodeComponents(this,thisModel);

        this.outputScene();

        return true;
    }
}
