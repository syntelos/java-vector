package vector;

import json.Json;
import json.ObjectJson;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Common implementation of {@link Component} for convenience
 */
public abstract class AbstractComponent
    extends Object
    implements Component
{

    protected boolean visible = true;

    protected Component.Container parent;

    protected final AffineTransform transform = new AffineTransform();

    protected final Rectangle2D.Float bounds = new Rectangle2D.Float(0,0,0,0);


    public AbstractComponent(){
        super();
    }


    /**
     * Create or restore the state following construction.
     * 
     * This method, defined here, sets the bounds of this component to
     * those of the parent.
     */
    public void init(){

        Component.Container parent = this.getParentVector();

        this.destroy();

        this.setTransformLocal(new AffineTransform());

        this.setBoundsVectorInit(parent);

        this.setParentVector(parent);
    }
    protected void init(Boolean init){
        if (null != init && init.booleanValue()){

            this.init();
        }
    }
    /**
     * This method, defined here, restores default visibility (true)
     * and releases the reference to the parent container (sets field
     * parent null).
     */
    public void destroy(){

        this.visible = true;
        this.parent = null;
    }
    /**
     * Overriding this method should call this method via
     * <code>super.resized()</code>
     */
    public void resized(){
    }
    /**
     * Overriding this method should call this method via
     * <code>super.modified()</code>
     */
    public void modified(){
    }
    /**
     * Overriding this method should call this method via
     * <code>super.relocated()</code>
     */
    public void relocated(){
    }
    public final Component.Container getParentVector(){
        return this.parent;
    }
    public final Component setParentVector(Component.Container parent){
        if (null != parent){
            if (null != this.parent)
                throw new IllegalStateException();
            else {
                this.parent = parent;

                return this;
            }
        }
        else
            throw new IllegalArgumentException();
    }
    public final boolean isVisible(){
        return this.visible;
    }
    public Component setVisibleVector(boolean visible){
        this.visible = visible;
        return this;
    }
    public final Rectangle2D.Float getBoundsVector(){

        return (Rectangle2D.Float)this.bounds.clone();
    }
    public final Component setBoundsVector(Rectangle2D.Float bounds){

        this.bounds.setFrame(bounds);

        return this;
    }
    /**
     * Dimensions of component for resizing to the parent with
     * component natural origin (0,0).  The component location is
     * changed to (0,0).
     */
    protected Component setBoundsVectorInit(Component component){
        Rectangle2D.Float bounds = component.getBoundsVector();
        bounds.x = 0f;
        bounds.y = 0f;

        return this.setBoundsVector(bounds);
    }
    /**
     * Union of origin and bounds for resizing to these extents of
     * within a component.  The component location is unchanged.
     */
    protected Component setBoundsVectorInit(Rectangle2D.Float bounds){

        bounds.width += bounds.x;
        bounds.height += bounds.y;
        bounds.x = this.bounds.x;
        bounds.y = this.bounds.y;

        return this.setBoundsVector(bounds);
    }
    public final boolean contains(int x, int y){

        return this.bounds.contains(x,y);
    }
    public final boolean contains(float x, float y){

        return this.bounds.contains(x,y);
    }
    public final boolean contains(Point2D.Float p){

        return this.bounds.contains(p);
    }
    public final Point2D.Float getLocationVector(){

        return new Point2D.Float(this.bounds.x,this.bounds.y);
    }
    public final Component setLocationVector(Point2D point){

        this.bounds.x = (float)point.getX();
        this.bounds.y = (float)point.getY();

        this.relocated();

        return this;
    }
    public final AffineTransform getTransformLocal(){
        return (AffineTransform)this.transform.clone();
    }
    public final AffineTransform getTransformParent(){
        AffineTransform transform = this.getTransformLocal();
        Point2D.Float location = this.getLocationVector();
        transform.translate(location.x,location.y);
        return transform;
    }
    protected AbstractComponent setTransformLocal(AffineTransform transform){
        if (null != transform)
            this.transform.setTransform(transform);
        return this;
    }
    protected AbstractComponent setTransformLocal(float sx, float sy){

        return this.setTransformLocal(AffineTransform.getScaleInstance(sx,sy));
    }
    public AbstractComponent scaleTransformLocalRelative(Rectangle2D bounds){
        if (null != bounds){
            Rectangle2D thisBounds = this.getBoundsVector();
            float sw = (float)(thisBounds.getWidth()/(bounds.getX()+bounds.getWidth()));
            float sh = (float)(thisBounds.getHeight()/(bounds.getY()+bounds.getHeight()));

            this.transform.scale(sw,sh);
        }
        return this;
    }
    public AbstractComponent scaleTransformLocalAbsolute(Rectangle2D bounds){
        if (null != bounds){
            Rectangle2D thisBounds = this.getBoundsVector();
            float sw = (float)(thisBounds.getWidth()/(bounds.getX()+bounds.getWidth()));
            float sh = (float)(thisBounds.getHeight()/(bounds.getY()+bounds.getHeight()));

            this.transform.setToScale(sw,sh);
        }
        return this;
    }
    protected final Component.Container getRootContainer(){
        Component.Container p = this.getParentVector();
        Component.Container pp = p;
        while (null != pp){
            p = pp;
            pp = p.getParentVector();
        }
        return p;
    }
    public boolean input(Event e){

        return false;
    }
    public boolean isMouseIn(){

        return false;
    }
    public Component outputScene(){
        Component.Container root = this.getRootContainer();
        if (null != root){
            root.outputScene();
            return this;
        }
        else
            throw new IllegalStateException();
    }
    public Component outputOverlay(){
        Component.Container root = this.getRootContainer();
        if (null != root){
            root.outputOverlay();
            return this;
        }
        else
            throw new IllegalStateException();
    }
    public Component outputOverlayAnimate(long period){
        Component.Container root = this.getRootContainer();
        if (null != root){
            root.outputOverlayAnimate(period);
            return this;
        }
        else
            throw new IllegalStateException();
    }

    public ObjectJson toJson(){
        ObjectJson thisModel = new ObjectJson();
        thisModel.setValue("class",this.getClass().getName());
        thisModel.setValue("init",Boolean.TRUE);
        thisModel.setValue("transform",this.transform);
        thisModel.setValue("bounds",this.getBoundsVector());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.init( (Boolean)thisModel.getValue("init"));

        this.setTransformLocal( Component.Tools.DecodeTransform(thisModel.getValue("transform")));

        this.scaleTransformLocalRelative( Component.Tools.DecodeBounds(thisModel.getValue("bounds")));

        return true;
    }
}
