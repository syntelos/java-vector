package vector;

import vector.event.Repainter;

import json.ArrayJson;
import json.Json;
import json.ObjectJson;

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

    protected Color background;

    protected Component[] components;

    private final AffineTransform transform = new AffineTransform();

    private final Output output = new Output();

    private Repainter outputOverlayAnimate;

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
        this.outputOverlayAnimateCancel();
        try {
            for (Component c: this){
                c.destroy();
            }
        }
        finally {
            this.components = null;

            this.flush();
        }
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

            Graphics2D og = offscreen.create();
            try {
                this.outputScene(og);
            }
            finally {
                og.dispose();
            }
            offscreen.blit(this,g);

            this.outputOverlay(g);
        }
    }
    public final Display outputScene(Graphics2D g){

        this.output.completedScene();

        if (null != this.background){
            g.setColor(this.background);
            Rectangle2D.Float bounds = this.getBoundsVector();
            bounds.x = 0;
            bounds.y = 0;
            g.fill(bounds);
        }

        for (Component c: this){

            Graphics2D cg = (Graphics2D)g.create();
            try {
                c.outputScene(cg);
            }
            finally {
                cg.dispose();
            }
        }
        return this;
    }
    public final Display outputOverlay(Graphics2D g){

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

        for (Component c: this.listMouseIn()){

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

    public Json toJson(){
        Json thisModel = new ObjectJson();
        thisModel.setValue("class",this.getClass().getName());
        thisModel.setValue("init",Boolean.TRUE);
        thisModel.setValue("transform",this.transform);
        thisModel.setValue("bounds",this.getBounds());
        thisModel.setValue("background",this.getBackground());
        thisModel.setValue("components",new ArrayJson(this));
        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.init( (Boolean)thisModel.getValue("init"));

        this.setTransformLocal( Component.Tools.DecodeTransform(thisModel.getValue("transform")));

        this.scaleTransformLocalRelative( Component.Tools.DecodeBounds(thisModel.getValue("bounds")));

        this.setBackground( thisModel.getValue("background",Color.class));

        Component.Tools.DecodeComponents(this,thisModel);

        this.outputScene();

        return true;
    }
}
