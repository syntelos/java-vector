package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class MouseUp
    extends AbstractMousePoint
{
    public MouseUp(Action action, Point2D point){
        super(Event.Type.MouseUp,action,point);
    }
}
