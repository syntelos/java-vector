/*
 * Vector (http://code.google.com/p/java-vector/)
 * Copyright (C) 2012, The DigiVac Company
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
package vector;

import json.Json;

import java.util.StringTokenizer;

/**
 * Mutable reference operator works on the {@link Component} + {@link
 * json.Json JSON} tree to dereference values defined in the JSON
 * tree.  
 * 
 * <h3>Usage</h3>
 * 
 * Employed via the {@link Component} modified operator, predecessors
 * in the Component tree evaluation (JSON listing) order will have
 * fully populated properties.  Components following the user in tree
 * order will have properties defined directly by their initialization
 * and JSON definitions.
 * 
 * <h3>Syntax</h3>
 * 
 * Similar to the subset of <a
 * href="http://www.w3.org/TR/xpath/">XPath</a> colloquially named
 * "Fast XPath".  Begins evaluation in members of {@link Component}
 * where the <code>Parent</code> term is valid, and then descends into
 * {@link json.Json} where the <code>Parent</code> term no longer
 * dereferences.
 * 
 * <pre>
 * PathExpression := PathSubexpression ( PathSep PathSubexpression )*
 * PathSubexpression := ( Current | Parent | Name | Index )
 * PathSep := '/'
 * Current := '.'
 * Parent := '..'
 * Name := [-_a-zA-Z0-9]+
 * Index := [0-9]+
 * </pre>
 * 
 * The ultimately dereferenced {@link json.Json JSON} has its {@link
 * json.Json#getValue() getValue} called.
 *
 * @see PointLabel
 */
