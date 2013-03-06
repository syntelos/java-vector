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

import platform.event.NamedAction;

import json.Json;
import json.ObjectJson;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * <p> A dialog should be added to the {@link Display} using the
 * <code>show</code> method, and may be dropped from the scene graph
 * with <code>this.drop(this)</code>. </p>
 * 
 * @see vector.dialog.Menu
 */
public class Dialog<E extends Enum<E>>
    extends Container
    implements Event.NamedAction.Consumer<E>
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

                if (this.mouseIn && action.isValueClass(this.enumClass)){

                    this.drop(this);

                    /*
                     * Can't do 
                     *     this.drop(this)
                     * and then
                     *     this.outputScene()
                     * because this has just been orphaned.
                     * 
                     * Display will perform an outputScene for each
                     * Action event.
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
