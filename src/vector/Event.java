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
import platform.event.KeyCode;

/**
 * Immutable universal event for a single input method, intended for
 * ease of application development.
 */
public interface Event {

    /**
     * Mouse drag is a point action
     * 
     * Mouse moved includes enter and exit
     */
    public enum Type {
        MouseEntered, MouseExited, MouseMoved, MouseDown, MouseUp, MouseDrag, MouseWheel, KeyDown, KeyUp, Action;

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
