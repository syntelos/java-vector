package vector;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * 
 * @see Display
 */
public interface Component
{

    public void destroy();

    public Component.Container getParentVector();

    public Component setParentVector(Component.Container parent);

    public boolean isVisible();

    public Component setVisibleVector(boolean visible);

    public Rectangle2D.Float getBoundsVector();

    public Component setBoundsVector(Rectangle2D.Float bounds);

    public boolean contains(int x, int y);

    public boolean contains(float x, float y);

    public boolean contains(Point2D.Float p);

    public Point2D.Float getLocationVector();

    public Component setLocationVector(Point2D point);
    /**
     * @return Transform within the local coordinate space as
     * translated from the parent's coordinate space
     */
    public AffineTransform getTransformLocal();
    /**
     * @return The transform to the local coordinate space from the
     * parent's coordinate space
     */
    public AffineTransform getTransformParent();
    /**
     * Parent has changed size
     */
    public void resized();
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
     * @param g A copy of the parent's graphics context.
     */
    public void output(Graphics2D g);

    /**
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

        public Component add(Component comp);

        public Component addUnique(Component comp);

        public Component remove(Component comp);

        public Component remove(int idx);

        public Component.Iterator listMouseIn();
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
