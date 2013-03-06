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
package vector;

import platform.Color;
import platform.Shape;
import platform.Path;

import platform.event.NamedAction;

import json.Json;
import json.ObjectJson;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;


/**
 * Non - text toggle buttons: {@link Toggle$Rollover Rollover}, {@link
 * Toggle$Check Check} and {@link Toggle$Colour Colour}.
 * 
 * @see Toggle$Rollover
 * @see Toggle$Check
 * @see Toggle$Colour
 */
public abstract class Toggle<E extends Enum<E>>
    extends BorderComponent
    implements Event.NamedAction.Producer<E>
{
    /**
     * Go low on mouse entered, and high on mouse exited: for subclass
     * extensions
     */
    public static class Rollover
        extends Toggle
    {

        protected boolean high;

        protected Color colorHigh, colorLow;


        public Rollover(){
            super();
        }


        @Override
        public void init(){
            super.init();

            this.high = false;
        }
        @Override
        public void destroy(){
            super.destroy();

            this.colorHigh = null;
            this.colorLow = null;
        }
        @Override
        public boolean isHigh(){
            return this.high;
        }
        @Override
        public boolean setHigh(){
            return (this.high = true);
        }
        @Override
        public boolean setLow(){
            return (this.high = false);
        }
        public Color getColorHigh(){
            return this.colorHigh;
        }
        public Rollover setColorHigh(Color c){
            if (null != c){
                this.colorHigh = c;
            }
            return this;
        }
        public Color getColorLow(){
            return this.colorLow;
        }
        public Rollover setColorLow(Color c){
            if (null != c){
                this.colorLow = c;
            }
            return this;
        }
        @Override
        public boolean input(Event e){

            if (super.input(e)){
                switch(e.getType()){
                case MouseEntered:

                    this.setLow();

                    this.outputScene();

                    return true;

                case MouseExited:

                    this.setHigh();

                    this.outputScene();

                    return true;

                default:
                    return false;
                }
            }
            else
                return true;

        }
        @Override
        public Rollover outputScene(Context g){

            super.outputScene(g);

            if (this.high){
                if (null != this.colorHigh)
                    g.setColor(this.colorHigh);
                else
                    g.setColor(this.color);
            }
            else if (null != this.colorLow)
                g.setColor(this.colorLow);
            else
                g.setColor(this.color);


            g.fill(this.getShape());

            return this;
        }
    }
    /**
     * Stateful button for crossed "X" checkbox, or colored checkbox.
     */
    public static class Check
        extends Toggle
    {

        protected boolean crossed, colored;

        protected boolean high;

        protected Color colorHigh, colorLow;


        public Check(){
            super();
        }



        @Override
        public void init(){
            super.init();

            this.crossed = false;
            this.colored = false;
            this.high = false;
        }
        @Override
        public void destroy(){
            super.destroy();

            this.colorHigh = null;
            this.colorLow = null;
        }
        @Override
        public void modified(){
            super.modified();

            if (null != this.colorHigh && null != this.colorLow){

                this.colored = (!this.crossed);
            }
            else {

                this.crossed = true;
            }
        }
        @Override
        public boolean isHigh(){
            return this.high;
        }
        @Override
        public boolean setHigh(){
            return (this.high = true);
        }
        @Override
        public boolean setLow(){
            return (this.high = false);
        }
        public boolean isCrossed(){
            return this.crossed;
        }
        public Boolean getCrossed(){
            if (this.crossed)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        }
        public Check setCrossed(Boolean b){
            if (null != b)
                this.crossed = b.booleanValue();
            return this;
        }
        public boolean isColored(){
            return this.colored;
        }
        public Boolean getColored(){
            if (this.colored)
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        }
        public Check setColored(Boolean b){
            if (null != b)
                this.colored = b.booleanValue();
            return this;
        }
        public Color getColorHigh(){
            return this.colorHigh;
        }
        public Check setColorHigh(Color c){
            if (null != c){
                this.colorHigh = c;
            }
            return this;
        }
        public Color getColorLow(){
            return this.colorLow;
        }
        public Check setColorLow(Color c){
            if (null != c){
                this.colorLow = c;
            }
            return this;
        }
        @Override
        public boolean mouseUp(){

            this.toggle();

            this.dispatchActionEvent();

            this.outputScene();

            return true;
        }
        @Override
        public Check outputScene(Context g){

            super.outputScene(g);

            if (this.high){
                if (null != this.colorHigh)
                    g.setColor(this.colorHigh);
                else
                    g.setColor(this.color);
            }
            else if (null != this.colorLow)
                g.setColor(this.colorLow);
            else
                g.setColor(this.color);

            if (this.crossed){

                if (this.high){

                    g.draw(this.getShape());
                }
                else {

                    g.draw(this.getBoundsVector());
                }
            }
            else if (this.colored){

                g.fill(this.getShape());
            }
            else if (this.high){

                g.draw(this.getShape());
            }
            return this;
        }
        protected Shape getShape(){
            Shape shape = this.shape;
            if (null == shape){
                final Bounds b = this.getBoundsVector();

                final float x0 = 0;
                final float y0 = 0;
                final float x1 = b.width;
                final float y1 = b.height;

                if (this.crossed){
                    platform.Path p = new platform.Path();
                    //(rect)
                    p.moveTo(x0,y0);
                    p.lineTo(x1,y0);
                    p.lineTo(x1,y1);
                    p.lineTo(x0,y1);
                    p.lineTo(x0,y0);

                    //(cross)
                    p.moveTo(x0,y0);
                    p.lineTo(x1,y1);

                    p.moveTo(x0,y1);
                    p.lineTo(x1,y0);

                    this.shape = p;
                    return p;
                }
                else {
                    this.shape = b;
                    return b;
                }
            }
            return shape;
        }
        public ObjectJson toJson(){

            ObjectJson thisModel = super.toJson();

            thisModel.setValue("crossed", this.getCrossed());
            thisModel.setValue("colored", this.getColored());
            thisModel.setValue("color-high", this.getColorHigh());
            thisModel.setValue("color-low", this.getColorLow());

            return thisModel;
        }
        public boolean fromJson(Json thisModel){

            super.fromJson(thisModel);

            this.setCrossed( (Boolean)thisModel.getValue("crossed"));
            this.setColored( (Boolean)thisModel.getValue("colored"));
            this.setColorHigh( (Color)thisModel.getValue("color-high",Color.class));
            this.setColorLow( (Color)thisModel.getValue("color-low",Color.class));

            return true;
        }
    }
    /**
     * Color palette picker
     * 
     * @see ColorPalette
     */
    public static class Colour
        extends Toggle
    {

        protected ColorPalette palette;


        public Colour(){
            super();
        }


        @Override
        public void destroy(){
            super.destroy();

            this.palette = null;
        }
        @Override
        public boolean isHigh(){
            return false;
        }
        @Override
        public boolean toggle(){

            if (null == this.palette){

                this.palette = new ColorPalette();
            }

            final Container parent = this.getParentVector();

            int idx = parent.indexOf(ColorPalette.class);
            if (-1 < idx)
                parent.remove(idx).destroy();
            else
                parent.add(this.palette);

            parent.modified();

            parent.outputScene();

            return true;
        }
        @Override
        public boolean setHigh(){
            return false;
        }
        @Override
        public boolean setLow(){
            return false;
        }
        @Override
        public Colour outputScene(Context g){

            super.outputScene(g);

            g.setColor(this.color);

            g.fill(this.getShape());

            return this;
        }
        @Override
        public boolean mouseUp(){

            this.toggle();

            this.outputScene();

            return true;
        }
        public ColorPalette getPalette(){
            return this.palette;
        }
        public Colour setPalette(ColorPalette c){

            if (null != c){

                this.palette = c;
            }
            return this;
        }
        public ObjectJson toJson(){

            ObjectJson thisModel = super.toJson();

            thisModel.setValue("palette", this.getPalette());

            return thisModel;
        }
        public boolean fromJson(Json thisModel){

            super.fromJson(thisModel);

            this.setPalette( (ColorPalette)thisModel.getValue("palette",ColorPalette.class));

            return true;
        }
    }


    protected Class<Enum<E>> enumClass;

    protected Enum<E> enumValue;

    protected Method enumValueOf;

    protected Shape shape;

    protected Color color;


    public Toggle(){
        super();
    }


    /**
     * @return Selected
     */
    public abstract boolean setHigh();
    /**
     * @return Selected
     */
    public abstract boolean setLow();
    /**
     * @return Selected
     */
    public abstract boolean isHigh();
    /**
     * @return Not selected
     */
    public final boolean isLow(){
        return (!this.isHigh());
    }
    /**
     * @return Selected
     */
    public boolean toggle(){
        if (this.isHigh())
            return this.setLow();
        else
            return this.setHigh();
    }
    @Override
    public void init(){
        super.init();

        this.color = Color.black;
    }

    @Override
    public void destroy(){
        super.destroy();

        this.enumClass = null;
        this.enumValueOf = null;
        this.enumValue = null;
        this.color = null;
        this.shape = null;
    }
    @Override
    public void modified(){
        super.modified();

        this.shape = null;
    }
    protected boolean mouseUp(){

        return this.dispatchActionEvent();
    }
    protected boolean dispatchActionEvent(){

        if (null != this.enumValue){

            final Component root = this.getRootContainer();
            if (null != root){

                NamedAction action = new NamedAction(this.enumValue);

                root.input(action);
            }
            else
                throw new IllegalStateException();
        }
        return true;
    }
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {
            switch(e.getType()){
            case MouseUp:
                if (this.mouseIn)
                    return this.mouseUp();
                else
                    return false;
            default:
                return false;
            }
        }
    }
    public Color getColor(){
        return this.color;
    }
    public Toggle setColor(Color c){
        if (null != c){
            this.color = c;
        }
        return this;
    }
    public final Class<Enum<E>> getEnumClass(){
        return this.enumClass;
    }
    public final Toggle setEnumClass(Class<Enum<E>> clas){
        if (null != clas)
            this.enumClass = clas;
        return this;
    }
    public final String getEnumClassName(){
        if (null != this.enumClass)
            return this.enumClass.getName();
        else
            return null;
    }
    public final Toggle setEnumClassName(String name){
        if (null != name){
            try {
                Class clas = Class.forName(name);
                if (clas.isEnum()){

                    this.enumClass = (Class<Enum<E>>)clas;

                    this.enumValueOf = Component.Tools.EnumValueMethod(this.enumClass);
                }
                else
                    throw new IllegalArgumentException(name);
            }
            catch (ClassNotFoundException exc){
                throw new IllegalArgumentException(name,exc);
            }
        }
        return this;
    }
    public final Enum<E> getEnumValue(){
        return this.enumValue;
    }
    public final Toggle setEnumValue(Enum<E> value){

        if (null != value)
            this.enumValue = value;

        return this;
    }
    public final String getEnumValueName(){
        if (null != this.enumValue)
            return this.enumValue.name();
        else
            return null;
    }
    public final Toggle setEnumValueName(String name){
        final Enum<E> value = Component.Tools.EnumValueOf(this.enumValueOf,name);
        return this.setEnumValue(value);
    }
    protected Shape getShape(){
        Shape shape = this.shape;
        if (null == shape){
            final Bounds b = this.getBoundsVector();

            this.shape = b;
            return b;
        }
        return shape;
    }

    public ObjectJson toJson(){

        ObjectJson thisModel = super.toJson();

        thisModel.setValue("enum-class", this.getEnumClassName());

        thisModel.setValue("enum-value", this.getEnumValueName());

        thisModel.setValue("color", this.getColor());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setEnumClassName( (String)thisModel.getValue("enum-class"));

        this.setEnumValueName( (String)thisModel.getValue("enum-value"));

        this.setColor( (Color)thisModel.getValue("color",Color.class));

        return true;
    }
}
