package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class MouseMoved
    extends AbstractMouseMotion
{
    public MouseMoved(Point2D point){
        super(Event.Type.MouseMoved,Event.Mouse.Action.Moved,point);
    }
}
