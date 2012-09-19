/*
 * Java Vector
 * Copyright (C) 2012, The DigiVac Company
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
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
