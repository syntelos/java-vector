package vector;

import java.awt.geom.Point2D;

/**
 * 
 */
public interface Event {
    /**
     * 
     */
    public enum Type {
        MouseMoved, MouseDown, MouseUp, KeyDown, KeyUp;

        public boolean isMouse(){
            switch(this){
            case MouseMoved:
            case MouseDown:
            case MouseUp:
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
     * 
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
