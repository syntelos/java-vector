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
public class MouseMoved
    extends AbstractMouseMotion
{
    public MouseMoved(Point2D.Float point){
        super(Event.Type.MouseMoved,Event.Mouse.Action.Moved,point);
    }
    public MouseMoved(Mouse e, Point2D.Float point){
        super(e.getType(),e.getAction(),point);
    }
}
