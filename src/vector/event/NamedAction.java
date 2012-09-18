package vector.event;

import vector.Event;

/**
 * Propagation of button and menu events.  Dispatch global application
 * actions via root container input.
 * 
 * The enum type permits application global consumers to validate an
 * action by type.
 */
public class NamedAction<T extends Enum<T>>
    extends AbstractEvent
    implements Event.NamedAction<T>
{

    public final Enum<T> value;


    public NamedAction(Enum<T> value){
        super(Event.Type.Action);
        if (null != value)
            this.value = value;
        else
            throw new IllegalArgumentException();
    }


    public final Class<T> getValueClass(){

        return (Class<T>)this.value.getClass();
    }
    public final T getValue(){

        return (T)this.value;
    }
    public final String getName(){

        return this.value.name();
    }
    protected StringBuilder toStringBuilder(){
        StringBuilder string = super.toStringBuilder();

        string.append(", name: \"");
        string.append(this.getName());
        string.append('\"');
        return string;
    }
}
