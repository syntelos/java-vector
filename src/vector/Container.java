/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, John Pritchard, Syntelos
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

import platform.Transform;
import platform.geom.Point;

import json.ArrayJson;
import json.Json;
import json.ObjectJson;

import lxl.List;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple implementation of the {@link Component$Container Container}
 * interface with a few essential application features.
 * 
 * <h3>clip</h3>
 * 
 * The boolean property named "clip" may enable the clipping of the
 * output of children to their bounding box.
 * 
 * <h3>content</h3>
 * 
 * The boolean property named "content" will cause a container to set
 * its bounds to the union of its content children.  It is mutually
 * exclusive of "parent".
 * 
 * <h3>parent</h3>
 * 
 * The boolean property named "parent" will cause a container to set
 * its bounds to the difference of the parent and its margin.  It is
 * mutually exclusive of "content".
 * 
 * <h3>scale</h3>
 * 
 * The boolean property named "scale" will define a scaling transform
 * to fit the content bounds into this bounding box.
 * 
 */
public class Container
    extends AbstractComponent
    implements Component.Container, 
               Component.Margin, 
               Component.Layout, 
               Component.Bordered<Border>, 
               Table.Col.Span
{

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    protected Component[] components;

    protected boolean content, parent, clip, scale;

    protected final Padding margin = new Padding();

    protected Integer colSpan;


    public Container(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.content = false;
        this.parent = false;
        this.clip = false;
        this.scale = false;
        this.margin.init();
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
        this.colSpan = null;
    }
    @Override
    public void resized(){
        super.resized();

        if (this.content){

            for (Component c: this.listContent(Component.class)){

                c.resized();
            }

            this.content();
        }
        else if (this.parent){

            this.parent();

            for (Component c: this){

                c.resized();
            }
        }
        else {

            for (Component c: this){

                c.resized();
            }
        }

        if (this.scale){

            this.scale();
        }
    }
    @Override
    public void modified(){
        super.modified();

        if (this.content){

            for (Component c: this.listContent(Component.class)){

                c.modified();
            }

            this.content();
        }
        else if (this.parent){

            this.parent();

            for (Component c: this){

                c.modified();
            }
        }
        else {

            for (Component c: this){

                c.modified();
            }
        }

        if (this.scale){

            this.scale();
        }
    }
    @Override
    public void relocated(){
        super.relocated();

        for (Component c: this){

            c.relocated();
        }
    }

    public Component.Layout.Order queryLayout(){

        if (this.parent)
            return Component.Layout.Order.Parent;
        else 
            return Component.Layout.Order.Content;
    }
    public void layout(Component.Layout.Order order){

        switch (order){
        case Parent:
            this.parent = true;
            this.content = false;
            break;
        case Content:
            this.parent = false;
            this.content = true;
            break;
        default:
            throw new IllegalStateException(order.name());
        }
        this.modified();
    }
    public <C extends Container> C clearContent(){

        this.components = Component.Tools.ClearContent(this.components);

        return (C)this;
    }
    /**
     * @return Geometric union of "content" children and (0,0) origin
     */
    public Bounds queryBoundsContent(){

        float w = 0.0f, h = 0.0f;

        for (Component c : this.listContent(Component.class)){

            Bounds cb = c.getBoundsVector();

            w = Math.max(w,(cb.x+cb.width));

            h = Math.max(h,(cb.y+cb.height));
        }
        return new Bounds(w,h);
    }
    /**
     * Resize to the geometric union of all children (except members
     * of class {@link Border}).
     */
    public final boolean isContent(){
        return this.content;
    }
    public final Boolean getContent(){
        if (this.content)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Container setContent(boolean content){

        this.content = content;

        return this;
    }
    public final Container setContent(Boolean content){
        if (null != content)
            return this.setContent(content.booleanValue());
        else
            return this;
    }
    public final boolean isParent(){
        return this.parent;
    }
    public final Boolean getParent(){
        if (this.parent)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Container setParent(boolean parent){

        this.parent = parent;

        return this;
    }
    public final Container setParent(Boolean parent){
        if (null != parent)
            return this.setParent(parent.booleanValue());
        else
            return this;
    }
    public final int getTableColSpan(){
        if (null != this.colSpan)
            return this.colSpan.intValue();
        else
            return 0;
    }
    public final boolean hasTableColSpan(){

        return (null != this.colSpan);
    }
    public final Container setTableColSpan(Integer value){

        this.colSpan = value;
        return this;
    }
    public final boolean isClip(){
        return this.clip;
    }
    public final Boolean getClip(){
        if (this.clip)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Container setClip(boolean clip){

        this.clip = clip;

        return this;
    }
    public final Container setClip(Boolean clip){
        if (null != clip)
            return this.setClip(clip.booleanValue());
        else
            return this;
    }
    public final boolean isScale(){
        return this.scale;
    }
    public final Boolean getScale(){
        if (this.scale)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    public final Container setScale(boolean scale){

        this.scale = scale;

        return this;
    }
    public final Container setScale(Boolean scale){
        if (null != scale)
            return this.setScale(scale.booleanValue());
        else
            return this;
    }
    public final Padding getMargin(){

        return this.margin.clone();
    }
    public final Container setMargin(Padding margin){

        if (null != margin){

            this.margin.set(margin);
        }
        return this;
    }
    /**
     * For {@link TextLayout} children
     */
    public final Container clearMargin(){

        this.margin.init();

        return this;
    }

    public boolean input(Event e){

        switch(e.getType()){

        case MouseEntered:{
            /*
             * Broadcast
             */
            this.mouseIn = true;

            if (Event.Debug.IsEntry){
                Event.Debug.trace("mouse en",this,e);
            }

            final Point point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());
            final Event entered = new platform.event.MouseEntered(point);
            final Event exited = new platform.event.MouseExited(point);

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
            /*
             * Broadcast
             */
            this.mouseIn = false;

            if (Event.Debug.IsEntry){
                Event.Debug.trace("mouse ex",this,e);
            }

            final Event m = ((Event.Mouse)e).transformFrom(this.getTransformParent());

            for (Component c: this.listMouseIn(Component.class)){

                c.input(m);
            }
            return true;
        }
        case MouseMoved:{
            /*
             * Broad-cast
             */
            final Point point = this.transformFromParent(((Event.Mouse.Motion)e).getPoint());

            final Event moved = new platform.event.MouseMoved(point);
            final Event entered = new platform.event.MouseEntered(point);
            final Event exited = new platform.event.MouseExited(point);

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
            /*
             * Narrow-cast
             */
            final Event m = ((Event.Mouse)e).transformFrom(this.getTransformParent());
            for (Component c: this){
                if (c.input(m))
                    return true;
            }
            return false;
        }
        case MouseDrag:{
            /*
             * Broad-cast
             */
            final Event.Mouse.Point m = (Event.Mouse.Point)e;
            final Point point = this.transformFromParent(m.getPoint());
            final Event dragged = new platform.event.MouseDrag(m,point);
            final Event entered = new platform.event.MouseEntered(point);
            final Event exited = new platform.event.MouseExited(point);

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
             * Broad-cast
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
    public Container outputScene(Context g){

        if (this.visible){

            if (this.clip)
                this.clipFrom(g);
            else
                this.transformFrom(g);

            for (Component c: this){

                if (c.isVisible()){

                    Context cg = g.create();
                    try {
                        c.outputScene(cg);
                    }
                    finally {
                        cg.dispose();
                    }
                }
            }
        }
        return this;
    }
    public Container outputOverlay(Context g){

        if (this.visible){

            if (this.clip)
                this.clipFrom(g);
            else
                this.transformFrom(g);

            for (Component c: this){

                if (c.isVisible()){

                    Context cg = g.create();
                    try {
                        c.outputOverlay(cg);
                    }
                    finally {
                        cg.dispose();
                    }
                }
            }
        }
        return this;
    }

    public final boolean isEmpty(){
        return (null == this.components);
    }
    public final boolean isNotEmpty(){
        return (null != this.components);
    }
    public final Component.Iterator iterator(){
        return new Component.Iterator(this.components);
    }
    public final int count(){

        return Component.Tools.Count(this.components);
    }
    public final boolean has(int idx){

        return Component.Tools.Has(this.components,idx);
    }
    public final <C extends Component> C get(int idx){

        return (C)Component.Tools.Get(this.components,idx);
    }
    public final <C extends Component> C set(C comp, int idx){

        final Component old = Component.Tools.Set(this.components,comp,idx);

        old.destroy();

        comp.setParentVector(this);
        comp.init();

        return comp;
    }
    public final <C extends Component> C replace(C a, C b){

        return (C)Component.Tools.Replace(this.components,a,b);
    }
    public final int indexOf(Component comp){

        return Component.Tools.IndexOf(this.components,comp);
    }
    public final int indexOf(Class<? extends Component> compClass){

        return Component.Tools.IndexOf(this.components,compClass);
    }
    public final <C extends Component> C add(C comp){
        if (null != comp){
            this.components = Component.Tools.Add(this.components,comp);

            comp.setParentVector(this);
            comp.init();
        }
        return comp;
    }
    public <C extends Component> C insert(C comp, int idx){
        if (null != comp){
            this.components = Component.Tools.Insert(this.components,comp,idx);

            comp.setParentVector(this);
            comp.init();
        }
        return comp;
    }
    public final <C extends Component> C addUnique(C comp){
        int idx = Component.Tools.IndexOf(this.components,comp.getClass());
        if (-1 < idx)
            return (C)Component.Tools.Get(this.components,idx);
        else 
            return this.add(comp);
    }
    public final <C extends Component> C remove(C comp){
        return this.remove(Component.Tools.IndexOf(this.components,comp));
    }
    public final <C extends Component> C remove(int idx){
        C comp = null;
        if (-1 < idx){
            comp = (C)this.components[idx];

            this.components = Component.Tools.Remove(this.components,idx);
        }
        return comp;
    }
    public final <C extends Component> Component.Iterator<C> listMouseIn(Class<C> clas){

        return Component.Tools.ListMouseIn(this.components,clas);
    }
    public final <C extends Component> Component.Iterator<C> list(Class<C> clas){

        return Component.Tools.List(this.components,clas);
    }
    public final <C extends Component> Component.Iterator<C> listContent(Class<C> clas){

        return Component.Tools.ListLayoutContent(this.components,clas);
    }
    public final <C extends Component> Component.Iterator<C> listParent(Class<C> clas){

        return Component.Tools.ListLayoutParent(this.components,clas);
    }
    public final Container warn(Throwable t, String fmt, Object... args){

        this.log.log(Level.WARNING,String.format(fmt,args),t);

        return this;
    }
    public final Container error(Throwable t, String fmt, Object... args){

        this.log.log(Level.SEVERE,String.format(fmt,args),t);

        return this;
    }
    /**
     * @see AbstractComponent
     */
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
    /**
     * @see AbstractComponent
     */
    @Override
    public boolean drop(Class<? extends Component> c){

        int idx = this.indexOf(c);
        if (-1 < idx){

            this.remove(idx).destroy();

            this.outputScene();

            return true;
        }
        else
            return super.drop(c);
    }
    public final Border getBorder(){
        int idx = this.indexOf(Border.class);
        if (-1 < idx)
            return this.get(idx);
        else
            return null;
    }
    public final Component.Bordered setBorder(Border border){
        int idx = this.indexOf(Border.class);
        if (-1 < idx)
            this.set(border,idx);
        else
            this.insert(border,0);

        return this;
    }
    public final Component.Bordered setBorder(Border border, Json model){

        this.setBorder(border);

        if (null != border){

            border.fromJson(model);
        }
        return this;
    }

    public String propertyNameOfValue(Class vac){
        if (null == vac)
            return null;
        else {
            String name = super.propertyNameOfValue(vac);
            if (null != name)
                return name;
            else {

                if (Component.class.isAssignableFrom(vac))
                    return "components";
                else if (Padding.class.isAssignableFrom(vac))
                    return "margin";
                else if (Integer.class.isAssignableFrom(vac))
                    return "col-span";
                else
                    return null;
            }
        }
    }
    public String propertyPathOfValue(Object value){
        if (null == value)
            return null;
        else {
            String name = this.propertyNameOfValue(value.getClass());
            if (null != name){
                StringBuilder string = new StringBuilder();

                string.append(name);

                final Component parent = super.parent;

                if (null != parent){

                    String path = parent.propertyPathOfValue(this);

                    if (null != path){
                        string.insert(0,"/");
                        string.insert(0,path);
                    }
                }

                if (Component.class.isAssignableFrom(value.getClass())){

                    final int idx = this.indexOf( (Component)value);
                    if (-1 < idx){

                        string.append('/');
                        string.append(idx);
                    }
                }
                return string.toString();
            }
            else
                return null;
        }
    }
    public ObjectJson toJson(){
        ObjectJson thisModel =  super.toJson();

        thisModel.setValue("content",this.getContent());
        thisModel.setValue("parent",this.getParent());
        thisModel.setValue("clip",this.getClip());
        thisModel.setValue("scale",this.getScale());
        thisModel.setValue("margin",this.getMargin());

        if (!(this instanceof Component.Build)){

            thisModel.setValue("components",new ArrayJson(this));
        }

        if (this.hasTableColSpan()){
            thisModel.setValue("col-span",this.getTableColSpan());
        }

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setContent( (Boolean)thisModel.getValue("content"));
        this.setParent( (Boolean)thisModel.getValue("parent"));
        this.setClip( (Boolean)thisModel.getValue("clip"));
        this.setScale( (Boolean)thisModel.getValue("scale"));
        this.setMargin( (Padding)thisModel.getValue("margin",Padding.class));
        this.setTableColSpan( (Integer)thisModel.getValue("col-span",Integer.class));

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

    public void content(){

        if (0 < this.count()){

            final Bounds bounds = this.getBoundsVector();

            final Bounds children = this.queryBoundsContent();

            bounds.width = (children.x+children.width);
            bounds.height = (children.y+children.height);

            this.setBoundsVector(bounds);

            for (Component.Layout c : this.listParent(Component.Layout.class)){

                c.resized();
            }
        }
    }
    public void parent(){

        this.setBoundsVectorInit(this.getParentVector(),this.getMargin());
    }
    public void scale(){

        if (0 < this.count()){

            final Bounds bounds = this.getBoundsVector();

            final Bounds children = this.queryBoundsContent();

            if (children.isNotEmpty() && (!children.equals(bounds))){

                this.setTransformLocal(bounds.scaleFromAbsolute(children));
            }
            else {

                this.setTransformLocal(1f,1f);
            }
        }
    }
    public Component setBoundsVectorCenter(){

        return this.setBoundsVectorCenter(this.margin);
    }

}
