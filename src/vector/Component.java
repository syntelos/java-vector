package vector;

import json.Json;

import lxl.List;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
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
 * and may apply additional transforms within this local coordate
 * space.  Some operations or properties are relative to the component
 * - local coordinate space, while others are relative to the parent
 * coordinate space.
 * 
 * <h3>Construction and initialization</h3>
 * 
 * <p> The {@link Component$Container Container} {@link
 * Component$Container#add(vector.Component) add} method is called
 * immediately following construction with a simple constructor method
 * (no arguments).  Within "add", the component is added to the
 * container list of components and then the {@link
 * Component#setParentVector(vector.Component$Container)
 * setParentVector} method is called on the component. </p>
 * 
 * <p> Following "add", the "fromJson" method may be called on the
 * component. </p>
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
 * java.lang.Class#getName() this.getClass().getName()}</code>. </p>
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
 * string. </p>
 * 
 * <h4><code>bounds</code></h4>
 * 
 * <p> A JSON component description should include a field named
 * <code>bounds</code> with the width and height of the component as
 * intended for <code>transform</code>. </p>
 * 
 * <h3>Scaling <code>transform</code> &amp; <code>bounds</code></h3>
 * 
 * <p> The {@link Display} bounds are defined externally by the {@link
 * Frame}. </p>
 * 
 * <p> The interpretation of <code>bounds</code> and
 * <code>transform</code> shall be the following.  </p>
 * 
 * <p> The transform is applied to the instance as its new matrix
 * value, while the component bounds are scaled via the scale
 * components of the {@link Display} local matrix.  </p>
 * 
 * <p> Each component class is free to choose a strategy for scaling
 * its bounds.  It may scale its local transform with the {@link
 * Display} local transform (typical), or it may apply the scale
 * components of the display local transform to its contents.  The
 * former is applicable to component classes that employ mouse input
 * (typical), and the latter is applicable to non-interactive
 * components. </p>
 * 
 * <p> See also {@link
 * #setBoundsVector(java.awt.geom.Rectangle2D$Float)
 * setBoundsVector}. </p>
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
     * Display has changed size
     */
    public void resized();
    /**
     * Content or properties of content visualization have changed,
     * called from content visualization property methods
     */
    public void modified();
    /**
     * Component has changed, called from {@link #setBoundsVector} and {@link #setLocationVector}
     */
    public void relocated();
    /**
     * This method is named for compatibility with the AWT for the
     * {@link Display} case.  The benefit of this choice has been that
     * {@link Display} is no more than the root container.
     * 
     * @return Parent container
     */
    public Component.Container getParentVector();
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
    public Component setParentVector(Component.Container parent);

    public boolean isVisible();

    public Component setVisibleVector(boolean visible);
    /**
     * @return A clone or copy of the bounding box
     */
    public Rectangle2D.Float getBoundsVector();
    /**
     * Define the objective size of the component.  Content is scaled
     * to this size.  See "Scaling <code>transform</code> &amp;
     * <code>bounds</code>", above.
     */
    public Component setBoundsVector(Rectangle2D.Float bounds);

    public boolean contains(int x, int y);

    public boolean contains(float x, float y);

    public boolean contains(Point2D.Float p);
    /**
     * @return A clone or copy of the location coordinates
     */
    public Point2D.Float getLocationVector();

    public Component setLocationVector(Point2D point);
    /**
     * Scales content
     * 
     * @return (Clone) Transform within the local coordinate space
     */
    public AffineTransform getTransformLocal();
    /**
     * Concatenate dimensional scale with current matrix
     */
    public Component scaleTransformLocalRelative(Rectangle2D bounds);
    /**
     * Define dimensional scale on current matrix
     */
    public Component scaleTransformLocalAbsolute(Rectangle2D bounds);
    /**
     * Converts input events
     * 
     * @return (Clone) The local transform translated from the
     * parent's coordinate space
     */
    public AffineTransform getTransformParent();
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
         * Request scene output
         */
        public Component.Container outputScene();
        /**
         * Request overlay output
         */
        public Component.Container outputOverlay();
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
        public static Rectangle2D.Float DecodeBounds(Object string){
            if (null == string)
                return null;
            else if (string instanceof String)
                return DecodeBounds((String)string);
            else if (string instanceof Rectangle2D.Float)
                return (Rectangle2D.Float)string;
            else 
                throw new IllegalArgumentException(string.getClass().getName());
        }
        public static Rectangle2D.Float DecodeBounds(String string){
            if (null == string)
                return null;
            else {
                StringTokenizer strtok = new StringTokenizer(string,"][,=");
                if (9 == strtok.countTokens()){
                    strtok.nextToken(); // drop class name
                    strtok.nextToken(); // drop identifier
                    try {
                        final float x = Float.parseFloat(strtok.nextToken());
                        strtok.nextToken(); // drop identifier
                        final float y = Float.parseFloat(strtok.nextToken());
                        strtok.nextToken(); // drop identifier
                        final float w = Float.parseFloat(strtok.nextToken());
                        strtok.nextToken(); // drop identifier
                        final float h = Float.parseFloat(strtok.nextToken());

                        return new Rectangle2D.Float(x,y,w,h);
                    }
                    catch (RuntimeException exc){
                        throw new IllegalArgumentException(string,exc);
                    }
                }
                else
                    throw new IllegalArgumentException(string);
            }
        }
        public static AffineTransform DecodeTransform(Object string){
            if (null == string)
                return null;
            else if (string instanceof String)
                return DecodeTransform((String)string);
            else if (string instanceof AffineTransform)
                return (AffineTransform)string;
            else 
                throw new IllegalArgumentException(string.getClass().getName());
        }
        public static AffineTransform DecodeTransform(String string){
            if (null == string)
                return null;
            else {
                StringTokenizer strtok = new StringTokenizer(string,"][");
                if (4 == strtok.countTokens()){
                    strtok.nextToken(); // drop class name
                    String row0 = strtok.nextToken();
                    strtok.nextToken(); // drop comma
                    String row1 = strtok.nextToken();

                    strtok = new StringTokenizer(row0,", ");
                    if (3 == strtok.countTokens()){
                        try {
                            float m00 = Float.parseFloat(strtok.nextToken());
                            float m01 = Float.parseFloat(strtok.nextToken());
                            float m02 = Float.parseFloat(strtok.nextToken());

                            strtok = new StringTokenizer(row1,", ");
                            if (3 == strtok.countTokens()){

                                float m10 = Float.parseFloat(strtok.nextToken());
                                float m11 = Float.parseFloat(strtok.nextToken());
                                float m12 = Float.parseFloat(strtok.nextToken());

                                return new AffineTransform(m00,m01,m02,m10,m11,m12);
                            }
                            else
                                throw new IllegalArgumentException(string);
                        }
                        catch (RuntimeException exc){
                            throw new IllegalArgumentException(string,exc);
                        }
                    }
                    else
                        throw new IllegalArgumentException(string);
                }
                else
                    throw new IllegalArgumentException(string);
            }
        }
        public static Color DecodeColor(String color){
            if (null == color)
                return null;
            else
                return Color.decode(color);
        }
        public static String EncodeColor(Color color){
            if (null == color)
                return null;
            else {
                StringBuilder string = new StringBuilder();
                string.append('#');

                String rgb = Integer.toHexString((color.getRGB() & 0xFFFFFF));

                for (int cc = rgb.length(); cc <= 6; cc++){
                    string.append('0');
                }
                string.append(rgb);

                return string.toString();
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
            this.index = 0;
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
