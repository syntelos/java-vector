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
 * A data enum is one or more data columns for one or more records in
 * a 2D cell table.
 * 
 * A syntactic enum may also implement this interface as the super
 * class of {@link DataOperator}.
 * 
 * @see DataService
 */
public interface DataField<D extends Enum<D>>
    extends java.lang.Comparable<D>, 
            java.io.Serializable
{
    /**
     * @see java.lang.Enum
     */
    public int ordinal();
    /**
     * @see java.lang.Enum
     */
    public String name();
    /**
     * @return Is an operator.  A data enum may implement DataOperator
     * while only some of its members are operators.  Operators should
     * have an alternative representation as a syntactic enum.
     */
    public boolean isOperator();

    /**
     * @return {@link #getSubfieldClass() getSubfieldClass} will
     * return a value, otherwise throw unsupported operation
     * exception.
     * 
     * @see DataSubfield
     */
    public boolean hasSubfieldClass();
    /**
     * Throw unsupported operation exception when {@link
     * #hasSubfieldClass() hasSubfieldClass} returns false.
     * 
     * @see DataSubfield
     * @exception java.lang.UnsupportedOperationException
     */
    public <S extends DataSubfield> Class<S> getSubfieldClass()
        throws java.lang.UnsupportedOperationException;
    /**
     * 
     */
    public boolean hasSubfieldDefault();
    /**
     * 
     */
    public <S extends DataSubfield> S getSubfieldDefault()
        throws java.lang.UnsupportedOperationException;
    /**
     * Class operator: string to object.  This step may perform URL
     * Percent Decoding.
     */
    public Object toObject(String value);
    /**
     * If the subfield method {@link DataSubfield#isDataPrimary()
     * isDataPrimary} returns true, then the subfield {@link
     * DataSubfield#toObject(java.lang.String) toObject} call is
     * returned.  Otherwise the implementor performs known (supported)
     * type conversions.
     */
    public Object toObject(DataSubfield subfield, String value);
    /**
     * Normalize value
     * 
     * @return Object for object sanity check.  If string try decode,
     * otherwise return.
     */
    public Object toObject(Object value);
    /**
     * If the subfield method {@link DataSubfield#isDataPrimary()
     * isDataPrimary} returns true, then the subfield {@link
     * DataSubfield#toObject(java.lang.Object) toObject} call is
     * returned.  Otherwise the implementor performs known (supported)
     * type conversions.
     */
    public Object toObject(DataSubfield subfield, Object value);
    /**
     * Class operator: object to string.  This step does not include
     * URL Percent Encoding.
     */
    public String toString(Object value);
    /**
     * If the subfield method {@link DataSubfield#isDataPrimary()
     * isDataPrimary} returns true, then the subfield {@link
     * DataSubfield#toString(java.lang.Object) toString} call is
     * returned.  Otherwise the implementor performs known (supported)
     * type conversions.
     */
    public String toString(DataSubfield subfield, Object value);
    /**
     * This enum value represents a list of enum values.  For example,
     * "A" maps to "A1", "A2", etc.
     */
    public boolean hasMapping();
    /**
     * When "has mapping" returns false, a call to this method throws
     * unsupported operation exception.
     *
     * @exception java.lang.UnsupportedOperationException
     */
    public <D extends DataField> Iterable<D> getMapping()
        throws java.lang.UnsupportedOperationException;


    /**
     * 
     */
    public static class Tools<D>
        extends Object
        implements java.lang.Iterable<D>,
                   java.util.Iterator<D>
    {
        private final int length;
        private final Object[] list;
        private int index;

        public Tools(Object[] list){
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


        public Tools reset(){
            this.index = 0;
            return this;
        }
        public boolean hasNext(){
            return (this.index < this.length);
        }
        public D next(){
            if (this.index < this.length)
                return (D)this.list[this.index++];
            else
                throw new java.util.NoSuchElementException(String.valueOf(this.index));
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public java.util.Iterator<D> iterator(){
            return this.reset();
        }


        /**
         * @return A maps onto B
         */
        public final static boolean Mapsto(DataField a, DataField b){
            if (a == b)
                return true;
            else if (a.hasMapping()){

                final Iterable<DataField> it = a.getMapping();

                for (DataField mapto : it){

                    if (mapto == b){
                        return true;
                    }
                }
                return false;
            }
            else
                return false;
        }
        public final static int IndexOf(DataField[] list, DataField field){
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
        public final static DataField[] Copy(DataField[] list){
            if (null == list)
                return null;
            else {
                final int len = list.length;
                DataField[] copier = new DataField[len];
                System.arraycopy(list,0,copier,0,len);
                return copier;
            }
        }
        public final static DataField[] Add(DataField[] list, DataField item){
            if (null == item)
                return list;
            else if (null == list)
                return new DataField[]{item};
            else {
                final int len = list.length;
                DataField[] copier = new DataField[len+1];
                System.arraycopy(list,0,copier,0,len);
                copier[len] = item;
                return copier;
            }
        }
        public final static DataField[] Add(DataField[] list, DataField[] sublist){
            if (null == sublist)
                return list;
            else if (null == list)
                return sublist;
            else {
                final int listl = list.length;
                final int sublistl = sublist.length;
                DataField[] copier = new DataField[listl+sublistl];
                System.arraycopy(list,0,copier,0,listl);
                System.arraycopy(sublist,0,copier,listl,sublistl);
                return copier;
            }
        }
        public final static DataField[] List(java.lang.Iterable<DataField> it){
            return List(it.iterator());
        }
        public final static DataField[] List(java.util.Iterator<DataField> it){
            DataField[] list = null;
            while (it.hasNext()){
                list = Add(list,it.next());
            }
            return list;
        }
    }
}
