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
package vector.services;

import vector.Component;
import vector.Display;
import vector.Document;
import vector.Reference;
import static vector.Reference.DisplayReference;

import vector.data.DataMessage;

import java.lang.Class;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Unbound implementation functions assume normal input for one of the
 * syntactic enums defined in this package.  Normal input requires the
 * correct syntactic enum with the zero'th ordinal, and an ordered
 * application of enum members and their arguments.
 * 
 * These functions are configured for use (binding) by including
 * <code>"services-impl-X.Y.Z.jar"</code> in the class path.
 */
public class DisplayService
    extends vector.DisplayService
{

    /**
     * 
     */
    public static DataMessage[] Add(Object... argv){
        if (null != argv){
            final int len = argv.length;
            if (5 == len){
                /*
                 * ADD S(Class|URI) T(URI)
                 */
                final Object sarg = argv[2];

                final URI targetURI = (URI)argv[4];

                if (!targetURI.isAbsolute()){

                    final DisplayReference target = new DisplayReference(targetURI.getPath());

                    if (sarg instanceof Class && Component.class.isAssignableFrom( (Class)sarg)){
                        Class<Component> source = (Class<Component>)sarg;
                        try {
                            final Component component = source.newInstance();

                            final Document doc = target.document();

                            return doc.add(component,target.reference);
                        }
                        catch (Exception exc){
                            throw new IllegalArgumentException(String.format("Error in 'add' constructing instance of (%s)",source.getName()),exc);
                        }
                    }
                    else if (sarg instanceof URI){

                        final URI sourceURI = (URI)sarg;

                        if (sourceURI.isAbsolute()){
                            try {
                                final URL source = sourceURI.toURL();

                                final Document doc = target.document();

                                return doc.add(source,target.reference);

                            }
                            catch (MalformedURLException exc){
                                throw new IllegalArgumentException(String.format("Error in 'add' constructing URL of (%s)",sourceURI),exc);
                            }
                        }
                        else {
                            try {
                                final DisplayReference source = new DisplayReference(sourceURI.getPath());

                                final Document doc = target.document();

                                return doc.add(source.reference,target.reference);

                            }
                            catch (Exception exc){
                                throw new IllegalArgumentException(String.format("Error in 'add' constructing URL of (%s)",sourceURI),exc);
                            }
                        }
                    }
                    else 
                        throw new IllegalArgumentException(String.format("Error in 'add', unrecognized arguments (%s)",sarg.getClass().getName()));
                }
                else 
                    throw new IllegalArgumentException(String.format("Error in 'add', unrecognized target is absolute URI (%s)",targetURI));
            }
            else 
                throw new IllegalArgumentException(String.format("Error in 'add', incorrect number of arguments (%d) require five",len));
        }
        return null;
    }
    public static DataMessage[] Cat(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }
    public static DataMessage[] Copy(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }
    public static DataMessage[] Create(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }
    public static DataMessage[] Edit(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }
    public static DataMessage[] List(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }
    public static DataMessage[] Move(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }
    public static DataMessage[] Remove(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }
    public static DataMessage[] Resize(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }
    public static DataMessage[] Show(Object... argv){

        throw new UnsupportedOperationException("placeholder");
    }

    protected DisplayService(){
        super();
    }
}
