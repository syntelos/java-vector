package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class MouseEntered
    extends AbstractMouseMotion
{
    public MouseEntered(Point2D point){
        super(Event.Type.MouseEntered,Event.Mouse.Action.Entered,point);
    }
}
