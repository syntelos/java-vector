package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class MouseDown
    extends AbstractMousePoint
{
    public MouseDown(Action action, Point2D point){
        super(Event.Type.MouseDown,action,point);
    }
}
