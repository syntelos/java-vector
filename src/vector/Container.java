package vector;

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

public class Container
    extends AbstractComponent
    implements Component.Container
{

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Color background;

    protected Component[] components;


    public Container(){
        super();
    }


    @Override
    public void destroy(){
        super.destroy();
        try {
            for (Component c: this){
                c.destroy();
            }
        }
        finally {
            this.components = null;
        }
    }
    public void resized(){

        for (Component c: this){

            c.resized();
        }
    }
    public final Color getBackground(){

        return this.background;
    }
    public final Container setBackground(Color background){
        if (null != background){
            this.background = background;
        }
        return this;
    }
    public final Container setBackground(String code){
        if (null != code)
            return this.setBackground(new Color(code));
        else
            return this;
    }
    public boolean input(Event e){

        switch(e.getType()){
        case MouseEntered:{

            final Point2D.Float point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());
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
            return false;
        }
        case MouseExited:{
            final Event m = ((Event.Mouse)e).apply(this.getTransformParent());

            for (Component c: this.listMouseIn()){

                c.input(m);
            }
            return false;
        }
        case MouseMoved:{
            final Point2D.Float point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());

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
            return false;
        }
        case MouseDown:
        case MouseUp:{
            final Event m = ((Event.Mouse)e).apply(this.getTransformParent());
            for (Component c: this){
                if (c.input(m))
                    return true;
            }
            return false;
        }
        case MouseDrag:{
            final Event.Mouse.Point m = (Event.Mouse.Point)e;
            final Point2D.Float point = this.transformFromParent(m.getPoint());
            final Event dragged = new vector.event.MouseDrag(m,point);
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
            return false;
        }
        case MouseWheel:
        case KeyDown:
        case KeyUp:
        case Action:
            for (Component c: this){
                if (c.input(e))
                    return true;
            }
            return false;
        default:
            throw new IllegalStateException(e.getType().name());
        }
    }
    public final Container outputScene(Graphics2D g){

        if (null != this.background){
            g.setColor(this.background);
            g.fill(this.getBoundsVector());
        }
        g.transform(this.getTransformParent());

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
    public final Container outputOverlay(Graphics2D g){

        g.transform(this.getTransformParent());

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
    @Override
    public Container outputScene(){
        return (Container)super.outputScene();
    }
    public Container outputOverlay(){
        return (Container)super.outputOverlay();
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

            this.modified();
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

            this.modified();
        }
        return comp;
    }
    public Component.Iterator listMouseIn(){

        return Component.Tools.ListMouseIn(this.components);
    }
    public final Container warn(Throwable t, String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args),t);

        return this;
    }
    public final Container error(Throwable t, String fmt, Object... args){

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

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("background",this.getBackground());
        thisModel.setValue("components",new ArrayJson(this));

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.setBackground( thisModel.getValue("background",Color.class));

        Component.Tools.DecodeComponents(this,thisModel);

        return true;
    }
}
