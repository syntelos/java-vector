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

import vector.event.NamedAction;

import json.Json;
import json.ObjectJson;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Dialog is a {@link Container} that will drop itself from the scene
 * graph when it receives input in its own action enum class.
 * 
 * A dialog should be added and removed from the {@link Display}.
 */
public class Dialog<E extends Enum<E>>
    extends Container
{
    public enum AcceptCancel {
        Accept, Cancel;
    }

    protected Class<Enum<E>> enumClass;



    public Dialog(){
        super();
    }


    @Override
    public void destroy(){
        super.destroy();

        this.enumClass = null;
    }
    public boolean input(Event e){
        if (super.input(e))
            return true;
        else {

            switch(e.getType()){
            case Action:{
                Event.NamedAction action = (Event.NamedAction)e;

                if (this.mouseIn && null != this.enumClass && this.enumClass.equals(action.getValueClass())){

                    this.drop(this);

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
    public final Dialog setEnumClass(Class<Enum<E>> clas){
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
    public final Dialog setEnumClassName(String name){
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
