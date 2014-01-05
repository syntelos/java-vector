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
package vector.dialog;

import vector.ActionLabel;
import vector.Border;
import vector.Button;
import vector.Component;
import vector.Event;

import platform.Stroke;

import json.Json;
import json.ObjectJson;

/**
 * Dialog menu for Display show will drop on its action class.
 * 
 * <p> See {@link Viewport} for an example of an enumerated list of
 * menu buttons that produce enum action events.  Typically these
 * events are interpreted by a subclass.  In the Viewport case, the
 * {@link vector.Viewport$Size Viewport Size} actions are interpreted
 * by the {@link platform.Display platform Display}. </p>
 * 
 * <p> Action enums may return their labels via the {@link
 * ActionLabel} interface. </p>
 * 
 * @see Style
 * @see vector.Dialog
 */
public class Menu<E extends Enum<E>>
    extends vector.TableSmall
    implements Event.NamedAction.Consumer<E>,
               Component.Build.InShow
{


    protected Class<Enum<E>> enumClass;

    protected java.lang.reflect.Method actorList;

    protected Enum<E>[] actionList;


    public Menu(){
        super();
    }
    public Menu(Class enumClass){
        super();
        this.enumClass = (Class<Enum<E>>)enumClass;
    }



    /**
     * Destroy and rebuild subtree within Container 'add', or JSON
     * "init".
     */
    public void init(){
        super.init();

        this.setCols(1);

        this.setCellSpacing(4);
    }
    public final boolean isEnumClassActionLabel(){
        final Class<Enum<E>> enumClass = this.enumClass;

        return ((null != enumClass)&&
                (ActionLabel.class.isAssignableFrom(enumClass)));
    }
    public Menu build(){
        final Enum<E>[] values = this.enumValues();

        if (null != values){

            this.outline(this);

            final boolean actionLabel = this.isEnumClassActionLabel();

            final int count = values.length;

            for (int cc = 0; cc < count; cc++){

                String label;

                if (actionLabel)
                    label = ((ActionLabel)values[cc]).getActionLabel();
                else
                    label = values[cc].toString();

                Button button = new Button();
                {
                    this.add(button);

                    this.style(button,values[cc],label);
                }
            }
        }
        return this;
    }
    /**
     * Text style
     */
    public Menu style(vector.Text c){

        if (null != c){

            c.setColor(Style.FG(0.8f));
            c.setFont(Style.FontLarge());
            c.setFixed(true);
        }
        return this;
    }
    /**
     * Button / TextEdit style calls {@link #style(vector.Text)
     * style(Text)}.
     */
    public Menu outline(Component.Bordered c){

        if (null != c){

            if (c instanceof vector.Text){

                this.style( (vector.Text)c);
            }

            Border border = new Border();
            {
                c.setBorder(border);

                border.setBackground(Style.BGD(0.5f));
                border.setColor(Style.FG(0.8f));
                border.setColorOver(Style.NG(0.8f));
                border.setStyle(Border.Style.ROUND);
                border.setArc(16.0);
                border.setStroke(new Stroke(2f));
                border.setStrokeOver(new Stroke(4f));
            }
        }
        return this;
    }
    public Menu style(Button button, Enum<E> value){

        return this.style(button,value,value.toString());
    }
    /**
     * Apply text, enum, and visual defaults to buttons
     */
    public Menu style(Button button, Enum<E> value, String label){

        this.outline(button);
        /*
         * More than style
         */
        button.setEnumClass(this.enumClass);
        button.setEnumValue(value);

        button.setText(label);

        return this;
    }
    /**
     * 
     */
    public java.lang.reflect.Method enumValuesMethod(){

        java.lang.reflect.Method m = this.actorList;
        if (null == m){
            if (null != this.enumClass){
                m = Component.Tools.ListEnumMethod(this.enumClass);
                this.actorList = m;
            }
        }
        return m;
    }
    public Enum<E>[] enumValues(){
        if (null == this.enumClass)
            return new Enum[0];
        else {
            Enum<E>[] list = this.actionList;
            if (null == list){
                list = Component.Tools.ListEnumOf(this.enumValuesMethod());

                if (null == list)
                    return new Enum[0];
                else
                    this.actionList = list;
            }
            return list.clone();
        }
    }
    public final Class<Enum<E>> getEnumClass(){
        return this.enumClass;
    }
    public final Menu setEnumClass(Class<Enum<E>> clas){
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
    public final Menu setEnumClassName(String name){
        if (null != name){
            try {
                Class clas = Class.forName(name);
                if (clas.isEnum()){

                    this.enumClass = (Class<Enum<E>>)clas;
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

    public String propertyNameOfValue(Class vac){
        if (null == vac)
            return null;
        else {
            String name = super.propertyNameOfValue(vac);
            if (null != name)
                return name;
            else {

                if (Enum.class.isAssignableFrom(vac))
                    return "enum-class";
                else
                    return null;
            }
        }
    }
    public ObjectJson toJson(){

        ObjectJson thisModel = super.toJson();

        thisModel.setValue("enum-class", this.getEnumClassName());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setEnumClassName( (String)thisModel.getValue("enum-class"));

        return true;
    }
}
