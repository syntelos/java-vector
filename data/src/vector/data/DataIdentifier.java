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

    public final D field;

    public final S subfield;

    public final int hashCode;

    public final String string;


    public DataIdentifier(D field){
        this(field,null);
    }
    public DataIdentifier(D field, S subfield){
        super();
        if (null != field){
            this.field = field;
            this.subfield = subfield;

            int h = (field.hashCode()|(field.getClass().getName().hashCode()));

            final StringBuilder string = new StringBuilder();

            string.append(field.name());

            if (null != subfield){

                h ^= (subfield.hashCode()|(subfield.getClass().getName().hashCode()));

                string.append('.');
                string.append(subfield.name());
            }
            this.hashCode = h;
            this.string = string.toString();
        }
        else
            throw new IllegalArgumentException();
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
