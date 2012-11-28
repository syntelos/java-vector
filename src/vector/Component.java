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
import platform.geom.Point;

import json.Json;

import lxl.List;

import java.lang.reflect.Method;

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
     * The implementor may ignore a null argument when the class has
     * coherent states with no reference to the parent.  
     * 
     * The implementor must throw an {@link
     * java.lang.IllegalStateException illegal state exception} for a
     * non-null argument applied in the case of an existing (not null)
     * field value.
     * 
     * A null argument must never be assigned to the internal parent
     * field (reference), as this would violate the component operator
     * protocol.
     * 
     * Ignoring a null argument preserves the full application
     * envelope for classes with coherent states that have no
     * reference to the parent component.  For classes in which a
     * possible use case is not attached to a conventional scene
     * graph, the initialization procedure depends on this method
     * ignoring a null argument.
     * 
     * In the {@link Display}, this method throws an {@link
     * java.lang.UnsupportedOperationException unsupported operation
     * exception}.
     * 
     * @param parent Reference to parent component (may be a
     * Container).
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

    public boolean contains(int x, int y);

    public boolean contains(float x, float y);

    public boolean contains(Point p);
    /**
     * @return A clone or copy of the location coordinates
     */
    public Point getLocationVector();

    public Component setLocationVector(Point point);
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
    public Component outputScene(Context g);
    /**
     * Overlay
     * 
     * @param g A copy of the parent's graphics context.
     */
    public Component outputOverlay(Context g);
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
     * parent, and destroy.
     * @return Requested removal performed successfully
     */
    public boolean drop(Component c);

    /**
     * Marker for layout and listing
     */
    public interface Margin
        extends Component
    {

        public Padding getMargin();
    }
    /**
     * Marker for layout and listing
     */
    public interface Align2D
        extends Component
    {

        public Align getAlign();
    }
    /**
     * Marker for layout and listing
     */
    public interface AlignVertical
        extends Component
    {

        public Align.Vertical getVertical();
    }
    /**
     * Marker for layout and listing
     */
    public interface AlignHorizontal
        extends Component
    {

        public Align.Horizontal getHorizontal();
    }


    /**
     * Layout determination support for strong container dependence on
     * its children.
     * 
     * <p> The general layout order is "content".  The "parent" layout
     * order applies only to the case where a bounding box is derived
     * from, or is identical to the parent's bounding box.  All other
     * cases are in the "content" category. </p>
     * 
     * @see Component$Layout$TextQuery
     * @see TextLayout
     */
    public interface Layout
        extends Component
    {
        /**
         * Layout strategy
         * 
         * <p> The general layout order is "content".  The "parent"
         * layout order applies only to the case where a bounding box
         * is derived from, or is identical to the parent's bounding
         * box.  All other cases are in the "content" category. </p>
         */
        public enum Order {
            /**
             * This component's layout strategy derives its bounding
             * box from Component Parent
             */
            Parent, 
            /**
             * This component's layout strategy derives its bounding
             * box from Component Content, including shapes and
             * children
             */
            Content;
        }

        /**
         * The implementor detects its layout strategy from its
         * explicit state, and returns an Order value to reflect that
         * state.
         * 
         * @return Current layout strategy
         */
        public Order queryLayout();
        /**
         * Valid when queryLayout returns Order Content, otherwise
         * likely to be stale.
         * 
         * @return Interior bounds of the content of this component
         */
        public Bounds queryBoundsContent();
        /**
         * This is the {@link Component$Layout layout component}
         * operator.  It may be called by a parent to notify its
         * children of layout requirements.  
         * 
         * The definition of this method shall update explicit state
         * as required and then call <code>this.modified()</code>.
         * 
         * It's operation is identical to other processes to update
         * explicit state and call <code>modified</code>.
         */
        public void layout(Order order);

        /**
         * Text layout determination supports text formatting.
         * 
         * @see Component$Layout
         * @see TextLayout
         */
        public interface TextQuery
            extends Layout
        {
            /**
             * Text formatting characteristics
             */
            public enum Whitespace {
                /**
                 * Non - white - space text element
                 */
                Inline,
                /**
                 * Vertical whitespace text element
                 */
                Vertical, 
                /**
                 * Horizontal whitespace text element
                 */
                Horizontal;
            }

            /**
             * @return Text formatting type of this object
             */
            public Whitespace queryLayoutText();
        }
    }

    /**
     * 
     */
    public interface Bordered<B extends Component>
        extends Component
    {

        public Bordered setBorder(B border);

        public Bordered setBorder(B border, Json model);

        public B getBorder();
    }

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

        public <C extends Component> C get(int idx);

        public <C extends Component> C set(C comp, int idx);

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
        public <C extends Component> C add(C comp);
        /**
         * @param comp New component to add to list
         * @param index New component index in list: zero-positive
         * indeces from head, negative indeces from tail, degenerate
         * insert to index zero.  For example, insertion index
         * negative two on list length one inserts to index zero.
         */
        public <C extends Component> C insert(C comp, int index);
        /**
         * If another component exists in the same class (including
         * subclass) of the argument, then return it.  Otherwise add
         * the argument.
         */
        public <C extends Component> C addUnique(C comp);
        /**
         * The container remove operation shall not destroy the
         * removed component.  
         * 
         * A forgotten and undestroyed component may not garbage
         * collect when container or demi-container (border component)
         * cyclic references exist in a removed scene graph subtree.
         * The cyclic reference in this case is between: the component
         * parent, and parent child or children fields.
         * 
         * @return Component not destroyed
         */
        public <C extends Component> C remove(C comp);
        /**
         * The container remove operation shall not destroy the
         * removed component.  
         * 
         * Note that the Component drop operation will destroy the
         * removed component.
         * 
         * @return Component not destroyed
         */
        public <C extends Component> C remove(int idx);
        /**
         * Iterable example
         * <pre>
         *  for (Component c : this.listMouseIn(Component.class)){
         *      ...
         *  }
         * </pre>
         * Array example
         * <pre>
         *  Component[] array = container.listMouseIn(Component.class)).list(Component.class);
         * </pre>
         * 
         * List immediate descendants with mouseIn true
         */
        public <C extends Component> Component.Iterator<C> listMouseIn(Class<C> clas);
        /**
         * Iterable example
         * <pre>
         *  for (Border b : container.list(Border.class)){
         *      ...
         *  }
         * </pre>
         * Array example
         * <pre>
         *  Border[] array = container.list(Border.class)).list(Border.class);
         * </pre>
         * 
         * List immediate descendants in (members of) the argument
         * class
         * @param cclass Required array component class, for example
         * {@link Component$Layout Component.Layout.class}
         */
        public <C extends Component> Component.Iterator<C> list(Class<C> clas);
        /**
         * Iterable example
         * <pre>
         *  for (Component c : container.listContent(Component.class)){
         *      ...
         *  }
         * </pre>
         * Array example
         * <pre>
         *  Component[] array = container.listContent(Component.class)).list(Component.class);
         * </pre>
         * 
         * @param cclass Array component class.  Use {@link Component
         * 'Component.class'} for a default.
         * @return Components not having layout order Parent, or not
         * implementing Layout.
         */
        public <C extends Component> Component.Iterator<C> listContent(Class<C> cclass);
        /**
         * Iterable example
         * <pre>
         *  for (Component.Layout c : container.listParent(Component.Layout.class)){
         *      ...
         *  }
         * </pre>
         * Array example
         * <pre>
         *  Component.Layout[] array = container.listParent(Component.Layout.class)).list(Component.Layout.class);
         * </pre>
         * 
         * @param cclass Array component class.  Use {@link Component
         * 'Component.class'} for a default.
         * @return Components implementing Layout and having layout
         * order Parent.
         */
        public <C extends Component> Component.Iterator<C> listParent(Class<C> cclass);
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
     * Implementations of common utilities.
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
        public static Component Set(Component[] components, Component comp, int idx){
            if (null != comp && -1 < idx && idx < Count(components)){
                Component old = components[idx];

                components[idx] = comp;

                return old;
            }
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
        public static Component[] Insert(Component[] components, Component comp, int index){
            if (null == comp)
                return components;
            else {
                final int len = ((null != components)?(components.length):(0));
                final int nlen = Math.max((len+1),(index+1));
                final Component[] copier = new Component[nlen];

                if (0 > index){
                    /*
                     * Index from tail of list, -1 for tail,
                     * degenerating to insert to index zero
                     */
                    index = Math.max(0,(index + len));
                }

                if (null != components){
                    if (0 == index){
                        System.arraycopy(components,0,copier,1,len);
                    }
                    else if (len == index){
                        System.arraycopy(components,0,copier,0,len);
                    }
                    else {
                        System.arraycopy(components,0,copier,0,index);
                        System.arraycopy(components,index,copier,(index+1),(len-index));
                    }
                }
                copier[index] = comp;
                return copier;
            }
        }
        public static <C extends Component> C[] Add(C[] components, Component comp, Class<C> clas){
            if (null != comp){
                if (null == components){
                    C[] array = (C[])java.lang.reflect.Array.newInstance(clas,1);
                    array[0] = (C)comp;
                    return array;
                }
                else {
                    int len = components.length;
                    C[] copier = (C[])java.lang.reflect.Array.newInstance(clas,(len+1));
                    System.arraycopy(components,0,copier,0,len);
                    copier[len] = (C)comp;
                    return copier;
                }
            }
            else
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
        public static <C extends Component> C[] Cat(C[] a, Component.Iterator<C> b, Class<C> clas){

            return Cat(a,b.list(clas),clas);
        }
        public static <C extends Component> C[] Cat(C[] a, C[] b, Class<C> clas){
            if (null == b)
                return a;
            else if (null == a)
                return b;
            else {
                final int alen = a.length;
                final int blen = b.length;

                C[] copier = (C[])java.lang.reflect.Array.newInstance(clas,(alen+blen));

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
        public static <C extends Component> Component.Iterator<C> ListMouseIn(Component[] components, Class<C> clas){
            C[] list = null;
            final int count = Count(components);
            for (int cc = 0; cc < count; cc++){
                Component component = components[cc];
                if (component.isMouseIn()){
                    try {
                        list = Add(list,component,clas);
                    }
                    catch (ClassCastException filter){
                    }
                }
            }
            return new Component.Iterator<C>(list,clas);
        }
        public static <C extends Component> Component.Iterator<C> List(Component[] components, Class<C> clas){
            C[] list = null;
            final int count = Count(components);
            for (int cc = 0; cc < count; cc++){
                Component component = components[cc];

                if (clas.isAssignableFrom(component.getClass())){

                    list = Add(list,component,clas);
                }
            }
            return new Component.Iterator<C>(list,clas);
        }
        /**
         * @return Components implementing Layout and having layout order Parent.
         */
        public static <C extends Component> Component.Iterator<C> ListLayoutParent(Component[] components, Class<C> clas){
            C[] list = null;
            final int count = Count(components);
            for (int cc = 0; cc < count; cc++){
                Component c = components[cc];
                if (c instanceof Component.Layout && 
                    Component.Layout.Order.Parent == ((Component.Layout)c).queryLayout())
                {
                    try {
                        list = Add(list,c,clas);
                    }
                    catch (ClassCastException filter){
                    }
                }
            }
            return new Component.Iterator<C>(list,clas);
        }
        /**
         * @return Components not having layout order Parent, or not
         * implementing Layout.
         */
        public static <C extends Component> Component.Iterator<C> ListLayoutContent(Component[] components, Class<C> clas){
            C[] list = null;
            final int count = Count(components);
            for (int cc = 0; cc < count; cc++){
                Component c = components[cc];
                if (c instanceof Component.Layout){

                    if (Component.Layout.Order.Content == ((Component.Layout)c).queryLayout()){
                        try {
                            list = Add(list,c,clas);
                        }
                        catch (ClassCastException filter){
                        }
                    }
                }
                else {
                    try {
                        list = Add(list,c,clas);
                    }
                    catch (ClassCastException filter){
                    }
                }
            }
            return new Component.Iterator<C>(list,clas);
        }
        public static boolean IsLayoutContent(Component c){

            if (c instanceof Component.Layout)

                return (Component.Layout.Order.Content == ((Component.Layout)c).queryLayout());
            else 
                return true;
        }
        public static Component[] ClearContent(Component[] components){

            for (int cc = (Component.Tools.Count(components)-1); -1 < cc; cc--){

                Component c = components[cc];

                if (c instanceof Component.Layout){

                    if (Component.Layout.Order.Content == ((Component.Layout)c).queryLayout()){

                        components = Component.Tools.Remove(components,cc);

                        c.destroy();
                    }
                }
                else {
                    components = Component.Tools.Remove(components,cc);

                    c.destroy();
                }
            }
            return components;
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
        public final static String Camel(String string){
            java.util.StringTokenizer strtok = new java.util.StringTokenizer(string,"-");
            StringBuilder camel = new StringBuilder();
            while (strtok.hasMoreTokens()){
                String tok = strtok.nextToken();
                int len = tok.length();
                if (0 < len){
                    camel.append(Character.toUpperCase(tok.charAt(0)));
                    if (1 < len){
                        camel.append(tok.substring(1).toLowerCase());
                    }
                }
            }
            if (0 < camel.length())
                return camel.toString();
            else
                return null;
        }
        public final static <T extends Enum<T>> Method EnumValueMethod(Class<Enum<T>> clas){
            try {
                return clas.getMethod("valueOf",String.class);
            }
            catch (NoSuchMethodException interr){
                throw new IllegalStateException(clas.getName(),interr);
            }
            catch (SecurityException interr){
                throw new IllegalStateException(clas.getName(),interr);
            }
        }
        public final static <T extends Enum<T>> Enum<T> EnumValueOf(Method m, String name){
            if (null != m && null != name){
                try {
                    return (Enum<T>)m.invoke(null,name);
                }
                catch (IllegalAccessException interr){

                    throw new IllegalArgumentException(m.getDeclaringClass().getName(),interr);
                }
                catch (java.lang.reflect.InvocationTargetException arg){
                    Throwable t = arg.getCause();
                    if (t instanceof RuntimeException)
                        throw (RuntimeException)t;
                    else
                        throw new IllegalArgumentException(name,t);
                }
            }
            else
                return null;
        }


        public final static <T extends Enum<T>> Method ListEnumMethod(Class<Enum<T>> clas){
            if (null == clas)
                return null;
            else {
                try {
                    return clas.getMethod("values");
                }
                catch (NoSuchMethodException interr){
                    throw new IllegalStateException(clas.getName(),interr);
                }
                catch (SecurityException interr){
                    throw new IllegalStateException(clas.getName(),interr);
                }
            }
        }
        public final static <T extends Enum<T>> Enum<T>[] ListEnumOf(Method m){
            if (null != m){
                try {
                    return (Enum<T>[])m.invoke(null);
                }
                catch (IllegalAccessException access){

                    throw new IllegalArgumentException(m.getDeclaringClass().getName(),access);
                }
                catch (java.lang.reflect.InvocationTargetException invocation){

                    Throwable t = invocation.getCause();
                    if (t instanceof RuntimeException)
                        throw (RuntimeException)t;
                    else
                        throw new IllegalArgumentException(m.getDeclaringClass().getName(),t);
                }
            }
            else
                return null;
        }
    }

    /**
     * 
     */
    public static class Iterator<C extends Component>
        extends Object
        implements Iterable<C>,
                   java.util.Iterator<C>
    {

        private final Component[] list;
        private final int count;
        private final Class<C> cclass;
        private int index;

        public Iterator(Component[] list){
            super();
            if (null == list){
                this.list = null;
                this.count = 0;
                this.cclass = null;
            }
            else {
                this.list = list;
                this.count = list.length;
                this.cclass = null;
            }
        }
        public Iterator(C[] list, Class<C> cclass){
            super();
            if (null == list){
                this.list = null;
                this.count = 0;
                this.cclass = cclass;
            }
            else {
                this.list = list;
                this.count = list.length;
                this.cclass = cclass;
            }
        }


        public Iterator reverse(){
            int head = 0, tail = (this.count-1);
            final int term = (tail>>1);
            for ( ; term < tail; head++,tail--){
                Component t = this.list[head];
                this.list[head] = this.list[tail];
                this.list[tail] = t;
            }
            return this;
        }
        public int count(){
            return this.count;
        }
        public boolean has(int idx){
            return Component.Tools.Has(this.list,idx);
        }
        public C get(int idx){
            return (C)Component.Tools.Get(this.list,idx);
        }
        public Component[] list(){
            if (0 == this.count)
                return null;
            else
                return this.list.clone();
        }
        public <C extends Component> C[] list(Class<C> clas){
            if (0 == this.count)
                return null;
            else if (clas.equals(this.cclass))
                return (C[])this.list.clone();
            else {
                C[] array = (C[])java.lang.reflect.Array.newInstance(clas,this.count);

                System.arraycopy(this.list,0,array,0,this.count);

                return array;
            }
        }
        public java.util.Iterator<C> iterator(){

            return this;
        }
        public boolean hasNext(){
            return (this.index < this.count);
        }
        public C next(){
            if (this.index < this.count)
                return (C)this.list[this.index++];
            else
                throw new java.util.NoSuchElementException(String.valueOf(this.index));
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
}
