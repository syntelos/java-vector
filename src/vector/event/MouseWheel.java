package vector.event;

import vector.Event;

import java.awt.geom.Point2D;

/**
 * 
 */
public class MouseWheel
    extends AbstractMouse
    implements Event.Mouse.Wheel
{
    public final int count;


    public MouseWheel(int count){
        super(Event.Type.MouseWheel,Event.Mouse.Action.Wheel);
        this.count = count;
    }


    public final int getCount(){
        return this.count;
    }
}
