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
package vector;

import json.ArrayJson;
import json.Json;
import json.ObjectJson;

import lxl.List;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Container<T extends Component>
    extends AbstractComponent
    implements Component.Container<T>
{

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Component[] components;

    protected boolean fit;


    public Container(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.fit = false;
    }
    @Override
    public void destroy(){
        super.destroy();
        try {
            for (Component c: this){
                c.destroy();
            }
        }
        finally {
            this.components = null;
        }
    }
    @Override
    public void resized(){
        super.resized();

        for (Component c: this){

            c.resized();
        }

        if (this.fit){

            this.fit();
        }
    }
    @Override
    public void modified(){
        super.modified();

        for (Component c: this){

            c.modified();
        }

        if (this.fit){

            this.fit();
        }
    }
    @Override
    public void relocated(){
        super.relocated();

        for (Component c: this){

            c.relocated();
        }

        if (this.fit){

            this.fit();
        }
    }
    /**
     * Resize to size of children
     */
    public final boolean isFit(){
        return this.fit;
    }
    public final boolean getFit(){
        return this.fit;
    }
    public final Container setFit(boolean fit){

        this.fit = fit;

        return this;
    }
    public final Container setFit(Boolean fit){
        if (null != fit)
            return this.setFit(fit.booleanValue());
        else
            return this;
    }
    public boolean input(Event e){

        switch(e.getType()){

        case MouseEntered:{
            this.mouseIn = true;

            final Point2D.Float point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());
            final Event entered = new vector.event.MouseEntered(point);
            final Event exited = new vector.event.MouseExited(point);

            for (Component c: this){

                if (c.contains(point)){

                    c.input(entered);
                }
                else if (c.isMouseIn()){

                    c.input(exited);
                }
            }
            return true;
        }
        case MouseExited:{
            this.mouseIn = false;

            final Event m = ((Event.Mouse)e).transformFrom(this.getTransformParent());

            for (Component c: this.listMouseIn()){

                c.input(m);
            }
            return true;
        }
        case MouseMoved:{
            final Point2D.Float point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());

            final Event moved = new vector.event.MouseMoved(point);
            final Event entered = new vector.event.MouseEntered(point);
            final Event exited = new vector.event.MouseExited(point);

            for (Component c: this){

                if (c.isMouseIn()){

                    if (c.contains(point)){

                        c.input(moved);
                    }
                    else {
                        c.input(exited);
                    }
                }
                else if (c.contains(point)){
                    c.input(entered);
                }
            }
            return false;
        }
        case MouseDown:
        case MouseUp:{
            final Event m = ((Event.Mouse)e).transformFrom(this.getTransformParent());
            for (Component c: this){
                if (c.input(m))
                    return true;
            }
            return false;
        }
        case MouseDrag:{
            final Event.Mouse.Point m = (Event.Mouse.Point)e;
            final Point2D.Float point = this.transformFromParent(m.getPoint());
            final Event dragged = new vector.event.MouseDrag(m,point);
            final Event entered = new vector.event.MouseEntered(point);
            final Event exited = new vector.event.MouseExited(point);

            for (Component c: this){

                if (c.isMouseIn()){

                    if (c.contains(point)){

                        c.input(dragged);
                    }
                    else {
                        c.input(exited);
                    }
                }
                else if (c.contains(point)){
                    c.input(entered);
                }
            }
            return false;
        }
        case MouseWheel:
        case KeyDown:
        case KeyUp:
            /*
             * Narrow-cast
             */
            for (Component c: this){
                if (c.input(e)){
                    return true;
                }
            }
            return false;
        case Action:{
            boolean re = false;
            /*
             * Broadcast
             */
            for (Component c: this){

                re = (c.input(e) || re);
            }
            return re;
        }
        default:
            throw new IllegalStateException(e.getType().name());
        }
    }
    public final Container outputScene(Graphics2D g){

        this.getTransformParent().transformFrom(g);

        for (Component c: this){

            Graphics2D cg = (Graphics2D)g.create();
            try {
                c.outputScene(cg);
            }
            finally {
                cg.dispose();
            }
        }
        return this;
    }
    public final Container outputOverlay(Graphics2D g){

        this.getTransformParent().transformFrom(g);

        for (Component c: this){

            Graphics2D cg = (Graphics2D)g.create();
            try {
                c.outputOverlay(cg);
            }
            finally {
                cg.dispose();
            }
        }
        return this;
    }
    @Override
    public Container outputScene(){
        return (Container)super.outputScene();
    }
    public Container outputOverlay(){
        return (Container)super.outputOverlay();
    }

    public final java.util.Iterator<T> iterator(){
        return new Component.Iterator(this.components);
    }
    public final int count(){

        return Component.Tools.Count(this.components);
    }
    public final boolean has(int idx){

        return Component.Tools.Has(this.components,idx);
    }
    public final T get(int idx){

        return (T)Component.Tools.Get(this.components,idx);
    }
    public final int indexOf(Component comp){

        return Component.Tools.IndexOf(this.components,comp);
    }
    public final int indexOf(Class<? extends Component> compClass){

        return Component.Tools.IndexOf(this.components,compClass);
    }
    public final T add(T comp){
        if (null != comp){
            this.components = Component.Tools.Add(this.components,comp);

            comp.setParentVector(this);
            comp.init();
        }
        return comp;
    }
    public final T addUnique(T comp){
        int idx = Component.Tools.IndexOf(this.components,comp.getClass());
        if (-1 < idx)
            return (T)Component.Tools.Get(this.components,idx);
        else 
            return this.add(comp);
    }
    public final T remove(T comp){
        return this.remove(Component.Tools.IndexOf(this.components,comp));
    }
    public final T remove(int idx){
        T comp = null;
        if (-1 < idx){
            comp = (T)this.components[idx];

            this.components = Component.Tools.Remove(this.components,idx);
        }
        return comp;
    }
    public Component.Iterator<T> listMouseIn(){

        return Component.Tools.ListMouseIn(this.components);
    }
    public final Container warn(Throwable t, String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args),t);

        return this;
    }
    public final Container error(Throwable t, String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args),t);

        return this;
    }
    @Override
    public boolean drop(Component c){

        int idx = this.indexOf(c);
        if (-1 < idx){

            this.remove(idx).destroy();

            this.outputScene();

            return true;
        }
        else
            return super.drop(c);
    }
    protected final Border getBorder(){
        int idx = this.indexOf(Border.class);
        if (-1 < idx)
            return (Border)this.get(idx);
        else
            return null;
    }

    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("fit",this.getFit());

        thisModel.setValue("components",new ArrayJson(this));

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setFit( (Boolean)thisModel.getValue("fit"));

        Component.Tools.DecodeComponents(this,thisModel);
        /*
         * Rely on Display rather than call multiply
         *
         *  this.modified() 
         * 
         * Because this modified calls its children, and the Display
         * will do the same, we can preserve the requirement, avoid
         * redundancy, and serve the subclass by not calling this
         * here.
         */
        return true;
    }

    protected Container fit(){

        if (0 < this.count()){

            final Bounds bounds = this.getBoundsVector();

            float x0 = Float.MAX_VALUE, y0 = Float.MAX_VALUE;
            float x1 = Float.MIN_VALUE, y1 = Float.MIN_VALUE;

            for (Component c : this){

                if (c instanceof Border)
                    continue;
                else {
                    Bounds cb = c.getBoundsVector();

                    x0 = Math.min(x0,cb.x);
                    y0 = Math.min(y0,cb.y);

                    x1 = Math.max(x1,(cb.x+cb.width));
                    y1 = Math.max(y1,(cb.y+cb.height));
                }
            }

            /*
             * Reflect padding offsets
             */
            final float width = (x1+x0);
            final float height = (y1+y0);

            bounds.width = width;
            bounds.height = height;

            this.setBoundsVector(bounds);

            /*
             *
             */
            Border border = this.getBorder();
            if (null != border){

                border.resized();
            }
        }
        return this;
    }
}
