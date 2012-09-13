package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class MouseExited
    extends AbstractMouseMotion
{
    public MouseExited(Point2D point){
        super(Event.Type.MouseExited,Event.Mouse.Action.Exited,point);
    }
}
