package vector.event;

import vector.Event;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * 
 */
public class AbstractMouse
    extends AbstractEvent
    implements Event.Mouse
{

    public final Action action;


    public AbstractMouse(Type type, Action action){
        super(type);
        if (null != action)
            this.action = action;
        else
            throw new IllegalArgumentException();
    }


    public final Action getAction(){
        return this.action;
    }
    public final boolean isMotion(){
        return this.action.isMotion();
    }
    public final boolean isPoint(){
        return this.action.isPoint();
    }
    public final boolean isWheel(){
        return this.action.isWheel();
    }
    public final Event apply(AffineTransform parent){
        if (this instanceof Motion){
            final Point2D src = ((Motion)this).getPoint();
            final Point2D dst = parent.transform(src,(new Point2D.Float(0,0)));

            switch(this.getType()){
            case MouseEntered:
                return new MouseEntered(this,dst);
            case MouseExited:
                return new MouseExited(this,dst);
            case MouseMoved:
                return new MouseMoved(this,dst);
            default:
                throw new IllegalStateException(this.getType().name());
            }
        }
        else if (this instanceof Point){
            final Point2D src = ((Point)this).getPoint();
            final Point2D dst = parent.transform(src,(new Point2D.Float(0,0)));

            switch(this.getType()){
            case MouseDown:
                return new MouseDown(this,dst);
            case MouseUp:
                return new MouseUp(this,dst);
            case MouseDrag:
                return new MouseDrag(this,dst);
            default:
                throw new IllegalStateException(this.getType().name());
            }
        }
        else
            return this;
    }
}
