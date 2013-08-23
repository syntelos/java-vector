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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Resource
 * <pre>
 * /META-INF/services/vector.data.DataField
 * </pre>
 * 
 * in a java archive (jar) in the application class path.
 * 
 * By convention, this file lists classes in the archive that
 * implement {@link DataField}.
 * 
 * {@link DataField} users are expected to identify their data field
 * classes of interest, so the services class initialization is for
 * applications with interest in enumerating the members of these
 * classes.
 */
public final class DataService
    extends services.Classes
{
    public final static DataService Instance = new DataService();
    /**
     * Indempotent class initialization
     */
    public static void Init(){
    }


    public final static <D extends DataField> D Search(String string){
        if (null != string){

            return (D)Instance.search(string);
        }
        else
            throw new IllegalArgumentException();
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
     * @param constructor Optional data identifier subclass
     * constructor for {@link DataField} and {@link DataSubfield}
     * arguments.
     * 
     * @param string Field name search for {@link DataKind}
     * 
     * @param list Subfield expansion argument
     * 
     * @return DataIdentifier or throw illegal argument exception
     * 
     * @exception java.lang.IllegalArgumentException
     * 
     * @see DataKind#identifier
     * @see DataIdentifier#DataIdentifier
     */
    public final static <D extends DataIdentifier> D IdentifierSearch(Constructor<D> constructor, String string, DataSubfield... list)
        throws java.lang.IllegalArgumentException
    {
        if (null != string){
            int idx = string.indexOf('.');
            if (-1 < idx){

                final String fieldName = string.substring(0,idx);

                final DataKind kind = Instance.kind(fieldName);

                if (null == kind)
                    throw new IllegalArgumentException(String.format("DataKind not found for field (%s)",fieldName));
                else {

                    final String subfieldName = string.substring(idx+1);

                    final DataField field = kind.fieldFor(fieldName);
                    final DataSubfield subfield = kind.subfieldFor(subfieldName);
                    try {
                        if (null == constructor){

                            return (D)(new DataIdentifier(field,subfield));
                        }
                        else {
                            try {
                                return constructor.newInstance(field,subfield);
                            }
                            catch (Exception ctor){

                                throw new IllegalArgumentException(String.format("Unable to construct identifier (%s) for (field: '%s', subfield '%s') in '%s'",constructor.getDeclaringClass().getName(),fieldName,subfieldName,kind),ctor);
                            }
                        }
                    }
                    catch (RuntimeException exc){

                        if (null == field)
                            throw new IllegalArgumentException(String.format("Unable to find field '%s' (sub '%s') in '%s'",fieldName,subfieldName,kind),exc);
                        else
                            throw new IllegalArgumentException(String.format("Error in field '%s' and subfield '%s' in '%s'",fieldName,subfieldName,kind),exc);
                    }
                }
            }
            else {
                final String fieldName = string;
                final DataKind kind = Instance.kind(fieldName);
                if (null == kind)
                    throw new IllegalArgumentException(String.format("DataKind not found for field (%s)",fieldName));
                else {

                    final DataField field = kind.fieldFor(fieldName);

                    DataSubfield subfield;
                    /*
                     * Check for default subfield value
                     */
                    if (field.hasSubfieldDefault())
                        subfield = field.getSubfieldDefault();
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

                    try {

                        if (null == constructor){

                            return (D)(new DataIdentifier(field,subfield));
                        }
                        else {
                            try {
                                return constructor.newInstance(field,subfield);
                            }
                            catch (Exception ctor){

                                throw new IllegalArgumentException(String.format("Unable to construct identifier (%s) for (field: '%s') in '%s'",constructor.getDeclaringClass().getName(),fieldName,kind),ctor);
                            }
                        }
                    }
                    catch (RuntimeException exc){
                        if (null == field)
                            throw new IllegalArgumentException(String.format("Unable to find field '%s' in '%s'",fieldName,kind),exc);
                        else
                            throw new IllegalArgumentException(String.format("Error constructing identifier for field '%s' in '%s'",fieldName,kind),exc);
                    }
                }
            }
        }
        else
            throw new IllegalArgumentException();
    }
    public final static DataKind SearchKind(String string){
        if (null != string){

            return Instance.kind(string);
        }
        else
            throw new IllegalArgumentException();
    }
    public final static java.lang.Iterable<DataDefaults> ListDefaults(){

        return Instance.defaults();
    }
    public final static <D extends DataField> D FieldFor(Class<D> d, String string){
        if (null != d && null != string){

            Method function = MethodField(d);
            try {
                return (D)function.invoke(null,string);
            }
            catch (Exception debug){
                debug.printStackTrace();

                return null;
            }
        }
        else
            throw new IllegalArgumentException();
    }
    public final static <S extends DataSubfield> S SubfieldFor(Class<S> s, String string){
        if (null != s && null != string){

            Method function = MethodSubfield(s);
            try {
                return (S)function.invoke(null,string);
            }
            catch (Exception debug){
                debug.printStackTrace();

                return null;
            }
        }
        else
            throw new IllegalArgumentException();
    }
    public final static <D extends DataField> Method MethodField(Class<D> d){
        if (null != d){

            try {
                return d.getMethod("For",String.class);
            }
            catch (Exception exc){
                try {
                    return d.getMethod("valueOf",String.class);
                }
                catch (Exception debug){

                    debug.printStackTrace();

                    return null;
                }
            }
        }
        else
            throw new IllegalArgumentException();
    }
    public final static <S extends DataSubfield> Method MethodSubfield(Class<S> s){
        if (null != s){

            try {
                return s.getMethod("For",String.class);
            }
            catch (Exception exc){
                try {
                    return s.getMethod("valueOf",String.class);
                }
                catch (Exception debug){

                    debug.printStackTrace();

                    return null;
                }
            }
        }
        else
            return null;
    }

    public final static <D extends DataField> D FieldFirst(Class kind){

        DataField[] values = EnumerateField(kind);

        return (D)values[0];
    }
    public final static <S extends DataSubfield> S SubfieldFirst(Class kind){

        if (null != kind){

            DataSubfield[] values = EnumerateSubfield(kind);

            return (S)values[0];
        }
        else
            return null;
    }

    public final static <D extends DataField, S extends DataSubfield> Class<S> SubfieldClassFor(Class d){
        D first = FieldFirst(d);
        if (first.hasSubfieldClass())
            return first.getSubfieldClass();
        else
            return null;
    }

    public final static DataField[] EnumerateField(Class kind){
        try {
            Method function = kind.getMethod("values");

            DataField[] enumeration = (DataField[])function.invoke(null);

            return enumeration;
        }
        catch (Exception debug){

            debug.printStackTrace();
        }
        return null;
    }
    public final static DataSubfield[] EnumerateSubfield(Class kind){
        if (null != kind){
            try {
                Method function = kind.getMethod("values");

                DataSubfield[] enumeration = (DataSubfield[])function.invoke(null);

                return enumeration;
            }
            catch (Exception debug){

                debug.printStackTrace();
            }
        }
        return null;
    }


    private final lxl.ArrayList<Method> search = new lxl.ArrayList();

    private final lxl.ArrayList<DataKind> kind = new lxl.ArrayList();


    private DataService(){
        super(vector.data.DataField.class);

        for (Class field: this){
            try {
                Method method = DataService.MethodField(field);
                this.search.add(method);

                System.err.printf("vector.data.DataField class: %s, method: %s%n",method.getDeclaringClass().getName(),method.getName());
            }
            catch (Exception debug){
                debug.printStackTrace();
            }
            try {
                DataKind kind = new DataKind(field);
                this.kind.add(kind);
            }
            catch (Exception debug){
                debug.printStackTrace();
            }
        }
    }


    public DataField search(String name){

        for (Method field: this.search){
            try {
                return (DataField) field.invoke(null,name);
            }
            catch (Exception search){
            }
        }
        return null;
    }
    public DataKind kind(String name){

        for (DataKind kind: this.kind){

            if (null != kind.fieldFor(name)){

                return kind;
            }
        }
        return null;
    }
    public java.lang.Iterable<DataDefaults> defaults(){

        DataDefaults[] list = null;

        for (DataKind kind: this.kind){

            list = DataDefaults.Add(list,kind.defaults());
        }
        return new DataDefaults.Iterator(list);
    }
}
