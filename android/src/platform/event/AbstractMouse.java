/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, John Pritchard, Syntelos
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
package platform.event;

import vector.Event;

import platform.Transform;

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
    public final Event transformFrom(Transform parent){
        if (this instanceof Motion){
            final platform.geom.Point src = ((Motion)this).getPoint();
            final platform.geom.Point dst = parent.transformFrom(src);

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
            final platform.geom.Point src = ((Point)this).getPoint();
            final platform.geom.Point dst = parent.transformFrom(src);

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
    protected StringBuilder toStringBuilder(){
        StringBuilder string = super.toStringBuilder();

        string.append(", action: ");
        string.append(this.action.name());
        return string;
    }
}
