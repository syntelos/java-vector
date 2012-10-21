/*
 * Vector (http://code.google.com/p/java-vector/)
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
    protected StringBuilder toStringBuilder(){
        StringBuilder string = new StringBuilder();

        string.append(this.getClass().getName());
        string.append(", type: ");
        string.append(this.type.name());
        return string;
    }
    public final String toString(){
        return this.toStringBuilder().toString();
    }
}