public class Reference
    extends Object
    implements Iterable<Reference.Element>
{
    /**
     * 
     */
    public static abstract class Element 
        extends Object
        implements Cloneable
    {

        public final String element;

        public Element(String element){
            super();
            if (null != element && 0 < element.length())
                this.element = element;
            else
                throw new IllegalArgumentException();
        }


        /**
         * @param current The current working {@link Component} or
         * {@link json.Json JSON}
         * 
         * @return Dereferenced value is null when the semantics of
         * the operator are not met
         */
        public abstract Object dereference(Object current);

        public Element clone(){
            try {
                return (Element)super.clone();
            }
            catch (CloneNotSupportedException exc){
                throw new InternalError();
            }
        }
        public int hashCode(){
            return this.element.hashCode();
        }
        public boolean equals(Object that){
            if (this == that)
                return true;
            else if (that instanceof Element)
                return this.element.equals(((Element)that).element);
            else if (that instanceof String)
                return this.element.equals((String)that);
            else
                return false;
        }
        public String toString(){
            return this.element;
        }

        /**
         * 
         */
        public static class Current
            extends Element
        {

            public Current(String element){
                super(element);
                if (!".".equals(element))
                    throw new IllegalArgumentException(element);
            }


            public Object dereference(Object current){
                if (current instanceof Component || current instanceof Json)
                    return current;
                else
                    return null;
            }
        }
        /**
         * 
         */
        public static class Parent
            extends Element
        {

            public Parent(String element){
                super(element);
                if (!"..".equals(element))
                    throw new IllegalArgumentException(element);
            }


            public Object dereference(Object current){
                if (current instanceof Component)
                    return ((Component)current).getParentVector();
                else
                    return null;
            }
        }
        /**
         * 
         */
        public static abstract class Child
            extends Element
        {

            public Child(String element){
                super(element);
            }


            /**
             * 
             */
            public static class Index
                extends Child
            {
                public final int index;


                public Index(int index){
                    super(String.valueOf(index));
                    this.index = index;
                }


                public Object dereference(Object current){

                    Json json;

                    if (current instanceof Component){

                        json = ((Component)current).toJson();
                    }
                    else 
                        json = (Json)current;

                    if (null != json)

                        return json.at(this.index);
                    else
                        return null;
                }
            }
            /**
             * 
             */
            public static class Name
                extends Child
            {

                public Name(String element){
                    super(element);
                }


                public Object dereference(Object current){

                    Json json;

                    if (current instanceof Component){

                        json = ((Component)current).toJson();
                    }
                    else 
                        json = (Json)current;

                    if (null != json)

                        return json.at(this.element);
                    else
                        return null;
                }
            }
        }


        public static int Count(Element[] elements){
            if (null == elements)
                return 0;
            else
                return elements.length;
        }
        public static boolean Has(Element[] elements, int idx){
            if (-1 < idx && idx < Count(elements))
                return true;
            else
                return false;
        }
        public static Element Get(Element[] elements, int idx){
            if (-1 < idx && idx < Count(elements))
                return elements[idx];
            else
                throw new java.util.NoSuchElementException(String.valueOf(idx));
        }
        public static Element[] Add(Element[] elements, Element comp){
            if (null != comp){
                if (null == elements)
                    elements = new Element[]{comp};
                else {
                    int len = elements.length;
                    Element[] copier = new Element[len+1];
                    System.arraycopy(elements,0,copier,0,len);
                    copier[len] = comp;
                    elements = copier;
                }
            }
            return elements;
        }
        /**
         * 
         */
        public static class Iterator
            extends Object
            implements Iterable<Element>,
                       java.util.Iterator<Element>
        {

            private final Element[] list;
            private final int count;

            private int index;


            public Iterator(Element[] list){
                super();
                if (null == list){
                    this.list = null;
                    this.count = 0;
                }
                else {
                    this.list = list;
                    this.count = list.length;
                }
            }


            public Element[] list(){
                if (0 == this.count)
                    return null;
                else
                    return this.list.clone();
            }
            public java.util.Iterator<Element> iterator(){

                return this;
            }
            public boolean hasNext(){
                return (this.index < this.count);
            }
            public Element next(){
                if (this.index < this.count)
                    return this.list[this.index++];
                else
                    throw new java.util.NoSuchElementException(String.valueOf(this.index));
            }
            public void remove(){
                throw new UnsupportedOperationException();
            }
        }


        public final static Element For(String tok){
            final int len = tok.length();
            if (0 < len){
                final int ch0 = tok.charAt(0);
                if ('.' == ch0){

                    switch (len){
                    case 1:
                        return new Element.Current(tok);
                    case 2:
                        return new Element.Parent(tok);
                    default:
                        throw new IllegalArgumentException(tok);
                    }
                }
                else if ('0' <= ch0 && '9' >= ch0){
                    try {
                        return new Element.Child.Index(Integer.parseInt(tok));
                    }
                    catch (RuntimeException exc){
                        return new Element.Child.Name(tok);
                    }
                }
                else if (0x20 < ch0 && 0x7f > ch0){

                    return new Element.Child.Name(tok);
                }
                else
                    throw new IllegalArgumentException(tok);
            }
            else
                throw new IllegalArgumentException(tok);
        }
    }


    protected Element[] elements;


    public Reference(){
        super();
    }
    public Reference(String ref){
        super();
        this.parse(ref);
    }



    public <O extends Object> O dereference(Component c, Class<O> v){

        Object current = c;
        {
            final Element[] elements = this.elements;
            final int count = Element.Count(elements);
            for (int cc = 0; cc < count; cc++){
                Element element = elements[cc];
                current = element.dereference(current);
                if (null == current){
                    return null;
                }
            }
        }

        if (current instanceof Json)
            return (O)((Json)current).getValue(v);
        else
            return null;
    }
    /**
     * Parse reference into this instance
     * 
     * @param string Reference syntax string
     * 
     * @throws java.lang.RuntimeException
     */
    public void parse(String string){
        this.elements = null;
        final StringTokenizer strtok = new StringTokenizer(string,"/");
        final int count = strtok.countTokens();
        if (0 < count){
            final Element[] elements = new Element[count];
            for (int cc = 0; cc < count; cc++){

                elements[cc] = Element.For(strtok.nextToken());
            }
            this.elements = elements;
        }
    }
    public final int count(){

        return Element.Count(this.elements);
    }
    public final boolean has(int idx){

        return Element.Has(this.elements,idx);
    }
    public final Element get(int idx){

        return Element.Get(this.elements,idx);
    }
    public Element.Iterator iterator(){
        return new Element.Iterator(this.elements);
    }
    public String toString(){
        StringBuilder string = new StringBuilder();

        if (null != this.elements){
            boolean once = true;

            for (Element element: this.elements){
                if (once)
                    once = false;
                else
                    string.append('/');

                string.append(element.element);
            }
        }
        return string.toString();
    }
}
