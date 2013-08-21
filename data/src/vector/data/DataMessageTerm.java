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

/**
 * 
 */
public class DataMessageTerm
    extends Object
{
    public final DataIdentifier name;

    public final Object value;

    public final char qualifier;

    public final String string;


    public DataMessageTerm(DataKind kind, Object name, Object value){
        super();
        if (null != kind && null != name){
            this.name = kind.identifier(name);
            this.value = value;

            if (null == value)
                this.qualifier = '?';
            else
                this.qualifier = '=';

            if (null == value)
                this.string = (name.toString()+this.qualifier+value);
            else
                this.string = (name.toString()+this.qualifier);
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    public int hashCode(){
        return this.string.hashCode();
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.string.equals(that.toString());
    }
    public String toString(){
        return this.string;
    }

    /**
     * 
     */
    public static class Iterator
        extends Object
        implements java.lang.Iterable<DataMessageTerm>,
                   java.util.Iterator<DataMessageTerm>
    {
        private final int length;
        private final DataMessageTerm[] list;
        private int index;

        public Iterator(DataMessageTerm[] list){
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
        public DataMessageTerm next(){
            if (this.index < this.length)
                return this.list[this.index++];
            else
                throw new java.util.NoSuchElementException(String.valueOf(this.index));
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public java.util.Iterator<DataMessageTerm> iterator(){
            return this.reset();
        }
    }


    public final static int IndexOf(DataMessageTerm[] list, DataMessageTerm field){
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
    public final static DataMessageTerm[] Copy(DataMessageTerm[] list){
        if (null == list)
            return null;
        else {
            final int len = list.length;
            DataMessageTerm[] copier = new DataMessageTerm[len];
            System.arraycopy(list,0,copier,0,len);
            return copier;
        }
    }
    public final static DataMessageTerm[] Add(DataMessageTerm[] list, DataMessageTerm item){
        if (null == item)
            return list;
        else if (null == list)
            return new DataMessageTerm[]{item};
        else {
            final int len = list.length;
            DataMessageTerm[] copier = new DataMessageTerm[len+1];
            System.arraycopy(list,0,copier,0,len);
            copier[len] = item;
            return copier;
        }
    }
    public final static DataMessageTerm[] Add(DataMessageTerm[] list, DataMessageTerm[] sublist){
        if (null == sublist)
            return list;
        else if (null == list)
            return sublist;
        else {
            final int listl = list.length;
            final int sublistl = sublist.length;
            DataMessageTerm[] copier = new DataMessageTerm[listl+sublistl];
            System.arraycopy(list,0,copier,0,listl);
            System.arraycopy(sublist,0,copier,listl,sublistl);
            return copier;
        }
    }
    public final static DataMessageTerm[] List(java.lang.Iterable<DataMessageTerm> it){
        return List(it.iterator());
    }
    public final static DataMessageTerm[] List(java.util.Iterator<DataMessageTerm> it){
        DataMessageTerm[] list = null;
        while (it.hasNext()){
            list = Add(list,it.next());
        }
        return list;
    }
}
