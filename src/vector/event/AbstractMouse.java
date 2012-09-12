package vector.event;

import vector.Event;

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
}
