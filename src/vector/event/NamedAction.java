package vector.event;

import vector.Event;

/**
 * 
 */
public class NamedAction
    extends AbstractEvent
    implements Event.NamedAction
{

    public final String name;


    public NamedAction(String name){
        super(Event.Type.Action);
        if (null != name && 0 < name.length())
            this.name = name;
        else
            throw new IllegalArgumentException();
    }


    public final String getName(){
        return this.name;
    }
}
