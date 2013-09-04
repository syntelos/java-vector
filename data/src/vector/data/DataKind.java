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
    /**
     * @return New {@link DataDefaults}
     */
    public final DataDefaults defaults(){

        return new DataDefaults(this);
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
     * <h3>Kind rebinding</h3>
     * 
     * Data fields and subfields will be rebound to this kind.  This
     * method accepts enum member collisions by binding the name of an
     * input object into this kind of field (and subfield) enum.
     * 
     * Kind rebinding is employed for the conversion of data enum
     * members to operator syntax members.  In processing operator
     * syntax, however, subfields are not employed.
     *
     *
     * @param id An instance of {@link java.lang.String}, {@link
     * DataIdentifier}, or {@link DataField} followed by one or more
     * {@link DataSubfield} (members or names).
     * 
     * @return Null for null argument, identifier for string or
     * object, otherwise throw illegal argument exception
     * 
     * @exception java.lang.IllegalArgumentException Unrecognized
     * argument not a member of accepted argument types
     * 
     * @see DataService#IdentifierSearch
     * @see DataIdentifier#DataIdentifier
     */
    public final DataIdentifier identifier(Object... argv)
        throws java.lang.IllegalArgumentException
    {
        final int argc = ((null != argv)?(argv.length):(0));

        final Object id = ((0 < argc)?(argv[0]):(null));
        
        if (null == id)
            return null;
        else {

            String string;
            if (id instanceof String)
                string = (String)id;
            else if (id instanceof DataField){
                /*
                 * Check field class 
                 * Rebind field collisions
                 */
                string = ((DataField)id).name();
            }
            else if (id instanceof DataIdentifier){
                /*
                 * Check field class 
                 * Rebind field collisions
                 */
                final DataIdentifier di = (DataIdentifier)id;
                if (di.field.getClass().equals(this.field))
                    return di;
                else
                    string = di.string;
            }
            else {
                throw new IllegalArgumentException(id.getClass().getName());
            }

            final int idx = string.indexOf('.');
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
                final DataField field = this.fieldFor(fieldName);

                DataSubfield subfield = null;
                if (null != field){
                    /*
                     * Check for default subfield value
                     */
                    if (field.hasSubfieldDefault()){
                        subfield = field.getSubfieldDefault();
                    }
                    if (1 < argc && null != this.subfield){
                        /*
                         * Ordered subfield expansion with (kind) rebinding
                         */
                        for (int cc = 1; cc < argc; cc++){

                            final Object argcc = argv[cc];
                            if (null != argcc){

                                if (argcc instanceof DataSubfield){

                                    DataSubfield argsf = (DataSubfield)argcc;
                                    /*
                                     * Check subfield class 
                                     */
                                    if (argsf.getClass().equals(this.subfield)){
                                        subfield = argsf;
                                        break;
                                    }
                                    else {
                                        /*
                                         * Rebind subfield collisions
                                         */
                                        argsf = this.subfieldFor(argsf.name());
                                        if (null != argsf){
                                            subfield = argsf;
                                            break;
                                        }
                                    }
                                }
                                else if (argcc instanceof String){
                                    /*
                                     * Bind subfield name
                                     */
                                    DataSubfield argsf = this.subfieldFor( (String)argcc);
                                    if (null != argsf){
                                        subfield = argsf;
                                        break;
                                    }
                                }
                                else {
                                    /*
                                     * Bind subfield name
                                     */
                                    DataSubfield argsf = this.subfieldFor( argcc.toString());
                                    if (null != argsf){
                                        subfield = argsf;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                try {
                    return new DataIdentifier(field,subfield);
                }
                catch (RuntimeException exc){
                    if (null == field)
                        throw new IllegalArgumentException(String.format("Unable to find field '%s' in '%s'",fieldName,this),exc);
                    else
                        throw new IllegalArgumentException(String.format("Error in field '%s' in '%s'",fieldName,this),exc);
                }
            }
        }
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
     * String identity
     */
    public final String toString(){
        return this.string;
    }
    /**
     * String identity
     */
    public final int hashCode(){
        return this.string.hashCode();
    }
    /**
     * String identity
     */
    public final boolean equals(Object that){
        if (this == that)
            return true;
        else if (null == that)
            return false;
        else
            return this.string.equals(that.toString());
    }
}
