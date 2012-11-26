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
package vector.dialog;

import vector.ActionLabel;
import vector.Border;
import vector.Button;
import vector.Component;
import vector.Event;
import vector.Stroke;

import json.Json;
import json.ObjectJson;

/**
 * Enumerated list of menu buttons that produce menu events.  These events are
 * interpreted by the user.
 * 
 * These enums may return their labels via the {@link ActionLabel}
 * interface.
 * 
 * @see Style
 */
public class Menu<E extends Enum<E>>
    extends vector.TableSmall
    implements Event.NamedAction.Consumer<E>
{


    protected Class<Enum<E>> enumClass;

    protected java.lang.reflect.Method actorList;

    protected Enum<E>[] actionList;


    public Menu(){
        super();
    }
    public Menu(Class<Enum<E>> enumClass){
        super();
        this.enumClass = enumClass;
    }



    /**
     * Destroy and rebuild subtree within Container 'add', or JSON
     * "init".
     */
    public void init(){
        super.init();

        this.setCols(1);

        this.setCellSpacing(4);

        this.build();
    }
    public Menu build(){
        final Enum<E>[] values = this.enumValues();

        if (null != values){

            this.style(this);

            final boolean actionLabel = (ActionLabel.class.isAssignableFrom(this.enumClass));

            final int count = values.length;

            final String[] columns = new String[count];

            int colw = 0;

            for (int cc = 0; cc < count; cc++){

                String label;

                if (actionLabel)
                    label = ((ActionLabel)values[cc]).getActionLabel();
                else
                    label = values[cc].toString();

                columns[cc] = label;

                colw = Math.max(colw,label.length());
            }

            for (int cc = 0; cc < count; cc++){

                Button button = new Button();
                {
                    this.add(button);

                    this.style(button,values[cc],columns[cc],colw);
                }
            }
        }
        return this;
    }
    public Menu style(Component.Bordered c){

        if (null != c){
            Border border = new Border();
            {
                c.setBorder(border);

                border.setBackground(Style.BGD(0.5f));
                border.setColor(Style.FG());
                border.setColorOver(Style.NG());
                border.setStyle(Border.Style.ROUND);
                border.setArc(16.0);
                border.setStroke(new Stroke(2f));
                border.setStrokeOver(new Stroke(4f));
            }
        }
        return this;
    }
    /**
     * Apply text, enum, and visual defaults to buttons
     */
    public Menu style(Button button, Enum<E> value, String label, int columns){

        this.style(button);

        button.setCols(columns);

        button.setEnumClass(this.enumClass);
        button.setEnumValue(value);

        button.setText(label);

        button.setColor(Style.FG());
        button.setFont(Style.FontLarge());

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
    /**
     * 
     */
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {

            switch(e.getType()){
            case Action:{
                Event.NamedAction action = (Event.NamedAction)e;

                if (this.mouseIn && action.isValueClass(this.enumClass)){

                    this.drop(this);

                    /*
                     * Can't do 
                     *     this.drop(this)
                     * and then
                     *     this.outputScene()
                     * because this has just been orphaned.
                     * 
                     * Another component must look at the event and
                     * ask for output.
                     */
                    return true;
                }
                else
                    return false;
            }
            default:
                return false;
            }
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
