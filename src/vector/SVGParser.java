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

import platform.Color;
import platform.Stroke;

import json.Json;
import json.ObjectJson;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;



/**
 * Work in progress (WIP) on SVG data retrieval.  The intent is to
 * extract useful subsets of SVG, but this could change.
 */
public class SVGParser
    extends org.xml.sax.HandlerBase
    implements javax.xml.namespace.NamespaceContext
{
    protected final static SAXParserFactory PF = SAXParserFactory.newInstance();
    static {
        PF.setNamespaceAware(true);
        PF.setValidating(false);
    }
    /**
     * Objective subset of SVG spec
     */
    public enum SVGElement {
        path, svg, unknown;

        public final static SVGElement For(QName name){
            try {
                return SVGElement.valueOf(name.getLocalPart());
            }
            catch (RuntimeException exc){

                return SVGElement.unknown;
            }
        }
    }
    /**
     * Objective subset of SVG spec
     */
    public enum SVGAttribute {
        xmlns, viewbox, d, style, fill, stroke, unknown;

        public final static SVGAttribute For(QName name){
            try {
                return SVGAttribute.valueOf(name.getLocalPart());
            }
            catch (RuntimeException exc){

                return SVGAttribute.unknown;
            }
        }
        public final static SVGAttribute For(String localPart){
            try {
                return SVGAttribute.valueOf(localPart);
            }
            catch (RuntimeException exc){

                return SVGAttribute.unknown;
            }
        }
    }
    /**
     * Parse a style attribute value as a sequence of name - value
     * pairs
     */
    public static class CSS
        extends lxl.ArrayList<CSS.Attribute>
    {
        public static class Attribute {

            public final String name, value;

            public Attribute(String name, String value){
                super();
                if (null != name){
                    this.name = name;
                    if (null != value && 0 < value.length())
                        this.value = value;
                    else
                        this.value = null;
                }
                else
                    throw new IllegalArgumentException();
            }
        }

        public CSS(String css){
            super();
            final StringTokenizer strtok = new StringTokenizer(css,";: ");
            final int count = strtok.countTokens();
            String name = null, value = null;
            for (int cc = 0; cc < count; cc++){
                String tok = strtok.nextToken();
                if (null == name)
                    name = tok;
                else {
                    value = tok;
                    this.add(new Attribute(name,value));
                    name = null;
                }
            }
        }
    }


    protected final SVG svg;

    protected final URL source;

    protected final String name;

    protected final SAXParser parser;


    protected final lxl.Stack<Component> stack = new lxl.ArrayList();

    protected final lxl.Map<String,String> nsmap = new lxl.Map();


    protected boolean debug, pathContent;

    protected int line, column;

    protected Exception error;


    public SVGParser(SVG svg){
        super();
        if (null != svg){
            this.svg = svg;
            URL source = svg.getSource();
            if (null != source){

                this.source = source;

                this.name = source.getFile();

                try {
                    this.parser = PF.newSAXParser();
                }
                catch (Exception exc){

                    throw new IllegalStateException(exc);
                }
            }
            else
                throw new IllegalStateException("source");
        }
        else
            throw new IllegalArgumentException("svg");
    }


    public String getNamespaceURI(String prefix){

        return this.nsmap.get(prefix);
    }
    public String getPrefix(String uri){

        throw new UnsupportedOperationException();
    }
    public java.util.Iterator getPrefixes(String uri){

        throw new UnsupportedOperationException();
    }
    public java.lang.Iterable<String> getPrefixes(){

        return this.nsmap.keys();
    }
    protected void defineNamespace(String prefix, String uri){

        this.nsmap.put(prefix,uri);
    }
    protected QName parseQName(String name){
        final int index = name.indexOf(':');
        if (0 > index){
            return new QName(XMLConstants.NULL_NS_URI,
                             name,
                             XMLConstants.DEFAULT_NS_PREFIX);
        }
        else {
            final String prefix = name.substring(0,index);
            final String localp = name.substring(index+1);
            final String nsuri = this.getNamespaceURI(prefix);

            if (null == nsuri){

                return new QName(XMLConstants.NULL_NS_URI,
                                 name,
                                 prefix);
            }
            else {
                return new QName(nsuri,
                                 name,
                                 prefix);
            }
        }
    }
    public void setDocumentLocator(Locator locator){

        this.line = locator.getLineNumber();
        this.column = locator.getColumnNumber();
    }
    public void startDocument()
        throws SAXException
    {
    }
    public void startElement(String name, AttributeList attributes)
        throws SAXException
    {
        QName qname = this.parseQName(name);

        switch(SVGElement.For(qname)){

        case svg:{
                final int count = attributes.getLength();

                for (int cc = 0; cc < count; cc++){

                    final QName at = this.parseQName(attributes.getName(cc));

                    if (SVGAttribute.xmlns == SVGAttribute.For(at.getPrefix())){

                        final String prefix = at.getLocalPart();

                        final String uri = attributes.getValue(cc);

                        this.defineNamespace(prefix,uri);
                    }
                    else {
                        switch (SVGAttribute.For(at.getLocalPart())){

                        case viewbox:{

                            this.svg.setViewbox(new Bounds(attributes.getValue(cc)));

                            break;
                        }

                        default:
                            break;
                        }
                    }
                }

            return;
        }

        case path:{

            final Path path = new Path();
            {
                this.svg.add(path);

                if (this.pathContent){

                    path.setContent(true);
                }

                this.stack.push(path);
                
                final int count = attributes.getLength();

                for (int cc = 0; cc < count; cc++){

                    final QName at = this.parseQName(attributes.getName(cc));

                    final String atv = attributes.getValue(cc);
                    try {
                        switch (SVGAttribute.For(at.getLocalPart())){

                        case d:
                            try {
                                path.setD(atv);
                            }
                            catch (RuntimeException exc){
                                System.err.printf("Exception in attribute name \"%s\", value \"%s\"%n","d",atv);
                                throw exc;
                            }
                            break;

                        case style:{
                            final CSS css = new CSS(atv);

                            for (CSS.Attribute cat: css){

                                switch (SVGAttribute.For(cat.name)){

                                case fill:
                                    path.setFill(true);
                                    path.setColor(cat.value);
                                    break;

                                case stroke:
                                    path.setStroke(new Stroke(1.0f,Color.decode(cat.value)));
                                    break;

                                default:
                                    break;
                                }
                            }
                            break;
                        }

                        case fill:
                            path.setFill(true);
                            path.setColor(atv);
                            break;

                        case stroke:
                            path.setStroke(new Stroke(1.0f,platform.Color.decode(atv)));
                            break;

                        default:
                            break;
                        }
                    }
                    catch (RuntimeException exc){
                        System.err.printf("Exception at %s:%d%d: %s%n",this.source,this.line,this.column,name);
                        throw exc;
                    }
                }
            }
            return;
        }

        default:
            break;
        }
    }
    public void endElement(String name)
        throws SAXException
    {
        if (SVGElement.unknown != SVGElement.For(this.parseQName(name)))
            this.stack.pop();
    }
    public void processingInstruction(String target, String data)
        throws SAXException
    {
    }
    public void endDocument()
        throws SAXException
    {
    }
    /**
     * @return Success
     */
    public boolean parse(){
        {
            boolean debug;
            {
                Boolean value = this.svg.getDebug();
                debug = (null != value && value.booleanValue());
            }
            this.debug = debug;
        }
        {
            boolean pathContent;
            {
                Boolean value = this.svg.getPathContent();
                pathContent = (null != value && value.booleanValue());
            }
            this.pathContent = pathContent;
        }
        try {
            System.out.printf("Open: %s%n",this.source);

            final InputStream in = this.source.openStream();

            if (null != in){

                System.out.printf("Parse: %s%n",this.source);

                this.parser.parse(in,this,"name:"+this.name);

                return true;
            }
        }
        catch (SAXException exc){
            this.error = exc;
            exc.printStackTrace();
        }
        catch (IOException exc){
            this.error = exc;
            exc.printStackTrace();
        }
        System.err.printf("Failed to open '%s'",this.source);
        return false;
    }
    public int getLineNumber(){
        return this.line;
    }
    public int getColumnNumber(){
        return this.column;
    }
    public boolean hasError(){
        return (null != this.error);
    }
    public Exception getError(){
        return this.error;
    }
    public int hashCode(){
        return this.source.hashCode();
    }
    public String toString(){
        return this.source.toString();
    }
    public boolean equals(Object that){
        if (this == that)
            return true;
        else
            return this.source.equals(that);
    }
}
