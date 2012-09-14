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
    public MouseEntered(Mouse e, Point2D point){
        super(e.getType(),e.getAction(),point);
    }
}
