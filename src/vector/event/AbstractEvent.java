package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class AbstractEvent
    extends Object
    implements Event
{

    public final Type type;


    public AbstractEvent(Type type){
        super();
        if (null != type)
            this.type = type;
        else
            throw new IllegalArgumentException();
    }


    public final Type getType(){
        return this.type;
    }
    public final boolean isMouse(){
        return this.type.isMouse();
    }
    public final boolean isKey(){
        return this.type.isKey();
    }
    public final boolean isAction(){
        return this.type.isAction();
    }
}
