package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class MouseDrag
    extends AbstractMousePoint
{
    public MouseDrag(Action action, Point2D point){
        super(Event.Type.MouseDrag,action,point);
    }
    public MouseDrag(Mouse e, Point2D point){
        super(e.getType(),e.getAction(),point);
    }
}
