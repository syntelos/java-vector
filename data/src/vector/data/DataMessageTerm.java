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
package vector.data;

/**
 * 
 */
public class DataMessageTerm
    extends Object
{
    /**
     * Convenience for message type ERROR
     */
    public static class Error
        extends DataMessageTerm
    {
        public Error(CharSequence string){
            super(DataMessageType.Kind.Instance,DataMessageType.ERROR,string);
        }
        public Error(long time, CharSequence string){
            super(time,DataMessageType.Kind.Instance,DataMessageType.ERROR,string);
        }
    }
    /**
     * Convenience for message type TEXT
     */
    public static class Text
        extends DataMessageTerm
    {
        public Text(CharSequence string){
            super(DataMessageType.Kind.Instance,DataMessageType.TEXT,string);
        }
        public Text(long time, CharSequence string){
            super(time,DataMessageType.Kind.Instance,DataMessageType.TEXT,string);
        }
    }
    /**
     * Convenience for message type CODE
     */
    public static class Code
        extends DataMessageTerm
    {
        public Code(CharSequence string){
            super(DataMessageType.Kind.Instance,DataMessageType.CODE,string);
        }
        public Code(long time, CharSequence string){
            super(time,DataMessageType.Kind.Instance,DataMessageType.CODE,string);
        }
    }
    /**
     * Convenience for message type INFO
     */
    public static class Info
        extends DataMessageTerm
    {
        public Info(CharSequence string){
            super(DataMessageType.Kind.Instance,DataMessageType.INFO,string);
        }
        public Info(long time, CharSequence string){
            super(time,DataMessageType.Kind.Instance,DataMessageType.INFO,string);
        }
    }


    public final long time;

    public final DataIdentifier name;

    public final DataKind kind;

    public final Object value;
    /**
     * If null is value, then qualifier is '?' (0x3F), otherwise
     * qualifier is '=' (0x3D).
     */
    public final char qualifier;
    /**
     * If (null is not) value, then name, qualifier, value; otherwise
     * name qualifier.
     */
    public final String string;


    public DataMessageTerm(DataKind kind, Object name, Object value){
        this(-1L,kind,name,value);
    }
    public DataMessageTerm(long time, DataIdentifier name, Object value){
        this(time,name.createKind(),name.field,value);
    }
    public DataMessageTerm(long time, DataKind kind, Object name){
        this(time,kind,name,null);
    }
    public DataMessageTerm(long time, DataKind kind, Object name, Object value){
        super();

        if (-1L < time)
            this.time = time;
        else
            this.time = DataClock.currentTimeMillis();

        if (null != kind && null != name){

            this.kind = kind;

            this.name = kind.identifier(name);

            this.value = this.name.toObject(value);

            if (null == value)
                this.qualifier = '?';
            else
                this.qualifier = '=';

            if (null == this.value)
                this.string = (name.toString()+this.qualifier+this.value);
            else
                this.string = (name.toString()+this.qualifier);
        }
        else {
            throw new IllegalArgumentException();
        }
    }


    /**
     * @return Qualifier is '?': null value.
     */
    public final boolean isQuery(){

        return ('?' == this.qualifier);
    }
    /**
     * @return Qualifier is '=': has value.
     */
    public final boolean isAssignment(){

        return ('=' == this.qualifier);
    }
    /**
     * @see DataIdentifier#operator
     */
    public final boolean isOperator(){

        return this.name.operator;
    }
    /**
     * String identity
     */
    public int hashCode(){
        return this.string.hashCode();
    }
    /**
     * String identity
     */
    public boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.string.equals(that.toString());
    }
    /**
     * String identity
     */
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

        public Iterator(){
            this(null);
        }
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
        public Iterator(DataMessageTerm[] source, int start, int end){
            super();
            if (null == source){
                this.list = null;
                this.length = 0;
            }
            else if (-1 < start && start < end){
                final int len = source.length;
                final int count = (end-start);
                if (count >= len){
                    this.list = source;
                    this.length = source.length;
                }
                else if (0 == count){
                    this.list = null;
                    this.length = 0;
                }
                else {
                    DataMessageTerm[] copier = new DataMessageTerm[count];
                    System.arraycopy(source,start,copier,0,count);
                    this.list = copier;
                    this.length = count;
                }
            }
            else
                throw new IllegalArgumentException(String.format("Invalid start (%d) or end (%d)",start,end));
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
    public final static int IndexOf(DataMessageTerm[] list, DataIdentifier id){
        if (null == id || null == list)
            return -1;
        else {
            final int count = list.length;
            for (int cc = 0; cc < count; cc++){

                if (id.matches(list[cc].name)){

                    return cc;
                }
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

    public final static long Floor(DataMessageTerm[] list){
        if (null == list)
            return 0L;
        else {
            long last = Long.MAX_VALUE;
            for (DataMessageTerm t: list){
                if (0L < t.time)
                    last = Math.min(last,t.time);
            }
            if (last < Long.MAX_VALUE)
                return last;
            else
                return 0L;
        }
    }
    public final static long Ceil(DataMessageTerm[] list){
        if (null == list)
            return 0L;
        else {
            long last = Long.MIN_VALUE;
            for (DataMessageTerm t: list){
                if (0L < t.time)
                    last = Math.max(last,t.time);
            }
            if (last < Long.MAX_VALUE)
                return last;
            else
                return 0L;
        }
    }

}
