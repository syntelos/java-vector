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

        DataIdentifier id = this.kind.identifier(key);

        Object normalized = id.toObject(value);

        return super.put(id,normalized);
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
}
