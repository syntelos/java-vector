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

import platform.Transform;
import platform.event.KeyCode;

import java.util.StringTokenizer;

/**
 * Immutable universal event for a single input method: intended for
 * simplified input method programming
 */
public interface Event {
    /**
     * System property <code>"vector.Event.Debug"</code> takes one of
     * these values, default "none".  This facility may be employed to
     * debug event based processes with the user's logging or
     * printing.
     * 
     * <pre>
     * if (vector.Event.Debug.IsClick){
     *     Event.Debug.trace("mouse up",this,e);
     * }
     * </pre>
     * 
     * @see AbstractComponent
     * @see BorderComponent
     */
    public enum Debug {
        /**
         * (Empty set)
         */
        NONE,
        /**
         * Mouse entry and exit events
         */
        ENTRY, 
        /**
         * Key release events
         */
        KEY,
        /**
         * Mouse motion, drag and drop events
         */
        MOTION,
        /**
         * Mouse release events
         */
        CLICK,
        /**
         * Action (synthesized or mapped input) events
         */
        ACTION,
        /**
         * All input events
         */
        ALL;


        public boolean isEntry(){
            switch(this){
            case ENTRY:
            case ALL:
                return true;
            default:
                return false;
            }
        }
        public boolean isKey(){
            switch(this){
            case KEY:
            case ALL:
                return true;
            default:
                return false;
            }
        }
        public boolean isMotion(){
            switch(this){
            case MOTION:
            case ALL:
                return true;
            default:
                return false;
            }
        }
        public boolean isClick(){
            switch(this){
            case CLICK:
            case ALL:
                return true;
            default:
                return false;
            }

        }
        public boolean isAction(){
            switch(this){
            case ACTION:
            case ALL:
                return true;
            default:
                return false;
            }

        }
        public boolean isAll(){
            return (Debug.ALL == this);
        }
        public boolean isNone(){
            return (Debug.NONE == this);
        }
        public boolean isAny(){
            return (Debug.NONE != this);
        }

        /**
         * 
         */
        public final static Debug.Set Config = new Debug.Set(System.getProperty("vector.Event.Debug"));

        public final static Debug Default = Debug.NONE;

        public final static boolean IsEntry = Config.isEntry();
        public final static boolean IsKey = Config.isKey();
        public final static boolean IsMotion = Config.isMotion();
        public final static boolean IsClick = Config.isClick();
        public final static boolean IsAction = Config.isAction();
        public final static boolean IsAll = Config.isAll();
        public final static boolean IsNone = Config.isNone();
        public final static boolean IsAny = Config.isAny();

        static {
            if (!Config.isNone()){
                DebugTrace.out.printf("vector.Event.Debug: %s%n",Config);
            }
        }


        /**
         * String binding with default value
         */
        public final static Debug For(String string){
            if (null != string){
                try {
                    return Debug.valueOf(string.toUpperCase());
                }
                catch (RuntimeException exc){
                }
            }
            return Debug.Default;
        }
        /**
         * Convenience method for event debugging.
         * <pre>
         * if (vector.Event.Debug.IsAny){
         *     Event.Debug.trace("%s",this,evt);
         * }
         * </pre>
         * 
         * @param fmt Format string 
         * @param caller Display graph location 
         * @param fargs Arguments to format string
         */
        public final static void trace(String fmt, Component caller, Object... fargs){

            String msg = String.format(fmt,fargs);

            DebugTrace.out.printf("#(%s)\t%s\t%s%n",msg,caller.getClass().getName(),caller.propertyPathOfThis());
        }


        /**
         * Set of enum values.  The empty set represents {@link Event$Debug#NONE}.
         * 
         * @see Event$Debug#Config
         */
        public static class Set
            extends lxl.Set<Debug>
        {

            public Set(){
                super();
            }
            public Set(String descriptor){
                super();
                this.add(descriptor);
            }


            public boolean isEntry(){
                return (this.contains(Debug.ENTRY) || this.contains(Debug.ALL));
            }
            public boolean isKey(){
                return (this.contains(Debug.KEY) || this.contains(Debug.ALL));
            }
            public boolean isMotion(){
                return (this.contains(Debug.MOTION) || this.contains(Debug.ALL));
            }
            public boolean isClick(){
                return (this.contains(Debug.CLICK) || this.contains(Debug.ALL));
            }
            public boolean isAction(){
                return (this.contains(Debug.ACTION) || this.contains(Debug.ALL));
            }
            public boolean isAll(){
                return this.contains(Debug.ALL);
            }
            public boolean isNone(){
                return (0 == this.size() || this.contains(Debug.ALL));
            }
            public boolean isAny(){
                return (0 != this.size() && (!this.contains(Debug.NONE)));
            }
            /**
             * @param descriptor One identifier from the set {@link
             * Event$Debug}, or a combination of two or more {@link
             * Event$Debug} identifiers separated by '+' (plus).
             */
            public Set add(String descriptor){

                DebugTrace.out.printf("vector.Event.Debug.Set #add(%s)%n",descriptor);

                if (null != descriptor){

                    if (-1 < descriptor.indexOf('+')){
                        final StringTokenizer strtok = new StringTokenizer(descriptor,"+");

                        while (strtok.hasMoreTokens()){

                            String tok = strtok.nextToken().toUpperCase();
                            try {
                                Debug dbg = Debug.valueOf(tok);

                                int ix = this.add(dbg);
                            
                                DebugTrace.out.printf("vector.Event.Debug.Set #add [success] (%d %s)%n",ix,dbg);
                            }
                            catch (RuntimeException exc){

                                DebugTrace.out.printf("vector.Event.Debug.Set #add [failure] (%s)%n",tok);
                            }
                        }
                    }
                    else {

                        this.add(Debug.For(descriptor));
                    }
                }
                return this;
            }

        }
    }

