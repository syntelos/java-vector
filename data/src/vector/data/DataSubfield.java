package vector.data;

/**
 * 
 */
public interface DataSubfield<E extends Enum<E>> {
    /**
     * @see java.lang.Enum
     */
    public int ordinal();
    /**
     * @see java.lang.Enum
     */
    public String name();
    /**
     * @return This value is a secret.  Most secrets must never
     * communicate with any non-local channel.  Some may communicate
     * with secure network channels.
     */
    public boolean isProtected();
    /**
     * @return The subfield determines the data type
     */
    public boolean isDataPrimary();
    /**
     * @return The primary field determines the data type
     */
    public boolean isDataSecondary();
    /**
     * This method is called from {@link
     * DataField#toObject(vector.data.DataSubfield,java.lang.String)
     * DataField toObject} to perform known (supported) type
     * conversions DataField when {@link DataSubfield#isDataPrimary()
     * isDataPrimary} returns true.
     * 
     * @param value String (is / may be) Percent Encoded
     * @return Object for string 
     */
    public Object toObject(String value);
    /**
     * Normalize value
     * 
     * This method is called from {@link
     * DataField#toObject(vector.data.DataSubfield,java.lang.Object)
     * DataField toObject} to perform known (supported) type
     * conversions DataField when {@link DataSubfield#isDataPrimary()
     * isDataPrimary} returns true.
     *
     * @return Object for object sanity check.  If string try decode,
     * otherwise return.
     */
    public Object toObject(Object value);
    /**
     * This method is called from {@link
     * DataField#toString(vector.data.DataSubfield,java.lang.Object)
     * DataField toString} to perform known (supported) type
     * conversions when {@link DataSubfield#isDataPrimary()
     * isDataPrimary} returns true
     * 
     * @return String for object, not Percent Encoded
     */
    public String toString(Object value);

    /**
     * 
     */
    public static class Tools<E extends Enum<E>>
        extends Object
        implements java.lang.Iterable<DataSubfield<E>>,
                   java.util.Iterator<DataSubfield<E>>
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
        public DataSubfield<E> next(){
            if (this.index < this.length)
                return (DataSubfield<E>)this.list[this.index++];
            else
                throw new java.util.NoSuchElementException(String.valueOf(this.index));
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public java.util.Iterator<DataSubfield<E>> iterator(){
            return this.reset();
        }


        public final static int IndexOf(DataSubfield[] list, DataSubfield subfield){
            if (null == subfield || null == list)
                return -1;
            else {
                final int count = list.length;
                for (int cc = 0; cc < count; cc++){
                    if (subfield == list[cc])
                        return cc;
                }
                return -1;
            }
        }
        public final static DataSubfield[] Copy(DataSubfield[] list){
            if (null == list)
                return null;
            else {
                final int len = list.length;
                DataSubfield[] copier = new DataSubfield[len];
                System.arraycopy(list,0,copier,0,len);
                return copier;
            }
        }
        public final static DataSubfield[] Add(DataSubfield[] list, DataSubfield item){
            if (null == item)
                return list;
            else if (null == list)
                return new DataSubfield[]{item};
            else {
                final int len = list.length;
                DataSubfield[] copier = new DataSubfield[len+1];
                System.arraycopy(list,0,copier,0,len);
                copier[len] = item;
                return copier;
            }
        }
        public final static DataSubfield[] Add(DataSubfield[] list, DataSubfield[] sublist){
            if (null == sublist)
                return list;
            else if (null == list)
                return sublist;
            else {
                final int listl = list.length;
                final int sublistl = sublist.length;
                DataSubfield[] copier = new DataSubfield[listl+sublistl];
                System.arraycopy(list,0,copier,0,listl);
                System.arraycopy(sublist,0,copier,listl,sublistl);
                return copier;
            }
        }
        public final static DataSubfield[] List(java.lang.Iterable<DataSubfield> it){
            return List(it.iterator());
        }
        public final static DataSubfield[] List(java.util.Iterator<DataSubfield> it){
            DataSubfield[] list = null;
            while (it.hasNext()){
                list = Add(list,it.next());
            }
            return list;
        }
    }
}
