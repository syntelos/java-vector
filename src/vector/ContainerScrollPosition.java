/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, John Pritchard, Syntelos
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

import platform.Shape;
import platform.geom.Point;
import platform.geom.Rectangle;

import json.Json;
import json.ObjectJson;

/**
 * Child of {@link ContainerScroll} implements scroll bar
 * visualization.  Instances of this class are managed by {@link
 * ContainerScroll} in their {@link #layoutScroll()} and {@link
 * #layoutFit()} methods.
 * 
 * This class requires its parent to be a member of {@link
 * Component$Container Component Container}, and its intended
 * operation requires layout scroll and fit calls.
 */
public abstract class ContainerScrollPosition
    extends AbstractComponent
    implements Component.Layout
{
    /**
     * 
     */
    public enum Axis {
        Horizontal, Vertical;

        public final static Axis For(String string){
            if (null == string)
                return null;
            else {
                try {
                    return Axis.valueOf(string);
                }
                catch (RuntimeException x1){
                    try {
                        return Axis.valueOf(Component.Tools.Camel(string));
                    }
                    catch (RuntimeException x2){
                        return null;
                    }
                }
            }
        }
    }


    protected Axis axis;
    /**
     * Position is a value between zero and one inclusive.
     */
    protected volatile float position;
    /**
     * Position is a value greater than zero and less than or equals
     * to one.
     */
    protected volatile float scale;

    protected volatile Bounds boundsViewport, boundsContent;


    public ContainerScrollPosition(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.scale = 1.0f;
        this.position = 0.0f;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.axis = null;
        this.boundsViewport = null;
        this.boundsContent = null;
    }
    @Override
    public void resized(){
        super.resized();

        this.layout();
    }
    @Override
    public void modified(){
        super.modified();

        this.layout();
    }
    public Order queryLayout(){

        return Order.Parent;
    }
    public Bounds queryBoundsContent(){

        return new Bounds();
    }
    public void layout(Order order){
        switch(order){
        case Parent:
            this.modified();
            return;
        case Content:
            throw new UnsupportedOperationException();
        default:
            throw new IllegalStateException(order.name());
        }
    }
    public final Axis getAxis(){
        return this.axis;
    }
    public final ContainerScrollPosition setAxis(Axis axis){
        this.axis = axis;
        return this;
    }
    public final String getAxisString(){
        Axis axis = this.axis;
        if (null != axis)
            return axis.name();
        else
            return null;
    }
    public final ContainerScrollPosition setAxisString(String axis){
        if (null != axis)
            return this.setAxis(Axis.For(axis));
        else
            return this.setAxis(null);
    }
    /**
     * @return Scroll indicator position as a point on the parent's
     * (viewport's) bounding box.
     */
    public Point queryPosition(){
        if (null != this.axis){

            final float x = this.boundsViewport.width;
            final float y = this.boundsViewport.height;

            switch(this.axis){

            case Horizontal:
                return new Point((this.position * x),y);

            case Vertical:
                return new Point(x,(this.position * y));

            default:
                throw new IllegalStateException(this.axis.name());
            }
        }
        else
            throw new IllegalStateException();
    }
    /**
     * @return Scroll indicator position as a point inside radius from
     * the parent's (viewport's) bounding box.
     */
    public Point queryPosition(float radius){
        if (null != this.axis){

            switch(this.axis){

            case Horizontal:{
                final float x = this.boundsViewport.width-(4.0f*radius);
                final float y = this.boundsViewport.height-(2.0f*radius);

                return new Point((this.position * x),y);
            }
            case Vertical:{
                final float x = this.boundsViewport.width-(2.0f*radius);
                final float y = this.boundsViewport.height-(4.0f*radius);

                return new Point(x,(this.position * y));
            }
            default:
                throw new IllegalStateException(this.axis.name());
            }
        }
        else
            throw new IllegalStateException();
    }
    /**
     * Show content shape area
     * 
     * @return Success (successful coherent state change)
     */
    public final boolean position(Rectangle area){

        if (this.visible){

            switch(this.axis){
            case Horizontal:
                float x1 = (float)(area.getX()+area.getWidth());
                return this.position(x1 / this.boundsContent.width);

            case Vertical:
                float y1 = (float)(area.getY()+area.getHeight());
                return this.position(y1 / this.boundsContent.height);

            default:
                throw new IllegalStateException();
            }
        }
        else
            return false;
    }
    /**
     * Update position and {@link #move(float) move}.
     * 
     * @param p Proportion of desired visible point in content area to
     * the associated dimension of the content area,
     * <i>i.e. (p/d)</i>.
     * 
     * @return Success (successful coherent state change)
     */
    protected final boolean position(float p){

        if (this.visible && p != this.position){

            final float ds = (this.scale*(this.position - p));

            this.position = p;

            return this.move(ds);
        }
        else
            return false;
    }
    /**
     * This scaled delta is <i>added</i> to the location of content
     * siblings.
     * 
     * @param ds Scale times position delta,
     * <i>i.e. (this.scale*(this.position - p))</i>.
     */
    public boolean move(float ds){
        /*
         * Requires container for listing content 
         */
        final Component.Container parent = this.getParentVector();

        if (Axis.Vertical == this.axis){
            /*
             * Require explicit type specification (apparently a javac bug)
             */
            final Iterable<Component> it = parent.listContent(Component.class);

            for (Component content: it){

                Bounds cb = content.getBoundsVector();

                cb.y += ds;

                content.setBoundsVector(cb);
                content.relocated();
            }
            return true;
        }
        else if (Axis.Horizontal == this.axis){
            /*
             * Require explicit type specification (apparently a javac bug)
             */
            final Iterable<Component> it = parent.listContent(Component.class);

            for (Component content: it){

                Bounds cb = content.getBoundsVector();

                cb.x += ds;

                content.setBoundsVector(cb);
                content.relocated();
            }
            return true;
        }
        else
            return false;
    }
    /**
     * Called by {@link ContainerScroll} to show or reposition scroll
     * bars.  Returns state change for subclasses.
     * 
     * @return State change in content bounds, scale, and possibly
     * visibility
     */
    public boolean layoutScroll(Bounds content){

        if (null == this.boundsContent || (!this.boundsContent.equals(content))){

            this.boundsContent = content;

            this.setVisibleVector(true);

            if (Axis.Vertical == this.axis){

                this.scale = (this.boundsContent.height - this.boundsViewport.height);
            }
            else if (Axis.Horizontal == this.axis){

                this.scale = (this.boundsContent.width - this.boundsViewport.width);
            }

            return true;
        }
        else
            return false;
    }
    /**
     * Called by {@link ContainerScroll} to hide scroll bars.  Returns
     * state change for subclasses.
     * 
     * @return State change in visibility
     */
    public boolean layoutFit(Bounds content){

        if (this.visible){

            this.boundsContent = null;

            this.setVisibleVector(false);

            return true;
        }
        else
            return false;
    }
    public void layout(){

        this.boundsViewport = this.getParentVector().getBoundsVector();
    }

    public ObjectJson toJson(){

        ObjectJson thisModel = super.toJson();

        thisModel.setValue("axis",this.getAxisString());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setAxisString( (String)thisModel.getValue("axis"));

        return true;
    }

}