    /**
     * {@link Event} types include mouse, key, and action.
     */
    public enum Type {
        MouseEntered, 
        MouseExited, 
        /**
         * Mouse moved includes enter and exit
         */
        MouseMoved, 
        MouseDown, 
        MouseUp, 
        /**
         * Point action
         */
        MouseDrag, 
        MouseWheel, 
        KeyDown, 
        KeyUp, 
        Action;

        public boolean isMouse(){
            switch(this){
            case MouseEntered:
            case MouseExited:
            case MouseMoved:
            case MouseDown:
            case MouseUp:
            case MouseDrag:
            case MouseWheel:
                return true;
            default:
                return false;
            }
        }
        public boolean isKey(){
            switch(this){
            case KeyDown:
            case KeyUp:
                return true;
            default:
                return false;
            }
        }
        public boolean isAction(){
            switch(this){
            case Action:
                return true;
            default:
                return false;
            }
        }

    }
    /**
     * Point action indicates button use
     */
    public interface Mouse
        extends Event
    {
        /**
         * Mouse input types
         */
        public enum Action {
            Entered, Exited, Moved, Point1, Point2, Point3, Wheel;


            public boolean isMotion(){
                switch(this){
                case Entered:
                case Exited:
                case Moved:
                    return true;
                default:
                    return false;
                }
            }
            public boolean isPoint(){
                switch(this){
                case Point1:
                case Point2:
                case Point3:
                    return true;
                default:
                    return false;
                }
            }
            public boolean isPointGt1(){
                switch(this){
                case Point2:
                case Point3:
                    return true;
                default:
                    return false;
                }
            }
            public boolean isWheel(){
                switch(this){
                case Wheel:
                    return true;
                default:
                    return false;
                }
            }
        }

        public Action getAction();

        public boolean isMotion();

        public boolean isPoint();

        public boolean isWheel();

        /**
         * Copy this event in class and parameters, transforming a
         * point from the parent coordinate space into the local
         * coordinate space using the argument (parent) transform.
         * 
         * @param parent Parent transform
         * 
         * @return This with no point to transform, or a copy
         * containing a transformed point
         */
        public Event transformFrom(Transform parent);

        public interface Motion
            extends Mouse
        {
            public platform.geom.Point getPoint();
        }
        public interface Point
            extends Mouse
        {
            public platform.geom.Point getPoint();
        }
        public interface Wheel
            extends Mouse
        {
            public int getCount();
        }
    }
    /**
     * Essential reliable properties 
     */
    public interface Key
        extends Event
    {

        public boolean isUp();

        public boolean isDown();
        /**
         * @return Code input
         * @see #isCode()
         */
        public KeyCode getCode();
        /**
         * @return Character input
         * @see #isChar()
         */
        public char getKeyChar();
        /**
         * @return With CONTROL modifier
         */
        public boolean isControl();
        /**
         * @return With ALT modifier
         */
        public boolean isAlt();
        /**
         * Use key code
         */
        public boolean isCode();
        /**
         * Use key char
         */
        public boolean isChar();
    }
    /**
     * Propagation of button and menu events.  Dispatch global
     * application actions via root container input.
     * 
     * The enum type permits application global consumers to validate
     * an action by type.
     */
    public interface NamedAction<T extends Enum<T>>
        extends Event
    {
        /**
         * Action producer
         * @see Button
         */
        public interface Producer<P extends Enum<P>>
            extends Component
        {
            public Class<Enum<P>> getEnumClass();

            public Producer setEnumClass(Class<Enum<P>> clas);

            public String getEnumClassName();

            public Producer setEnumClassName(String name);

            public Enum<P> getEnumValue();

            public Producer setEnumValue(Enum<P> value);

            public String getEnumValueName();

            public Producer setEnumValueName(String name);
        }
        /**
         * Action consumer
         * @see Dialog
         */
        public interface Consumer<C extends Enum<C>>
            extends Component
        {
            public Class<Enum<C>> getEnumClass();

            public Consumer setEnumClass(Class<Enum<C>> clas);

            public String getEnumClassName();

            public Consumer setEnumClassName(String name);
        }

        public Class<T> getValueClass();

        /**
         * @param clas If null, return false
         * 
         * @return Result of class equals, or false for null argument
         */
        public boolean isValueClass(Class<? extends Enum> clas);

        public T getValue();

        public String getName();
    }

    public Type getType();

    public boolean isMouse();

    public boolean isKey();

    public boolean isAction();

    public String toString();

}
