/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2013, The DigiVac Company
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
package vector.data;

import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import java.util.Enumeration;

/**
 * Data enum bindings by properties file.  Application default values
 * are discovered from the class path.
 */
public class DataDefaults<D extends DataField, S extends DataSubfield>
    extends java.util.Properties
{

    public final DataKind<D,S> kind;

    public final String resource;


    public DataDefaults(DataKind<D,S> kind){
        super();

        if (null != kind){

            this.kind = kind;

            final String name = kind.field.getName();

            this.resource = name+".properties";

            try {
                Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(this.resource);
                while (resources.hasMoreElements()){
                    URL resource = resources.nextElement();
                    InputStream in = resource.openStream();
                    if (null != in){
                        try {

                            this.load(in);
                        }
                        catch (IOException debug){

                            debug.printStackTrace();
                        }
                        finally {
                            try {
                                in.close();
                            }
                            catch (IOException ignore){
                            }
                        }
                    }
                }
            }
            catch (IOException debug){

                debug.printStackTrace();
            }
        }
        else
            throw new IllegalArgumentException();
    }


    public final boolean isFieldAssignableFrom(Class field){

        return this.kind.isFieldAssignableFrom(field);
    }
    public final boolean isSubfieldAssignableFrom(Class field){

        return this.kind.isSubfieldAssignableFrom(field);
    }
    public final Object put(Object key, Object value){

        try {
            DataIdentifier id = this.kind.identifier(key);

            Object normalized = id.toObject(value);

            return super.put(id,normalized);
        }
        catch (RuntimeException exc){
            throw new IllegalArgumentException(String.format("Error using key '%s' with '%s'",key,this.kind),exc);
        }
    }
    public final <V> V get(DataIdentifier id){

        return (V)super.get(id);
    }
    /*
     * string identity
     */
    public final String toString(){
        return this.resource;
    }
    public final int hashCode(){
        return this.resource.hashCode();
    }
    public final boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.resource.equals(that.toString());
    }


    /**
     * 
     */
    public static class Iterator
        extends Object
        implements java.lang.Iterable<DataDefaults>,
                   java.util.Iterator<DataDefaults>
    {
        private final int length;
        private final DataDefaults[] list;
        private int index;

        public Iterator(DataDefaults[] list){
            super();
            if (null == list){
                this.list = null;
                this.length = 0;
            }
            else {
                this.list = list;
                this.length = list.length;
            }
        }


        public Iterator reset(){
            this.index = 0;
            return this;
        }
        public boolean hasNext(){
            return (this.index < this.length);
        }
        public DataDefaults next(){
            if (this.index < this.length)
                return this.list[this.index++];
            else
                throw new java.util.NoSuchElementException(String.valueOf(this.index));
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public java.util.Iterator<DataDefaults> iterator(){
            return this.reset();
        }
    }



    public final static int IndexOf(DataDefaults[] list, DataDefaults field){
        if (null == field || null == list)
            return -1;
        else {
            final int count = list.length;
            for (int cc = 0; cc < count; cc++){
                if (field == list[cc])
                    return cc;
            }
            return -1;
        }
    }
    public final static DataDefaults[] Copy(DataDefaults[] list){
        if (null == list)
            return null;
        else {
            final int len = list.length;
            DataDefaults[] copier = new DataDefaults[len];
            System.arraycopy(list,0,copier,0,len);
            return copier;
        }
    }
    public final static DataDefaults[] Add(DataDefaults[] list, DataDefaults item){
        if (null == item)
            return list;
        else if (null == list)
            return new DataDefaults[]{item};
        else {
            final int len = list.length;
            DataDefaults[] copier = new DataDefaults[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }
    public final static DataDefaults[] Add(DataDefaults[] list, DataDefaults[] sublist){
        if (null == sublist)
            return list;
        else if (null == list)
            return sublist;
        else {
            final int listl = list.length;
            final int sublistl = sublist.length;
            DataDefaults[] copier = new DataDefaults[listl+sublistl];
            System.arraycopy(list,0,copier,0,listl);
            System.arraycopy(sublist,0,copier,listl,sublistl);
            return copier;
        }
    }
    public final static DataDefaults[] List(java.lang.Iterable<DataDefaults> it){
        return List(it.iterator());
    }
    public final static DataDefaults[] List(java.util.Iterator<DataDefaults> it){
        DataDefaults[] list = null;
        while (it.hasNext()){
            list = Add(list,it.next());
        }
        return list;
    }
}
