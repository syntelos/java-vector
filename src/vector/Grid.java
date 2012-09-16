package vector;

import json.Json;
import json.ObjectJson;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * <p> This class will assume the bounds and transform of the
 * immediate successor in the parent's list of children, and draw a
 * grid as defined by the domain and range.  It can optionally
 * track mouse motion. </p>
 * 
 * <h3>Domain &amp; Range</h3>
 * 
 * <p> The domain and range lists are coordinates in the successor's
 * shape coordinate space, under the successor's local transform. </p>
 * 
 * <h3>Grid Future</h3>
 * 
 * <p> Instances of this class are intended as visual display
 * predecessors to their successors: they paint first so the successor
 * paints over them.  </p>
 * 
 * <p> This requirement imposes a temporal conflict: the grid will
 * initialize before the objective successor from which it derives
 * critical state. </p>
 * 
 * <p> Therefore application of this class requires that the Grid
 * {@link #init()} method be called (again) after the scene graph has
 * loaded and initialized (all components added).  A repaint may be
 * required to display the effects of this reinitialization. </p>
 * 
 */
public class Grid
    extends AbstractComponent
    implements TailInit
{

    protected Color color = Color.black;

    protected boolean mouse;

    protected float[] domain, range;

    protected Path2D.Float shape, pointer;


    public Grid(){
        super();
    }

    /**
     * Grid Future: called after defining user properties in Grid
     */
    @Override
    public void init(){
        super.init();

        this.shape = null;
        this.pointer = null;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.shape = null;
        this.pointer = null;
    }
    /**
     * Calls {@link #layout()}
     */
    @Override
    public void resized(){
        super.resized();

        this.shape = null;
        this.pointer = null;

        this.layout();
    }
    /**
     * Clears the visual state derived from properties, and calls
     * {@link #layout()}
     */
    @Override
    public void modified(){
        super.modified();

        this.shape = null;
        this.pointer = null;

        this.layout();
    }
    @Override
    public void relocated(){
        super.relocated();

        this.shape = null;
        this.pointer = null;

        this.layout();
    }
    public final Color getColor(){

        return this.color;
    }
    public final Grid setColor(Color color){
        if (null != color){
            this.color = color;
        }
        return this;
    }
    public final Grid setColor(String code){
        if (null != code)
            return this.setColor(new Color(code));
        else
            return this;
    }
    public final boolean isMouse(){
        return this.mouse;
    }
    public final Boolean getMouse(){
        return this.mouse;
    }
    public final Grid setMouse(boolean mouse){
        this.mouse = mouse;
        return this;
    }
    public final Grid setMouse(Boolean mouse){
        if (null != mouse)
            this.mouse = mouse;
        return this;
    }
    public final float[] getDomain(){
        float[] domain = this.domain;
        if (null != domain)
            return domain.clone();
        else
            return new float[0];
    }
    public final Grid setDomain(float[] domain){

        if (null != domain && 0 < domain.length)

            this.domain = domain.clone();
        else
            this.domain = null;

        this.modified();
        return this;
    }
    public final float[] getRange(){
        float[] range = this.range;
        if (null != range)
            return range.clone();
        else
            return new float[0];
    }
    public final Grid setRange(float[] range){

        if (null != range && 0 < range.length)

            this.range = range.clone();
        else
            this.range = null;

        this.modified();
        return this;
    }
    protected Path2D.Float shape(){
        if (null == this.shape){
            /*
             * Producing property defaults into fields will obscure
             * the user's intended state of these properties (range
             * and domain) from the life cycle operators -- and then
             * propagate incorrect state via JSON
             */
            final Bounds bounds = this.getBoundsVector();
            if (!bounds.isEmpty()){
                float[] range = this.range;
                if (null == range){
                    range = Default(bounds.height);
                }
                float[] domain = this.domain;
                if (null == domain){
                    domain = Default(bounds.width);
                }

                final float x0 = 0f;
                final float x1 = bounds.width;
                final float y0 = 0f;
                final float y1 = bounds.height;

                final Path2D.Float shape = new Path2D.Float();

                for (float x: domain){
                    shape.moveTo(x,y0);
                    shape.lineTo(x,y1);
                }
                for (float y: range){
                    shape.moveTo(x0,y);
                    shape.lineTo(x1,y);
                }
                return (this.shape = shape);
            }
        }
        return this.shape;
    }
    public Grid outputScene(Graphics2D g){
        Shape shape = this.shape();
        if (null != shape){
            g.transform(this.getTransformParent());
            g.setColor(this.getColor());
            g.draw(shape);
        }
        return this;
    }
    public Grid outputOverlay(Graphics2D g){
        Shape shape = this.pointer;
        if (null != shape){
            g.setColor(this.getColor());
            g.draw(shape);
        }
        return this;
    }
    @Override
    public boolean input(Event e){

        switch(e.getType()){

        case MouseEntered:
            this.mouseIn = true;
            return true;
        case MouseExited:
            this.mouseIn = false;
            if (this.mouse){
                this.pointer = null;
                this.outputOverlay();
            }
            return true;
        case MouseMoved:
            if (this.mouse){

                final float radius = Radius(this.getBoundsVector());

                final Point2D mouse = ((Event.Mouse.Motion)e).getPoint();
                {
                    final float x = (float)mouse.getX();
                    final float y = (float)mouse.getY();
                    final float x0 = (x-radius);
                    final float x1 = (x+radius);
                    final float y0 = (y-radius);
                    final float y1 = (y+radius);

                    final Path2D.Float pointer = new Path2D.Float();
                    {
                        pointer.moveTo(x0,y);
                        pointer.lineTo(x1,y);
                        pointer.moveTo(x,y0);
                        pointer.lineTo(x,y1);
                    }
                    this.pointer = pointer;
                }
                this.outputOverlay();
            }
            return false;
        default:
            return false;
        }
    }
    /**
     * Called from {@link #modified()}, {@link #resized()}, and {@link
     * #relocated()}.  Employ a successor, or if not found, the
     * parent.
     */
    protected void layout(){
        /*
         *
         */
        Component.Container parent = this.getParentVector();
        if (null != parent){
            int idx = parent.indexOf(this);
            if (-1 < idx){
                idx += 1;
                if (parent.has(idx)){
                    final Component successor = parent.get(idx);

                    this.setBoundsVector(successor.getBoundsVector());
                    this.setTransformLocal(successor.getTransformLocal());
                }
                else {
                    this.setBoundsVectorInit(parent);
                    this.setTransformLocal(parent.getTransformLocal());
                }
            }
        }
    }

    public ObjectJson toJson(){
        ObjectJson thisModel = (ObjectJson)super.toJson();

        thisModel.setValue("color", this.getColor());

        thisModel.setValue("mouse",this.getMouse());

        thisModel.setValue("domain", this.getDomain());

        thisModel.setValue("range", this.getRange());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){
        super.fromJson(thisModel);

        this.setColor( (Color)thisModel.getValue("color",Color.class));

        this.setMouse( (Boolean)thisModel.getValue("mouse"));

        this.setDomain( (float[])thisModel.getValue("domain",float[].class));

        this.setRange( (float[])thisModel.getValue("range",float[].class));

        return true;
    }


    public final static float[] Default(double end){
        float[] list = new float[10];
        double start = 0.0;
        double inc = (end / 9.0);
        for (int cc = 0; cc < 10; cc++, start += inc){
            list[cc] = (float)start;
        }
        return list;
    }
    public final static float Radius(Bounds dim){
        float gross = Math.max(dim.width,dim.height);
        if (100f < gross)
            return (0.01f * gross);
        else
            return (0.05f * gross);
    }
}
