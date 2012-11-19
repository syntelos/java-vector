/*
 * Gap Data
 * Copyright (C) 2009 John Pritchard
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package oso.data;


import gap.*;
import gap.data.*;
import gap.hapax.TemplateDataDictionary;
import gap.hapax.TemplateName;
import gap.util.*;

import json.Json;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.blobstore.BlobKey;

import java.util.Date;

import javax.annotation.Generated;

/**
 * Generated bean data binding.
 *
 * @see Person
 */
@Generated(value={"gap.service.OD","BeanData.java"},date="2012-11-19T05:53:59.544Z")
public abstract class PersonData
    extends gap.data.BigTable
    implements DataInheritance<Person>
{

    private final static long serialVersionUID = 1;

    public final static Kind KIND = Kind.Create("Person","oso.data","Person","/people");

    public final static String ClassName = "Person";

    public final static String DefaultSortBy = "logonId";


    public final static gap.service.od.ClassDescriptor ClassDescriptorFor(){
        return ClassDescriptorFor(Person.class);
    }
    /**
     * @see gap.data.Kind#pathto()
     */
    public final static String PathTo(){
        return KIND.pathto();
    }
    public final static String PathTo(String subpath){
        return KIND.pathto(subpath);
    }

    /**
     * Long instance key from parent key
     */
    public static Key KeyLong(Json json){
        final String logonId = json.getValue("logonId",String.class);

        return KeyLongIdFor( logonId);
    }
    /**
     * Long instance key without parent key
     */
    public final static Key KeyLongIdFor(String logonId){
        String id = Person.IdFor( logonId);
        return KeyLongFor(id);
    }
    /**
     * Used by gap.data.Kind
     *
     * Calls {@link #KeyLongIdFor}
     */
    public final static Key KeyIdFor(Object... args){
        return Person.KeyLongIdFor((String)args[0]);
    }
    /**
     * Used by setId
     *
     * Calls {@link #KeyLongFor}
     */
    public final static Key KeyFor(Object... args){
        return Person.KeyLongFor( (String)args[0]);
    }
    /**
     * Identifier for unique fields
     */
    public final static String IdFor(String logonId){
        if (null != logonId){
            String logonIdString = logonId;
            return gap.data.Hash.For(logonIdString);
        }
        else
            throw new IllegalArgumentException();
    }

    /**
     * Instance lookup or create
     */
    public static Person ForLong(Json json){
        final String logonId = json.getValue("logonId",String.class);

        return ForLongLogonId( logonId);
    }
    /**
     * Instance lookup
     */
    public final static Person ForLongLogonId(String logonId){
        if (null != logonId){
            Key key = Person.KeyLongIdFor( logonId);
            Person instance = (Person)gap.data.Store.GetClass(key);
            if (null != instance)
                return instance;
            else {
                Query q = Person.CreateQueryFor(key);
                return (Person)gap.data.Store.Query1Class(q);
            }
        }
        else
            throw new IllegalArgumentException();
    }

    /**
     * Instance lookup or create
     */
    public static Person GetCreateLong(String logonId){
        return GetCreateLongLogonId( logonId);
    }
    /**
     * Instance lookup or create
     */
    public static Person GetCreateLong(Json json){
        final String logonId = json.getValue("logonId",String.class);

        return GetCreateLong( logonId);
    }
    /**
     * Instance lookup or create
     */
    public final static Person GetCreateLongLogonId(String logonId){
        Person person = Person.ForLongLogonId( logonId);
        if (null == person){
            person = new Person( logonId);
            person = (Person)gap.data.Store.PutClass(person);
        }
        return person;
    }
    /**
     * Instance lookup or create from (presumed correct and coherent) instance key and data
     *
     * Used by long and short lists
     *
     * @param key Key derived from data
     *
     * @param data Data instance of this class
     *
     * @return Possibly dirty (in need of save)
     */
    public final static Person GetCreate(Key key, Json json){
        Person instance = gap.data.Store.GetClass(key);
        if (null == instance){
            final String logonId = json.getValue("logonId",String.class);
            instance = new Person( logonId);
        }
        return instance;
    }

    public final static Key KeyLongFor(String id){
        return KeyFactory.createKey(KIND.getName(),id);
    }


    public final static Person ForLongId(String id){
        if (null != id){
            Key key = Person.KeyLongFor(id);
            Person instance = (Person)gap.data.Store.GetClass(key);
            if (null != instance)
                return instance;
            else {
                Query q = Person.CreateQueryFor(key);
                return (Person)gap.data.Store.Query1Class(q);
            }
        }
        else
            throw new IllegalArgumentException();
    }

    /**
     * Used by gap.data.Kind
     */
    public final static Person Get(Key key){
        if (null != key){
            Person instance = (Person)gap.data.Store.GetClass(key);
            if (null != instance)
                return instance;
            else {
                Query q = Person.CreateQueryFor(key);
                return (Person)gap.data.Store.Query1Class(q);
            }
        }
        else
            throw new IllegalArgumentException();
    }
    /**
     * @param entity Use entity for its key (only)
     */
    public final static Person Get(Entity entity){
        if (null != entity)
            return Get(entity.getKey());
        else
            throw new IllegalArgumentException();
    }
    public final static Key GetKey(Key key){
        if (null != key){
            Query q = Person.CreateQueryFor(key);
            return gap.data.Store.Query1Key(q);
        }
        else
            throw new IllegalArgumentException();
    }
    public final static Person FromObject(Object value){
        if (null == value)
            return null;
        else if (value instanceof Person)
            return (Person)value;
        else if (value instanceof Key)
            return Get( (Key)value);
        else if (value instanceof String){
            /*
             * TODO: ilarg: not key.enc; Key For ID.
             */
            Key key = gap.Strings.KeyFromString( (String)value);
            return Get(key);
        }
        else
            throw new IllegalArgumentException(value.getClass().getName());
    }


    /**
     * Anonymous random key cannot be mapped to network identifier
     * @see Person#IdFor
     *
     * Test for uniqueness and iterate under collisions.
     */
    public final static Key NewRandomKeyLong(){
        /*
         * Source matter for data local uniqueness
         */
        long matter = (gap.data.Hash.Djb64(ClassName) ^ (serialVersionUID<<3) ^ serialVersionUID);
        /*
         * Random matter for network global uniqueness
         */
        java.util.Random random = new java.util.Random();
        do {
            matter ^= random.nextLong();
            String idString = gap.data.Hash.Hex(matter);
            Key key = KeyFactory.createKey(KIND.getName(),idString);
            if (null == GetKey(key))
                return key;
        }
        while (true);
    }
    /**
     * Drop the instance from memcache and datastore.
     */
    public final static void Delete(Person instance){
        if (null != instance){

            Delete(instance.getKey());
        }
    }
    /**
     * Drop the instance from memcache and datastore.
     */
    public final static void Delete(Key instanceKey){
        if (null != instanceKey){

            gap.data.Store.Delete(instanceKey);
        }
    }
    /**
     * @param entity Use entity for its key (only)
     */
    public final static void Delete(Entity entity){
        if (null != entity)
            Delete(entity.getKey());
    }
    /**
     * Drop the instance from memcache, exclusively.
     */
    public final static void Clean(Person instance){
        if (null != instance){

            gap.data.Store.Clean(instance.getKey());
        }
    }
    /**
     * Store the instance.
     */
    public final static void Save(Person instance){
        if (null != instance){

            gap.data.Store.PutClass(instance);
        }
    }
    /**
     * Write the instance to store.
     */
    public final static void Store(Person instance){
        if (null != instance){

            gap.data.Store.PutClass(instance);
        }
    }
    /**
     * Default sort
     */
    public final static Query CreateQueryFor(){
        return new Query(KIND.getName()).addSort(DefaultSortBy);
    }
    /**
     * Default sort
     */
    public final static Query CreateQueryFor(Key key){
        return new Query(KIND.getName(),key).addSort(DefaultSortBy);
    }
    /**
     * Filter ops
     */
    public final static Query CreateQueryFor(Filter filter){
        Query query = new Query(KIND.getName());
        return filter.update(query);
    }
    
    public final static Person Query1(Query query){
        if (null != query)
            return (Person)gap.data.Store.Query1Class(query);
        else
            throw new IllegalArgumentException();
    }
    public final static BigTableIterator<Person> ListPage(Page page){

        return Person.QueryN(Person.CreateQueryFor(),page);
    }
    public final static BigTableIterator<Person> QueryN(Query query, Page page){
        if (null != query && null != page)
            return gap.data.Store.QueryNClass(query,page);
        else
            throw new IllegalArgumentException();
    }
    public final static Key QueryKey1(Query query){
        if (null != query)
            return gap.data.Store.Query1Key(query);
        else
            throw new IllegalArgumentException();
    }
    public final static List.Primitive<Key> QueryNKey(Query query, Page page){
        if (null != query)
            return gap.data.Store.QueryNKey(query,page);
        else
            throw new IllegalArgumentException();
    }
    public final static List.Primitive<Key> QueryNKey(Query query){
        if (null != query)
            return gap.data.Store.QueryNKey(query);
        else
            throw new IllegalArgumentException();
    }
    /**
     * @return Entities having only keys, unbuffered
     */
    public final static Iterable<Entity> QueryNKeyUnbuffered(Query query){
        if (null != query)
            return gap.data.Store.QueryNKeyUnbuffered(query);
        else
            throw new IllegalArgumentException();
    }

    /**
     * Persistent fields' binding for {@link Person}
     */
    public static enum Field
        implements gap.data.Field<Person.Field>
    {
        InheritFromKey("inheritFromKey",Type.Primitive),
        Key("key",Type.Primitive),
        Id("id",Type.Primitive),
        LogonId("logonId",Type.Primitive),
        Resource("resource",Type.Primitive),
        Available("available",Type.Primitive);

        private final static lxl.Map<String,Field> FieldName = new lxl.Map<String,Field>();
        public static final String[] AllNames;
        static {
            Field[] allFields = Field.values();
            int count = allFields.length;
            String[] names = new String[count];
            for (int cc = 0; cc < count; cc++) {
                Field field = allFields[cc];
                String fieldName = field.getFieldName();
                names[cc] = fieldName;
                FieldName.put(fieldName,field);
            }
            AllNames = names;
        }
        public static Field getField(String name) {
            return FieldName.get(name);
        }
        public static Field For(String name) {
            Field field = FieldName.get(name);
            if (null == field)
                try {
                    return Field.valueOf(name);
                }
                catch (IllegalArgumentException notFound){
                    return null;
                }
            else
                return field;
        }
        /**
         * Field statistics are maintained for persistent fields exclusively
         */
        public final static class Statistics
            extends gap.data.Field.Statistics<Person.Field>
        {
            public Statistics(){
                super(Person.Field.class);
            }
        }
        /**
         * Dynamic binding operator for field data type
         *
         * Persistent BigTable fields are represented by the string ID.
         */
        public static Object Get(Field field, Person instance, boolean mayInherit){
            switch(field){
            case InheritFromKey:
                return instance.getInheritFromKey();
            case Key:
                return instance.getKey();
            case Id:
                return instance.getId();
            case LogonId:
                return instance.getLogonId(mayInherit);
            case Resource:
                return instance.getResource(mayInherit);
            case Available:
                return instance.getAvailable(mayInherit);
            default:
                throw new IllegalArgumentException(field.toString()+" in Person");
            }
        }
        /**
         * Dynamic binding operator for field data type
         *
         * Persistent BigTable fields are represented by the string ID.
         */
        public static boolean Set(Field field, Person instance, Object value){
            switch(field){
            case InheritFromKey:
                return instance.setInheritFromKey(gap.Objects.KeyFromObject(value));
            case Key:
                return instance.setKey(gap.Objects.KeyFromObject(value));
            case Id:
                return instance.setId(gap.Objects.StringFromObject(value));
            case LogonId:
                return instance.setLogonId(gap.Objects.StringFromObject(value));
            case Resource:
                return instance.setResource(gap.Objects.StringFromObject(value));
            case Available:
                return instance.setAvailable(gap.Objects.BooleanFromObject(value));
            default:
                throw new IllegalArgumentException(field.toString()+" in Person");
            }
        }
        /**
         * Dynamic binding operator for field storage type
         *
         * Persistent BigTable fields are represented by the string ID.
         */
        public static java.io.Serializable Storage(Field field, Person instance){
            switch(field){
            case InheritFromKey:
                return instance.getInheritFromKey();
            case Key:
                return instance.getKey();
            case Id:
                return instance.getId();
            case LogonId:
                return instance.getLogonId(MayNotInherit);
            case Resource:
                return instance.getResource(MayNotInherit);
            case Available:
                return instance.getAvailable(MayNotInherit);
            default:
                throw new IllegalArgumentException(field.toString()+" in Person");
            }
        }
        /**
         * Dynamic binding operator for field storage type
         *
         * Persistent BigTable fields are represented by the string ID.
         */
        public static void Storage(Field field, Person instance, java.io.Serializable value){
            switch(field){
            case InheritFromKey:
                instance.setInheritFromKey( (Key)value);
                return;
            case Key:
                instance.setKey( (Key)value);
                return;
            case Id:
                instance.setId( (String)value);
                return;
            case LogonId:
                instance.setLogonId( (String)value);
                return;
            case Resource:
                instance.setResource( (String)value);
                return;
            case Available:
                instance.setAvailable( (Boolean)value);
                return;
            default:
                throw new IllegalArgumentException(field.toString()+" in Person");
            }
        }


        public final static class List
            extends gap.util.ArrayList<Person.Field>
        {
            public List(){
                super();
            }
            public List(Field[] fields){
                super(fields);
            }
            public List(Iterable<Field> fields){
                super();
                for (Field field : fields)
                    this.add(field);
            }
        }


        private final String fieldName;

        private final Type fieldType;

        private final boolean fieldTypePrimitive, fieldTypeBigTable, fieldTypeCollection;

        private final boolean fieldNameKeyOrId;


        Field(String fieldName, Type fieldType){
            if (null != fieldName && null != fieldType){
                this.fieldName = fieldName;
                this.fieldType = fieldType;
                this.fieldNameKeyOrId = BigTable.IsKeyOrId(fieldName);
                /*
                 * Using a switch here causes a null pointer
                 * initializing the switch map.
                 */
                if (Type.Primitive == fieldType){
                    this.fieldTypePrimitive = true;
                    this.fieldTypeBigTable = false;
                    this.fieldTypeCollection = false;
                }
                else if (Type.BigTable == fieldType){
                    this.fieldTypePrimitive = false;
                    this.fieldTypeBigTable = true;
                    this.fieldTypeCollection = false;
                }
                else if (Type.Collection == fieldType){
                    this.fieldTypePrimitive = false;
                    this.fieldTypeBigTable = false;
                    this.fieldTypeCollection = true;
                }
                else if (Type.PrimitiveCollection == fieldType){
                    this.fieldTypePrimitive = true;
                    this.fieldTypeBigTable = false;
                    this.fieldTypeCollection = true;
                }
                else
                    throw new IllegalStateException("Unimplemented field type "+fieldType);
            }
            else
                throw new IllegalStateException();
        }


        public String getFieldName(){
            return this.fieldName;
        }
        public Type getFieldType(){
            return this.fieldType;
        }
        public boolean isFieldTypePrimitive(){
            return this.fieldTypePrimitive;
        }
        public boolean isNotFieldTypePrimitive(){
            return (!this.fieldTypePrimitive);
        }
        public boolean isFieldTypeBigTable(){
            return this.fieldTypeBigTable;
        }
        public boolean isNotFieldTypeBigTable(){
            return (!this.fieldTypeBigTable);
        }
        public boolean isFieldTypeCollection(){
            return this.fieldTypeCollection;
        }
        public boolean isNotFieldTypeCollection(){
            return (!this.fieldTypeCollection);
        }
        public boolean isFieldNameKeyOrId(){
            return this.fieldNameKeyOrId;
        }
        public boolean isNotFieldNameKeyOrId(){
            return (!this.fieldNameKeyOrId);
        }
        public String toString(){
            return this.fieldName;
        }
    }

    private transient Person.Field.Statistics fieldStatistics = new Person.Field.Statistics();

    private transient Person inheritFrom;


    private String logonId;
    private String resource;
    private Boolean available;




    protected PersonData() {
        super();
    }
    protected PersonData(String logonId) {
        super();
        this.setLogonId(logonId);
        {
            final String id = Person.IdFor(logonId);
            final Key key = Person.KeyLongFor(id);
            this.setKey(key);
        }
    }


    private Person.Field.Statistics fieldStatistics(){
        Person.Field.Statistics fieldStatistics = this.fieldStatistics;
        if (null == fieldStatistics){
            fieldStatistics = new Person.Field.Statistics();
            this.fieldStatistics = fieldStatistics;
        }
        return fieldStatistics;
    }
    public void destroy(){
        this.inheritFrom = null;
        this.logonId = null;
        this.resource = null;
        this.available = null;
    }
    public final String getId(){

        String id = Person.IdFor(KIND.name, this.key);
        if (null != id)
            return id;
        else
            return Person.IdFor(this.logonId);
    }
    public final boolean setId(String id){
        if (null == id){
            if (null != this.key){
                this.key = null;
                return true;
            }
            else
                return false;
        }
        else if (null == this.key){
            this.key = Person.KeyLongFor(id);
            return true;
        }
        else
            return false;
    }
    public final boolean hasInheritFrom(){
        return (null != this.inheritFrom || null != this.inheritFromKey);
    }
    public final boolean hasNotInheritFrom(){
        return (null == this.inheritFrom && null == this.inheritFromKey);
    }
    public final Person getInheritFrom(){
        Person inheritFrom = this.inheritFrom;
        if (null == inheritFrom){
            Key inheritFromKey = this.inheritFromKey;
            if (null != inheritFromKey){
                inheritFrom = Person.Get(inheritFromKey);
                this.inheritFrom = inheritFrom;
            }
        }
        return inheritFrom;
    }
    public final boolean setInheritFrom(Person ancestor){
        if (IsNotEqual(this.inheritFrom,ancestor)){

            this.inheritFrom = ancestor;
            if (null != ancestor)
                this.inheritFromKey = ancestor.getKey();
            return true;
        }
        else
            return false;
    }
    public final boolean inheritFrom(Person ancestor){
        if (IsNotEqual(this.inheritFrom,ancestor)){

            this.inheritFrom = ancestor;
            if (null != ancestor)
                this.inheritFromKey = ancestor.getKey();
            return true;
        }
        else
            return false;
    }
    public final boolean hasLogonId(boolean mayInherit){
        return (null != this.getLogonId(mayInherit));
    }
    public final boolean hasNotLogonId(boolean mayInherit){
        return (null == this.getLogonId(mayInherit));
    }
    public final boolean dropLogonId(){
        if (null != this.logonId){
            this.fieldStatistics().markDirty(Person.Field.LogonId);
            this.logonId = null;
            return true;
        }
        else
            return false;
    }
    public final String getLogonId(){
        return this.logonId;
    }
    public final String getLogonId(boolean mayInherit){
        return this.getLogonId();
    }
    public final boolean setLogonId(String logonId){
        if (IsNotEqual(this.logonId,logonId)){
            this.fieldStatistics().markDirty(Person.Field.LogonId);
            this.logonId = logonId;
            return true;
        }
        else
            return false;
    }
    public final boolean hasResource(boolean mayInherit){
        return (null != this.getResource(mayInherit));
    }
    public final boolean hasNotResource(boolean mayInherit){
        return (null == this.getResource(mayInherit));
    }
    public final boolean dropResource(){
        if (null != this.resource){
            this.fieldStatistics().markDirty(Person.Field.Resource);
            this.resource = null;
            return true;
        }
        else
            return false;
    }
    public final String getResource(){
        return this.getResource(Notation.MayInherit);
    }
    public final String getResource(boolean mayInherit){
        if (mayInherit){
            String resource = this.resource;
            if (null == resource && this.hasInheritFrom()){
                Person inheritFrom = this.getInheritFrom();
                return inheritFrom.getResource(Notation.MayInherit);
            }
            return resource;
        }
        else
            return this.resource;
    }
    public final boolean setResource(String resource, boolean withInheritance){
        if (IsNotEqual(this.resource,this.getResource(withInheritance))){
            this.fieldStatistics().markDirty(Person.Field.Resource);
            this.resource = resource;
            return true;
        }
        else
            return false;
    }
    public final boolean setResource(String resource){
        if (IsNotEqual(this.resource,resource)){
            this.fieldStatistics().markDirty(Person.Field.Resource);
            this.resource = resource;
            return true;
        }
        else
            return false;
    }
    public final boolean hasAvailable(boolean mayInherit){
        return (null != this.getAvailable(mayInherit));
    }
    public final boolean hasNotAvailable(boolean mayInherit){
        return (null == this.getAvailable(mayInherit));
    }
    public final boolean dropAvailable(){
        if (null != this.available){
            this.fieldStatistics().markDirty(Person.Field.Available);
            this.available = null;
            return true;
        }
        else
            return false;
    }
    public final Boolean getAvailable(){
        return this.getAvailable(Notation.MayInherit);
    }
    public final Boolean getAvailable(boolean mayInherit){
        if (mayInherit){
            Boolean available = this.available;
            if (null == available && this.hasInheritFrom()){
                Person inheritFrom = this.getInheritFrom();
                return inheritFrom.getAvailable(Notation.MayInherit);
            }
            return available;
        }
        else
            return this.available;
    }
    public final boolean setAvailable(Boolean available, boolean withInheritance){
        if (IsNotEqual(this.available,this.getAvailable(withInheritance))){
            this.fieldStatistics().markDirty(Person.Field.Available);
            this.available = available;
            return true;
        }
        else
            return false;
    }
    public final boolean setAvailable(Boolean available){
        if (IsNotEqual(this.available,available)){
            this.fieldStatistics().markDirty(Person.Field.Available);
            this.available = available;
            return true;
        }
        else
            return false;
    }
    public Json toJsonLogonId(){
        String logonId = this.getLogonId();
        return Json.Wrap( logonId);
    }
    public boolean fromJsonLogonId(Json json){
        if (null == json)
            return false;
        else 
            return this.setLogonId((String)json.getValue(String.class));
    }
    public Json toJsonResource(){
        String resource = this.getResource();
        return Json.Wrap( resource);
    }
    public boolean fromJsonResource(Json json){
        if (null == json)
            return false;
        else 
            return this.setResource((String)json.getValue(String.class));
    }
    public Json toJsonAvailable(){
        Boolean available = this.getAvailable();
        return Json.Wrap( available);
    }
    public boolean fromJsonAvailable(Json json){
        if (null == json)
            return false;
        else 
            return this.setAvailable((Boolean)json.getValue(Boolean.class));
    }
    /*
     * Data binding supports
     */
    public final Kind getClassKind(){
        return KIND;
    }
    public final String getClassName(){
        return ClassName;
    }
    public final gap.data.List<gap.data.Field> getClassFields(){
        gap.data.List re = new Person.Field.List(Field.values());
        /*
         * Compiler has a type astigmatism (parameterized interface gap.data.Field)
         */
        return (gap.data.List<gap.data.Field>)re;
    }
    public final gap.data.Field getClassFieldByName(String name){
        return Field.getField(name);
    }
    public Json toJson(){
        Json json = new json.ObjectJson();
        Json logonId = this.toJsonLogonId();
        if (null != logonId)
            json.set("logonId",logonId);
        Json resource = this.toJsonResource();
        if (null != resource)
            json.set("resource",resource);
        Json available = this.toJsonAvailable();
        if (null != available)
            json.set("available",available);
        return json;
    }
    public boolean fromJson(Json json){
        boolean modified = false;
        modified = (this.fromJsonResource(json.at("resource")) || modified);
        modified = (this.fromJsonAvailable(json.at("available")) || modified);
        return modified;
    }
    public boolean updateFrom(Request req) throws ValidationError {
        boolean change = false;
        String resourceRequest = req.getParameter("resource");
        if (null != resourceRequest && 0 < resourceRequest.length()){
            try {
                String resource = resourceRequest;
                if (this.setResource(resource)){
                    change = true;
                }
            }
            catch (RuntimeException exc){
                throw new ValidationError(ClassName,"resource",resourceRequest,exc);
            }
        }
        String availableRequest = req.getParameter("available");
        if (null != availableRequest && 0 < availableRequest.length()){
            try {
                Boolean available = gap.Strings.BooleanFromString(availableRequest);
                if (this.setAvailable(available)){
                    change = true;
                }
            }
            catch (RuntimeException exc){
                throw new ValidationError(ClassName,"available",availableRequest,exc);
            }
        }
        return change;
    }
    public final boolean updateFrom(BigTable proto){
        return this.updateFrom( (Person)proto);
    }
    public final boolean updateFrom(Person proto){
        boolean mayInherit = (!this.hasInheritFromKey());
        boolean change = false;
        String resource = proto.getResource(mayInherit);
        if (null != resource && this.setResource(resource)){
            change = true;
        }
        Boolean available = proto.getAvailable(mayInherit);
        if (null != available && this.setAvailable(available)){
            change = true;
        }
        return change;
    }
    public java.io.Serializable valueStorage(gap.data.Field field){

        return Field.Storage( (Field)field, (Person)this);
    }
    public void defineStorage(gap.data.Field field, java.io.Serializable value){

        Field.Storage( (Field)field, (Person)this, value);
    }
    public final Person markClean(){

        this.fieldStatistics().markClean();
        return (Person)this;
    }
    public final Person markDirty(){

        this.fieldStatistics().markDirty();
        return (Person)this;
    }
    public final Person markDirty(gap.data.Field field){

        this.fieldStatistics().markDirty(field);
        return (Person)this;
    }
    public final Person markDirty(java.io.Serializable instance){
        if (instance == this.logonId){
            gap.data.Field field = Person.Field.LogonId;
            return this.markDirty(field);
        }
        else if (instance == this.resource){
            gap.data.Field field = Person.Field.Resource;
            return this.markDirty(field);
        }
        else if (instance == this.available){
            gap.data.Field field = Person.Field.Available;
            return this.markDirty(field);
        }
        else if (null != instance)
            throw new IllegalArgumentException(instance.getClass().getName());
        else
            throw new IllegalArgumentException();
    }
    public final Iterable<gap.data.Field> listClean(){

        return this.fieldStatistics().listClean();
    }
    public final Iterable<gap.data.Field> listDirty(){

        return this.fieldStatistics().listDirty();
    }
    public final boolean isClean(){

        return this.fieldStatistics().isClean();
    }
    public final boolean isDirty(){

        return this.fieldStatistics().isDirty();
    }
    public final gap.service.od.ClassDescriptor getClassDescriptorFor(){
        return Person.ClassDescriptorFor();
    }
    /*
     * Template Data Dictionary
     */
    public boolean hasVariable(TemplateName name){
        Field field = Person.Field.For(name.getTerm());
        if (null != field){
            switch (field){
            case Id:
                if (name.is(0)){
                    String id = this.getId();
                    return (null != id);
                }
                else
                    return false;
            case LogonId:
                if (name.has(1))
                    throw new IllegalStateException(field.name());
                else {
                    /*
                     * Synthesize section for Field (EXISTS)
                     */
                    return this.hasLogonId(true);
                }
            case Resource:
                if (name.has(1))
                    throw new IllegalStateException(field.name());
                else {
                    /*
                     * Synthesize section for Field (EXISTS)
                     */
                    return this.hasResource(true);
                }
            case Available:
                if (name.has(1))
                    throw new IllegalStateException(field.name());
                else {
                    /*
                     * Synthesize section for Boolean (EXISTS && TRUE)
                     */
                    return (this.hasAvailable(true) && this.getAvailable(true));
                }
            default:
                break;
            }
        }
        return super.hasVariable(name);
    }
    public String getVariable(TemplateName name){
        Field field = Person.Field.For(name.getTerm());
        if (null != field){
            switch (field){
            case Id:
                if (name.is(0))
                    return this.getId();
                else
                    return null;
            case LogonId:
                if (name.has(1))
                    throw new IllegalStateException(field.name());
                else
                    return this.getLogonId(true);
            case Resource:
                if (name.has(1))
                    throw new IllegalStateException(field.name());
                else
                    return this.getResource(true);
            case Available:
                if (name.has(1))
                    throw new IllegalStateException(field.name());
                else
                    return gap.Strings.BooleanToString(this.getAvailable(true));
            default:
                break;
            }
        }
        return super.getVariable(name);
    }
    public void setVariable(TemplateName name, String value){
        Field field = Person.Field.For(name.getTerm());
        if (null != field){
            if (name.has(1)){
                switch (field){
                case Id:
                    throw new UnsupportedOperationException(field.name());
                case LogonId:
                    throw new IllegalStateException(field.name());
                case Resource:
                    throw new IllegalStateException(field.name());
                case Available:
                    throw new IllegalStateException(field.name());
                default:
                    throw new IllegalStateException(field.name());
                }
            }
            else
                Field.Set(field,((Person)this),value);
        }
        else {
            super.setVariable(name,value);
        }
    }
    public List.Short<TemplateDataDictionary> getSection(TemplateName name){
        Field field = Person.Field.For(name.getTerm());
        if (null != field){
            switch (field){
            case LogonId:
                return null;
            case Resource:
                return null;
            case Available:
                return null;
            default:
                throw new IllegalStateException(field.name());
            }
        }
        else {
            return super.getSection(name);
        }
    }
    public Person clone(){
        return (Person)super.clone();
    }
    public String pathto(){
        return PathTo(this.getId());
    }
    public String pathto(String subpath){
        StringBuilder string = new StringBuilder();
        string.append(this.getId());
        if (null != subpath){
            if (0 == subpath.length() || '/' != subpath.charAt(0))
                string.append('/');
            string.append(subpath);
        }
        return PathTo(string.toString());
    }
}
