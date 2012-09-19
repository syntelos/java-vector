/*
 * Java Vector
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

import lxl.List;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.StringTokenizer;

/**
 * A vector component is not a subclass of AWT or Swing component
 * hierarchy.  It is the child of a {@link Component$Container vector
 * container}, for example {@link Display}.
 * 
 * All components receive input and output events, and all components
 * are reproducible and updatable via {@link json.Json JSON}.
 * 
 * <h3>Coordinate spaces</h3>
 * 
 * Each component is translated from its parent, possibly null (0,0),
 * and may apply a scaling transform within this local coordate space.
 * Some operations or properties are relative to the component - local
 * coordinate space, while others are relative to the parent
 * coordinate space.
 * 
 * <h3>State Operators</h3>
 * 
 * <p> Operators are defined in the {@link Component} interface with
 * the <code>"void"</code> return type.  These are {@link #init()},
 * {@link #destroy()}, {@link #resized()}, {@link #modified()} and
 * {@link #relocated()}. </p>
 * 
 * <p> Operators must be indempotent.  Multiple calls to an instance
 * object with a particular state must produce an identical state.
 * This requirement preserves the application of operators, which is
 * partially coherent. </p>
 * 
 * <p> Operators are applied with coherence.  An operator is called
 * following a coherent step in the state of an instance object.
 * Multiple properties may be involved in a coherent state. </p>
 * 
 * <p> For example the in {@link Text} class, both "fixed" and
 * "bounds" are required in order to avoid loosing bounding box
 * information from a call to {@link #modified()}. </p>
 *
 * <p> A class hierarchy implies partial (or class-local) coherence.
 * The coherence of the super class will be a part of the coherence of
 * a sub class.  As each class is responsible for its own operations,
 * the subclass will tolerate the effective partial coherence
 * inherited from the super class with the application of these
 * principles. </p>
 * 
 * <p> Operators are never called from property setters.  Calling an
 * operator from a property setter would probably violate coherence
 * requirements in the class, and would definitely violate the
 * general coherence requirements of subclasses. </p>
 * 
 * <h3>Construction and initialization</h3>
 * 
 * <p> The {@link Component$Container Container} {@link
 * Component$Container#add(vector.Component) add} method is called
 * immediately following construction with a simple constructor method
 * (no arguments).  Within "add", the component is added to the
 * container list of components and then the {@link
 * Component#setParentVector(vector.Component$Container)
 * setParentVector} and {@link #init()} methods are called (in this
 * order) on the component. </p>
 * 
 * <p> Following "add", the "fromJson" method may be called on the
 * component. </p>
 * 
 * <p> The modification operator, {@link #modified()}, should be
 * called from the tail of any definition of the "fromJson" method.
 * The {@link Display} and {@link Container} will propagate {@link
 * #modified()}.</p>
 * 
 * <h3>Initialization and destruction</h3>
 * 
 * <p> The operator indempotence requirement has an interesting case
 * for {@link #init()} and {@link #destroy()}. </p>
 * 
 * <p> The intializer is responsible for leaving the instance object
 * in a consistent state.  For this reason it calls {@link
 * #destroy()}, while preserving the parent container reference across
 * this call. </p>
 * 
 * <p> The destructor is responsible for releasing cyclic references
 * and clearing implied state (any internal state not exposed by
 * public getters and setters (and JSON I/O) but is implied by the
 * state of explicit properties). </p>
 * 
 * <h3>Bounding box</h3>
 * 
 * <p> The bounding box (rectangle) may be derived from properties of
 * layout or content features.  When more specific information is not
 * available, the bounds of a component shall default to the
 * dimensions of the parent and origin coordinates (0,0).  Its state
 * must be defined by {@link Component#init() init}. </p>
 * 
 * <p> The {@link Component#add(vector.Component) add} process calls
 * {@link Component#setParentVector(vector.Component$Container} and
 * {@link Component#init()}, in that order. </p>
 * 
 * <h3>JSON</h3>
 * 
 * <p> A JSON message begins with the display.  That is, the {@link
 * Display} is top level component interpreter of the JSON message.
 * The {@link Frame} is not described by the JSON message packet. </p>
 * 
 * <p> Each component (including display or container) is described in
 * the JSON object format.  The outer-most formatting of the JSON
 * message is the object format (describing display). </p>
 * 
 * <h4><code>class</code></h4>
 * 
 * <p> Each component (including container) defines the JSON I/O
 * methods defined in {@link json.Builder}.  Each component must
 * produce JSON output which includes a field named
 * <code>"class"</code> with the string value returned by <code>{@link
 * java.lang.Class#getName() this.getClass().getName()}</code> (input
 * to {@link java.lang.Class#forName(java.lang.String)
 * Class.forName}). </p>
 * 
 * <p> In reading the JSON input, a component should not check or
 * validate the class field.  This liberal acceptance will permit
 * containers 
 * 
 * <h4><code>init</code></h4>
 * 
 * <p> A JSON component (or container) description that is complete
 * defines a field named <code>init</code> with the boolean literal
 * value "true".  This special instruction shall have the effect of
 * first clearing the component (or container) to the state found
 * immediately following the construction and "add" of a new instance
 * of the class.  This operation is defined in the method of the same
 * name, {@link #init()}. </p>
 * 
 * <h4><code>transform</code></h4>
 * 
 * <p> A JSON component description should include a field named
 * <code>transform</code> with the value of the local transform to
 * string.  </p>
 * 
 * <p> The local transform should be employed for scaling transforms
 * exclusively, otherwise an independent scaling transform will be
 * needed to maintain a dynamic - layout scaling transform. </p>
 * 
 * <h4><code>bounds</code></h4>
 * 
 * <p> A JSON component description should include a field named
 * <code>bounds</code> with the width and height of the component as
 * intended for <code>transform</code>. </p>
 * 
 * <h5>Scaling <code>transform</code> &amp; <code>bounds</code></h5>
 * 
 * <p> The {@link Display} bounds are
 * defined externally by the {@link Frame} (and its features or
 * containers). </p>
 * 
 * <p> The {@link #fromJson(json.Json) fromJson} method defines the
 * order of the application of properties. </p>
 * 
 * <p> The {@link Display} class scales the scene to the frame.  </p>
 * 
 * <h4><code>components</code></h4>
 * 
 * <p> Each container must produce JSON output which includes a field
 * named <code>components</code> which is a JSON array of its
 * immediate descendants. </p>
 * 
 * @see Display
 */
