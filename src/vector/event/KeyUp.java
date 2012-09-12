package vector.event;

import vector.Event;

import java.awt.event.KeyEvent;

/**
 * 
 */
public class KeyUp
    extends AbstractKey
{
    public KeyUp(KeyEvent evt){
        super(Event.Type.KeyUp,evt);
    }
}
