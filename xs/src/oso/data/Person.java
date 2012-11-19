
package oso.data;

import xs.XAddress;

import gap.*;
import gap.data.*;
import gap.util.*;

import json.Json;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.xmpp.*;

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
    private final static Filter FilterRecipients = (new Filter(Person.KIND)).add(new Filter.Term(Person.Field.Available,Filter.Op.eq,Boolean.TRUE));

    private final static Page PageAll = new Page(0,Integer.MAX_VALUE);
    private final static Page PageFirst = new Page();


    public final static Person Get(JID jid){
        return Person.GetCreate(new XAddress.Logon(jid));
    }
    public final static Person GetCreate(JID jid){
        return Person.GetCreate(new XAddress.Logon(jid));
    }
    public final static Person Get(XAddress addr){
        if (null != addr && null != addr.logon)
            return Person.ForLongLogonId(addr.logon);
        else
            return null;
    }
    public final static Person GetCreate(XAddress addr){
        if (null != addr && null != addr.logon)
            return Person.GetCreateLong(addr.logon);
        else if (null != addr)
            throw new IllegalArgumentException(addr.toString());
        else
            throw new IllegalArgumentException();
    }
    public final static JID[] ListRecipients(XAddress except){
        JID[] list = null;
        Query query = Person.CreateQueryFor(FilterRecipients);
        if (null != except && null != except.logon){
            final String exceptLogonId = except.logon;

            for (Person p: Person.QueryN(query,PageAll)){
                if (!p.getLogonId().equals(exceptLogonId)){
                    list = Add(list,p.getJID());
                }
            }
        }
        else {
            for (Person p: Person.QueryN(query,PageAll)){

                list = Add(list,p.getJID());
            }
        }
        return list;
    }
    public final static boolean ActiveRecipients(XAddress except){

        Query query = Person.CreateQueryFor(FilterRecipients);
        if (null != except && null != except.logon){

            final Key exceptKey = Person.KeyLongIdFor(except.logon);

            for (Key key: Person.QueryNKey(query,PageFirst)){

                if (!BigTable.Equals(key,exceptKey)){

                    return true;
                }
            }
        }
        else {
            for (Key key: Person.QueryNKey(query,PageFirst)){

                return true;
            }
        }
        return false;
    }
    public final static JID[] Add(JID[] list, JID item){
        if (null == item)
            return list;
        else if (null == list)
            return new JID[]{item};
        else {
            final int len = list.length;
            JID[] copier = new JID[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }



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


    public JID getJID(){

        if (this.hasResource(false))

            return new JID(this.getLogonId()+'/'+this.getResource());
        else
            return new JID(this.getLogonId());
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
