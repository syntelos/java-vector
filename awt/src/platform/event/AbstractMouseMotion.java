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

/**
 * 
 */
public class AbstractMouseMotion
    extends AbstractMouse
    implements Event.Mouse.Motion
{

    public final platform.geom.Point point;


    public AbstractMouseMotion(Type type, Action action, platform.geom.Point point){
        super(type,action);
        if (null != point)
            this.point = point;
        else
            throw new IllegalArgumentException();
    }


    public final platform.geom.Point getPoint(){
        return this.point;
    }
    protected StringBuilder toStringBuilder(){
        StringBuilder string = super.toStringBuilder();

        string.append(", point: ");
        string.append(this.point.toString());
        return string;
    }
}
