package vector;

import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.*;
import java.awt.geom.Point2D;

/**
 * 
 */
public interface Event {
    /**
     * Mouse drag is a point action
     * 
     * Mouse moved includes enter and exit
     */
    public enum Type {
        MouseMoved, MouseDown, MouseUp, MouseDrag, MouseWheel, KeyDown, KeyUp;

        public boolean isMouse(){
            switch(this){
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
            public boolean isWheel(){
                switch(this){
                case Wheel:
                    return true;
                default:
                    return false;
                }
            }

            public final static Action PointButton(MouseEvent evt){
                switch(evt.getButton()){
                case BUTTON1:
                    return Event.Mouse.Action.Point1;

                case BUTTON2:
                    return Event.Mouse.Action.Point2;

                case BUTTON3:
                    return Event.Mouse.Action.Point3;

                default:
                    return null;
                }
            }
        }

        public Action getAction();

        public boolean isMotion();

        public boolean isPoint();

        public boolean isWheel();

        public interface Motion
            extends Mouse
        {

            public Point2D getPoint();
        }
        public interface Point
            extends Mouse
        {

            public Point2D getPoint();
        }
        public interface Wheel
            extends Mouse
        {

            public int getCount();
        }
    }
    /**
     * 
     */
    public interface Key
        extends Event
    {

    }

    public Type getType();

    public boolean isMouse();

    public boolean isKey();

}
