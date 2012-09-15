package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class AbstractMousePoint
    extends AbstractMouse
    implements Event.Mouse.Point
{

    public final Point2D point;


    public AbstractMousePoint(Type type, Action action, Point2D point){
        super(type,action);
        if (null != point)
            this.point = point;
        else
            throw new IllegalArgumentException();
    }


    public final Point2D getPoint(){
        return this.point;
    }
    protected StringBuilder toStringBuilder(){
        StringBuilder string = super.toStringBuilder();

        string.append(", point: ");
        string.append(this.point.toString());
        return string;
    }
}
