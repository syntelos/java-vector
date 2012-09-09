package vector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Display
    extends java.awt.Canvas
    implements Component.Container,
               java.awt.event.KeyListener,
               java.awt.event.MouseListener,
               java.awt.event.MouseMotionListener,
               java.awt.event.ComponentListener
{

    protected Component[] components;

    private final AffineTransform local = new AffineTransform();

    private boolean mouseIn;


    public Display(){
        super();
        super.addKeyListener(this);
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
        super.addComponentListener(this);
    }


    public void destroy(){
    }
    public Component.Container getParentVector(){

        return null;
    }
    public Component setParentVector(Component.Container parent){

        throw new UnsupportedOperationException();
    }
    public Component setVisibleVector(boolean visible){
        super.setVisible(visible);
        return this;
    }
    public Rectangle2D.Float getBoundsVector(){
        Rectangle bounds = super.getBounds();
        return new Rectangle2D.Float(bounds.x,bounds.y,bounds.width,bounds.height);
    }
    public Component setBoundsVector(Rectangle2D.Float bounds){
        super.setBounds(new Rectangle((int)Math.floor(bounds.x),(int)Math.floor(bounds.y),(int)Math.ceil(bounds.width),(int)Math.ceil(bounds.height)));
        return this;
    }
    public boolean contains(float x, float y){
        return super.contains( (int)x, (int)y);
    }
    public boolean contains(Point2D.Float p){
        return super.contains( (int)p.x, (int)p.y);
    }
    public Point2D.Float getLocationVector(){
        Point p = super.getLocation();
        return new Point2D.Float(p.x,p.y);
    }
    public Component setLocationVector(Point2D p){
        super.setLocation( (int)p.getX(), (int)p.getY());
        return this;
    }
    public AffineTransform getTransformLocal(){

        return (AffineTransform)this.local.clone();
    }
    public AffineTransform getTransformParent(){
        Point2D.Float location = this.getLocationVector();
        return AffineTransform.getTranslateInstance(location.x,location.y);
    }
    public void resized(){

        for (Component c: this){

            c.resized();
        }
    }
    public boolean input(Event e){
        return true;
    }
    public boolean isMouseIn(){
        return this.mouseIn;
    }
    public final void update(Graphics g){

        this.output((Graphics2D)g);
    }
    public final void paint(Graphics g){

        this.output((Graphics2D)g);
    }
    public void output(Graphics2D g){

        for (Component c: this){

            Graphics2D cg = (Graphics2D)g.create();
            try {
                c.output(cg);
            }
            finally {
                cg.dispose();
            }
        }
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

    public void mouseClicked(MouseEvent evt){
    }
    public void mousePressed(MouseEvent evt){
    }
    public void mouseReleased(MouseEvent evt){
    }
    public void mouseEntered(MouseEvent evt){
        this.mouseIn = true;

    }
    public void mouseExited(MouseEvent evt){
        this.mouseIn = false;

    }
    public void mouseDragged(MouseEvent evt){
    }
    public void mouseMoved(MouseEvent evt){
    }
    public void keyTyped(KeyEvent e){
    }
    public void keyPressed(KeyEvent e){
    }
    public void keyReleased(KeyEvent e){
    }
    public final void componentResized(ComponentEvent evt){
        this.resized();
        this.repaint();
    }
    public final void componentMoved(ComponentEvent evt){
    }
    public final void componentShown(ComponentEvent evt){
    }
    public final void componentHidden(ComponentEvent evt){
    }
}
