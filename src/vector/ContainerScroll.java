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

import platform.Shape;
import platform.geom.Rectangle;

/**
 * This {@link Container} expects to have zero or one or two {@link
 * ContainerScrollPosition} children which it manages.
 */
public class ContainerScroll
    extends Container
    implements Component.Layout
{


    public ContainerScroll(){
        super();
    }

    @Override
    public void resized(){

        this.content = false;

        super.resized();

        this.layout();
    }
    @Override
    public void modified(){

        this.content = false;

        super.modified();

        this.layout();
    }
    @Override
    public void relocated(){

        this.content = false;

        super.relocated();

        this.layout();
    }
    public Component.Layout.Order queryLayout(){

        return Component.Layout.Order.Parent;
    }
    public void layout(Component.Layout.Order order){

        if (Component.Layout.Order.Parent != order)
            throw new UnsupportedOperationException();
        else
            this.modified();
    }
    /**
     * Make the objective area visible.  The objective may be a
     * cursor, for example.
     * @param shape Objective area
     */
    public boolean position(Shape shape){
        ContainerScrollPosition hor = null, ver = null;
        {
            final ContainerScrollPosition[] p = listContainerScrollPosition();
            if (null != p){
                switch(p.length){
                case 1:{
                    ContainerScrollPosition pp = p[0];
                    if (ContainerScrollPosition.Axis.Horizontal == pp.axis)
                        hor = pp;
                    else
                        ver = pp;

                    break;
                }
                case 2:
                    hor = p[0];
                    ver = p[1];
                    break;
                }
            }
        }

        boolean re = false;

        if (null != hor || null != ver){

            Bounds bounds = shape.getBoundsVector();

            if (null != hor)
                re = (hor.position(bounds) || re);

            if (null != ver)
                re = (ver.position(bounds) || re);
        }
        return re;
    }
    /**
     * @return Order horizontal, vertical in array null, one or two.
     * An array of one may be horizontal or vertical.  An array of two
     * is always ordered.
     */
    public ContainerScrollPosition[] listContainerScrollPosition(){

        ContainerScrollPosition[] p = this.list(ContainerScrollPosition.class).list(ContainerScrollPosition.class);
        if (null == p)
            return null;
        else {
            switch(p.length){

            case 1:
                return p;

            case 2:{
                ContainerScrollPosition hor = null, ver = null;
                ContainerScrollPosition pp;

                pp = p[0];
                if (ContainerScrollPosition.Axis.Horizontal == pp.axis)
                    hor = pp;
                else if (ContainerScrollPosition.Axis.Vertical == pp.axis)
                    ver = pp;

                pp = p[1];
                if (ContainerScrollPosition.Axis.Horizontal == pp.axis)
                    hor = pp;
                else if (ContainerScrollPosition.Axis.Vertical == pp.axis)
                    ver = pp;

                if (null != hor && null != ver){

                    p[0] = hor;
                    p[1] = ver;

                    return p;
                }
                else if (null != hor)
                    return new ContainerScrollPosition[]{hor};
                else if (null != ver)
                    return new ContainerScrollPosition[]{ver};
                else
                    return null;
            }
            default:
                return null;
            }
        }
    }
    public void layout(){

        this.setBoundsVectorInit(this.getParentVector());

        ContainerScrollPosition hor = null, ver = null;
        {
            final ContainerScrollPosition[] p = listContainerScrollPosition();
            if (null != p){
                switch(p.length){
                case 1:{
                    ContainerScrollPosition pp = p[0];
                    if (ContainerScrollPosition.Axis.Horizontal == pp.axis)
                        hor = pp;
                    else
                        ver = pp;

                    break;
                }
                case 2:
                    hor = p[0];
                    ver = p[1];
                    break;
                }
            }
        }

        if (null != hor || null != ver){

            Bounds children = this.queryBoundsContent();

            if (children.width > this.bounds.width){

                if (null != hor)
                    hor.layoutScroll(children);
            }
            else if (children.width < this.bounds.width){

                if (null != hor)
                    hor.layoutFit(children);
            }

            if (children.height > this.bounds.height){

                if (null != ver)
                    ver.layoutScroll(children);
            }
            else if (children.height < this.bounds.height){

                if (null != ver)
                    ver.layoutFit(children);
            }
        }
    }
}
