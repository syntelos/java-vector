package vector;

import java.awt.geom.Point2D;

/**
 * 
 */
public class AbstractEvent
    extends Object
    implements Event
{
    /**
     * 
     */
    public static class AbstractMouse
        extends AbstractEvent
        implements Event.Mouse
    {
        /**
         * 
         */
        public static class AbstractMouseMotion
            extends AbstractMouse
            implements Event.Mouse.Motion
        {
            public static class Entered
                extends AbstractMouseMotion
            {
                public Entered(Point2D point){
                    super(Event.Type.MouseMoved,Event.Mouse.Action.Entered,point);
                }
            }
            public static class Exited
                extends AbstractMouseMotion
            {
                public Exited(Point2D point){
                    super(Event.Type.MouseMoved,Event.Mouse.Action.Exited,point);
                }
            }
            public static class Moved
                extends AbstractMouseMotion
            {
                public Moved(Point2D point){
                    super(Event.Type.MouseMoved,Event.Mouse.Action.Moved,point);
                }
            }

            public final Point2D point;


            public AbstractMouseMotion(Type type, Action action, Point2D point){
                super(type,action);
                if (null != point)
                    this.point = point;
                else
                    throw new IllegalArgumentException();
            }


            public Point2D getPoint(){
                return this.point;
            }
        }
        /**
         * 
         */
        public static class AbstractMousePoint
            extends AbstractMouse
            implements Event.Mouse.Point
        {
            public static class Down
                extends AbstractMousePoint
            {
                public Down(Action action, Point2D point){
                    super(Event.Type.MouseDown,action,point);
                }
            }
            public static class Up
                extends AbstractMousePoint
            {
                public Up(Action action, Point2D point){
                    super(Event.Type.MouseUp,action,point);
                }
            }
            public static class Drag
                extends AbstractMousePoint
            {
                public Drag(Action action, Point2D point){
                    super(Event.Type.MouseDrag,action,point);
                }
            }


            public final Point2D point;


            public AbstractMousePoint(Type type, Action action, Point2D point){
                super(type,action);
                if (null != point)
                    this.point = point;
                else
                    throw new IllegalArgumentException();
            }


            public Point2D getPoint(){
                return this.point;
            }
        }
        /**
         * 
         */
        public static class AbstractMouseWheel
            extends AbstractMouse
            implements Event.Mouse.Wheel
        {

            public final int count;


            public AbstractMouseWheel(int count){
                super(Type.MouseWheel,Action.Wheel);
                this.count = count;
            }


            public int getCount(){
                return this.count;
            }
        }


        public final Action action;


        public AbstractMouse(Type type, Action action){
            super(type);
            if (null != action)
                this.action = action;
            else
                throw new IllegalArgumentException();
        }


        public Action getAction(){
            return this.action;
        }
        public boolean isMotion(){
            return this.action.isMotion();
        }
        public boolean isPoint(){
            return this.action.isPoint();
        }
        public boolean isWheel(){
            return this.action.isWheel();
        }
    }

    public final Type type;


    public AbstractEvent(Type type){
        super();
        if (null != type)
            this.type = type;
        else
            throw new IllegalArgumentException();
    }


    public final Type getType(){
        return this.type;
    }
    public final boolean isMouse(){
        return this.type.isMouse();
    }
    public final boolean isKey(){
        return this.type.isKey();
    }
}
