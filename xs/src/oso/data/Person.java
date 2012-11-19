
package oso.data;


import gap.*;
import gap.data.*;
import gap.util.*;

import json.Json;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.blobstore.*;

import java.util.Date;

/**
 * Generated once (user) bean.
 * This source file will not be overwritten unless deleted,
 * so it can be edited for extensions.
 *
 * @see PersonData
 */
public final class Person
    extends PersonData
{

    public Person() {
        super();
    }
    public Person(String logonId) {
        super( logonId);
    }
    public Person(Json json){
        super();
        String logonId = (String)json.getValue("logonId",String.class);
        
        if (null != logonId){
            Key key = Person.KeyLongIdFor( logonId);
            this.setLogonId(logonId);
            this.setKey(key);
            this.fillFrom(gap.data.Store.Get(key));
            this.markClean();
            /*
             */
            this.fromJson(json);
            if (this.isDirty())
                this.save();
        }
        else {
            throw new IllegalArgumentException("Unable to create new");
        }
    }

    public void onread(){
    }
    public void onwrite(){
    }
    public java.io.Serializable valueOf(gap.data.Field field, boolean mayInherit){
        return (java.io.Serializable)Field.Get((Field)field,this,mayInherit);
    }
    public void define(gap.data.Field field, java.io.Serializable value){
        Field.Set((Field)field,this,value);
    }
    public void drop(){
        Delete(this);
    }
    public void clean(){
        Clean(this);
    }
    public void save(){
        Save(this);
    }
    public void store(){
        Store(this);
    }
    /**
     * To disable json access via 'toJson', redefine this method to "return null".
     */
    @Override
    public Json toJsonLogonId(){
        return super.toJsonLogonId();
    }
    /**
     * To disable json access via 'fromJson', redefine this method to "return false".
     */
    @Override
    public boolean fromJsonLogonId(Json json){
        return super.fromJsonLogonId(json);
    }
}
