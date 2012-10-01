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
package vector;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Children track mouse motion, are visible on mouse in the container,
 * have scene output to overlay, and are repainted with mouse motion.
 */
public class TrackPointer
    extends Container
    implements Component.Layout
{

    public TrackPointer(){
        super();
    }


    @Override
    public void resized(){
        this.fit = false;

        this.setBoundsVectorInit(this.getParentVector());

        super.resized();
    }
    @Override
    public void modified(){
        this.fit = false;

        this.setBoundsVectorInit(this.getParentVector());

        super.modified();
    }
    public Component.Layout.Order queryLayout(){
        return Component.Layout.Order.Parent;
    }
    public Bounds queryBoundsContent(){

        return this.getBoundsVector();
    }
    public void layout(Component.Layout.Order order){
        switch(order){
        case Parent:
            this.modified();
            return;
        default:
            throw new UnsupportedOperationException(order.name());
        }
    }
    public boolean input(Event e){

        switch(e.getType()){

        case MouseEntered:{
            this.mouseIn = true;

            final Point2D.Float point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());
            final Event entered = new vector.event.MouseEntered(point);

            final Component.Iterator<Component> it = this.iterator();

            for (Component c: it){

                c.input(entered);
            }
            return true;
        }
        case MouseExited:{
            this.mouseIn = false;

            final Event exited = ((Event.Mouse)e).transformFrom(this.getTransformParent());

            final Component.Iterator<Component> it = this.iterator();

            for (Component c: it){

                c.input(exited);
            }
            return true;
        }
        case MouseMoved:{
            final Point2D.Float point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());

            final Event moved = new vector.event.MouseMoved(point);

            final Component.Iterator<Component> it = this.iterator();

            for (Component c: it){

                c.input(moved);
            }
            return false;
        }
        case MouseDown:
        case MouseUp:{
            final Event m = ((Event.Mouse)e).transformFrom(this.getTransformParent());

            final Component.Iterator<Component> it = this.iterator();

            for (Component c: it){
                if (c.input(m))
                    return true;
            }
            return false;
        }
        case MouseDrag:{
            final Event.Mouse.Point m = (Event.Mouse.Point)e;
            final Point2D.Float point = this.transformFromParent(m.getPoint());
            final Event dragged = new vector.event.MouseDrag(m,point);

            final Component.Iterator<Component> it = this.iterator();

            for (Component c: it){

                c.input(dragged);
            }
            return false;
        }
        case MouseWheel:
        case KeyDown:
        case KeyUp:{
            /*
             * Narrow-cast
             */
            final Component.Iterator<Component> it = this.iterator();

            for (Component c: it){
                if (c.input(e)){
                    return true;
                }
            }
            return false;
        }
        case Action:{
            boolean re = false;
            /*
             * Broadcast
             */
            final Component.Iterator<Component> it = this.iterator();

            for (Component c: it){

                re = (c.input(e) || re);
            }
            return re;
        }
        default:
            throw new IllegalStateException(e.getType().name());
        }
    }
    public Container outputOverlay(Graphics2D g){

        this.getTransformParent().transformFrom(g);

        final Component.Iterator<Component> it = this.iterator();

        for (Component c: it){

            if (c.isVisible()){

                Graphics2D cg = (Graphics2D)g.create();
                try {
                    c.outputScene(cg);
                }
                finally {
                    cg.dispose();
                }
            }
        }
        return this;
    }

}
