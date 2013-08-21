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

import java.lang.reflect.Method;

/**
 * Instances of this class are constructed for a specific {@link
 * DataService} programming context.  
 * 
 * The programming patterns created with the use of an instance of
 * this class will provide the best runtime performance for direct
 * programming with known enum types.
 */
public class DataKind<D extends DataField, S extends DataSubfield>
    extends Object
{

    public final Class<D> field;

    public final Class<S> subfield;

    public final int hashCode;

    public final String string;

    private Method fieldFor, subfieldFor;


    public DataKind(Class<D> field){
        super();
        if (null != field){
            this.field = field;

            this.subfield = DataService.SubfieldClassFor(field);

            int h = field.getName().hashCode();

            final StringBuilder string = new StringBuilder();

            string.append("kind://");
            string.append(field.getName());

            if (null != subfield){

                h ^= subfield.getName().hashCode();

                string.append('/');
                string.append(subfield.getName());
            }
            this.hashCode = h;
            this.string = string.toString();
        }
        else
            throw new IllegalArgumentException();
    }


    /**
     * @return New {@link DataDefaults}
     */
    public final DataDefaults defaults(){

        return new DataDefaults(this);
    }
    /**
     * @return Lazy field
     */
    public final Method methodFieldFor(){
        Method fieldFor = this.fieldFor;
        if (null != fieldFor)
            return fieldFor;
        else {
            return (this.fieldFor = DataService.MethodField(this.field));
        }
    }
    /**
     * @return Lazy field
     */
    public final Method methodSubfieldFor(){
        Method subfieldFor = this.subfieldFor;
        if (null != subfieldFor)
            return subfieldFor;
        else {
            return (this.subfieldFor = DataService.MethodSubfield(this.subfield));
        }
    }
    /**
     * @param id Instance of {@link java.lang.String} or {@link DataIdentifier}
     * 
     * @return Null for null argument, identifier for string or
     * object, otherwise throw illegal argument exception
     * 
     * @exception java.lang.IllegalArgumentException Unrecognized argument
     */
    public final DataIdentifier identifier(Object id)
        throws java.lang.IllegalArgumentException
    {
        if (null == id)
            return null;
        else if (id instanceof DataIdentifier)
            return (DataIdentifier)id;
        else if (id instanceof String){
            String string = (String)id;
            int idx = string.indexOf('.');
            if (-1 < idx){
                final String fieldName = string.substring(0,idx);
                final String subfieldName = string.substring(idx+1);
                final D field = this.fieldFor(fieldName);
                final S subfield = this.subfieldFor(subfieldName);
                try {
                    return new DataIdentifier(field,subfield);
                }
                catch (RuntimeException exc){

                    if (null == field)
                        throw new IllegalArgumentException(String.format("Unable to find field '%s' (sub '%s') in '%s'",fieldName,subfieldName,this),exc);
                    else
                        throw new IllegalArgumentException(String.format("Error in field '%s' and subfield '%s' in '%s'",fieldName,subfieldName,this),exc);
                }
            }
            else {
                final String fieldName = string;
                final D field = this.fieldFor(fieldName);
                try {
                    return new DataIdentifier(field);
                }
                catch (RuntimeException exc){
                    if (null == field)
                        throw new IllegalArgumentException(String.format("Unable to find field '%s' in '%s'",fieldName,this),exc);
                    else
                        throw new IllegalArgumentException(String.format("Error in field '%s' in '%s'",fieldName,this),exc);
                }
            }
        }
        else
            throw new IllegalArgumentException(id.getClass().getName());
    }
    /**
     * @param id Field name in this field class
     * 
     * @return Null for not found
     */
    public final D fieldFor(String id){
        Method fieldFor = this.methodFieldFor();
        if (null != fieldFor){
            try {
                return (D)fieldFor.invoke(null,id);
            }
            catch (Exception debug){
                debug.printStackTrace();
            }
        }
        return null;
    }
    /**
     * @param id Subfield name in the optional subfield class
     * 
     * @return Null for not found
     */
    public final S subfieldFor(String id){
        Method subfieldFor = this.methodSubfieldFor();
        if (null != subfieldFor){
            try {
                return (S)subfieldFor.invoke(null,id);
            }
            catch (Exception debug){
                debug.printStackTrace();
            }
        }
        return null;
    }
    public final boolean isFieldClass(Class field){

        return this.field.equals(field);
    }
    public final boolean isSubfieldClass(Class subfield){

        if (null != this.subfield)
            return this.subfield.equals(subfield);
        else
            return false;
    }
    public final boolean isFieldAssignableFrom(Class field){

        return this.field.isAssignableFrom(field);
    }
    public final boolean isSubfieldAssignableFrom(Class subfield){

        if (null != this.subfield)
            return this.subfield.isAssignableFrom(subfield);
        else
            return false;
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
