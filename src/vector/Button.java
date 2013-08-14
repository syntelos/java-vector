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

import platform.event.NamedAction;

import json.Json;
import json.ObjectJson;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Button based on an enumerated set of buttons for input programming.
 * 
 * For example, see {@link vector.dialog.Menu}.
 */
public class Button<E extends Enum<E>>
    extends Text
    implements Event.NamedAction.Producer<E>
{

    protected Class<Enum<E>> enumClass;

    protected Enum<E> enumValue;

    private Method enumValueOf;


    public Button(){
        super();
    }


    @Override
    public void init(){
        super.init();

        this.fixed = true;
    }
    @Override
    public void destroy(){
        super.destroy();

        this.enumClass = null;
        this.enumValueOf = null;
        this.enumValue = null;
    }
    /**
     * Instances of this class call this method from this input event.
     * 
     * This method produces an action event when the enum value is not
     * null.  In this case, the production of the action event will
     * occur before subclass programming executes.  
     * 
     * Subclasses that need to do something before the action event is
     * produced must overrride this method to do nothing, and then
     * subsequently produce the action event.
     */
    protected Button buttonInputAction(){
        if (null != this.enumValue){
            final Component root = this.getRootContainer();
            if (null != root){
                NamedAction action = new NamedAction(this.enumValue);

                root.input(action);
            }
            else
                throw new IllegalStateException("Orphan");
        }
        return this;
    }
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {

            switch(e.getType()){
            case MouseUp:
                if (Event.Debug.IsClick){
                    Event.Debug.trace("mouse up",this,e);
                }

                if (this.mouseIn){

                    this.buttonInputAction();

                    return true;
                }
                else
                    return false;

            default:
                return false;
            }
        }
    }
    public final Class<Enum<E>> getEnumClass(){
        return this.enumClass;
    }
    public final Button setEnumClass(Class<Enum<E>> clas){
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
    public final Button setEnumClassName(String name){
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
    public final Button setEnumValue(Enum<E> value){

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
    public final Button setEnumValueName(String name){
        final Enum<E> value = Component.Tools.EnumValueOf(this.enumValueOf,name);
        return this.setEnumValue(value);
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
                    return "enum-value";
                else
                    return null;
            }
        }
    }
    public ObjectJson toJson(){

        ObjectJson thisModel = super.toJson();

        thisModel.setValue("enum-class", this.getEnumClassName());

        thisModel.setValue("enum-value", this.getEnumValueName());

        return thisModel;
    }
    public boolean fromJson(Json thisModel){

        super.fromJson(thisModel);

        this.setEnumClassName( (String)thisModel.getValue("enum-class"));

        this.setEnumValueName( (String)thisModel.getValue("enum-value"));

        return true;
    }
}
