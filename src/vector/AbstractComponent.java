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

import json.Json;
import json.ObjectJson;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Common implementation of {@link Component} for convenience
 */
public abstract class AbstractComponent
    extends Object
    implements Component
{

    protected boolean visible, mouseIn;

    protected Component parent;

    protected final Transform transform = new Transform();

    protected final Bounds bounds = new Bounds();


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
    public final <T extends Component> T getParentVector(){

        return (T)this.parent;
    }
    protected final Bounds getParentBounds(){

        if (null != this.parent)
            return this.parent.getBoundsVector();
        else
            throw new IllegalStateException();
    }
    public final Component setParentVector(Component parent){
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
    public final Bounds getBoundsVector(){

        return this.bounds.clone();
    }
    public final Component setBoundsVector(Bounds bounds){
        if (null != bounds){

            this.bounds.setFrame(bounds);
        }
        return this;
    }
    /**
     * Called by {@link #fromJson}
     */
    public Component setBoundsVectorForScale(Bounds bounds){
        if (null != bounds){

            this.setBoundsVector(bounds);
        }
        return this;
    }
    /**
     * Dimensions of component for resizing to the parent with
     * component origin (0,0).  The component location is changed to
     * (0,0).
     */
    protected Component setBoundsVectorInit(Component parent){
        Bounds bounds = parent.getBoundsVector();
        bounds.x = 0f;
        bounds.y = 0f;

        return this.setBoundsVector(bounds);
    }
    /**
     * Union of origin and bounds for resizing to these extents of
     * within a component.  The component location is unchanged.
     */
    protected Component setBoundsVectorInit(Bounds content){
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
    protected Component setBoundsVectorInit(Component parent, Padding margin){
        Bounds bounds = parent.getBoundsVector();

        bounds.x = margin.left;
        bounds.y = margin.top;
        bounds.width -= margin.getWidth();
        bounds.height -= margin.getHeight();

        return this.setBoundsVector(bounds);
    }
    public boolean contains(int x, int y){

        return this.bounds.contains(x,y);
    }
    public boolean contains(float x, float y){

        return this.bounds.contains(x,y);
    }
    public boolean contains(Point2D.Float p){

        return this.bounds.contains(p.x,p.y);
    }
    public final Point2D.Float getLocationVector(){

        return new Point2D.Float(this.bounds.x,this.bounds.y);
    }
    public final Component setLocationVector(Point2D point){

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
    protected AbstractComponent setTransformLocal(Transform transform){
        if (null != transform)
            this.transform.setTransform(transform);
        return this;
    }
    protected AbstractComponent setTransformLocal(float sx, float sy){

        return this.setTransformLocal(Transform.getScaleInstance(sx,sy));
    }
    protected AbstractComponent setTransformLocal(Shape shape){

        return this.setTransformLocal(new Bounds(shape));
    }
    protected AbstractComponent setTransformLocal(Bounds shape){

        return this.setTransformLocal(this.getBoundsVector().scaleFrom(shape));
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
     * @see #transformFrom(java.awt.Graphics2D)
     */
    protected Graphics2D clipFrom(Graphics2D g){

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
     * @see #clipFrom(java.awt.Graphics2D)
     */
    protected Graphics2D transformFrom(Graphics2D g){

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
            throw new IllegalStateException();
    }
    public Component outputOverlay(){
        Component root = this.getRootContainer();
        if (null != root){
            root.outputOverlay();
            return this;
        }
        else
            throw new IllegalStateException();
    }
    public Component outputOverlayAnimate(long period){
        Component root = this.getRootContainer();
        if (null != root){
            root.outputOverlayAnimate(period);
            return this;
        }
        else
            throw new IllegalStateException();
    }
    protected final Point2D.Float transformFromParent(Point2D point){

        return this.getTransformParent().transformFrom(point);
    }
    public boolean drop(Component c){

        Component p = this.getParentVector();
        while (null != p){
            if (p.drop(c))
                return true;
            else
                p = p.getParentVector();
        }
        return false;
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

        this.setTransformLocal( thisModel.getValue("transform",Transform.class));

        this.setBoundsVectorForScale( thisModel.getValue("bounds",Bounds.class));
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
    protected StringBuilder toStringBuilder(){
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
