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

import java.awt.geom.Point2D;

/**
 * 
 */
public class AbstractMouseMotion
    extends AbstractMouse
    implements Event.Mouse.Motion
{

    public final Point2D.Float point;


    public AbstractMouseMotion(Type type, Action action, Point2D.Float point){
        super(type,action);
        if (null != point)
            this.point = point;
        else
            throw new IllegalArgumentException();
    }


    public final Point2D.Float getPoint(){
        return this.point;
    }
    protected StringBuilder toStringBuilder(){
        StringBuilder string = super.toStringBuilder();

        string.append(", point: ");
        string.append(this.point.toString());
        return string;
    }
}
