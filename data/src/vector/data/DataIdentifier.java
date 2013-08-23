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
 * Data enum identifier with optional subfield.
 */
public class DataIdentifier<D extends DataField, S extends DataSubfield>
    extends Object
{
    public final static DataSubfield[] NARG = new DataSubfield[0];


    public final D field;

    public final S subfield;

    public final boolean subfieldDefault;

    public final int hashCode;

    public final String string;

    /**
     * Field with field class default subfield value (may be null)
     */
    public DataIdentifier(D field){
        this(field,NARG);
    }
    /**
     * Interaction API accepts a list of subfield possibilities for
     * subfield (possibility) injection style programming.
     * 
     * <h3>Field default subfield value</h3>
     * 
     * The field default subfield value may be replaced by the first
     * non - null subfield argument.
     * 
     * <h3>Ordered subfield expansion</h3>
     * 
     * The first non null value in this list is accepted as the
     * subfield value.
     * 
     * @see DataKind#identifier
     * @see DataService#IdentifierSearch
     */
    public DataIdentifier(DataField field, DataSubfield... list){
        super();
        if (null != field){
            this.field = (D)field;

            DataSubfield subfield;

            DataSubfield subfieldDefault = null;
            /*
             * Check for default subfield value
             */
            if (field.hasSubfieldDefault()){

                subfield = field.getSubfieldDefault();
                subfieldDefault = subfield;
            }
            else
                subfield = null;
            /*
             * Ordered subfield expansion
             */
            if (null != list && 0 < list.length){
                for (int cc = 0, count = list.length; cc < count; cc++){
                    if (null != list[cc]){
                        subfield = list[cc];
                        break;
                    }
                }
            }
            this.subfield = (S)subfield;
            this.subfieldDefault = (null != subfieldDefault && subfieldDefault == subfield);

            /*
             * Identity
             */
            int h = (field.hashCode()|(field.getClass().getName().hashCode()));

            final StringBuilder string = new StringBuilder();

            string.append(field.name());

            if (null != this.subfield){

                h ^= (this.subfield.hashCode()|(this.subfield.getClass().getName().hashCode()));

                if (!this.subfieldDefault){
                    string.append('.');
                    string.append(this.subfield.name());
                }
            }
            this.hashCode = h;
            this.string = string.toString();
        }
        else
            throw new IllegalArgumentException();
    }
    public DataIdentifier( DataIdentifier id, DataSubfield generic){
        this(id.field,id.subfield,generic);
    }
    public DataIdentifier( DataIdentifier id){
        this(id.field,id.subfield);
    }


    public final boolean isField(D field){

        return this.field.equals(field);
    }
    public final boolean isSubfield(S subfield){

        if (null != this.subfield)
            return this.subfield.equals(subfield);
        else
            return false;
    }
    public final boolean isFieldAssignableFrom(Class field){

        return this.field.getClass().isAssignableFrom(field);
    }
    public final boolean isSubfieldAssignableFrom(Class subfield){

        if (null != this.subfield)
            return this.subfield.getClass().isAssignableFrom(subfield);
        else
            return false;
    }
    public final Object toObject(Object value){

        if (null != this.subfield && this.subfield.isDataPrimary()){

            return this.subfield.toObject(value);
        }
        return this.field.toObject(value);
    }
    public final Object toObject(String value){

        if (null != this.subfield && this.subfield.isDataPrimary()){

            return this.subfield.toObject(value);
        }
        return this.field.toObject(value);
    }
    public final String toString(Object value){

        if (null != this.subfield && this.subfield.isDataPrimary()){

            return this.subfield.toString(value);
        }
        return this.field.toString(value);
    }
    /*
     * string identity
     */
    public final String toString(){
        return this.string;
    }
    public final int hashCode(){
        return this.string.hashCode();
    }
    public final boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.string.equals(that.toString());
    }
}
