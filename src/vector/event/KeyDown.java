package vector.event;

import vector.Event;

import java.awt.event.KeyEvent;

/**
 * 
 */
public class KeyDown
    extends AbstractKey
{
    public KeyDown(KeyEvent evt){
        super(Event.Type.KeyDown,evt);
    }
}