public interface Component
    extends json.Builder
{
    /**
     * Reset state to the state following the construction of a new
     * instance by calling {@link #destroy()}, and then restoring
     * values as required.  Save and restore the parent (container)
     * reference during this procedure -- preferably using the
     * interface get and set methods (for the benefit of subclasses).
     */
    public void init();
    /**
     * Call destroy on any descendants and release references (set
     * field "components" to null) in containers, tear-down any non
     * final field values (set to null), release all cyclic references
     * including the parent (container).
     */
    public void destroy();
    /**
     * Display has changed size.  The definition of this method often
     * calls {@link #setBoundsVector}.
     */
    public void resized();
    /**
     * Content or properties of content visualization have changed.
     * 
     * This cannot be called from property setter methods, as multiple
     * properties may need to be defined before a coherent information
     * set can be correctly applied.
     * 
     * This method is called from the tail of "fromJson" methods, and
     * at similar points after a coherent set of properties have been
     * defined.
     */
    public void modified();
    /**
     * Component has changed location (bounds x,y)
     */
    public void relocated();
    /**
     * A parent is not necessarily a Container.  It's possible for a
     * Component define a single child relationship.
     * 
     * This method is named for compatibility with the AWT for the
     * {@link Display} case.  The benefit of this choice has been that
     * {@link Display} is no more than the root container.
     * 
     * @return Parent component
     */
    public <T extends Component> T getParentVector();
    /**
     * 
     */
    public <T extends Component> T getRootContainer();
    /**
     * In the {@link Display}, this is an {@link
     * java.lang.UnsupportedOperationException unsupported operation}.
     * In any component, it is an {@link
     * java.lang.IllegalArgumentException illegal argument} for the
     * argument to be null, and an {@link
     * java.lang.IllegalStateException illegal state} for a non-null
     * argument to be applied in the case of a non-null field value.
     * 
     * @param parent Required parent.
     * 
     * @return this
     */
    public Component setParentVector(Component parent);

    public boolean isVisible();

    public Component setVisibleVector(boolean visible);
    /**
     * @return A clone or copy of the bounding box
     */
    public Bounds getBoundsVector();
    /**
     * Define the objective size of the component.  Content is scaled
     * to this size.  See "Scaling <code>transform</code> &amp;
     * <code>bounds</code>", above.
     */
    public Component setBoundsVector(Bounds bounds);
    /**
     * Called by 'fromJson' to set bounds vector
     */
    public Component setBoundsVectorForScale(Bounds in);

    public boolean contains(int x, int y);

    public boolean contains(float x, float y);

    public boolean contains(Point2D.Float p);
    /**
     * @return A clone or copy of the location coordinates
     */
    public Point2D.Float getLocationVector();

    public Component setLocationVector(Point2D point);
    /**
     * Scaling transform.  In many classes, or their possible
     * subclasses, the scaling transform is determined dynamically to
     * define the local transform.  Maintaining additional transforms
     * in the local transform would conflict with these cases, and
     * would be lost in these cases.
     * 
     * @return (Clone) Transform within the local coordinate space
     */
    public Transform getTransformLocal();
    /**
     * Scaling and translation transform
     * 
     * @return (Clone) The local transform translated from the
     * parent's coordinate space
     */
    public Transform getTransformParent();
    /**
     * @param e Event in local coordinate space
     * 
     * @return Consumed (halt further propagation)
     */
    public boolean input(Event e);
    /**
     * @return Have consumed mouse input, but not yet "mouse exit"
     */
    public boolean isMouseIn();
    /**
     * Scene
     * 
     * @param g A copy of the parent's graphics context.
     */
    public Component outputScene(Graphics2D g);
    /**
     * Overlay
     * 
     * @param g A copy of the parent's graphics context.
     */
    public Component outputOverlay(Graphics2D g);
    /**
     * Request global overlay animation from the root container.
     * 
     * @param period Consumer's animation cycle period
     */
    public Component outputOverlayAnimate(long period);
    /**
     * Request scene output from the root container
     */
    public Component outputScene();
    /**
     * Request overlay output from the root container
     */
    public Component outputOverlay();
    /**
     * Recurse up the scene graph to remove the argument from its
     * parent, and delete.
     * @return Requested removal performed successfully
     */
    public boolean drop(Component c);

    /**
     * Ordered list of component children.
     * 
     * @see Display
     */
    public interface Container
        extends Component,
                Iterable<Component>
    {

        public int count();

        public boolean has(int idx);

        public Component get(int idx);

        public int indexOf(Component comp);

        public int indexOf(Class<? extends Component> compClass);
        /**
         * Add the component to the list of children, then call {@link
         * Component#setParentVector(vector.Component$Container)
         * Component setParentVector} and {@link Component#init()
         * Component init}, in order.
         * 
         * This process have been defined in the {@link
         * Component$Tools Tools}.
         */
        public Component add(Component comp);
        /**
         * If another component exists in the same class (including
         * subclass) of the argument, then return it.  Otherwise add
         * the argument.
         */
        public Component addUnique(Component comp);

        public Component remove(Component comp);

        public Component remove(int idx);

        public Component.Iterator listMouseIn();
        /**
         * Log warning
         */
        public Component.Container warn(Throwable t, String fmt, Object... args);
        /**
         * Log error
         */
        public Component.Container error(Throwable t, String fmt, Object... args);
    }

    /**
     * 
     */
    public static class Tools {

        public static int Count(Component[] components){
            if (null == components)
                return 0;
            else
                return components.length;
        }
        public static boolean Has(Component[] components, int idx){
            if (-1 < idx && idx < Count(components))
                return true;
            else
                return false;
        }
        public static Component Get(Component[] components, int idx){
            if (-1 < idx && idx < Count(components))
                return components[idx];
            else
                throw new java.util.NoSuchElementException(String.valueOf(idx));
        }
        public static int IndexOf(Component[] components, Component comp){
            if (null == comp || null == components)
                return -1;
            else {
                int count = components.length;
                for (int idx = 0; idx < count; idx++){
                    if (comp == components[idx])
                        return idx;
                }
                return -1;
            }
        }
        public static int IndexOf(Component[] components, Class<? extends Component> compClass){
            if (null == compClass || null == components)
                return -1;
            else {
                int count = components.length;
                for (int idx = 0; idx < count; idx++){
                    Component comp = components[idx];
                    if (compClass.isAssignableFrom(comp.getClass()))
                        return idx;
                }
                return -1;
            }
        }
        public static Component[] Add(Component[] components, Component comp){
            if (null != comp){
                if (null == components)
                    components = new Component[]{comp};
                else {
                    int len = components.length;
                    Component[] copier = new Component[len+1];
                    System.arraycopy(components,0,copier,0,len);
                    copier[len] = comp;
                    components = copier;
                }
            }
            return components;
        }
        public static Component[] Cat(Component[] a, Component.Iterator b){

            return Cat(a,b.list());
        }
        public static Component[] Cat(Component[] a, Component[] b){
            if (null == b)
                return a;
            else if (null == a)
                return b;
            else {
                final int alen = a.length;
                final int blen = b.length;

                Component[] copier = new Component[alen+blen];

                System.arraycopy(a,0,copier,0,alen);
                System.arraycopy(b,0,copier,alen,blen);
                return copier;
            }
        }
        public static Component[] Remove(Component[] components, Component comp){

            return Remove(components,IndexOf(components,comp));
        }
        public static Component[] Remove(Component[] components, int idx){

            if (-1 < idx){

                final int count = components.length;
                final int term = (count-1);
                if (0 == idx){
                    if (1 == count)
                        components = null;
                    else {
                        Component[] copier = new Component[term];
                        System.arraycopy(components,1,copier,0,term);
                        components = copier;
                    }
                }
                else if (idx < term){
                    Component[] copier = new Component[term];
                    System.arraycopy(components,0,copier,0,idx);
                    System.arraycopy(components,(idx+1),copier,idx,(term-idx));
                    components = copier;
                }
                else {
                    Component[] copier = new Component[term];
                    System.arraycopy(components,0,copier,0,term);
                    components = copier;
                }
            }
            return components;
        }
        public static Component.Iterator ListMouseIn(Component[] components){
            Component[] list = null;
            final int count = Count(components);
            for (int cc = 0; cc < count; cc++){
                Component component = components[cc];
                if (component.isMouseIn()){
                    list = Add(list,component);
                }
                else if (component instanceof Container){

                    list = Cat(list,((Container)component).listMouseIn());
                }
            }
            return new Component.Iterator(list);
        }
        /**
         * While the argument JSON components list matches the
         * instance classes found in an existing components list, this
         * function will update.  Otherwise this function will add new
         * components.  To overwrite an existing components list, the
         * "init" feature is employed.
         * 
         * This approach is intended to permit updating and adding
         * with the greatest flexibility.
         */
        public static void DecodeComponents(Component.Container container, Json containerModel){
            List<Json> components = containerModel.getValue("components");
            if (null != components){
                boolean updating = true;

                final int count = components.size();
                for (int cc = 0; cc < count; cc++){

                    Json componentModel = components.get(cc);
                    if (null != componentModel && (!componentModel.isNull())){

                        String componentClassName = componentModel.getValue("class");
                        if (null != componentClassName && 0 < componentClassName.length()){
                            try {
                                Class<Component> componentClass = (Class<Component>)Class.forName(componentClassName);
                                /*
                                 * 
                                 */
                                if (updating && container.has(cc)){

                                    Component component = container.get(cc);

                                    if (component.getClass().equals(componentClass)){

                                        component.fromJson(componentModel);

                                        continue;
                                    }
                                    else {
                                        updating = false;
                                    }
                                }

                                try {
                                    Component component = componentClass.newInstance();

                                    container.add(component);

                                    component.fromJson(componentModel);
                                }
                                catch (InstantiationException exc){

                                    container.warn(exc,"Decoding component [%d] in class '%s'",cc,componentClassName);
                                }
                                catch (IllegalAccessException exc){

                                    container.warn(exc,"Decoding component [%d] in class '%s'",cc,componentClassName);
                                }
                            }
                            catch (ClassNotFoundException exc){

                                container.warn(exc,"Decoding component [%d] in class '%s'",cc,componentClassName);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     */
    public static class Iterator
        extends Object
        implements Iterable<Component>,
                   java.util.Iterator<Component>
    {

        private final Component[] list;
        private final int count;
        private int index;

        public Iterator(Component[] list){
            super();
            if (null == list){
                this.list = null;
                this.count = 0;
            }
            else {
                this.list = list;
                this.count = list.length;
            }
        }


        public Component[] list(){
            if (0 == this.count)
                return null;
            else
                return this.list.clone();
        }
        public java.util.Iterator<Component> iterator(){

            return this;
        }
        public boolean hasNext(){
            return (this.index < this.count);
        }
        public Component next(){
            if (this.index < this.count)
                return this.list[this.index++];
            else
                throw new java.util.NoSuchElementException(String.valueOf(this.index));
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
}
