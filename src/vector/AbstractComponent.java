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

import platform.Transform;

import json.Json;
import json.ObjectJson;

import platform.Shape;
import platform.geom.Point;
import platform.geom.Rectangle;

/**
 * Common implementation of {@link Component} for convenience
 */
public abstract class AbstractComponent
    extends Object
    implements Component,
               java.lang.Cloneable
{

    protected boolean visible, mouseIn;

    protected Component parent;

    protected final Transform transform = new Transform();

    protected final Bounds bounds = new Bounds();

    protected Boolean debug;


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

        Component parent = this.getParentVector();

        this.destroy();

        this.visible = true;
        this.mouseIn = false;

        this.transform.init();

        this.bounds.init();

        this.setParentVector(parent);
    }
    public void init(Boolean init){
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

        this.parent = null;
        this.debug = null;
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
    public <C extends Component> C clone(){
        try {
            return (C)super.clone();
        }
        catch (CloneNotSupportedException exc){

            throw new InternalError();
        }
    }
    public final boolean hasParentVector(){

        return (null != this.parent);
    }
    public final <T extends Component> T getParentVector(){

        return (T)this.parent;
    }
    public final boolean isDebug(){
        return (null != this.debug && this.debug.booleanValue());
    }
    public final boolean hasDebug(){
        return (null != this.debug);
    }
    public final Boolean getDebug(){
        return this.debug;
    }
    public final Component setDebug(Boolean debug){
        this.debug = debug;
        return this;
    }
    public final Bounds getParentBounds(){

        if (null != this.parent)
            return this.parent.getBoundsVector();
        else
            throw new IllegalStateException();
    }
    /**
     * This method ignores a null argument.  Subclasses that require a
     * parent in all coherent states should check the parent field in
     * the init method, and throw an {@link
     * java.lang.IllegalStateException illegal state exception} when
     * null.
     */
    public final Component setParentVector(Component parent){
        if (null != parent){
            if (null != this.parent && parent != this.parent)
                throw new IllegalStateException();
            else {
                this.parent = parent;
            }
        }
        return this;
    }
    public final <T extends Component> T getRootContainer(){
        Component p = this.getParentVector();
        Component pp = p;
        while (null != pp){
            p = pp;
            pp = p.getParentVector();
        }
        return (T)p;
    }
    protected final Transform getRootTransformLocal(){
        Component root = this.getRootContainer();
        if (null != root)
            return root.getTransformLocal();
        else
            throw new IllegalStateException();
    }
    public final boolean isVisible(){
        return this.visible;
    }
    public Component setVisibleVector(boolean visible){
        this.visible = visible;
        return this;
    }
    public boolean toggleVisibility(){

        return (this.visible = (!this.visible));
    }
    public final Bounds getBoundsVector(){

        return this.bounds.clone();
    }
    public final Component setBoundsVector(Bounds bounds){
        if (null != bounds){

            this.bounds.setFrame(bounds.x,bounds.y,bounds.width,bounds.height);
        }
        return this;
    }
    /**
     * Dimensions of component for resizing to the parent with
     * component origin (0,0).  The component location is changed to
     * (0,0).
     */
    protected final Component setBoundsVectorInit(Component parent){
        if (null != parent){
            Bounds bounds = parent.getBoundsVector();
            bounds.x = 0f;
            bounds.y = 0f;

            return this.setBoundsVector(bounds);
        }
        else
            throw new IllegalArgumentException("Require parent");
    }
    /**
     * Union of origin and bounds for resizing to these extents of
     * within a component.  The component location is unchanged.
     */
    protected final Component setBoundsVectorInit(Bounds content){
        if (null != content){

            content.width += content.x;
            content.height += content.y;
            content.x = this.bounds.x;
            content.y = this.bounds.y;

            return this.setBoundsVector(content);
        }
        else
            return this;
    }
    /**
     * Dimensions of component for resizing to the parent with
     * component origin (margin.left,margin.top).  The component
     * location is changed to (margin.left,margin.top), and the
     * component bounds are changed to subtract the margin.
     */
    protected final Component setBoundsVectorInit(Component parent, Padding margin){
        if (null != parent){
            Bounds bounds = parent.getBoundsVector();

            bounds.x = margin.left;
            bounds.y = margin.top;
            bounds.width -= margin.getWidth();
            bounds.height -= margin.getHeight();

            return this.setBoundsVector(bounds);
        }
        else
            throw new IllegalArgumentException("Require parent");
    }
    /**
     * Change location for center in parent, require this and parent
     * dimensions (previously defined).
     * 
     * @param parent Required (container dimensions)
     * 
     * @param margin Optional (may be null)
     */
    protected final Component setBoundsVectorCenter(Bounds parent, Padding margin){
        if (null != parent){
            Bounds bounds = this.getBoundsVector();


            if (null == margin){
                bounds.x = (parent.width/2)-(bounds.width/2);
                bounds.y = (parent.height/2)-(bounds.height/2);
            }
            else {
                bounds.x = (parent.width/2)-((bounds.width+margin.getWidth())/2)+(margin.left);
                bounds.y = (parent.height/2)-((bounds.height+margin.getHeight())/2)+(margin.top);
            }

            return this.setBoundsVector(bounds);
        }
        else
            throw new IllegalArgumentException("Require parent");
    }
    public Component setBoundsVectorCenter(Padding margin){
        Component parent = this.getParentVector();
        if (null != parent)
            return this.setBoundsVectorCenter(parent.getBoundsVector(),margin);
        else
            throw new IllegalStateException("Orphan");
    }
    public boolean contains(int x, int y){

        return this.bounds.contains(x,y);
    }
    public boolean contains(float x, float y){

        return this.bounds.contains(x,y);
    }
    public boolean contains(Point p){

        return this.bounds.contains(p.x,p.y);
    }
    public final Point getLocationVector(){

        return new Point(this.bounds.x,this.bounds.y);
    }
    public final Component setLocationVector(Point point){

        this.bounds.x = (float)point.getX();
        this.bounds.y = (float)point.getY();

        return this;
    }
    public final Component setLocationVector(float x, float y){

        this.bounds.x = x;
        this.bounds.y = y;

        return this;
    }
    public final Transform getTransformLocal(){

        return this.transform.clone();
    }
    public final Transform getTransformParent(){

        return this.getTransformLocal().translateLocation(this.getLocationVector());
    }
    public AbstractComponent setTransformLocal(Transform transform){
        if (null != transform)
            this.transform.setTransform(transform);
        return this;
    }
    public AbstractComponent setTransformLocal(float sx, float sy){

        return this.setTransformLocal(Transform.getScaleInstance(sx,sy));
    }
    public AbstractComponent setTransformLocal(Shape shape){

        return this.setTransformLocal(new Bounds(shape));
    }
    public AbstractComponent setTransformLocal(Bounds shape){

        return this.setTransformLocal(this.getBoundsVector().scaleFromAbsolute(shape));
    }
    /**
     * Apply parent transform via clipping: an alternative to the
     * unclipped {@link Container} pattern
     * <pre>
     * this.getTransformParent().transformFrom(g);
     * </pre> which is
     * <pre>
     * this.getTransformLocal().transformFrom(this.getBoundsVector().clip(g));
     * </pre>
     * 
     * @param g Graphics context
     * 
     * @return The argument graphics context (not a clone or copy),
     * clipped to this bounding box and transformed with the local
     * transform
     * 
     * @see #transformFrom(Context)
     */
    public Context clipFrom(Context g){

        return this.getTransformLocal().transformFrom(this.getBoundsVector().clip(g));
    }
    /**
     * Apply parent transform without clipping: an alternative to the
     * clipped {@link Container} pattern 
     * <pre>
     * this.getTransformLocal().transformFrom(this.getBoundsVector().clip(g));
     * </pre> which is
     * <pre>
     * this.getTransformParent().transformFrom(g);
     * </pre>
     * 
     * @param g Graphics context
     * 
     * @return The argument graphics context (not a clone or copy),
     * clipped to this bounding box and transformed with the local
     * transform
     * 
     * @see #clipFrom(Context)
     */
    public Context transformFrom(Context g){

        return this.getTransformParent().transformFrom(g);
    }
    public boolean input(Event e){

        switch(e.getType()){
        case MouseEntered:
            this.mouseIn = true;
            return true;
        case MouseExited:
            this.mouseIn = false;
            return true;
        default:
            return false;
        }
    }
    public final boolean isMouseIn(){

        return this.mouseIn;
    }
    public Component outputScene(){
        Component root = this.getRootContainer();
        if (null != root){
            root.outputScene();
            return this;
        }
        else
            throw new IllegalStateException("Orphan missing root container");
    }
    public Component outputOverlay(){
        Component root = this.getRootContainer();
        if (null != root){
            root.outputOverlay();
            return this;
        }
        else
            throw new IllegalStateException("Orphan missing root container");
    }
    public Component outputOverlayAnimate(long period){
        Component root = this.getRootContainer();
        if (null != root){
            root.outputOverlayAnimate(period);
            return this;
        }
        else
            throw new IllegalStateException("Orphan missing root container");
    }
    public final Point transformFromParent(Point point){

        return this.getTransformParent().transformFrom(point);
    }
    /**
     * @see Container
     */
    public boolean drop(Component c){

        Component p = this.getParentVector();
        if (null != p)
            return p.drop(c);
        else
            return false;
    }
    /**
     * @see Container
     */
    public boolean drop(Class<? extends Component> c){

        Component p = this.getParentVector();
        if (null != p)
            return p.drop(c);
        else
            return false;
    }
    public Display show(Component c){
        Display display = this.getRootContainer();
        if (null != display && null != c)
            display.show(c);
        return display;
    }
    public Display show(Class<? extends Component> c){
        Display display = this.getRootContainer();
        if (null != display && null != c)
            display.show(c);
        return display;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel = new ObjectJson();
        thisModel.setValue("class",this.getClass().getName());
        thisModel.setValue("init",Boolean.TRUE);
        thisModel.setValue("transform",this.transform);
        thisModel.setValue("bounds",this.getBoundsVector());

        if (this.hasDebug()){
            thisModel.setValue("debug",this.getDebug());
        }

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        this.init( (Boolean)thisModel.getValue("init"));

        this.setTransformLocal( thisModel.getValue("transform",Transform.class));

        this.setBoundsVector( thisModel.getValue("bounds",Bounds.class));

        this.setDebug( (Boolean)thisModel.getValue("debug"));
        /*
         * If we assert that the abstract class does not have a
         * coherent state, subclasses may employ the transform and
         * bounds defined here with additional information.  Which
         * makes a lot of sense, given the requirement that components
         * are free to choose scaling and layout strategies.
         * 
         *  this.modified()
         */
        return true;
    }
    /**
     * Information for debugging
     */
    public StringBuilder toStringBuilder(){
        StringBuilder string = new StringBuilder();
        string.append(this.getClass().getName());
        string.append(", x: ");
        string.append(this.bounds.x);
        string.append(", y: ");
        string.append(this.bounds.y);
        string.append(", w: ");
        string.append(this.bounds.width);
        string.append(", h: ");
        string.append(this.bounds.height);
        return string;
    }
    /**
     * Overriden in {@link Text} for CharSequence API
     */
    public String toString(){
        return this.toStringBuilder().toString();
    }
}
